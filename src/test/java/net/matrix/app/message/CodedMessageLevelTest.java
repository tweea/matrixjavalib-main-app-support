/*
 * Copyright(C) 2009 matrix
 * All right reserved.
 */
package net.matrix.app.message;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CodedMessageLevelTest {
    @Test
    public void testForCode() {
        Assertions.assertThat(CodedMessageLevel.forCode(1)).isEqualTo(CodedMessageLevel.TRACE);
        Assertions.assertThat(CodedMessageLevel.forCode(null)).isNull();
        Assertions.assertThat(CodedMessageLevel.forCode(19)).isNull();
    }

    @Test
    public void testGetCode() {
        Assertions.assertThat(CodedMessageLevel.TRACE.getCode()).isEqualTo(1);
    }
}
