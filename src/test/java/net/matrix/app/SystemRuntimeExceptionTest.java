/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import org.junit.jupiter.api.Test;

import net.matrix.app.message.CodedMessageLevel;
import net.matrix.app.message.CodedMessageMx;

import static org.assertj.core.api.Assertions.assertThat;

class SystemRuntimeExceptionTest {
    @Test
    void testNew() {
        SystemRuntimeException exception = new SystemRuntimeException();
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }

    @Test
    void testNew2() {
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
    void testNew3() {
        SystemRuntimeException exception = new SystemRuntimeException(CodedMessageMx.info("System.Error"));
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFO);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }

    @Test
    void testNew4() {
        SystemRuntimeException exception = new SystemRuntimeException(new Exception(), CodedMessageMx.info("System.Error"));
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFO);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();

        exception = new SystemRuntimeException(exception, CodedMessageMx.info("System.Error"));
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFO);
        assertThat(exception.getCodedMessage().getMessages()).hasSize(1);
    }
}
