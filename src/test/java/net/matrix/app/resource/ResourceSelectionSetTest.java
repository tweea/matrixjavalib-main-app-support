/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.util.Set;

import org.assertj.core.util.introspection.FieldSupport;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceSelectionSetTest {
    FieldSupport fieldSupport = FieldSupport.extraction();

    @Test
    void testAdd() {
        ResourceSelectionSet set = new ResourceSelectionSet();
        ResourceSelection selection = new ResourceSelection("configset", "set1/1", "configset.xml");

        set.add(selection);
        Set<ResourceSelection> selections = fieldSupport.fieldValue("selections", Set.class, set);
        assertThat(selections).hasSize(1);
        assertThat(selections).contains(selection);
    }

    @Test
    void testContains() {
        ResourceSelectionSet set = new ResourceSelectionSet();
        ResourceSelection selection = new ResourceSelection("configset", "set1/1", "configset.xml");
        set.add(selection);

        assertThat(set.contains(selection)).isTrue();
    }

    @Test
    void testRemove() {
        ResourceSelectionSet set = new ResourceSelectionSet();
        ResourceSelection selection = new ResourceSelection("configset", "set1/1", "configset.xml");
        set.add(selection);

        set.remove(selection);
        Set<ResourceSelection> selections = fieldSupport.fieldValue("selections", Set.class, set);
        assertThat(selections).isEmpty();
    }

    @Test
    void testGetCatalogs() {
        ResourceSelectionSet set = new ResourceSelectionSet();
        set.add(new ResourceSelection("configset", "set2/1", "configset.xml"));
        set.add(new ResourceSelection("naming", "1", "paths.xml"));
        set.add(new ResourceSelection("test", "1/rat", "big.xml"));
        set.add(new ResourceSelection("test", "1/mouse", "small.xml"));
        set.add(new ResourceSelection("test/orz", "1", "foo.xml"));
        set.add(new ResourceSelection("test/orz", "1", "bar.xml"));
        set.add(new ResourceSelection("test/virtual", "2", "foo.xml"));

        assertThat(set.getCatalogs()).hasSize(5);
        assertThat(set.getCatalogs()).contains("naming");
        assertThat(set.getCatalogs()).contains("test/orz");
        assertThat(set.getNames("test")).hasSize(2);
        assertThat(set.getNames("test")).contains("big.xml");
        assertThat(set.getSelections("configset", "configset.xml")).isNotEmpty();
    }

    @Test
    void testCheckDiff() {
        ResourceSelectionSet set1 = new ResourceSelectionSet();
        set1.add(new ResourceSelection("configset", "set1/1", "configset.xml"));
        set1.add(new ResourceSelection("naming", "1", "paths.xml"));
        set1.add(new ResourceSelection("test", "1/mouse", "big.xml"));
        set1.add(new ResourceSelection("test", "2/mouse", "small.xml"));
        set1.add(new ResourceSelection("test/orz", "1", "foo.xml"));
        set1.add(new ResourceSelection("test/virtual", "1", "foo.xml"));

        ResourceSelectionSet set2 = new ResourceSelectionSet();
        set2.add(new ResourceSelection("configset", "set2/1", "configset.xml"));
        set2.add(new ResourceSelection("naming", "1", "paths.xml"));
        set2.add(new ResourceSelection("test", "1/rat", "big.xml"));
        set2.add(new ResourceSelection("test", "1/mouse", "small.xml"));
        set2.add(new ResourceSelection("test/orz", "1", "foo.xml"));
        set2.add(new ResourceSelection("test/orz", "1", "bar.xml"));
        set2.add(new ResourceSelection("test/virtual", "2", "foo.xml"));

        Set<ResourceSelection> updateInfoList = set1.checkDiff(set2);
        assertThat(updateInfoList).hasSize(5);
    }
}
