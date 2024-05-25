/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import org.junit.jupiter.api.Test;

import net.matrix.app.message.CodedMessageLevel;
import net.matrix.app.message.CodedMessages;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemRuntimeExceptionTest {
    @Test
    public void testNew() {
        SystemRuntimeException exception = new SystemRuntimeException();
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }

    @Test
    public void testNew2() {
        SystemRuntimeException exception = new SystemRuntimeException(new Exception());
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();

        exception = new SystemRuntimeException(exception);
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        assertThat(exception.getCodedMessage().getMessages()).hasSize(1);
    }

    @Test
    public void testNew3() {
        SystemRuntimeException exception = new SystemRuntimeException(CodedMessages.information("System.Error"));
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFO);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }

    @Test
    public void testNew4() {
        SystemRuntimeException exception = new SystemRuntimeException(new Exception(), CodedMessages.information("System.Error"));
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFO);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();

        exception = new SystemRuntimeException(exception, CodedMessages.information("System.Error"));
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFO);
        assertThat(exception.getCodedMessage().getMessages()).hasSize(1);
    }
}
