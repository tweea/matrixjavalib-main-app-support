/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CodedMessageTest {
    @BeforeAll
    static void beforeAll() {
        CodedMessageDefinition.add(new CodedMessageDefinition("Message.Test1", Locale.ROOT, "测试消息：{0}"));
        CodedMessageDefinition.add(new CodedMessageDefinition("Message.Test2", Locale.ROOT, "测试消息 B：{0}{1}"));
    }

    @Test
    void testNew() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFO);
        assertThat(message.getCode()).isEqualTo("Message.Test1");
        assertThat(message.getTime()).isPositive();
        assertThat(message.getLevel()).isEqualTo(CodedMessageLevel.INFO);
        assertThat(message.getArguments()).isEmpty();
        assertThat(message.getUnformattedArguments()).isEmpty();
        assertThat(message.getMessages()).isEmpty();
    }

    @Test
    void testAddArgument() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFO);

        message.addArgument("abc");
        assertThat(message.getArguments()).hasSize(1);
        assertThat(message.getArguments()).containsExactly("abc");
    }

    @Test
    void testAddUnformattedArgument() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFO);

        message.addUnformattedArgument("abc");
        assertThat(message.getUnformattedArguments()).hasSize(1);
        assertThat(message.getUnformattedArguments()).containsExactly("abc");
    }

    @Test
    void testAddMessage() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFO);
        CodedMessage message2 = new CodedMessage("Message.Test2", CodedMessageLevel.INFO);

        message.addMessage(message2);
        assertThat(message.getMessages()).hasSize(1);
        assertThat(message.getMessages()).containsExactly(message2);
    }

    @Test
    void testHasLevel() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFO);
        CodedMessage message2 = new CodedMessage("Message.Test2", CodedMessageLevel.WARN);
        message.addMessage(message2);

        assertThat(message.hasLevel(CodedMessageLevel.INFO)).isTrue();
        assertThat(message.hasLevel(CodedMessageLevel.WARN)).isTrue();
        assertThat(message.hasLevel(CodedMessageLevel.ERROR)).isFalse();
    }

    @Test
    void testFormat() {
        CodedMessage message = new CodedMessage("Message.Test2", CodedMessageLevel.INFO, "Test", "test2");

        String formatString = message.format();
        assertThat(formatString).isEqualTo("测试消息 B：Testtest2");
    }

    @Test
    void testFormat_fallback() {
        CodedMessage message = new CodedMessage("Message.Fallback", CodedMessageLevel.INFO, "Test", "test2");

        String formatString = message.format();
        assertThat(formatString).isEqualTo("Message.Fallback，Test，test2");
    }

    @Test
    void testFormat_unformatted() {
        CodedMessage message = new CodedMessage("Message.Test2", CodedMessageLevel.INFO, "Test", "test2");
        message.addUnformattedArgument("12345");

        String formatString = message.format();
        assertThat(formatString).isEqualTo("测试消息 B：Testtest2|12345");
    }

    @Test
    void testFormatAll() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFO, "a");
        message.addMessage(new CodedMessage("Message.Test2", CodedMessageLevel.INFO, "b", "c"));

        String formatString = message.formatAll();
        assertThat(formatString).isEqualTo("测试消息：a\n\t测试消息 B：bc");
    }
}
