/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.util.Set;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceContextConfigTest {
    private static ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));

    private static ResourceSelection selection1 = new ResourceSelection("configset", "set1", "configset.xml");

    private static ResourceSelection selection2 = new ResourceSelection("configset", "set2", "configset.xml");

    private static ResourceContextConfig set1;

    private static ResourceContextConfig set2;

    @BeforeAll
    public static void beforeAll()
        throws ConfigurationException {
        set1 = new ResourceContextConfig();
        set1.load(repo.getResource(selection1));
        set2 = new ResourceContextConfig();
        set2.load(repo.getResource(selection2));
    }

    @Test
    public void testLoad1() {
        assertThat(set1.catalogNames()).hasSize(5);
        assertThat(set1.catalogNames()).contains("naming");
        assertThat(set1.catalogNames()).contains("test/orz");
        assertThat(set1.resourceNames("test")).hasSize(2);
        assertThat(set1.resourceNames("test")).contains("big.xml");
        assertThat(set1.getSelection("configset", "configset.xml")).isNotNull();
    }

    @Test
    public void testLoad2() {
        assertThat(set2.catalogNames()).hasSize(5);
        assertThat(set2.catalogNames()).contains("naming");
        assertThat(set2.catalogNames()).contains("test/orz");
        assertThat(set2.resourceNames("test")).hasSize(2);
        assertThat(set2.resourceNames("test")).contains("big.xml");
        assertThat(set2.getSelection("configset", "configset.xml")).isNotNull();
    }

    @Test
    public void testCheckDiff() {
        Set<ResourceSelection> updateInfoList = set1.checkDiff(set2);
        assertThat(updateInfoList).hasSize(5);
    }
}
