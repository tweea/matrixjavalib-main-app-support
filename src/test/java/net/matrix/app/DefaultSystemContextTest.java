/*
 * Copyright(C) 2011 matrix
 * All right reserved.
 */
package net.matrix.app;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DefaultSystemContextTest {
	@Test
	public void testRegisterObject() {
		DefaultSystemContext context = new DefaultSystemContext();
		Object obj = new Object();
		context.registerObject(Object.class, obj);
		Assertions.assertThat(context.lookupObject(Object.class)).isSameAs(obj);
	}
}
