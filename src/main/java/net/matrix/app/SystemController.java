/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import javax.annotation.Nonnull;

import net.matrix.lang.Resettable;

/**
 * 系统控制器，负责系统的初始化、启动、停止。
 */
public interface SystemController
    extends Resettable {
    /**
     * 获取系统环境。
     *
     * @return 系统环境。
     */
    @Nonnull
    SystemContext getContext();

    /**
     * 设置系统环境。
     *
     * @param context
     *     系统环境。
     */
    void setContext(@Nonnull SystemContext context);

    /**
     * 初始化系统。
     */
    void init();

    /**
     * 启动系统。
     */
    void start();

    /**
     * 停止系统。
     */
    void stop();
}
