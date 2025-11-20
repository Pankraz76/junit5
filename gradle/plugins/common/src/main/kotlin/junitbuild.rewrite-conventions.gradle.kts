plugins {
	id("org.openrewrite.rewrite")
}

rewrite {
	activeRecipe("org.junit.openrewrite.SanityCheck")
	exclusion("**CollectionUtils.java")
	setExportDatatables(true)
	setFailOnDryRunResults(true)
}

dependencies {
	rewrite(platform("org.openrewrite.recipe:rewrite-recipe-bom:3.17.0"))
	rewrite("org.openrewrite.recipe:rewrite-migrate-java:3.20.0")
	rewrite("org.openrewrite.recipe:rewrite-java-security:3.19.2")
	rewrite("org.openrewrite.recipe:rewrite-rewrite:0.14.1")
	rewrite("org.openrewrite.recipe:rewrite-static-analysis:2.20.0")
	rewrite("org.openrewrite.recipe:rewrite-third-party:0.30.0")
}
