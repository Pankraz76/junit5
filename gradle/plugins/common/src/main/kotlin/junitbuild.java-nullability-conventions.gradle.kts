import junitbuild.extensions.dependencyFromLibs
import net.ltgt.gradle.errorprone.errorprone
import net.ltgt.gradle.nullaway.nullaway
import org.gradle.jvm.toolchain.JvmImplementation.J9

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
			// https://github.com/junit-team/junit-framework/pull/5039#discussion_r2414490581
		}
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.errorprone {
//		allErrorsAsWarnings = true
//		disableAllWarnings = true
		disableAllChecks = !(J9 != java.toolchain.implementation.orNull && "compileJava" == name)
		if (!disableAllChecks.get()) {
			// errorproneArgs.add("-XepOpt:Refaster:NamePattern=^(?!.*Rules\\$).*") // currently failing Refaster; might consider whitelist.
			disable(
				"AnnotateFormatMethod", // We don`t want to use ErrorProne`s annotations.
				"BadImport", // This check is opinionated wrt. which method names it considers unsuitable for import which includes a few of our own methods in `ReflectionUtils` etc.
				"DoNotCallSuggester",
				"ImmutableEnumChecker",
				"InlineMeSuggester",
				"MissingSummary", // Produces a lot of findings that we consider to be false positives, for example for package-private classes and methods.
				"StringSplitter", // We don`t want to use Guava.
				"UnnecessaryLambda", // The findings of this check are subjective because a named constant can be more readable in many cases.
				// picnic (https://error-prone.picnic.tech)
				"ConstantNaming",
				"DirectReturn", // https://github.com/junit-team/junit-framework/pull/5006#discussion_r2403984446
				"FormatStringConcatenation",
				"IdentityConversion",
				"LexicographicalAnnotationAttributeListing",
				"LexicographicalAnnotationListing",
				"UnnecessarilyFullyQualified",
				"MissingTestCall",
				"NestedOptionals",
				"StaticImport",
				"NonStaticImport",
				"CanIgnoreReturnValueSuggester",
				"OptionalOrElseGet",
				"ThrowsUncheckedException",
				"TimeZoneUsage",
			)
			error(
				"PackageLocation",
				"RedundantStringConversion",
				"RedundantStringEscape",
			)
			// introduce new check by activating once:
			// CompositeFilter.java:75: Note: [Refaster Rule] OptionalRules.OptionalOrElseThrow: Refactoring opportunity

			errorproneArgs.addAll(
				"-Xep:Refaster:ERROR", // TODO  why failing not on warning error prone stuff? https://errorprone.info/bugpattern/AvoidObjectArrays
				"-XepOpt:Refaster:NamePattern=OptionalRules", // without this it's going cray-cray, having to disable all failing checks.
//				"-XepPatchChecks:OptionalRules",
				"-XepPatchLocation:IN_PLACE",
				"-XepOpt:Refaster:NamePattern=OptionalRules.OptionalOrElseThrow"
			)
		}
		nullaway {
			if (J9 == java.toolchain.implementation.orNull) {
				enable()
			} else {
				disable()
			}
			checkContracts = true
			customContractAnnotations.add("org.junit.platform.commons.annotation.Contract")
			isJSpecifyMode = true
			onlyNullMarked = true
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
