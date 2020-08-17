/*
 * Copyright(C) 2011 matrix
 * All right reserved.
 */
package net.matrix.app;

import org.apache.commons.configuration2.Configuration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultSystemContextTest {
    @Test
    public void testGetConfig() {
        DefaultSystemContext context = new DefaultSystemContext();

        Configuration config = context.getConfig();
        assertThat(config).isNotNull();
        assertThat(config.getString("test")).isEqualTo("a");
        assertThat(config.getString("xyz")).isEqualTo("1");
    }

    @Test
    public void testGetConfig2() {
        System.setProperty("systemConfigLocation", "classpath:sysconfig.cfg,classpath:sysconfig2.cfg");
        DefaultSystemContext context = new DefaultSystemContext();

        Configuration config = context.getConfig();
        assertThat(config).isNotNull();
        assertThat(config.getString("test")).isEqualTo("b");
        assertThat(config.getString("xyz")).isEqualTo("1");

        System.clearProperty("systemConfigLocation");
    }

    @Test
    public void testRegisterObject() {
        DefaultSystemContext context = new DefaultSystemContext();
        Object obj = new Object();

        context.registerObject(Object.class, obj);
        assertThat(context.lookupObject(Object.class)).isSameAs(obj);
    }

    @Test
    public void testGetController() {
        DefaultSystemContext context = new DefaultSystemContext();

        SystemController controller = context.getController();
        assertThat(controller).isInstanceOf(DefaultSystemController.class);
        assertThat(controller.getContext()).isSameAs(context);
    }

    @Test
    public void testGetController2() {
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
