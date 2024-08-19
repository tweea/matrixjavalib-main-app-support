/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Iterables;

import net.matrix.configuration.XMLConfigurationContainer;

/**
 * 资源仓库加载环境配置，包含资源仓库选择集合。
 */
public class ResourceContextConfig
    extends XMLConfigurationContainer {
    /**
     * 资源仓库选择集合。
     */
    @Nonnull
    private ResourceSelectionSet selectionSet;

    @Override
    public void reset() {
        super.reset();

        XMLConfiguration config;
        try {
            config = getConfig();
        } catch (ConfigurationException e) {
            throw new ConfigurationRuntimeException(e);
        }

        selectionSet = new ResourceSelectionSet();
        // catalog 节点
        List<HierarchicalConfiguration<ImmutableNode>> catalogNodes = config.configurationsAt("catalog");
        for (HierarchicalConfiguration catalogNode : catalogNodes) {
            String catalog = catalogNode.getString("[@name]");
            String version = catalogNode.getString("[@version]");
            // resource 节点
            List<HierarchicalConfiguration> resourceNodes = catalogNode.configurationsAt("file");
            if (resourceNodes.isEmpty()) {
                selectionSet.add(new ResourceSelection(catalog, version, null));
            } else {
                for (HierarchicalConfiguration resourceNode : resourceNodes) {
                    String resourceName = resourceNode.getString("[@name]");
                    String resourceVersion = resourceNode.getString("[@version]");
                    String branch = resourceNode.getString("[@branch]");
                    if (StringUtils.isEmpty(resourceVersion)) {
                        resourceVersion = version;
                    }
                    if (!StringUtils.isEmpty(branch)) {
                        resourceVersion = resourceVersion + '/' + branch;
                    }
                    selectionSet.add(new ResourceSelection(catalog, resourceVersion, resourceName));
                }
            }
        }
    }

    /**
     * 获取所有资源仓库选择的类别集合。
     *
     * @return 类别集合。
     */
    @Nonnull
    public Set<String> getCatalogs() {
        checkReload();
        return selectionSet.getCatalogs();
    }

    /**
     * 获取特定类别的资源仓库选择的名称集合。
     *
     * @param catalog
     *     类别。
     * @return 名称集合。
     */
    @Nonnull
    public Set<String> getNames(@Nonnull String catalog) {
        checkReload();
        return selectionSet.getNames(catalog);
    }

    /**
     * 选择指定类别的默认名称资源。
     *
     * @param catalog
     *     类别。
     * @return 资源仓库选择。
     */
    @Nullable
    public ResourceSelection getSelection(@Nonnull String catalog) {
        checkReload();
        Set<ResourceSelection> result = selectionSet.getSelections(catalog);
        return Iterables.getFirst(result, null);
    }

    /**
     * 选择指定类别的指定名称资源。
     *
     * @param catalog
     *     类别。
     * @param name
     *     名称。
     * @return 资源仓库选择。
     */
    @Nullable
    public ResourceSelection getSelection(@Nonnull String catalog, @Nonnull String name) {
        checkReload();
        Set<ResourceSelection> result = selectionSet.getSelections(catalog, name);
        return Iterables.getFirst(result, null);
    }

    /**
     * 检查本配置与另一配置包含资源仓库选择的差异。
     *
     * @param target
     *     另一配置。
     * @return 差异集合。
     */
    @Nonnull
    public Set<ResourceSelection> checkDiff(@Nonnull ResourceContextConfig target) {
        checkReload();
        return selectionSet.checkDiff(target.selectionSet);
    }
}
