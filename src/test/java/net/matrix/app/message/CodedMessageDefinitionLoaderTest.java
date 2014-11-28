/*
 * Copyright(C) 2009 matrix
 * All right reserved.
 */
package net.matrix.app.message;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import net.matrix.app.GlobalSystemContext;
import net.matrix.text.Locales;

public class CodedMessageDefinitionLoaderTest {
	@BeforeClass
	public static void setUp() {
		CodedMessageDefinitionLoader.loadDefinitions(GlobalSystemContext.get().getResourcePatternResolver());
	}

	@Test
	public void getDefinition() {
		Locales.current(Locale.CHINA);
		CodedMessageDefinition part = CodedMessageDefinition.getDefinition("System.Error");
		Assertions.assertThat(part.getCode()).isEqualTo("System.Error");
		Assertions.assertThat(part.getTemplate()).isEqualTo("系统错误");

		Locales.current(Locale.US);
		part = CodedMessageDefinition.getDefinition("System.Error");
		Assertions.assertThat(part.getCode()).isEqualTo("System.Error");
		Assertions.assertThat(part.getTemplate()).isEqualTo("System error");
	}
}
