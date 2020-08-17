/*
 * Copyright(C) 2009 matrix
 * All right reserved.
 */
package net.matrix.app.message;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.matrix.text.Locales;

import static org.assertj.core.api.Assertions.assertThat;

public class CodedMessageDefinitionLoaderTest {
    @BeforeAll
    public static void beforeAll() {
        CodedMessageDefinitionLoader.loadBuiltinDefinitions();
    }

    @Test
    public void testGetDefinition() {
        Locales.current(Locale.CHINA);
        CodedMessageDefinition part = CodedMessageDefinition.getDefinition("System.Error");
        assertThat(part.getCode()).isEqualTo("System.Error");
        assertThat(part.getTemplate()).isEqualTo("系统错误");

        Locales.current(Locale.US);
        part = CodedMessageDefinition.getDefinition("System.Error");
        assertThat(part.getCode()).isEqualTo("System.Error");
        assertThat(part.getTemplate()).isEqualTo("System error");
    }
}
