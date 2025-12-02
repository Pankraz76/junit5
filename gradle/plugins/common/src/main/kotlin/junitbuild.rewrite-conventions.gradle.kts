plugins {
	id("org.openrewrite.rewrite")
}

rewrite {
	activeRecipe("org.junit.openrewrite.SanityCheck")
	configFile = project.getRootProject().file("gradle/config/rewrite.yml")
	// blank out breaking, consider individual fix.
	exclusion("**AssertEqualsAssertionsTests.java")
	exclusion("**AssertIterableEqualsAssertionsTests.java")
	exclusion("**AssertSameAssertionsTests.java")
	setExportDatatables(true)
	setFailOnDryRunResults(true)
}

dependencies {
	rewrite("org.openrewrite.recipe:rewrite-testing-frameworks:3.22.0")
}
