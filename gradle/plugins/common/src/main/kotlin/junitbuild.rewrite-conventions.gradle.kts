plugins {
	id("org.openrewrite.rewrite")
}

dependencies {
	rewrite("org.openrewrite.recipe:rewrite-testing-frameworks:3.23.0")
}

rewrite {
	activeRecipe("org.junit.openrewrite.SanityCheck")
	configFile = project.getRootProject().file("gradle/config/rewrite.yml")
	exclusion(
		// "**AssertionsTests.java", // https://github.com/openrewrite/rewrite-static-analysis/issues/799
		// legacy
		"**TestCase.java",
		"**TestCases.java",
		"**documentation/src/test/java/example**",
		"**testFixtures/java/org/junit/vintage/engine/samples**",
	)
	setExportDatatables(true)
	setFailOnDryRunResults(true)
}
