/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * 相对定位资源。
 */
@Immutable
public class RelativeResource {
    /**
     * 根路径名。
     */
    @Nonnull
    private final String rootName;

    /**
     * 相对路径。
     */
    @Nonnull
    private final String relativePath;

    /**
     * 构造器，使用基于根路径名的相对路径。
     * 
     * @param rootName
     *     根路径名。
     * @param relativePath
     *     相对路径。
     */
    public RelativeResource(@Nonnull String rootName, @Nonnull String relativePath) {
        this.rootName = rootName;
        this.relativePath = relativePath;
    }

    /**
     * 构造器，使用基于上级资源的相对路径。
     * 
     * @param parent
     *     上级资源。
     * @param relativePath
     *     相对路径。
     */
    public RelativeResource(@Nonnull RelativeResource parent, @Nonnull String relativePath) {
        this.rootName = parent.rootName;
        this.relativePath = parent.relativePath + '/' + relativePath;
    }

    /**
     * 获取根路径名。
     * 
     * @return 根路径名。
     */
    @Nonnull
    public String getRootName() {
        return rootName;
    }

    /**
     * 获取相对路径。
     * 
     * @return 相对路径。
     */
    @Nonnull
    public String getRelativePath() {
        return relativePath;
    }

    @Override
    public String toString() {
        return "RelativeResource[" + rootName + '/' + relativePath + ']';
    }

    @Override
    public int hashCode() {
        return Objects.hash(relativePath, rootName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RelativeResource)) {
            return false;
        }
        RelativeResource other = (RelativeResource) obj;
        return Objects.equals(relativePath, other.relativePath) && Objects.equals(rootName, other.rootName);
    }
}
