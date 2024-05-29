/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceSelectionTest {
    @Test
    public void testNew() {
        ResourceSelection selection = new ResourceSelection("a/b", "1", null);
        assertThat(selection.getCatalog()).isEqualTo("a/b");
        assertThat(selection.getVersion()).isEqualTo("1");
        assertThat(selection.getName()).isEqualTo("b");
    }
}
