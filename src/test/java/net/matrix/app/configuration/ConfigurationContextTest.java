/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.configuration;

import java.io.IOException;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import net.matrix.app.resource.RelativeResource;
import net.matrix.app.resource.RelativeResourceRootRegister;
import net.matrix.app.resource.ResourceRepository;
import net.matrix.app.resource.ResourceSelection;
import net.matrix.configuration.ReloadableConfigurationContainer;
import net.matrix.configuration.XMLConfigurationContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ConfigurationContextTest {
    @Test
    public void testLoad()
        throws ConfigurationException {
        ResourceRepository repository = new ResourceRepository(new ClassPathResource("repo1/"));
        ResourceSelection selection = new ResourceSelection("configset", "set2", "configset.xml");

        ConfigurationContext context = ConfigurationContext.load(repository, selection);
        assertThat(context.getContextConfig().getCatalogs()).hasSize(5);
    }

    public ConfigurationContext loadContext()
        throws ConfigurationException {
        ResourceRepository repository = new ResourceRepository(new ClassPathResource("repo1/"));
        ResourceSelection selection = new ResourceSelection("configset", "set1", "configset.xml");
        return ConfigurationContext.load(repository, selection);
    }

    @Test
    public void testGetConfigurationResource()
        throws ConfigurationException, IOException {
        ConfigurationContext context = loadContext();
        RelativeResourceRootRegister register = new RelativeResourceRootRegister();
        register.registerRoot("test", new ClassPathResource("repo1/"));

        Resource resource = context.getConfigurationResource(new ResourceSelection("test", null, "small.xml"));
        assertThat(register.getResource(new RelativeResource("test", "test/small.xml"))).isEqualTo(resource);
        resource = context.getConfigurationResource(new ResourceSelection("test", "1", "middle.xml"));
        assertThat(register.getResource(new RelativeResource("test", "test/1/middle.xml"))).isEqualTo(resource);
        resource = context.getConfigurationResource(new ResourceSelection("test", "1/mouse", "big.xml"));
        assertThat(register.getResource(new RelativeResource("test", "test/1/mouse/big.xml"))).isEqualTo(resource);
        resource = context.getConfigurationResource(new ResourceSelection("test", "1/rat", "middle.xml"));
        assertThat(register.getResource(new RelativeResource("test", "test/1/middle.xml"))).isEqualTo(resource);
    }

    @Test
    public void testGetConfiguration()
        throws ConfigurationException {
        ConfigurationContext context = loadContext();
        ResourceSelection selection = new ResourceSelection("test", null, "small.xml");

        ReloadableConfigurationContainer<XMLConfiguration> container = context.getConfiguration(XMLConfigurationContainer.class, selection);
        assertThat(container.getConfig().getInt("[@length]")).isEqualTo(50);
    }

    @Test
    public void testGetConfiguration2()
        throws ConfigurationException {
        ConfigurationContext context = loadContext();
        ResourceSelection selection = new ResourceSelection("testxxx", null, "abc.xml");

        assertThatExceptionOfType(ConfigurationException.class).isThrownBy(() -> context.getConfiguration(ReloadableConfigurationContainer.class, selection));
    }

    @Test
    public void testGetConfiguration3()
        throws ConfigurationException {
        ConfigurationContext context = loadContext();
        ResourceSelection selection = new ResourceSelection("test", null, "abc");

        assertThatExceptionOfType(ConfigurationException.class).isThrownBy(() -> context.getConfiguration(ReloadableConfigurationContainer.class, selection));
    }
}
