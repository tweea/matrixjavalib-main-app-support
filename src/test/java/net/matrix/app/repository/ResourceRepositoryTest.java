/*
 * Copyright(C) 2010 matrix
 * All right reserved.
 */
package net.matrix.app.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ResourceRepositoryTest {
    @Test
    public void getResource0() {
        ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));
        // 默认资源名
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", "1", null)).exists()).isTrue();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test/orz", "1", null))).isNull();
    }

    @Test
    public void getResource1() {
        ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));
        // 分类
        Assertions.assertThat(repo.getResource(new ResourceSelection("naming", "1", "paths.xml")).exists()).isTrue();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", "1", "middle.xml")).exists()).isTrue();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test1", "1", "middle.xml"))).isNull();
        // 版本
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", "2", "middle.xml"))).isNull();
        // 资源名
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", "1", "middle1.xml"))).isNull();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", "1", "big.xml"))).isNull();
    }

    @Test
    public void getResource2() {
        ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));
        // 默认版本
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", null, "small.xml")).exists()).isTrue();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", null, "big.xml"))).isNull();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test/orz", null, "small.xml"))).isNull();
    }

    @Test
    public void getResource3() {
        ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));
        // 多级
        Assertions.assertThat(repo.getResource(new ResourceSelection("test/orz", "1", "bar.xml")).exists()).isTrue();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test/orz", "2", "big.xml"))).isNull();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test/ora", "1", "big.xml"))).isNull();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", "1/mouse", "big.xml")).exists()).isTrue();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", "1/mouse", "middle.xml")).exists()).isTrue();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", "2/rat", "big.xml"))).isNull();
        Assertions.assertThat(repo.getResource(new ResourceSelection("test", "1/rat", "small.xml")).exists()).isTrue();
    }
}
