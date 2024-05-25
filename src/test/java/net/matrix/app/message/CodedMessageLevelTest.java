/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodedMessageLevelTest {
    @Test
    public void testForCode() {
        assertThat(CodedMessageLevel.forCode(1)).isEqualTo(CodedMessageLevel.TRACE);
        assertThat(CodedMessageLevel.forCode(null)).isNull();
        assertThat(CodedMessageLevel.forCode(19)).isNull();
    }
}
