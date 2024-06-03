/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.commons.configuration2.tree.OverrideCombiner;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import net.matrix.java.lang.reflect.ReflectionMx;
import net.matrix.text.ResourceBundleMessageFormatter;

/**
 * 默认的系统环境。
 */
public class DefaultSystemContext
    implements SystemContext {
    /**
     * 日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSystemContext.class);

    /**
     * 区域相关资源。
     */
    private static final ResourceBundleMessageFormatter RBMF = new ResourceBundleMessageFormatter(DefaultSystemContext.class).useCurrentLocale();

    /**
     * 系统配置位置的系统属性名。
     */
    private static final String CONFIG_LOCATION_PROPERTY = "systemConfigLocation";

    /**
     * 默认的系统配置位置。
     */
    private static final String DEFAULT_CONFIG_LOCATION = "classpath:sysconfig.cfg,classpath:sysconfig.dev.cfg";

    /**
     * 系统控制器类名的系统属性名。
     */
    private static final String CONTROLLER_CLASS_PROPERTY = "systemControllerClass";

    /**
     * 系统资源加载器。
     */
    protected ResourceLoader resourceLoader;

    /**
     * 系统资源扫描器。
     */
    protected ResourcePatternResolver resourcePatternResolver;

    /**
     * 系统配置。
     */
    protected Configuration config;

    /**
     * 已注册对象。
     */
    protected final Map<String, Object> objects;

    /**
     * 系统控制器。
     */
    protected SystemController controller;

    /**
     * 构造器。
     */
    public DefaultSystemContext() {
        this.objects = new HashMap<>();
    }

    @Override
    public ResourceLoader getResourceLoader() {
        if (resourceLoader == null) {
            resourceLoader = new DefaultResourceLoader();
        }
        return resourceLoader;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ResourcePatternResolver getResourcePatternResolver() {
        if (resourcePatternResolver == null) {
            if (getResourceLoader() instanceof ResourcePatternResolver) {
                resourcePatternResolver = (ResourcePatternResolver) resourceLoader;
            } else {
                resourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
            }
        }
        return resourcePatternResolver;
    }

    @Override
    public void setResourcePatternResolver(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @Override
    public Configuration getConfig() {
        if (config == null) {
            String configLocationProperty = System.getProperty(CONFIG_LOCATION_PROPERTY, DEFAULT_CONFIG_LOCATION);
            String[] configLocations = StringUtils.split(configLocationProperty, ",; \t\n");
            configLocations = StringUtils.stripAll(configLocations);

            List<AbstractConfiguration> configList = new ArrayList<>();
            for (String configLocation : configLocations) {
                if (StringUtils.isBlank(configLocation)) {
                    continue;
                }

                Resource configResource = getResourceLoader().getResource(configLocation);
                if (!configResource.exists()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(RBMF.get("未找到系统配置文件 {}"), configResource);
                    }
                    continue;
                }

                try {
                    PropertiesConfiguration memberConfig = new PropertiesConfiguration();
                    FileHandler fileHandler = new FileHandler(memberConfig);
                    fileHandler.load(configResource.getInputStream());
                    configList.add(memberConfig);
                    LOG.info(RBMF.get("系统配置文件 {} 加载完成"), configResource);
                } catch (IOException | ConfigurationException e) {
                    throw new ConfigurationRuntimeException(RBMF.format("系统配置文件 {0} 加载失败", configResource), e);
                }
            }
            if (configList.isEmpty()) {
                LOG.info(RBMF.get("未加载系统配置文件"));
                config = new PropertiesConfiguration();
            } else if (configList.size() == 1) {
                config = configList.get(0);
            } else {
                CombinedConfiguration combinedConfig = new CombinedConfiguration(new OverrideCombiner());
                for (int index = configList.size() - 1; index >= 0; index--) {
                    combinedConfig.addConfiguration(configList.get(index));
                }
                config = combinedConfig;
            }
        }
        return config;
    }

    @Override
    public void setConfig(Configuration config) {
        this.config = config;
    }

    @Override
    public void registerObject(String name, Object object) {
        objects.put(name, object);
    }

    @Override
    public <T> void registerObject(Class<T> type, T object) {
        registerObject(type.getName(), object);
    }

    @Override
    public <T> T lookupObject(String name) {
        return (T) objects.get(name);
    }

    @Override
    public <T> T lookupObject(String name, Class<T> type) {
        return type.cast(lookupObject(name));
    }

    @Override
    public <T> T lookupObject(Class<T> type) {
        return lookupObject(type.getName(), type);
    }

    @Override
    public SystemController getController() {
        if (controller == null) {
            String controllerClassProperty = System.getProperty(CONTROLLER_CLASS_PROPERTY);
            if (controllerClassProperty == null) {
                controller = new DefaultSystemController();
            } else {
                try {
                    Class<?> controllerClass = ClassUtils.getClass(controllerClassProperty);
                    Constructor<?> controllerConstructor = controllerClass.getDeclaredConstructor();
                    ReflectionMx.makeAccessible(controllerConstructor);
                    controller = (SystemController) controllerConstructor.newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new ConfigurationRuntimeException(RBMF.format("控制器类 {0} 实例化失败", controllerClassProperty), e);
                }
            }
            controller.setContext(this);
        }
        return controller;
    }

    @Override
    public void setController(SystemController controller) {
        this.controller = controller;
        if (this.controller != null) {
            this.controller.setContext(this);
        }
    }
}
