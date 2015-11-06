/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.app;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// TODO 支持多个系统环境
/**
 * 全局系统环境，保存系统环境的全局实例。
 */
public final class GlobalSystemContext {
	/**
	 * 系统环境的全局实例。
	 */
	private static final ConcurrentMap<String, SystemContext> GLOBAL_CONTEXTS = new ConcurrentHashMap<>();

	/**
	 * 阻止实例化。
	 */
	private GlobalSystemContext() {
	}

	/**
	 * 获取系统环境的全局实例，如果不存在则建立默认的系统环境实例。
	 * 
	 * @return 系统环境的全局实例
	 */
	public static SystemContext get() {
		if (!GLOBAL_CONTEXTS.containsKey("")) {
			GLOBAL_CONTEXTS.putIfAbsent("", new DefaultSystemContext());
		}
		return GLOBAL_CONTEXTS.get("");
	}

	/**
	 * 设置系统环境的全局实例。
	 * 
	 * @param context
	 *            系统环境
	 */
	public static void set(final SystemContext context) {
		if (context == null) {
			GLOBAL_CONTEXTS.remove("");
		} else {
			GLOBAL_CONTEXTS.put("", context);
		}
	}
}
