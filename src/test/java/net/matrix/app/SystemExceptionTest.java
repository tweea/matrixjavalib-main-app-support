/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import org.junit.jupiter.api.Test;

import net.matrix.app.message.CodedMessageLevel;
import net.matrix.app.message.CodedMessages;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemExceptionTest {
    @Test
    public void testNew() {
        SystemException exception = new SystemException();
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }

    @Test
    public void testNew2() {
        SystemException exception = new SystemException(new Exception());
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();

        exception = new SystemException(exception);
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        assertThat(exception.getCodedMessage().getMessages()).hasSize(1);
    }

    @Test
    public void testNew3() {
        SystemException exception = new SystemException(CodedMessages.information("System.Error"));
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFO);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }

    @Test
    public void testNew4() {
        SystemException exception = new SystemException(new Exception(), CodedMessages.information("System.Error"));
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFO);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();

        exception = new SystemException(exception, CodedMessages.information("System.Error"));
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("System.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.INFO);
        assertThat(exception.getCodedMessage().getMessages()).hasSize(1);
    }
}
