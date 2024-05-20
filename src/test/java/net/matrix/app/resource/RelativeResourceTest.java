/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RelativeResourceTest {
    @Test
    public void testRelativeResource_rootName() {
        RelativeResource resource = new RelativeResource("a", "b");
        assertThat(resource.getRootName()).isEqualTo("a");
        assertThat(resource.getRelativePath()).isEqualTo("b");
    }

    @Test
    public void testRelativeResource_parent() {
        RelativeResource parent = new RelativeResource("a", "b");

        RelativeResource resource = new RelativeResource(parent, "c");
        assertThat(resource.getRootName()).isEqualTo("a");
        assertThat(resource.getRelativePath()).isEqualTo("b/c");
    }
}
