/*
 * $Id: CodedMessageLevelTest.java 806 2013-12-26 08:33:13Z tweea@263.net $
 * Copyright(C) 2009 matrix
 * All right reserved.
 */
package net.matrix.app.message;

import org.junit.Assert;
import org.junit.Test;

public class CodedMessageLevelTest {
	@Test
	public void testForCode() {
		Assert.assertEquals(CodedMessageLevel.TRACE, CodedMessageLevel.forCode(1));
		Assert.assertNull(CodedMessageLevel.forCode(null));
		Assert.assertNull(CodedMessageLevel.forCode(19));
	}

	@Test
	public void testGetCode() {
		Assert.assertEquals(Integer.valueOf(1), CodedMessageLevel.TRACE.getCode());
	}
}
