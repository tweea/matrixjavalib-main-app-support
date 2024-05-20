/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.util.List;
import java.util.Set;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Iterables;

import net.matrix.configuration.XMLConfigurationContainer;

/**
 * 资源仓库加载环境配置，保存了一系列资源仓库选择。
 */
public class ResourceContextConfig
    extends XMLConfigurationContainer {
    /**
     * 根据配置内容生成的选择集合。
     */
    private ResourceSelectionSet selectionSet;

    @Override
    public void reset() {
        super.reset();
        selectionSet = new ResourceSelectionSet();

        XMLConfiguration config;
        try {
            config = getConfig();
        } catch (ConfigurationException e) {
            throw new ConfigurationRuntimeException(e);
        }

        // catalog 节点
        List<HierarchicalConfiguration<ImmutableNode>> catalogsNode = config.configurationsAt("catalog");
        for (HierarchicalConfiguration catalogNode : catalogsNode) {
            String catalog = catalogNode.getString("[@name]");
            String version = catalogNode.getString("[@version]");
            // resource 节点
            List<HierarchicalConfiguration> resourcesNode = catalogNode.configurationsAt("file");
            if (resourcesNode.isEmpty()) {
                selectionSet.add(new ResourceSelection(catalog, version, null));
            } else {
                for (HierarchicalConfiguration resourceNode : resourcesNode) {
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
     * 类别名称集合。
     */
    public Set<String> catalogNames() {
        checkReload();
        return selectionSet.catalogNames();
    }

    /**
     * 资源名称集合。
     * 
     * @param catalog
     *     类别
     * @return 资源名称集合
     */
    public Set<String> resourceNames(final String catalog) {
        checkReload();
        return selectionSet.resourceNames(catalog);
    }

    /**
     * 选择指定类别的默认名称资源。
     * 
     * @param catalog
     *     类别
     * @return 资源选择
     */
    public ResourceSelection getSelection(final String catalog) {
        checkReload();
        Set<ResourceSelection> result = selectionSet.getSelections(catalog);
        return Iterables.getFirst(result, null);
    }

    /**
     * 选择指定类别的指定名称资源。
     * 
     * @param catalog
     *     类别
     * @param name
     *     资源名
     * @return 资源选择
     */
    public ResourceSelection getSelection(final String catalog, final String name) {
        checkReload();
        Set<ResourceSelection> result = selectionSet.getSelections(catalog, name);
        return Iterables.getFirst(result, null);
    }

    /**
     * 检查本配置与另一配置包含资源仓库选择的差异。
     * 
     * @param target
     *     另一配置
     * @return 差异集合
     */
    public Set<ResourceSelection> checkDiff(final ResourceContextConfig target) {
        checkReload();
        return selectionSet.checkDiff(target.selectionSet);
    }
}
