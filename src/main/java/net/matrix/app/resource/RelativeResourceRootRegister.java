/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.springframework.core.io.Resource;

import net.matrix.text.ResourceBundleMessageFormatter;

/**
 * 管理相对定位资源的根资源，提供将相对定位资源转换为实际资源的方法。
 * 操作相对定位资源前，需要先注册对应根资源。
 */
public class RelativeResourceRootRegister {
    /**
     * 区域相关资源。
     */
    private static final ResourceBundleMessageFormatter RBMF = new ResourceBundleMessageFormatter(RelativeResourceRootRegister.class).useCurrentLocale();

    /**
     * 所有已注册的根资源。
     */
    @Nonnull
    private final Map<String, Resource> roots;

    /**
     * 构造器。
     */
    public RelativeResourceRootRegister() {
        this.roots = new HashMap<>();
    }

    /**
     * 注册根资源到根路径名。
     * 
     * @param name
     *     根路径名。
     * @param root
     *     根资源。
     */
    public void registerRoot(@Nonnull String name, @Nonnull Resource root) {
        roots.put(name, root);
    }

    /**
     * 获取已注册的根资源。
     * 
     * @param name
     *     根路径名。
     * @return 根资源。
     */
    @Nullable
    public Resource getRoot(@Nonnull String name) {
        return roots.get(name);
    }

    /**
     * 获取相对定位资源对应的实际资源。
     * 
     * @param relativeResource
     *     相对定位资源。
     * @return 实际资源。
     * @throws IOException
     *     获取失败。
     * @throws IllegalStateException
     *     根路径名未注册。
     */
    @Nonnull
    public Resource getResource(@Nonnull RelativeResource relativeResource)
        throws IOException {
        String name = relativeResource.getRootName();
        Resource root = roots.get(name);
        if (root == null) {
            throw new IllegalStateException(RBMF.format("根路径名 {0} 未注册", name));
        }
        return root.createRelative(relativeResource.getRelativePath());
    }
}
