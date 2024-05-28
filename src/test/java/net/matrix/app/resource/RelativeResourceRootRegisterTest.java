/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class RelativeResourceRootRegisterTest {
    @Test
    public void testRegisterRoot() {
        RelativeResourceRootRegister register = new RelativeResourceRootRegister();

        register.registerRoot("test", new ClassPathResource(""));
        assertThat(register.getRoot("test")).isNotNull();
    }

    @Test
    public void testGetResource()
        throws IOException {
        RelativeResourceRootRegister register = new RelativeResourceRootRegister();
        register.registerRoot("test", new ClassPathResource(""));
        RelativeResource relativeResource = new RelativeResource("test", "bar.xml");

        assertThat(register.getResource(relativeResource).exists()).isTrue();
    }

    @Test
    public void testGetResource_notRegister() {
        RelativeResourceRootRegister register = new RelativeResourceRootRegister();
        RelativeResource relativeResource = new RelativeResource("test", "bar.xml");

        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> register.getResource(relativeResource));
    }
}
