/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.suite.api;

import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;

/**
 * {@code @SelectModules} specifies the modules to <em>select</em> when running
 * a test suite on the JUnit Platform.
 *
 * @since 1.8
 * @see Suite
 * @see org.junit.platform.engine.discovery.DiscoverySelectors#selectModules(java.util.Set)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
@API(status = STABLE, since = "1.10")
public @interface SelectModules {

	/**
	 * One or more modules to select.
	 */
	String[] value();

}
