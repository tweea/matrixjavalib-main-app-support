/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.configuration2.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 系统环境，管理系统运行所需资源。
 */
public interface SystemContext {
    /**
     * 获取系统资源加载器。
     *
     * @return 资源加载器。
     */
    @Nonnull
    ResourceLoader getResourceLoader();

    /**
     * 设置系统资源加载器。
     *
     * @param resourceLoader
     *     资源加载器。
     */
    void setResourceLoader(@Nullable ResourceLoader resourceLoader);

    /**
     * 获取系统资源扫描器。
     *
     * @return 资源扫描器。
     */
    @Nonnull
    ResourcePatternResolver getResourcePatternResolver();

    /**
     * 设置系统资源扫描器。
     *
     * @param resourcePatternResolver
     *     资源扫描器。
     */
    void setResourcePatternResolver(@Nullable ResourcePatternResolver resourcePatternResolver);

    /**
     * 获取系统配置。
     *
     * @return 配置。
     */
    @Nonnull
    Configuration getConfig();

    /**
     * 设置系统配置。
     *
     * @param config
     *     配置。
     */
    void setConfig(@Nullable Configuration config);

    /**
     * 按名称注册对象。
     *
     * @param name
     *     名称。
     * @param object
     *     对象。
     */
    void registerObject(@Nonnull String name, @Nullable Object object);

    /**
     * 按类型注册对象。
     *
     * @param type
     *     类型。
     * @param object
     *     对象。
     */
    <T> void registerObject(@Nonnull Class<T> type, @Nullable T object);

    /**
     * 按名称查找对象。
     *
     * @param name
     *     名称。
     * @return 对象。
     */
    @Nullable
    <T> T lookupObject(@Nonnull String name);

    /**
     * 按名称和类型查找对象。
     *
     * @param name
     *     名称。
     * @param type
     *     类型。
     * @return 对象。
     */
    @Nullable
    <T> T lookupObject(@Nonnull String name, @Nonnull Class<T> type);

    /**
     * 按类型查找对象。
     *
     * @param type
     *     类型。
     * @return 对象。
     */
    @Nullable
    <T> T lookupObject(@Nonnull Class<T> type);

    /**
     * 获取系统控制器。
     *
     * @return 系统控制器。
     */
    @Nonnull
    SystemController getController();

    /**
     * 设置系统控制器。
     *
     * @param controller
     *     系统控制器。
     */
    void setController(@Nullable SystemController controller);
}
