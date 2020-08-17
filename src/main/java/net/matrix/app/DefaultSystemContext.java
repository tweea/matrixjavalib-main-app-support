/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import java.io.IOException;
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
     * 系统配置位置的系统属性名。
     */
    private static final String CONFIG_LOCATION_PROPERTY = "systemConfigLocation";

    /**
     * 系统控制器类名的系统属性名。
     */
    private static final String CONTROLLER_CLASS_PROPERTY = "systemControllerClass";

    protected ResourceLoader resourceLoader;

    protected ResourcePatternResolver resourceResolver;

    protected Configuration config;

    protected final Map<String, Object> objects;

    protected SystemController controller;

    /**
     * 默认构造器。
     */
    public DefaultSystemContext() {
        this.objects = new HashMap<>();
    }

    @Override
    public void setResourceLoader(final ResourceLoader loader) {
        this.resourceLoader = loader;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        if (resourceLoader == null) {
            resourceLoader = new DefaultResourceLoader();
        }
        return resourceLoader;
    }

    @Override
    public ResourcePatternResolver getResourcePatternResolver() {
        if (resourceResolver == null) {
            if (getResourceLoader() instanceof ResourcePatternResolver) {
                resourceResolver = (ResourcePatternResolver) resourceLoader;
            } else {
                resourceResolver = new PathMatchingResourcePatternResolver(resourceLoader);
            }
        }
        return resourceResolver;
    }

    @Override
    public void setConfig(final Configuration config) {
        this.config = config;
    }

    @Override
    public Configuration getConfig() {
        if (config == null) {
            String configLocationsProperty = System.getProperty(CONFIG_LOCATION_PROPERTY);
            if (configLocationsProperty == null) {
                configLocationsProperty = "classpath:sysconfig.cfg,classpath:sysconfig.dev.cfg";
            }

            String[] configLocations = StringUtils.split(configLocationsProperty, ",; \t\n");
            configLocations = StringUtils.stripAll(configLocations);

            List<AbstractConfiguration> configList = new ArrayList<>();
            for (String configLocation : configLocations) {
                if (StringUtils.isBlank(configLocation)) {
                    continue;
                }
                Resource configResource = getResourceLoader().getResource(configLocation);
                if (!configResource.exists()) {
                    LOG.info("系统配置文件 {} 不存在", configResource);
                    continue;
                }
                try {
                    PropertiesConfiguration memberConfig = new PropertiesConfiguration();
                    FileHandler fileHandler = new FileHandler(memberConfig);
                    fileHandler.load(configResource.getInputStream());
                    configList.add(memberConfig);
                    LOG.info("系统配置文件 {} 加载完成", configResource);
                } catch (IOException e) {
                    throw new ConfigurationRuntimeException("系统配置文件 " + configResource + " 加载失败", e);
                } catch (ConfigurationException e) {
                    throw new ConfigurationRuntimeException("系统配置文件 " + configResource + " 加载失败", e);
                }
            }
            if (configList.isEmpty()) {
                LOG.info("系统配置文件未找到");
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
    public void registerObject(final String name, final Object object) {
        objects.put(name, object);
    }

    @Override
    public <T> void registerObject(final Class<T> type, final T object) {
        registerObject(type.getName(), object);
    }

    @Override
    public Object lookupObject(final String name) {
        return objects.get(name);
    }

    @Override
    public <T> T lookupObject(final String name, final Class<T> type) {
        return type.cast(lookupObject(name));
    }

    @Override
    public <T> T lookupObject(final Class<T> type) {
        return lookupObject(type.getName(), type);
    }

    @Override
    public void setController(final SystemController controller) {
        this.controller = controller;
        if (this.controller != null) {
            this.controller.setContext(this);
        }
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
                    controller = (SystemController) controllerClass.newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new ConfigurationRuntimeException("控制器类 " + controllerClassProperty + " 实例化失败", e);
                }
            }
            controller.setContext(this);
        }
        return controller;
    }
}
