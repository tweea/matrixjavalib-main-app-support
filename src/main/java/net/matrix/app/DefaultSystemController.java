/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import javax.annotation.Nonnull;

/**
 * 默认的系统控制器，在应用中继承本类以扩展功能。
 */
public class DefaultSystemController
    implements SystemController {
    /**
     * 系统环境。
     */
    @Nonnull
    private SystemContext context;

    @Override
    public SystemContext getContext() {
        return context;
    }

    @Override
    public void setContext(SystemContext context) {
        this.context = context;
    }

    @Override
    public void init() {
        // 空实现
    }

    @Override
    public void start() {
        // 空实现
    }

    @Override
    public void stop() {
        // 空实现
    }

    @Override
    public void reset() {
        // 空实现
    }
}
