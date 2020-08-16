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
    public void testGetById() {
        SystemContext context1 = GlobalSystemContext.get("a");
        SystemContext context2 = GlobalSystemContext.get("b");
        SystemContext context3 = GlobalSystemContext.get("");
        Assertions.assertThat(context1).isNotSameAs(context2);
        Assertions.assertThat(context1).isNotSameAs(context3);
        Assertions.assertThat(context2).isNotSameAs(context3);
        Assertions.assertThat(GlobalSystemContext.get("a")).isSameAs(context1);
        Assertions.assertThat(GlobalSystemContext.get("b")).isSameAs(context2);
        Assertions.assertThat(GlobalSystemContext.get()).isSameAs(context3);
    }

    @Test
    public void testSet() {
        SystemContext context = new DefaultSystemContext();
        GlobalSystemContext.set(context);
        Assertions.assertThat(GlobalSystemContext.get()).isSameAs(context);
        GlobalSystemContext.set(null);
        Assertions.assertThat(GlobalSystemContext.get()).isNotSameAs(context);
    }

    @Test
    public void testSetById() {
        SystemContext context1 = new DefaultSystemContext();
        SystemContext context2 = new DefaultSystemContext();
        SystemContext context3 = new DefaultSystemContext();
        GlobalSystemContext.set("a", context1);
        GlobalSystemContext.set("b", context2);
        GlobalSystemContext.set("", context3);
        Assertions.assertThat(GlobalSystemContext.get("a")).isSameAs(context1);
        Assertions.assertThat(GlobalSystemContext.get("b")).isSameAs(context2);
        Assertions.assertThat(GlobalSystemContext.get("")).isSameAs(context3);
        GlobalSystemContext.set("a", null);
        GlobalSystemContext.set("b", null);
        GlobalSystemContext.set("", null);
        Assertions.assertThat(GlobalSystemContext.get("a")).isNotSameAs(context1);
        Assertions.assertThat(GlobalSystemContext.get("b")).isNotSameAs(context2);
        Assertions.assertThat(GlobalSystemContext.get()).isNotSameAs(context3);
    }
}
