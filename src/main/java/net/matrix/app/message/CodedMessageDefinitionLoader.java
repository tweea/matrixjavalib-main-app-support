/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.io.IOException;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import net.matrix.text.ResourceBundleMessageFormatter;

/**
 * 编码消息定义加载工具。
 */
@ThreadSafe
public final class CodedMessageDefinitionLoader {
    /**
     * 日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(CodedMessageDefinitionLoader.class);

    /**
     * 区域相关资源。
     */
    private static final ResourceBundleMessageFormatter RBMF = new ResourceBundleMessageFormatter(CodedMessageDefinitionLoader.class).useCurrentLocale();

    /**
     * 阻止实例化。
     */
    private CodedMessageDefinitionLoader() {
    }

    /**
     * 扫描类路径，加载所有名称符合 codedMessageDefinition(_*).xml 的配置文件。
     */
    public static void loadBuiltinDefinitions() {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        loadDefinitions(resourcePatternResolver, "classpath*:codedMessageDefinition*.xml", "codedMessageDefinition_");
    }

    /**
     * 加载所有名称符合特定匹配模式的配置文件。
     * 
     * @param resourcePatternResolver
     *     资源扫描器。
     * @param locationPattern
     *     匹配模式。
     * @param filenamePrefix
     *     文件名前缀。
     */
    public static void loadDefinitions(@Nonnull ResourcePatternResolver resourcePatternResolver, @Nonnull String locationPattern,
        @Nonnull String filenamePrefix) {
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(locationPattern);
        } catch (IOException e) {
            LOG.error(RBMF.get("编码消息定义加载失败"), e);
            return;
        }

        for (Resource resource : resources) {
            String fileBaseName = FilenameUtils.getBaseName(resource.getFilename());
            Locale locale;
            if (fileBaseName.startsWith(filenamePrefix)) {
                locale = LocaleUtils.toLocale(fileBaseName.substring(filenamePrefix.length()));
            } else {
                locale = Locale.ROOT;
            }
            loadDefinitions(locale, resource);
        }
    }

    /**
     * 加载配置文件。
     * 
     * @param locale
     *     区域。
     * @param resource
     *     配置文件。
     */
    public static void loadDefinitions(@Nonnull Locale locale, @Nonnull Resource resource) {
        XMLConfiguration config = new XMLConfiguration();
        try {
            FileHandler fileHandler = new FileHandler(config);
            fileHandler.load(resource.getInputStream());
        } catch (IOException | ConfigurationException e) {
            LOG.error(RBMF.get("编码消息定义加载失败"), e);
        }

        for (HierarchicalConfiguration definitionConfig : config.configurationsAt("definition")) {
            String code = definitionConfig.getString("[@code]");
            String template = definitionConfig.getString("[@template]");
            CodedMessageDefinition.add(new CodedMessageDefinition(code, locale, template));
        }
    }
}
