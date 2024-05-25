/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalSystemContextTest {
    @Test
    public void testGet() {
        SystemContext context1 = GlobalSystemContext.get();
        SystemContext context2 = GlobalSystemContext.get();
        assertThat(context1).isSameAs(context2);
    }

    @Test
    public void testGet_id() {
        SystemContext context1 = GlobalSystemContext.get("a");
        SystemContext context2 = GlobalSystemContext.get("b");
        SystemContext context3 = GlobalSystemContext.get("");
        assertThat(context1).isNotSameAs(context2);
        assertThat(context1).isNotSameAs(context3);
        assertThat(context2).isNotSameAs(context3);
        assertThat(GlobalSystemContext.get("a")).isSameAs(context1);
        assertThat(GlobalSystemContext.get("b")).isSameAs(context2);
        assertThat(GlobalSystemContext.get()).isSameAs(context3);
    }

    @Test
    public void testSet() {
        SystemContext context = new DefaultSystemContext();

        GlobalSystemContext.set(context);
        assertThat(GlobalSystemContext.get()).isSameAs(context);
        GlobalSystemContext.set(null);
        assertThat(GlobalSystemContext.get()).isNotSameAs(context);
    }

    @Test
    public void testSet_id() {
        SystemContext context1 = new DefaultSystemContext();
        SystemContext context2 = new DefaultSystemContext();
        SystemContext context3 = new DefaultSystemContext();

        GlobalSystemContext.set("a", context1);
        GlobalSystemContext.set("b", context2);
        GlobalSystemContext.set("", context3);
        assertThat(GlobalSystemContext.get("a")).isSameAs(context1);
        assertThat(GlobalSystemContext.get("b")).isSameAs(context2);
        assertThat(GlobalSystemContext.get("")).isSameAs(context3);
        GlobalSystemContext.set("a", null);
        GlobalSystemContext.set("b", null);
        GlobalSystemContext.set("", null);
        assertThat(GlobalSystemContext.get("a")).isNotSameAs(context1);
        assertThat(GlobalSystemContext.get("b")).isNotSameAs(context2);
        assertThat(GlobalSystemContext.get()).isNotSameAs(context3);
    }
}
