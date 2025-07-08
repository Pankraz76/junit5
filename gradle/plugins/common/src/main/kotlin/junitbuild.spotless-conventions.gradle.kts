import junitbuild.extensions.requiredVersionFromLibs

plugins {
	id("com.diffplug.spotless")
}

val license: License by rootProject.extra

spotless {

	format("misc") {
		target("*.gradle.kts", "gradle/plugins/**/*.gradle.kts", "*.gitignore")
		targetExclude("gradle/plugins/**/build/**")
		leadingSpacesToTabs()
		trimTrailingWhitespace()
		endWithNewline()
	}

	format("documentation") {
		target("*.adoc", "*.md", "src/**/*.adoc", "src/**/*.md")
		trimTrailingWhitespace()
		endWithNewline()
	}

	pluginManager.withPlugin("java") {

		val configDir = rootProject.layout.projectDirectory.dir("gradle/config/eclipse")
		val importOrderConfigFile = configDir.file("junit-eclipse.importorder")
		val javaFormatterConfigFile = configDir.file("junit-eclipse-formatter-settings.xml")

		java {
			targetExclude("**/module-info.java")
			importOrderFile(importOrderConfigFile)
			val fullVersion = requiredVersionFromLibs("eclipse")
			val majorMinorVersion = "([0-9]+\\.[0-9]+).*".toRegex().matchEntire(fullVersion)!!.let { it.groups[1]!!.value }
			eclipse(majorMinorVersion).configFile(javaFormatterConfigFile)
			trimTrailingWhitespace()
			endWithNewline()
			removeUnusedImports()
		}

		format("moduleDescriptor") {
			target(fileTree(layout.projectDirectory.dir("src/main/java")) {
				include("module-info.java")
			})
			trimTrailingWhitespace()
			endWithNewline()
		}
	}

	pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
		kotlin {
			targetExclude("**/src/test/resources/**")
			ktlint(requiredVersionFromLibs("ktlint"))
			trimTrailingWhitespace()
			endWithNewline()
		}
		configurations.named { it.startsWith("spotless") }.configureEach {
			// Workaround for CVE-2024-12798 and CVE-2024-12801
			resolutionStrategy {
				eachDependency {
					if (requested.group == "ch.qos.logback") {
						useVersion(requiredVersionFromLibs("logback"))
					}
				}
			}
		}
	}

	pluginManager.withPlugin("groovy") {
		groovy {
			trimTrailingWhitespace()
			endWithNewline()
		}
	}
}

tasks {
	named("spotlessDocumentation") {
		outputs.doNotCacheIf("negative avoidance savings") { true }
	}
	named("spotlessMisc") {
		outputs.doNotCacheIf("negative avoidance savings") { true }
	}
}
