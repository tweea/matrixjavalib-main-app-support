/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * 全局系统环境，保存系统环境的全局实例。
 */
@ThreadSafe
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
     * 获取系统环境的全局实例，使用缺省的唯一标识，如果不存在则建立默认的系统环境实例。
     *
     * @return 系统环境的全局实例。
     */
    @Nonnull
    public static SystemContext get() {
        return get("");
    }

    /**
     * 获取系统环境的全局实例，使用指定的唯一标识，如果不存在则建立默认的系统环境实例。
     *
     * @param id
     *     唯一标识。
     * @return 系统环境的全局实例。
     */
    @Nonnull
    public static SystemContext get(@Nonnull String id) {
        return GLOBAL_CONTEXTS.computeIfAbsent(id, key -> new DefaultSystemContext());
    }

    /**
     * 设置系统环境的全局实例，使用缺省的唯一标识。
     *
     * @param context
     *     系统环境。
     */
    public static void set(@Nullable SystemContext context) {
        set("", context);
    }

    /**
     * 设置系统环境的全局实例，使用指定的唯一标识。
     *
     * @param id
     *     唯一标识。
     * @param context
     *     系统环境。
     */
    public static void set(@Nonnull String id, @Nullable SystemContext context) {
        if (context == null) {
            GLOBAL_CONTEXTS.remove(id);
        } else {
            GLOBAL_CONTEXTS.put(id, context);
        }
    }
}
