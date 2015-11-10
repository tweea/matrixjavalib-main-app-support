/*
 * 版权所有 2015 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class GlobalSystemContextTest {
	@Test
	public void testGet() {
		SystemContext context1 = GlobalSystemContext.get();
		SystemContext context2 = GlobalSystemContext.get();
		Assertions.assertThat(context1).isSameAs(context2);
	}

	@Test
	public void testSet() {
		SystemContext context = new DefaultSystemContext();
		GlobalSystemContext.set(context);
		Assertions.assertThat(GlobalSystemContext.get()).isSameAs(context);
		GlobalSystemContext.set(null);
		Assertions.assertThat(GlobalSystemContext.get()).isNotSameAs(context);
	}
}
