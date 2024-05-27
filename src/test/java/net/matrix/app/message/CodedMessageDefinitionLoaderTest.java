/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import net.matrix.java.util.LocaleMx;

import static org.assertj.core.api.Assertions.assertThat;

public class CodedMessageDefinitionLoaderTest {
    @Test
    public void testLoadBuiltinDefinitions() {
        CodedMessageDefinitionLoader.loadBuiltinDefinitions();

        LocaleMx.current(Locale.CHINA);
        CodedMessageDefinition definition = CodedMessageDefinition.get("System.Error");
        assertThat(definition.getCode()).isEqualTo("System.Error");
        assertThat(definition.getTemplate()).isEqualTo("系统错误");

        LocaleMx.current(Locale.US);
        definition = CodedMessageDefinition.get("System.Error");
        assertThat(definition.getCode()).isEqualTo("System.Error");
        assertThat(definition.getTemplate()).isEqualTo("System error");
    }
}
