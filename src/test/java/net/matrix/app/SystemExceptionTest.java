/*
 * Copyright(C) 2010 matrix
 * All right reserved.
 */
package net.matrix.app;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import net.matrix.app.message.CodedMessageLevel;
import net.matrix.app.message.CodedMessages;

public class SystemExceptionTest {
    @Test
    public void testSystemException() {
        SystemException exception = new SystemException();
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }

    @Test
    public void testSystemException2() {
        SystemException exception = new SystemException(new Exception());
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).isEmpty();
        exception = new SystemException(exception);
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).hasSize(1);
    }

    @Test
    public void testSystemException3() {
        SystemException exception = new SystemException(CodedMessages.information("System.Error"));
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFORMATION);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }

    @Test
    public void testSystemException4() {
        SystemException exception = new SystemException(new Exception(), CodedMessages.information("System.Error"));
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFORMATION);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).isEmpty();
        exception = new SystemException(exception, CodedMessages.information("System.Error"));
        Assertions.assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        Assertions.assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFORMATION);
        Assertions.assertThat(exception.getCodedMessage().getMessages()).hasSize(1);
    }
}
