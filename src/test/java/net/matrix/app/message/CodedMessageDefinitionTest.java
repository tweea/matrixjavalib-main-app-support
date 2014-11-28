/*
 * Copyright(C) 2009 matrix
 * All right reserved.
 */
package net.matrix.app.message;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import net.matrix.text.Locales;

public class CodedMessageDefinitionTest {
	@BeforeClass
	public static void beforeClass() {
		CodedMessageDefinition.define(new CodedMessageDefinition("Test1", Locale.ROOT, "message1"));
		CodedMessageDefinition.define(new CodedMessageDefinition("Test1", Locale.CHINA, "message2"));
		CodedMessageDefinition.define(new CodedMessageDefinition("Test2", Locale.ROOT, "messageB"));
	}

	@Test
	public void testGetDefinition() {
		CodedMessageDefinition part = CodedMessageDefinition.getDefinition("notfound");
		Assertions.assertThat(part).isNull();

		Locales.current(Locale.CHINA);
		part = CodedMessageDefinition.getDefinition("Test1");
		Assertions.assertThat(part.getCode()).isEqualTo("Test1");
		Assertions.assertThat(part.getTemplate()).isEqualTo("message2");
		part = CodedMessageDefinition.getDefinition("Test2");
		Assertions.assertThat(part.getCode()).isEqualTo("Test2");
		Assertions.assertThat(part.getTemplate()).isEqualTo("messageB");

		Locales.current(Locale.ENGLISH);
		part = CodedMessageDefinition.getDefinition("Test1");
		Assertions.assertThat(part.getCode()).isEqualTo("Test1");
		Assertions.assertThat(part.getTemplate()).isEqualTo("message1");
		part = CodedMessageDefinition.getDefinition("Test2");
		Assertions.assertThat(part.getCode()).isEqualTo("Test2");
		Assertions.assertThat(part.getTemplate()).isEqualTo("messageB");
	}
}
