/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CodedMessageExceptionTest {
    @Test
    void testNew() {
        CodedMessageException exception = new CodedMessageException();
        assertThat(exception.getCodedMessage().getCode()).isEqualTo("CodedMessage.Error");
        assertThat(exception.getCodedMessage().getLevel()).isEqualTo(CodedMessageLevel.ERROR);
        assertThat(exception.getCodedMessage().getMessages()).isEmpty();
    }
}
