/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.springframework.core.io.Resource;

/**
 * 资源仓库加载环境，根据资源仓库加载环境配置在资源仓库中定位资源。
 */
public class ResourceContext {
    /**
     * 资源仓库。
     */
    @Nonnull
    private final ResourceRepository repository;

    /**
     * 资源仓库加载环境配置。
     */
    @Nonnull
    private final ResourceContextConfig contextConfig;

    /**
     * 构造器。
     *
     * @param repository
     *     资源仓库。
     * @param contextConfig
     *     资源仓库加载环境配置。
     */
    public ResourceContext(@Nonnull ResourceRepository repository, @Nonnull ResourceContextConfig contextConfig) {
        this.repository = repository;
        this.contextConfig = contextConfig;
    }

    /**
     * 获取资源仓库。
     */
    @Nonnull
    public ResourceRepository getRepository() {
        return repository;
    }

    /**
     * 获取资源仓库加载环境配置。
     */
    @Nonnull
    public ResourceContextConfig getContextConfig() {
        return contextConfig;
    }

    /**
     * 重新加载配置。
     */
    public void reload() {
        contextConfig.checkReload();
    }

    /**
     * 定位资源。
     *
     * @param catalog
     *     类别。
     * @return 资源。
     */
    @Nullable
    public Resource getResource(@Nonnull String catalog) {
        ResourceSelection selection = contextConfig.getSelection(catalog);
        if (selection == null) {
            return null;
        }

        return repository.getResource(selection);
    }

    /**
     * 定位资源。
     *
     * @param catalog
     *     类别。
     * @param name
     *     名称。
     * @return 资源。
     */
    @Nullable
    public Resource getResource(@Nonnull String catalog, @Nonnull String name) {
        ResourceSelection selection = contextConfig.getSelection(catalog, name);
        if (selection == null) {
            return null;
        }

        return repository.getResource(selection);
    }

    /**
     * 定位资源。
     *
     * @param selection
     *     资源仓库选择。
     * @return 资源。
     */
    @Nullable
    public Resource getResource(@Nonnull ResourceSelection selection) {
        String catalog = selection.getCatalog();
        String version = selection.getVersion();
        String name = selection.getName();
        if (version == null) {
            return getResource(catalog, name);
        } else {
            return repository.getResource(selection);
        }
    }
}
