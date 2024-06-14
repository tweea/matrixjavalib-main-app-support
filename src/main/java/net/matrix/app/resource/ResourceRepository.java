/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import net.matrix.text.ResourceBundleMessageFormatter;

/**
 * 资源仓库，根据指定的根资源相对定位所有资源。
 */
@Immutable
public class ResourceRepository {
    /**
     * 日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(ResourceRepository.class);

    /**
     * 区域相关资源。
     */
    private static final ResourceBundleMessageFormatter RBMF = new ResourceBundleMessageFormatter(ResourceRepository.class).useCurrentLocale();

    /**
     * 根资源。
     */
    @Nonnull
    private final Resource root;

    /**
     * 构造器。
     */
    public ResourceRepository(@Nonnull Resource root) {
        this.root = root;
    }

    /**
     * 定位资源。
     * 
     * @param selection
     *     资源仓库选择。
     * @return 资源。
     */
    @Nullable
    public Resource getResource(@Nonnull ResourceSelection selection) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(RBMF.get("开始定位资源 {}/{}"), root, selection);
        }

        String catalog = selection.getCatalog();
        String version = selection.getVersion();
        String name = selection.getName();

        String path = catalog;
        if (StringUtils.isNotBlank(version)) {
            path = path + '/' + version;
        }
        while (true) {
            Resource resource;
            try {
                resource = root.createRelative(path + '/' + name);
            } catch (IOException e) {
                LOG.warn(RBMF.get("定位资源 {}/{}/{} 失败"), root, path, name, e);
                break;
            }
            if (resource.exists()) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace(RBMF.get("定位资源 {}/{} 到 {}"), root, selection, resource);
                }
                return resource;
            }
            if (path.length() <= catalog.length()) {
                break;
            }
            path = path.substring(0, path.lastIndexOf('/'));
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(RBMF.get("没有定位资源 {}/{}"), root, selection);
        }
        return null;
    }
}
