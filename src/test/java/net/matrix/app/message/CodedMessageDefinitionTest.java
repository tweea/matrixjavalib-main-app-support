/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.matrix.java.util.LocaleMx;

import static org.assertj.core.api.Assertions.assertThat;

public class CodedMessageDefinitionTest {
    @BeforeAll
    public static void beforeAll() {
        CodedMessageDefinition.add(new CodedMessageDefinition("Test1", Locale.ROOT, "message1"));
        CodedMessageDefinition.add(new CodedMessageDefinition("Test1", Locale.CHINA, "message2"));
        CodedMessageDefinition.add(new CodedMessageDefinition("Test2", Locale.ROOT, "messageB"));
    }

    @Test
    public void testGet() {
        CodedMessageDefinition definition = CodedMessageDefinition.get("notfound");
        assertThat(definition).isNull();

        LocaleMx.current(Locale.CHINA);
        definition = CodedMessageDefinition.get("Test1");
        assertThat(definition.getCode()).isEqualTo("Test1");
        assertThat(definition.getTemplate()).isEqualTo("message2");
        definition = CodedMessageDefinition.get("Test2");
        assertThat(definition.getCode()).isEqualTo("Test2");
        assertThat(definition.getTemplate()).isEqualTo("messageB");

        LocaleMx.current(Locale.ENGLISH);
        definition = CodedMessageDefinition.get("Test1");
        assertThat(definition.getCode()).isEqualTo("Test1");
        assertThat(definition.getTemplate()).isEqualTo("message1");
        definition = CodedMessageDefinition.get("Test2");
        assertThat(definition.getCode()).isEqualTo("Test2");
        assertThat(definition.getTemplate()).isEqualTo("messageB");
    }
}
