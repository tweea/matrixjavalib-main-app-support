/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.matrix.java.util.LocaleMx;

import static org.assertj.core.api.Assertions.assertThat;

public class CodedMessageDefinitionLoaderTest {
    @BeforeAll
    public static void beforeAll() {
        CodedMessageDefinitionLoader.loadBuiltinDefinitions();
    }

    @Test
    public void testGetDefinition() {
        LocaleMx.current(Locale.CHINA);
        CodedMessageDefinition part = CodedMessageDefinition.get("System.Error");
        assertThat(part.getCode()).isEqualTo("System.Error");
        assertThat(part.getTemplate()).isEqualTo("系统错误");

        LocaleMx.current(Locale.US);
        part = CodedMessageDefinition.get("System.Error");
        assertThat(part.getCode()).isEqualTo("System.Error");
        assertThat(part.getTemplate()).isEqualTo("System error");
    }
}
