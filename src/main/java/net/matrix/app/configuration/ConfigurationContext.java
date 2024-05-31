/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.configuration;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import net.matrix.app.resource.ResourceContext;
import net.matrix.app.resource.ResourceContextConfig;
import net.matrix.app.resource.ResourceRepository;
import net.matrix.app.resource.ResourceSelection;
import net.matrix.configuration.ReloadableConfigurationContainer;
import net.matrix.text.ResourceBundleMessageFormatter;

/**
 * 配置仓库加载环境，加载和缓存配置信息。
 */
public final class ConfigurationContext
    extends ResourceContext {
    /**
     * 日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationContext.class);

    /**
     * 区域相关资源。
     */
    private static final ResourceBundleMessageFormatter RBMF = new ResourceBundleMessageFormatter(ConfigurationContext.class).useCurrentLocale();

    /**
     * 缓存的配置信息。
     */
    private Map<Resource, ReloadableConfigurationContainer> containerCache;

    /**
     * 构造器。
     * 
     * @param repository
     *     资源仓库。
     * @param contextConfig
     *     资源仓库加载环境配置。
     */
    public ConfigurationContext(ResourceRepository repository, ResourceContextConfig contextConfig) {
        super(repository, contextConfig);
        this.containerCache = new ConcurrentHashMap<>();
    }

    /**
     * 从资源仓库的指定位置加载。
     * 
     * @param repository
     *     资源仓库。
     * @param selection
     *     资源仓库选择。
     * @return 配置仓库加载环境。
     * @throws ConfigurationException
     *     加载失败。
     */
    public static ConfigurationContext load(ResourceRepository repository, ResourceSelection selection)
        throws ConfigurationException {
        Resource resource = repository.getResource(selection);
        if (resource == null) {
            throw new ConfigurationException(RBMF.format("没有定位配置资源 {0}", selection));
        }

        ResourceContextConfig contextConfig = new ResourceContextConfig();
        contextConfig.load(resource);
        return new ConfigurationContext(repository, contextConfig);
    }

    /**
     * 重新加载配置，清除缓存的配置信息。
     */
    @Override
    public void reload() {
        super.reload();
        containerCache = new ConcurrentHashMap<>();
        LOG.info(RBMF.get("重新加载配置，清除缓存的配置信息"));
    }

    /**
     * 定位配置资源。
     * 
     * @param selection
     *     资源仓库选择。
     * @return 配置资源。
     * @throws ConfigurationException
     *     定位失败。
     */
    public Resource getConfigurationResource(ResourceSelection selection)
        throws ConfigurationException {
        Resource resource = getResource(selection);
        if (resource == null) {
            throw new ConfigurationException(RBMF.format("没有定位配置资源 {0}", selection));
        }
        return resource;
    }

    /**
     * 加载配置资源。
     * 
     * @param type
     *     配置对象类型。
     * @param selection
     *     资源仓库选择。
     * @return 配置对象。
     * @throws ConfigurationException
     *     加载失败。
     */
    public <T extends ReloadableConfigurationContainer> T getConfiguration(Class<T> type, ResourceSelection selection)
        throws ConfigurationException {
        Resource resource = getConfigurationResource(selection);
        T container = (T) containerCache.get(resource);
        if (container == null) {
            try {
                Constructor<T> constructor = ConstructorUtils.getAccessibleConstructor(type);
                container = constructor.newInstance();
            } catch (ReflectiveOperationException e) {
                throw new ConfigurationException(RBMF.format("配置对象类型 {0} 实例化失败", type.getName()), e);
            }
            // 加载配置
            LOG.debug(RBMF.get("从 {} 加载配置"), resource);
            try {
                container.load(resource);
            } catch (ConfigurationException e) {
                throw new ConfigurationException(RBMF.format("从 {0} 加载配置失败", resource.toString()), e);
            }
            container = ObjectUtils.defaultIfNull((T) containerCache.putIfAbsent(resource, container), container);
        }
        container.checkReload();
        return container;
    }
}
