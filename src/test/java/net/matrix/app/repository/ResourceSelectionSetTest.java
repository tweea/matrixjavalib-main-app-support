/*
 * Copyright(C) 2009 matrix
 * All right reserved.
 */
package net.matrix.app.repository;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

public class ResourceSelectionSetTest {
    private static ResourceSelectionSet set1;

    private static ResourceSelectionSet set2;

    @BeforeClass
    public static void beforeClass() {
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
    public void new1() {
        Assertions.assertThat(set1.catalogNames()).hasSize(5);
        Assertions.assertThat(set1.catalogNames()).contains("naming");
        Assertions.assertThat(set1.catalogNames()).contains("test/orz");
        Assertions.assertThat(set1.resourceNames("test")).hasSize(2);
        Assertions.assertThat(set1.resourceNames("test")).contains("big.xml");
        Assertions.assertThat(set1.getSelections("configset", "configset.xml")).isNotEmpty();
    }

    @Test
    public void new2() {
        Assertions.assertThat(set2.catalogNames()).hasSize(5);
        Assertions.assertThat(set2.catalogNames()).contains("naming");
        Assertions.assertThat(set2.catalogNames()).contains("test/orz");
        Assertions.assertThat(set2.resourceNames("test")).hasSize(2);
        Assertions.assertThat(set2.resourceNames("test")).contains("big.xml");
        Assertions.assertThat(set2.getSelections("configset", "configset.xml")).isNotEmpty();
    }

    @Test
    public void checkDiff() {
        Set<ResourceSelection> updateInfoList = set1.checkDiff(set2);
        Assertions.assertThat(updateInfoList).hasSize(5);
    }
}
