/*
 * Copyright(C) 2009 matrix
 * All right reserved.
 */
package net.matrix.app.repository;

import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ResourceContextConfigTest {
    private static ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));

    private static ResourceSelection selection1 = new ResourceSelection("configset", "set1", "configset.xml");

    private static ResourceSelection selection2 = new ResourceSelection("configset", "set2", "configset.xml");

    private static ResourceContextConfig set1;

    private static ResourceContextConfig set2;

    @BeforeClass
    public static void beforeClass()
        throws ConfigurationException {
        set1 = new ResourceContextConfig();
        set1.load(repo.getResource(selection1));
        set2 = new ResourceContextConfig();
        set2.load(repo.getResource(selection2));
    }

    @Test
    public void new1() {
        Assertions.assertThat(set1.catalogNames()).hasSize(5);
        Assertions.assertThat(set1.catalogNames()).contains("naming");
        Assertions.assertThat(set1.catalogNames()).contains("test/orz");
        Assertions.assertThat(set1.resourceNames("test")).hasSize(2);
        Assertions.assertThat(set1.resourceNames("test")).contains("big.xml");
        Assertions.assertThat(set1.getSelection("configset", "configset.xml")).isNotNull();
    }

    @Test
    public void new2() {
        Assertions.assertThat(set2.catalogNames()).hasSize(5);
        Assertions.assertThat(set2.catalogNames()).contains("naming");
        Assertions.assertThat(set2.catalogNames()).contains("test/orz");
        Assertions.assertThat(set2.resourceNames("test")).hasSize(2);
        Assertions.assertThat(set2.resourceNames("test")).contains("big.xml");
        Assertions.assertThat(set2.getSelection("configset", "configset.xml")).isNotNull();
    }

    @Test
    public void checkDiff() {
        Set<ResourceSelection> updateInfoList = set1.checkDiff(set2);
        Assertions.assertThat(updateInfoList).hasSize(5);
    }
}
