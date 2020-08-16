/*
 * Copyright(C) 2010 matrix
 * All right reserved.
 */
package net.matrix.app;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import net.matrix.app.message.CodedMessageLevel;
import net.matrix.app.message.CodedMessages;

public class SystemRuntimeExceptionTest {
    @Test
    public void testSystemRuntimeException() {
        SystemRuntimeException exception = new SystemRuntimeException();
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }

    @Test
    public void testSystemRuntimeException2() {
        SystemRuntimeException exception = new SystemRuntimeException(new Exception());
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).isEmpty();
        exception = new SystemRuntimeException(exception);
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).hasSize(1);
    }

    @Test
    public void testSystemRuntimeException3() {
        SystemRuntimeException exception = new SystemRuntimeException(CodedMessages.information("System.Error"));
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFORMATION);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }

    @Test
    public void testSystemRuntimeException4() {
        SystemRuntimeException exception = new SystemRuntimeException(new Exception(), CodedMessages.information("System.Error"));
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFORMATION);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).isEmpty();
        exception = new SystemRuntimeException(exception, CodedMessages.information("System.Error"));
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFORMATION);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).hasSize(1);
    }
}
