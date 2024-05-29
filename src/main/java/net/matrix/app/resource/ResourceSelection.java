/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.resource;

import java.util.Objects;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 资源仓库选择，指向资源仓库中的一项资源。
 */
@Immutable
public class ResourceSelection {
    /**
     * 类别。
     */
    private final String catalog;

    /**
     * 版本。
     */
    private final String version;

    /**
     * 名称。
     */
    private final String name;

    /**
     * 构造器。如果未提供名称，则根据类别生成默认的名称。
     * 
     * @param catalog
     *     类别。
     * @param version
     *     版本。
     * @param name
     *     名称。
     */
    public ResourceSelection(String catalog, String version, String name) {
        this.catalog = catalog;
        this.version = version;
        if (StringUtils.isBlank(name)) {
            this.name = generateName(catalog);
        } else {
            this.name = name;
        }
    }

    /**
     * 根据类别生成默认的名称。
     * 
     * @param catalog
     *     类别。
     * @return 名称。
     */
    public static String generateName(String catalog) {
        String[] catalogs = StringUtils.split(catalog, '/');
        return catalogs[catalogs.length - 1];
    }

    /**
     * 获取类别。
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * 获取版本。
     */
    public String getVersion() {
        return version;
    }

    /**
     * 获取名称。
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ResourceSelection[" + catalog + ':' + ObjectUtils.defaultIfNull(version, "") + ':' + name + ']';
    }

    @Override
    public int hashCode() {
        return Objects.hash(catalog, name, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ResourceSelection)) {
            return false;
        }
        ResourceSelection other = (ResourceSelection) obj;
        return Objects.equals(catalog, other.catalog) && Objects.equals(name, other.name) && Objects.equals(version, other.version);
    }
}
