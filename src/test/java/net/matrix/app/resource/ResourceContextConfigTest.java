/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.util.Set;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceContextConfigTest {
    static ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));

    static ResourceSelection selection1 = new ResourceSelection("configset", "set1", "configset.xml");

    static ResourceSelection selection2 = new ResourceSelection("configset", "set2", "configset.xml");

    static ResourceContextConfig set1;

    static ResourceContextConfig set2;

    @BeforeAll
    static void beforeAll()
        throws ConfigurationException {
        set1 = new ResourceContextConfig();
        set1.load(repo.getResource(selection1));
        set2 = new ResourceContextConfig();
        set2.load(repo.getResource(selection2));
    }

    @Test
    void testLoad1() {
        assertThat(set1.getCatalogs()).hasSize(5);
        assertThat(set1.getCatalogs()).contains("naming");
        assertThat(set1.getCatalogs()).contains("test/orz");
        assertThat(set1.getNames("test")).hasSize(2);
        assertThat(set1.getNames("test")).contains("big.xml");
        assertThat(set1.getSelection("configset", "configset.xml")).isNotNull();
    }

    @Test
    void testLoad2() {
        assertThat(set2.getCatalogs()).hasSize(5);
        assertThat(set2.getCatalogs()).contains("naming");
        assertThat(set2.getCatalogs()).contains("test/orz");
        assertThat(set2.getNames("test")).hasSize(2);
        assertThat(set2.getNames("test")).contains("big.xml");
        assertThat(set2.getSelection("configset", "configset.xml")).isNotNull();
    }

    @Test
    void testCheckDiff() {
        Set<ResourceSelection> updateInfoSet = set1.checkDiff(set2);
        assertThat(updateInfoSet).hasSize(5);
    }
}
