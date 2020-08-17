/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.io.IOException;
import java.util.Locale;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 加载编码消息定义。
 */
public final class CodedMessageDefinitionLoader {
    /**
     * 日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(CodedMessageDefinitionLoader.class);

    /**
     * 阻止实例化。
     */
    private CodedMessageDefinitionLoader() {
    }

    /**
     * 从类路径中加载所有名为 codedMessageDefinition.xml 的配置文件。
     */
    public static void loadBuiltinDefinitions() {
        loadDefinitions(new PathMatchingResourcePatternResolver(), "classpath*:codedMessageDefinition*.xml");
    }

    /**
     * 加载所有匹配的配置文件。
     * 
     * @param resolver
     *     资源加载策略
     * @param locationPattern
     *     匹配模式
     */
    public static void loadDefinitions(final ResourcePatternResolver resolver, final String locationPattern) {
        try {
            Resource[] resources = resolver.getResources(locationPattern);
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                Locale locale = Locale.ROOT;
                String filenamePrefix = "codedMessageDefinition_";
                if (filename.startsWith(filenamePrefix)) {
                    locale = LocaleUtils.toLocale(filename.substring(filenamePrefix.length(), filename.lastIndexOf(".xml")));
                }
                loadDefinitions(locale, resource);
            }
        } catch (IOException e) {
            LOG.error("加载失败", e);
        }
    }

    /**
     * 从指定位置加载配置文件。
     * 
     * @param locale
     *     区域
     * @param resource
     *     配置文件位置
     */
    public static void loadDefinitions(final Locale locale, final Resource resource) {
        try {
            XMLConfiguration config = new XMLConfiguration();
            FileHandler fileHandler = new FileHandler(config);
            fileHandler.load(resource.getInputStream());
            for (HierarchicalConfiguration definitionConfig : config.configurationsAt("definition")) {
                String code = definitionConfig.getString("[@code]");
                String template = definitionConfig.getString("[@template]");
                CodedMessageDefinition.define(new CodedMessageDefinition(code, locale, template));
            }
        } catch (IOException e) {
            LOG.error("加载失败", e);
        } catch (ConfigurationException e) {
            LOG.error("加载失败", e);
        }
    }
}
