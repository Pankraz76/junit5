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
	errorprone(dependencyFromLibs("refaster-runner"))
	constraints {
		errorprone("com.google.guava:guava") {
			version {
				require("33.4.8-jre")
			}
			because("Older versions use deprecated methods in sun.misc.Unsafe")
		}
	}
}

nullaway {
	onlyNullMarked = true
}

tasks.withType<JavaCompile>().configureEach {
	options.errorprone {
		allDisabledChecksAsWarnings = true
		disableAllChecks = java.toolchain.implementation.orNull == JvmImplementation.J9 && name == "compileJava"
		disableWarningsInGeneratedCode = true
		errorproneArgs.add("-XepOpt:Refaster:NamePattern=^(?!.*Rules\\$).*") // currently failing Refaster; might consider whitelist.
		if (!disableAllChecks.get()) {
			disable(
				"AnnotateFormatMethod", // We don`t want to use ErrorProne`s annotations.
				"BadImport", // This check is opinionated wrt. which method names it considers unsuitable for import which includes a few of our own methods in `ReflectionUtils` etc.
				"DirectReturn", // https://github.com/junit-team/junit-framework/pull/5006#discussion_r2403984446
				"DoNotCallSuggester",
				"ImmutableEnumChecker",
				"InlineMeSuggester",
				"MissingSummary", // Produces a lot of findings that we consider to be false positives, for example for package-private classes and methods.
				"StringSplitter", // We don`t want to use Guava.
				"UnnecessaryLambda", // The findings of this check are subjective because a named constant can be more readable in many cases.
			)
			error(
				"MissingOverride",
				"PackageLocation",
				"RedundantStringConversion",
				"RedundantStringEscape",
				"UnusedVariable",
				// why failing ?
				// VerifierSupport.java:41: warning: [LexicographicalAnnotationListing] Sort annotations lexicographically where possible
				// "LexicographicalAnnotationAttributeListing",
				// "LexicographicalAnnotationListing",
				// "StaticImport",
			)
			if (!getenv().containsKey("CI") && getenv("IN_PLACE").toBoolean()) {
				errorproneArgs.addAll(
					"-XepPatchLocation:IN_PLACE",
					"-XepPatchChecks:" +
//							"LexicographicalAnnotationAttributeListing," +
//							"LexicographicalAnnotationListing," +
							"MissingOverride," +
							"PackageLocation," +
							"RedundantStringConversion," +
							"RedundantStringEscape" +
							"StaticImport," +
							"UnusedVariable,"
				)
			}
		}
		nullaway {
			if (disableAllChecks.get()) {
				disable()
			} else {
				enable()
			}
			checkContracts = true
			customContractAnnotations.add("org.junit.platform.commons.annotation.Contract")
			isJSpecifyMode = true
			suppressionNameAliases.add("DataFlowIssue")
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
