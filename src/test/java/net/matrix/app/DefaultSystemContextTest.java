/*
 * Copyright(C) 2011 matrix
 * All right reserved.
 */
package net.matrix.app;

import org.apache.commons.configuration.Configuration;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DefaultSystemContextTest {
	@Test
	public void testGetConfig() {
		DefaultSystemContext context = new DefaultSystemContext();

		Configuration config = context.getConfig();
		Assertions.assertThat(config).isNotNull();
		Assertions.assertThat(config.getString("test")).isEqualTo("a");
		Assertions.assertThat(config.getString("xyz")).isEqualTo("1");
	}

	@Test
	public void testGetConfig2() {
		System.setProperty("systemConfigLocation", "classpath:sysconfig.cfg,classpath:sysconfig2.cfg");
		DefaultSystemContext context = new DefaultSystemContext();

		Configuration config = context.getConfig();
		Assertions.assertThat(config).isNotNull();
		Assertions.assertThat(config.getString("test")).isEqualTo("b");
		Assertions.assertThat(config.getString("xyz")).isEqualTo("1");

		System.clearProperty("systemConfigLocation");
	}

	@Test
	public void testRegisterObject() {
		DefaultSystemContext context = new DefaultSystemContext();

		Object obj = new Object();
		context.registerObject(Object.class, obj);
		Assertions.assertThat(context.lookupObject(Object.class)).isSameAs(obj);
	}

	@Test
	public void testGetController() {
		DefaultSystemContext context = new DefaultSystemContext();

		SystemController controller = context.getController();
		Assertions.assertThat(controller).isInstanceOf(DefaultSystemController.class);
		Assertions.assertThat(controller.getContext()).isSameAs(context);
	}

	@Test
	public void testGetController2() {
		System.setProperty("systemControllerClass", TestController.class.getName());
		DefaultSystemContext context = new DefaultSystemContext();

		SystemController controller = context.getController();
		Assertions.assertThat(controller).isInstanceOf(TestController.class);
		Assertions.assertThat(controller.getContext()).isSameAs(context);

		System.clearProperty("systemControllerClass");
	}

	static class TestController
		extends DefaultSystemController {
	}
}
