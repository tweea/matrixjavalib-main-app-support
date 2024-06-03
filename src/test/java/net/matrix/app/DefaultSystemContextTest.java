/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import org.apache.commons.configuration2.Configuration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultSystemContextTest {
    @Test
    void testGetResourceLoader() {
        DefaultSystemContext context = new DefaultSystemContext();

        assertThat(context.getResourceLoader()).isNotNull();
    }

    @Test
    void testGetResourcePatternResolver() {
        DefaultSystemContext context = new DefaultSystemContext();

        assertThat(context.getResourcePatternResolver()).isNotNull();
    }

    @Test
    void testGetConfig() {
        DefaultSystemContext context = new DefaultSystemContext();

        Configuration config = context.getConfig();
        assertThat(config).isNotNull();
        assertThat(config.getString("test")).isEqualTo("a");
        assertThat(config.getString("xyz")).isEqualTo("1");
    }

    @Test
    void testGetConfig2() {
        System.setProperty("systemConfigLocation", "classpath:sysconfig.cfg,classpath:sysconfig2.cfg");
        DefaultSystemContext context = new DefaultSystemContext();

        Configuration config = context.getConfig();
        assertThat(config).isNotNull();
        assertThat(config.getString("test")).isEqualTo("b");
        assertThat(config.getString("xyz")).isEqualTo("1");

        System.clearProperty("systemConfigLocation");
    }

    @Test
    void testRegisterObject_name() {
        DefaultSystemContext context = new DefaultSystemContext();
        Object object = new Object();

        context.registerObject("test", object);
        assertThat(context.lookupObject("test", Object.class)).isSameAs(object);
    }

    @Test
    void testRegisterObject_type() {
        DefaultSystemContext context = new DefaultSystemContext();
        Object object = new Object();

        context.registerObject(Object.class, object);
        assertThat(context.lookupObject(Object.class)).isSameAs(object);
    }

    @Test
    void testGetController() {
        DefaultSystemContext context = new DefaultSystemContext();

        SystemController controller = context.getController();
        assertThat(controller).isInstanceOf(DefaultSystemController.class);
        assertThat(controller.getContext()).isSameAs(context);
    }

    @Test
    void testGetController2() {
        System.setProperty("systemControllerClass", TestController.class.getName());
        DefaultSystemContext context = new DefaultSystemContext();

        SystemController controller = context.getController();
        assertThat(controller).isInstanceOf(TestController.class);
        assertThat(controller.getContext()).isSameAs(context);

        System.clearProperty("systemControllerClass");
    }

    static class TestController
        extends DefaultSystemController {
    }
}
