/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;

/**
 * 管理相对定位资源的根资源，提供将相对定位资源转换为实际资源的方法。
 * 操作相对定位资源前，需要先注册对应根资源。
 */
public class RelativeResourceRootRegister {
    /**
     * 所有注册的根资源。
     */
    private final Map<String, Resource> roots;

    /**
     * 构造未注册根资源的实例。
     */
    public RelativeResourceRootRegister() {
        this.roots = new HashMap<>();
    }

    /**
     * 注册根资源到根路径名。
     * 
     * @param name
     *     根路径名
     * @param root
     *     根资源
     */
    public void registerRoot(final String name, final Resource root) {
        roots.put(name, root);
    }

    /**
     * 获取注册的根资源。
     * 
     * @param name
     *     根路径名
     * @return 根资源
     */
    public Resource getRoot(final String name) {
        return roots.get(name);
    }

    /**
     * 获取相对定位资源对应的实际资源。
     * 
     * @param relativeResource
     *     相对定位资源
     * @return 实际资源
     * @throws IOException
     *     获取失败
     * @throws IllegalStateException
     *     根路径未注册
     */
    public Resource getResource(final RelativeResource relativeResource)
        throws IOException {
        Resource root = getRoot(relativeResource.getRootName());
        if (root == null) {
            throw new IllegalStateException("根路径 " + relativeResource.getRootName() + " 未注册");
        }
        return root.createRelative(relativeResource.getRelativePath());
    }
}
