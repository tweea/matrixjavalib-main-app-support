/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.matrix.java.util.LocaleMx;

/**
 * 编码消息定义。
 */
public class CodedMessageDefinition {
    /**
     * 加载的编码消息定义。
     */
    private static final Map<String, Map<Locale, CodedMessageDefinition>> DEFINITIONS = new HashMap<>();

    /**
     * 编码。
     */
    @Nonnull
    private final String code;

    /**
     * 区域。
     */
    @Nonnull
    private final Locale locale;

    /**
     * 模板。
     */
    @Nonnull
    private final String template;

    /**
     * 获取编码消息定义。
     * 
     * @param code
     *     编码。
     * @return 编码消息定义。
     */
    @Nullable
    public static CodedMessageDefinition get(@Nonnull String code) {
        Map<Locale, CodedMessageDefinition> definitions = DEFINITIONS.get(code);
        if (definitions == null) {
            return null;
        }

        CodedMessageDefinition definition = definitions.get(LocaleMx.current());
        if (definition == null) {
            definition = definitions.get(Locale.ROOT);
        }
        return definition;
    }

    /**
     * 增加编码消息定义。
     * 
     * @param definition
     *     编码消息定义。
     */
    public static void add(@Nonnull CodedMessageDefinition definition) {
        String code = definition.getCode();
        Locale locale = definition.getLocale();

        Map<Locale, CodedMessageDefinition> definitions = DEFINITIONS.computeIfAbsent(code, key -> new HashMap<>());
        definitions.put(locale, definition);
    }

    /**
     * 构造器。
     * 
     * @param code
     *     编码。
     * @param locale
     *     区域。
     * @param template
     *     模板。
     */
    public CodedMessageDefinition(@Nonnull String code, @Nonnull Locale locale, @Nonnull String template) {
        this.code = code;
        this.locale = locale;
        this.template = template;
    }

    /**
     * 获取编码。
     */
    @Nonnull
    public String getCode() {
        return code;
    }

    /**
     * 获取区域。
     */
    @Nonnull
    public Locale getLocale() {
        return locale;
    }

    /**
     * 获取模板。
     */
    @Nonnull
    public String getTemplate() {
        return template;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, locale, template);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CodedMessageDefinition)) {
            return false;
        }
        CodedMessageDefinition other = (CodedMessageDefinition) obj;
        return Objects.equals(code, other.code) && Objects.equals(locale, other.locale) && Objects.equals(template, other.template);
    }
}
