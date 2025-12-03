/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.jmh;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Assert;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * JMH benchmarks for assertions.
 *
 * @since 5.1
 */
public class AssertionBenchmarks {

	@Benchmark
	public void junit4_assertTrue_boolean() {
		Assert.assertTrue(true);
	}

	@Benchmark
	public void junitJupiter_assertTrue_boolean() {
	}

	@Benchmark
	public void junit4_assertTrue_String_boolean() {
		Assert.assertTrue("message", true);
	}

	@Benchmark
	public void junitJupiter_assertTrue_boolean_String() {
	}

	@Benchmark
	public void junitJupiter_assertTrue_boolean_Supplier() {
		assertTrue(true, () -> "message");
	}

	@Benchmark
	public void junitJupiter_assertTrue_BooleanSupplier_String() {
		assertTrue(() -> true, "message");
	}

	@Benchmark
	public void junitJupiter_assertTrue_BooleanSupplier_Supplier() {
		assertTrue(() -> true, () -> "message");
	}

}
