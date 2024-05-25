/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

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
    ResourceLoader getResourceLoader();

    /**
     * 设置系统资源加载器。
     * 
     * @param resourceLoader
     *     资源加载器。
     */
    void setResourceLoader(ResourceLoader resourceLoader);

    /**
     * 获取系统资源扫描器。
     * 
     * @return 资源扫描器。
     */
    ResourcePatternResolver getResourcePatternResolver();

    /**
     * 设置系统资源扫描器。
     * 
     * @param resourcePatternResolver
     *     资源扫描器。
     */
    void setResourcePatternResolver(ResourcePatternResolver resourcePatternResolver);

    /**
     * 获取系统配置。
     * 
     * @return 配置。
     */
    Configuration getConfig();

    /**
     * 设置系统配置。
     * 
     * @param config
     *     配置。
     */
    void setConfig(Configuration config);

    /**
     * 按名称注册对象。
     * 
     * @param name
     *     名称。
     * @param object
     *     对象。
     */
    void registerObject(String name, Object object);

    /**
     * 按类型注册对象。
     * 
     * @param type
     *     类型。
     * @param object
     *     对象。
     */
    <T> void registerObject(Class<T> type, T object);

    /**
     * 按名称查找对象。
     * 
     * @param name
     *     名称。
     * @return 对象。
     */
    <T> T lookupObject(String name);

    /**
     * 按名称和类型查找对象。
     * 
     * @param name
     *     名称。
     * @param type
     *     类型。
     * @return 对象。
     */
    <T> T lookupObject(String name, Class<T> type);

    /**
     * 按类型查找对象。
     * 
     * @param type
     *     类型。
     * @return 对象。
     */
    <T> T lookupObject(Class<T> type);

    /**
     * 获取系统控制器。
     * 
     * @return 系统控制器。
     */
    SystemController getController();

    /**
     * 设置系统控制器。
     * 
     * @param controller
     *     系统控制器。
     */
    void setController(SystemController controller);
}
