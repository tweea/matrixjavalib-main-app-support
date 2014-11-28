/*
 * Copyright(C) 2009 matrix
 * All right reserved.
 */
package net.matrix.app.message;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

public class CodedMessageTest {
	@BeforeClass
	public static void beforeClass() {
		CodedMessageDefinition.define(new CodedMessageDefinition("Message.Test1", Locale.ROOT, "测试消息：{0}"));
		CodedMessageDefinition.define(new CodedMessageDefinition("Message.Test2", Locale.ROOT, "测试消息 B：{0}{1}"));
	}

	@Test
	public void testCodedMessage() {
		CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFORMATION);
		Assertions.assertThat(message.getCode()).isEqualTo("Message.Test1");
		Assertions.assertThat(message.getLevel()).isEqualTo(CodedMessageLevel.INFORMATION);
		Assertions.assertThat(message.getArguments()).isEmpty();
		Assertions.assertThat(message.getUnformattedArguments()).isEmpty();
		Assertions.assertThat(message.getMessages()).isEmpty();
	}

	@Test
	public void testAddArgument() {
		CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFORMATION);
		Assertions.assertThat(message.getCode()).isEqualTo("Message.Test1");
		Assertions.assertThat(message.getLevel()).isEqualTo(CodedMessageLevel.INFORMATION);
		Assertions.assertThat(message.getArguments()).isEmpty();
		message.addArgument("abc");
		Assertions.assertThat(message.getArguments()).hasSize(1);
		Assertions.assertThat(message.getArguments()).containsExactly("abc");
	}

	@Test
	public void testAddUnformattedArgument() {
		CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFORMATION);
		Assertions.assertThat(message.getCode()).isEqualTo("Message.Test1");
		Assertions.assertThat(message.getLevel()).isEqualTo(CodedMessageLevel.INFORMATION);
		Assertions.assertThat(message.getArguments()).isEmpty();
		message.addUnformattedArgument("abc");
		Assertions.assertThat(message.getUnformattedArguments()).hasSize(1);
		Assertions.assertThat(message.getUnformattedArguments()).containsExactly("abc");
	}

	@Test
	public void testHasLevel() {
		CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFORMATION);
		Assertions.assertThat(message.hasLevel(CodedMessageLevel.INFORMATION)).isTrue();
		Assertions.assertThat(message.hasLevel(CodedMessageLevel.ERROR)).isFalse();
	}

	@Test
	public void testFormat() {
		CodedMessage message = new CodedMessage("Message.Test2", CodedMessageLevel.INFORMATION, "Test", "test2");
		String formatString = message.format();
		Assertions.assertThat(formatString).isEqualTo("测试消息 B：Testtest2");
	}

	@Test
	public void testFormatFallback() {
		CodedMessage message = new CodedMessage("Message.Fallback", CodedMessageLevel.INFORMATION, "Test", "test2");
		String formatString = message.format();
		Assertions.assertThat(formatString).isEqualTo("Message.Fallback, Test, test2");
	}

	@Test
	public void testFormatUnformatted() {
		CodedMessage message = new CodedMessage("Message.Test2", CodedMessageLevel.INFORMATION, "Test", "test2");
		message.addUnformattedArgument("12345");
		String formatString = message.format();
		Assertions.assertThat(formatString).isEqualTo("测试消息 B：Testtest2，12345");
	}

	@Test
	public void testFormatAll() {
		CodedMessage message = new CodedMessage("Message.Test1", CodedMessageLevel.INFORMATION, "a");
		message.getMessages().add(new CodedMessage("Message.Test2", CodedMessageLevel.INFORMATION, "b", "c"));
		String formatString = message.formatAll();
		Assertions.assertThat(formatString).isEqualTo("测试消息：a\n\t测试消息 B：bc");
	}
}
