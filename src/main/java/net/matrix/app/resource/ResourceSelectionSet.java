/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import net.matrix.java.util.CollectionMx;

/**
 * 资源仓库选择集合。
 */
public class ResourceSelectionSet {
    /**
     * 内部选择集合。
     */
    @Nonnull
    private final Set<ResourceSelection> selections;

    /**
     * 构造器。
     */
    public ResourceSelectionSet() {
        this.selections = new HashSet<>();
    }

    /**
     * 增加一个资源仓库选择。
     *
     * @param selection
     *     资源仓库选择。
     */
    public void add(@Nonnull ResourceSelection selection) {
        selections.add(selection);
    }

    /**
     * 判断是否已包含指定资源仓库选择。
     *
     * @param selection
     *     资源仓库选择。
     * @return 是否已包含指定资源仓库选择。
     */
    public boolean contains(@Nonnull ResourceSelection selection) {
        return selections.contains(selection);
    }

    /**
     * 移除指定资源仓库选择。
     *
     * @param selection
     *     资源仓库选择。
     * @return 是否已包含指定资源仓库选择。
     */
    public boolean remove(@Nonnull ResourceSelection selection) {
        return selections.remove(selection);
    }

    /**
     * 获取所有资源仓库选择的类别集合。
     *
     * @return 类别集合。
     */
    @Nonnull
    public Set<String> getCatalogs() {
        return CollectionMx.buildSet(selections, ResourceSelection::getCatalog);
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
        Set<String> names = new HashSet<>();
        for (ResourceSelection selection : selections) {
            if (selection.getCatalog().equals(catalog)) {
                names.add(selection.getName());
            }
        }
        return names;
    }

    /**
     * 选择指定类别的默认名称资源。
     *
     * @param catalog
     *     类别。
     * @return 资源仓库选择集合。
     */
    @Nonnull
    public Set<ResourceSelection> getSelections(@Nonnull String catalog) {
        return getSelections(catalog, ResourceSelection.generateName(catalog));
    }

    /**
     * 选择指定类别的指定名称资源。
     *
     * @param catalog
     *     类别。
     * @param name
     *     名称。
     * @return 资源仓库选择集合。
     */
    @Nonnull
    public Set<ResourceSelection> getSelections(@Nonnull String catalog, @Nonnull String name) {
        Set<ResourceSelection> result = new HashSet<>();
        for (ResourceSelection selection : selections) {
            if (selection.getCatalog().equals(catalog) && selection.getName().equals(name)) {
                result.add(selection);
            }
        }
        return result;
    }

    /**
     * 检查本集合与另一集合包含资源仓库选择的差异。
     *
     * @param target
     *     另一集合。
     * @return 差异集合。
     */
    @Nonnull
    public Set<ResourceSelection> checkDiff(@Nonnull ResourceSelectionSet target) {
        Set<ResourceSelection> diffs = new HashSet<>();
        diffs.addAll(target.selections);
        diffs.removeAll(selections);
        return diffs;
    }
}
