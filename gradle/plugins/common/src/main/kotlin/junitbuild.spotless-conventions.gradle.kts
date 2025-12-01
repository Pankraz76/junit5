import com.diffplug.spotless.LineEnding
import junitbuild.extensions.requiredVersionFromLibs

plugins {
	id("com.diffplug.spotless")
}

spotless {
	val license: License by rootProject.extra
	format("misc") {
		endWithNewline()
		leadingSpacesToTabs()
		target("*.gradle.kts", "gradle/plugins/**/*.gradle.kts", "*.gitignore")
		targetExclude("gradle/plugins/**/build/**")
		trimTrailingWhitespace()
	}
	format("documentation") {
		endWithNewline()
		target("*.adoc", "*.md", "src/**/*.adoc", "src/**/*.md")
		trimTrailingWhitespace()
	}
	pluginManager.withPlugin("java") {
		val configDir = rootProject.layout.projectDirectory.dir("gradle/config/eclipse")
		java {
			eclipse("([0-9]+\\.[0-9]+).*".toRegex().matchEntire(requiredVersionFromLibs("eclipse"))!!.let<MatchResult, String> { it.groups[1]!!.value })
				.configFile(configDir.file("junit-eclipse-formatter-settings.xml"))
			endWithNewline()
			importOrderFile(configDir.file("junit-eclipse.importorder"))
			licenseHeaderFile(license.headerFile, "(package|import) ")
			removeUnusedImports()
			targetExclude("**/module-info.java")
			trimTrailingWhitespace()
			// expandWildcardImports() // https://github.com/diffplug/spotless/pull/2744
			// forbidWildcardImports() // https://docs.openrewrite.org/recipes/java/removeunusedimports
		}
		format("moduleDescriptor") {
			target(fileTree(layout.projectDirectory.dir("src/main/java")) {
				include("module-info.java")
			})
			licenseHeaderFile(license.headerFile, "^$")
			trimTrailingWhitespace()
			endWithNewline()
		}
	}
	pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
		kotlin {
			endWithNewline()
			ktlint(requiredVersionFromLibs("ktlint"))
			licenseHeaderFile(license.headerFile)
			targetExclude("**/src/test/resources/**")
			toggleOffOn("formatter:off", "formatter:on")
			trimTrailingWhitespace()
		}
	}
	pluginManager.withPlugin("groovy") {
		groovy {
			endWithNewline()
			licenseHeaderFile(license.headerFile)
			trimTrailingWhitespace()
		}
	}
	// Explicitly configure line endings to avoid Spotless to search for .gitattributes file
	// see https://github.com/gradle/gradle/issues/25469#issuecomment-3444231151
	lineEndings = LineEnding.UNIX
}

tasks {
	named("spotlessDocumentation") {
		outputs.doNotCacheIf("negative avoidance savings") { true }
	}
	named("spotlessMisc") {
		outputs.doNotCacheIf("negative avoidance savings") { true }
	}
}
