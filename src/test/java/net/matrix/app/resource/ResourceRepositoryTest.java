/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceRepositoryTest {
    @Test
    void testGetResource0() {
        ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));

        // 默认资源名
        assertThat(repo.getResource(new ResourceSelection("test", "1", null)).exists()).isTrue();
        assertThat(repo.getResource(new ResourceSelection("test/orz", "1", null))).isNull();
    }

    @Test
    void testGetResource1() {
        ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));

        // 分类
        assertThat(repo.getResource(new ResourceSelection("naming", "1", "paths.xml")).exists()).isTrue();
        assertThat(repo.getResource(new ResourceSelection("test", "1", "middle.xml")).exists()).isTrue();
        assertThat(repo.getResource(new ResourceSelection("test1", "1", "middle.xml"))).isNull();
        // 版本
        assertThat(repo.getResource(new ResourceSelection("test", "2", "middle.xml"))).isNull();
        // 资源名
        assertThat(repo.getResource(new ResourceSelection("test", "1", "middle1.xml"))).isNull();
        assertThat(repo.getResource(new ResourceSelection("test", "1", "big.xml"))).isNull();
    }

    @Test
    void testGetResource2() {
        ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));

        // 默认版本
        assertThat(repo.getResource(new ResourceSelection("test", null, "small.xml")).exists()).isTrue();
        assertThat(repo.getResource(new ResourceSelection("test", null, "big.xml"))).isNull();
        assertThat(repo.getResource(new ResourceSelection("test/orz", null, "small.xml"))).isNull();
    }

    @Test
    void testGetResource3() {
        ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));

        // 多级
        assertThat(repo.getResource(new ResourceSelection("test/orz", "1", "bar.xml")).exists()).isTrue();
        assertThat(repo.getResource(new ResourceSelection("test/orz", "2", "big.xml"))).isNull();
        assertThat(repo.getResource(new ResourceSelection("test/ora", "1", "big.xml"))).isNull();
        assertThat(repo.getResource(new ResourceSelection("test", "1/mouse", "big.xml")).exists()).isTrue();
        assertThat(repo.getResource(new ResourceSelection("test", "1/mouse", "middle.xml")).exists()).isTrue();
        assertThat(repo.getResource(new ResourceSelection("test", "2/rat", "big.xml"))).isNull();
        assertThat(repo.getResource(new ResourceSelection("test", "1/rat", "small.xml")).exists()).isTrue();
    }
}
