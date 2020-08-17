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

public class CodedMessageDefinitionTest {
    @BeforeAll
    public static void beforeAll() {
        CodedMessageDefinition.define(new CodedMessageDefinition("Test1", Locale.ROOT, "message1"));
        CodedMessageDefinition.define(new CodedMessageDefinition("Test1", Locale.CHINA, "message2"));
        CodedMessageDefinition.define(new CodedMessageDefinition("Test2", Locale.ROOT, "messageB"));
    }

    @Test
    public void testGetDefinition() {
        CodedMessageDefinition part = CodedMessageDefinition.getDefinition("notfound");
        assertThat(part).isNull();

        Locales.current(Locale.CHINA);
        part = CodedMessageDefinition.getDefinition("Test1");
        assertThat(part.getCode()).isEqualTo("Test1");
        assertThat(part.getTemplate()).isEqualTo("message2");
        part = CodedMessageDefinition.getDefinition("Test2");
        assertThat(part.getCode()).isEqualTo("Test2");
        assertThat(part.getTemplate()).isEqualTo("messageB");

        Locales.current(Locale.ENGLISH);
        part = CodedMessageDefinition.getDefinition("Test1");
        assertThat(part.getCode()).isEqualTo("Test1");
        assertThat(part.getTemplate()).isEqualTo("message1");
        part = CodedMessageDefinition.getDefinition("Test2");
        assertThat(part.getCode()).isEqualTo("Test2");
        assertThat(part.getTemplate()).isEqualTo("messageB");
    }
}
