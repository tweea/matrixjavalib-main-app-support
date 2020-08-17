/*
 * Copyright(C) 2009 matrix
 * All right reserved.
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

    @Test
    public void testGetCode() {
        assertThat(CodedMessageLevel.TRACE.getCode()).isEqualTo(1);
    }
}
