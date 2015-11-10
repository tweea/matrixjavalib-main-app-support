/*
 * Copyright(C) 2008 Matrix
 * All right reserved.
 */
package net.matrix.app;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
		return get("");
	}

	/**
	 * 获取系统环境的全局实例，如果不存在则建立默认的系统环境实例。
	 *
	 * @param id
	 *            系统环境 ID
	 * @return 系统环境的全局实例
	 */
	public static SystemContext get(final String id) {
		if (!GLOBAL_CONTEXTS.containsKey(id)) {
			GLOBAL_CONTEXTS.putIfAbsent(id, new DefaultSystemContext());
		}
		return GLOBAL_CONTEXTS.get(id);
	}

	/**
	 * 设置系统环境的全局实例。
	 * 
	 * @param context
	 *            系统环境
	 */
	public static void set(final SystemContext context) {
		set("", context);
	}

	/**
	 * 设置系统环境的全局实例。
	 * 
	 * @param id
	 *            系统环境 ID
	 * @param context
	 *            系统环境
	 */
	public static void set(final String id, final SystemContext context) {
		if (context == null) {
			GLOBAL_CONTEXTS.remove(id);
		} else {
			GLOBAL_CONTEXTS.put(id, context);
		}
	}
}
