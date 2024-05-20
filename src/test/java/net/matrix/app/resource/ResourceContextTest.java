/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceContextTest {
    @Test
    public void testGetResource()
        throws ConfigurationException {
        ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));
        ResourceContextConfig selectionSet = new ResourceContextConfig();
        selectionSet.load(repo.getResource(new ResourceSelection("configset", "set1", "configset.xml")));
        ResourceContext context = new ResourceContext(repo, selectionSet);

        assertThat(context.getResource("test/orz", "foo.xml").exists()).isTrue();
    }
}
