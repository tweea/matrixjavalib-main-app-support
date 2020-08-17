/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodedMessageTest {
    @BeforeAll
    public static void beforeAll() {
        CodedMessageDefinition.define(new CodedMessageDefinition("Message.Test1", Locale.ROOT, "测试消息：{0}"));
        CodedMessageDefinition.define(new CodedMessageDefinition("Message.Test2", Locale.ROOT, "测试消息 B：{0}{1}"));
    }

    @Test
    public void testCodedMessage() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFORMATION);
        assertThat(message.getCode()).isEqualTo("Message.Test1");
        assertThat(message.getLevel()).isEqualTo(CodedMessageLevel.INFORMATION);
        assertThat(message.getArguments()).isEmpty();
        assertThat(message.getUnformattedArguments()).isEmpty();
        assertThat(message.getMessages()).isEmpty();
    }

    @Test
    public void testAddArgument() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFORMATION);

        message.addArgument("abc");
        assertThat(message.getArguments()).hasSize(1);
        assertThat(message.getArguments()).containsExactly("abc");
    }

    @Test
    public void testAddUnformattedArgument() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFORMATION);

        message.addUnformattedArgument("abc");
        assertThat(message.getUnformattedArguments()).hasSize(1);
        assertThat(message.getUnformattedArguments()).containsExactly("abc");
    }

    @Test
    public void testHasLevel() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFORMATION);

        assertThat(message.hasLevel(CodedMessageLevel.INFORMATION)).isTrue();
        assertThat(message.hasLevel(CodedMessageLevel.ERROR)).isFalse();
    }

    @Test
    public void testFormat() {
        CodedMessage message = new CodedMessage("Message.Test2", CodedMessageLevel.INFORMATION, "Test", "test2");

        String formatString = message.format();
        assertThat(formatString).isEqualTo("测试消息 B：Testtest2");
    }

    @Test
    public void testFormat_fallback() {
        CodedMessage message = new CodedMessage("Message.Fallback", CodedMessageLevel.INFORMATION, "Test", "test2");

        String formatString = message.format();
        assertThat(formatString).isEqualTo("Message.Fallback, Test, test2");
    }

    @Test
    public void testFormat_unformatted() {
        CodedMessage message = new CodedMessage("Message.Test2", CodedMessageLevel.INFORMATION, "Test", "test2");
        message.addUnformattedArgument("12345");

        String formatString = message.format();
        assertThat(formatString).isEqualTo("测试消息 B：Testtest2|12345");
    }

    @Test
    public void testFormatAll() {
        CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFORMATION, "a");
        message.getMessages().add(new CodedMessage("Message.Test2", CodedMessageLevel.INFORMATION, "b", "c"));

        String formatString = message.formatAll();
        assertThat(formatString).isEqualTo("测试消息：a\n\t测试消息 B：bc");
    }
}
