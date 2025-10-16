import junitbuild.extensions.dependencyFromLibs
import net.ltgt.gradle.errorprone.errorprone
import net.ltgt.gradle.nullaway.nullaway
import java.lang.System.getenv

plugins {
	`java-library`
	id("net.ltgt.errorprone")
	id("net.ltgt.nullaway")
}

dependencies {
	errorprone(dependencyFromLibs("error-prone-contrib"))
	errorprone(dependencyFromLibs("error-prone-core"))
	errorprone(dependencyFromLibs("nullaway"))
	constraints {
		errorprone("com.google.guava:guava") {
			version {
				require("33.4.8-jre")
			}
			because("Older versions use deprecated methods in sun.misc.Unsafe")
			// https://github.com/junit-team/junit-framework/pull/5039#discussion_r2414490581
		}
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.errorprone {
		disableAllChecks = true // consider removal to avoid error prone error´s, following convention over configuration.
		allErrorsAsWarnings = true
		error(
			// "ConstantNaming", # https://github.com/PicnicSupermarket/error-prone-support/issues/1923
			"Unused",
			"UnusedMethod",
			"UnusedVariable",
			"RedundantStringConversion",
		)
		if (!getenv().containsKey("CI") && getenv("IN_PLACE").toBoolean()) {
			errorproneArgs.addAll(
				"-XepPatchLocation:IN_PLACE",
				"-XepPatchChecks:" +
						"RedundantStringConversion," +
						"Unused," +
						"UnusedMethod," +
						"UnusedVariable,"
			)
		}
	}
}

tasks.withType<JavaCompile>().named { it.startsWith("compileTest") }.configureEach {
	options.errorprone.nullaway {
		handleTestAssertionLibraries = true
		excludedFieldAnnotations.addAll(
			"org.junit.jupiter.api.io.TempDir",
			"org.junit.jupiter.params.Parameter",
			"org.junit.runners.Parameterized.Parameter",
			"org.mockito.Captor",
			"org.mockito.InjectMocks",
			"org.mockito.Mock",
			"org.mockito.Spy",
		)
	}
}
