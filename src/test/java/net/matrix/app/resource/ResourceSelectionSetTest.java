/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceSelectionSetTest {
    private static ResourceSelectionSet set1;

    private static ResourceSelectionSet set2;

    @BeforeAll
    public static void beforeAll() {
        set1 = new ResourceSelectionSet();
        set1.add(new ResourceSelection("configset", "set1/1", "configset.xml"));
        set1.add(new ResourceSelection("naming", "1", "paths.xml"));
        set1.add(new ResourceSelection("test", "1/mouse", "big.xml"));
        set1.add(new ResourceSelection("test", "2/mouse", "small.xml"));
        set1.add(new ResourceSelection("test/orz", "1", "foo.xml"));
        set1.add(new ResourceSelection("test/virtual", "1", "foo.xml"));
        set2 = new ResourceSelectionSet();
        set2.add(new ResourceSelection("configset", "set2/1", "configset.xml"));
        set2.add(new ResourceSelection("naming", "1", "paths.xml"));
        set2.add(new ResourceSelection("test", "1/rat", "big.xml"));
        set2.add(new ResourceSelection("test", "1/mouse", "small.xml"));
        set2.add(new ResourceSelection("test/orz", "1", "foo.xml"));
        set2.add(new ResourceSelection("test/orz", "1", "bar.xml"));
        set2.add(new ResourceSelection("test/virtual", "2", "foo.xml"));
    }

    @Test
    public void testAdd1() {
        assertThat(set1.catalogNames()).hasSize(5);
        assertThat(set1.catalogNames()).contains("naming");
        assertThat(set1.catalogNames()).contains("test/orz");
        assertThat(set1.resourceNames("test")).hasSize(2);
        assertThat(set1.resourceNames("test")).contains("big.xml");
        assertThat(set1.getSelections("configset", "configset.xml")).isNotEmpty();
    }

    @Test
    public void testAdd2() {
        assertThat(set2.catalogNames()).hasSize(5);
        assertThat(set2.catalogNames()).contains("naming");
        assertThat(set2.catalogNames()).contains("test/orz");
        assertThat(set2.resourceNames("test")).hasSize(2);
        assertThat(set2.resourceNames("test")).contains("big.xml");
        assertThat(set2.getSelections("configset", "configset.xml")).isNotEmpty();
    }

    @Test
    public void testCheckDiff() {
        Set<ResourceSelection> updateInfoList = set1.checkDiff(set2);
        assertThat(updateInfoList).hasSize(5);
    }
}
