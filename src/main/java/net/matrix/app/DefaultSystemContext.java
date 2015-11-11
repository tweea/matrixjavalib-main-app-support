/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationRuntimeException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.tree.OverrideCombiner;
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
			String configLocationsParam = "classpath:sysconfig.cfg,classpath:sysconfig.dev.cfg";

			String[] configLocations = StringUtils.split(configLocationsParam, ",; \t\n");
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
					configList.add(new PropertiesConfiguration(configResource.getURL()));
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
	}

	@Override
	public SystemController getController() {
		if (controller == null) {
			controller = new DefaultSystemController();
			controller.setContext(this);
		}
		return controller;
	}
}
