/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationRuntimeException;
import org.apache.commons.configuration.PropertiesConfiguration;
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

	private ResourceLoader resourceLoader;

	private ResourcePatternResolver resourceResolver;

	private Configuration config;

	private final Map<String, Object> objects;

	private SystemController controller;

	/**
	 * 默认构造器。
	 */
	public DefaultSystemContext() {
		objects = new HashMap<>();
	}

	@Override
	public void setResourceLoader(final ResourceLoader loader) {
		resourceLoader = loader;
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
		// 尝试加载默认位置
		if (config == null) {
			LOG.info("加载默认配置");
			Resource resource = getResourceLoader().getResource("classpath:sysconfig.cfg");
			// TODO sysconfig.dev.cfg
			try {
				config = new PropertiesConfiguration(resource.getURL());
			} catch (IOException e) {
				throw new ConfigurationRuntimeException("sysconfig.cfg 加载失败", e);
			} catch (ConfigurationException e) {
				throw new ConfigurationRuntimeException("sysconfig.cfg 加载失败", e);
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
