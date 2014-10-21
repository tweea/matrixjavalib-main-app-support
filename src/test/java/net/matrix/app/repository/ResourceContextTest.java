/*
 * $Id: ResourceContextTest.java 710 2013-09-05 03:30:36Z tweea@263.net $
 * Copyright(C) 2009 matrix
 * All right reserved.
 */
package net.matrix.app.repository;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ResourceContextTest {
	@Test
	public void getResource()
		throws ConfigurationException {
		ResourceRepository repo = new ResourceRepository(new ClassPathResource("repo1/"));
		ResourceContextConfig selectionSet = new ResourceContextConfig();
		selectionSet.load(repo.getResource(new ResourceSelection("configset", "set1", "configset.xml")));
		ResourceContext context = new ResourceContext(repo, selectionSet);
		Assert.assertTrue(context.getResource("test/orz", "foo.xml").exists());
	}
}
