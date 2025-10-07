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
		disableWarningsInGeneratedCode = true
		errorproneArgs.add("-XepOpt:Refaster:NamePattern=^(?!.*Rules\\$).*") // might consider.
		error(
			"ConstantNaming",
			"EmptyMethod",
			"EmptyMonoZip",
			"LexicographicalAnnotationAttributeListing",
			"LexicographicalAnnotationListing",
			"MissingOverride",
			"MissingTestCall",
			"NonEmptyMono",
			"OptionalMapUnusedValue",
			"OptionalOfRedundantMethod",
			"RedundantSetterCall",
			"RedundantStringConversion",
			"RedundantStringEscape",
			"StaticImport",
			"StringJoin",
			"UnnecessaryCheckNotNull",
			"UnnecessaryTypeArgument",
			"UnusedAnonymousClass",
			"UnusedCollectionModifiedInPlace",
		)
		if (getenv("IN_PLACE").toBoolean()) {
			errorproneArgs.addAll(
				"-XepPatchLocation:IN_PLACE", // why only certain picnic rules apply?
				"-XepPatchChecks:" +
						"AddNullMarkedToPackageInfo," +
						"AlwaysThrows," +
						"AmbiguousJsonCreator," +
						"AndroidInjectionBeforeSuper," +
						"ArrayEquals," +
						"ArrayFillIncompatibleType," +
						"ArrayHashCode," +
						"ArrayToString," +
						"ArraysAsListPrimitiveArray," +
						"AssertJNullnessAssertion," +
						"AsyncCallableReturnsNull," +
						"AsyncFunctionReturnsNull," +
						"AutoValueBuilderDefaultsInConstructor," +
						"AutoValueConstructorOrderChecker," +
						"AutowiredConstructor," +
						"BadAnnotationImplementation," +
						"BadShiftAmount," +
						"BanJNDI," +
						"BoxedPrimitiveEquality," +
						"BundleDeserializationCast," +
						"CanonicalAnnotationSyntax," +
						"CanonicalClassNameUsage," +
						"ChainingConstructorIgnoresParameter," +
						"CheckNotNullMultipleTimes," +
						"CheckReturnValue," +
						"ClassCastLambdaUsage," +
						"CollectionIncompatibleType," +
						"CollectionToArraySafeParameter," +
						"CollectorMutability," +
						"ComparableType," +
						"ComparingThisWithNull," +
						"ComparisonOutOfRange," +
						"CompatibleWithAnnotationMisuse," +
						"CompileTimeConstant," +
						"ComputeIfAbsentAmbiguousReference," +
						"ConditionalExpressionNumericPromotion," +
						"ConstantNaming," +
						"ConstantOverflow," +
						"DaggerProvidesNull," +
						"DangerousLiteralNull," +
						"DeadException," +
						"DeadThread," +
						"DefaultCharset," +
						"DereferenceWithNullBranch," +
						"DiscardedPostfixExpression," +
						"DoNotCall," +
						"DoNotMock," +
						"DoubleBraceInitialization," +
						"DuplicateMapKeys," +
						"DurationFrom," +
						"DurationGetTemporalUnit," +
						"DurationTemporalUnit," +
						"DurationToLongTimeUnit," +
						"EagerStringFormatting," +
						"EmptyMethod," +
						"EmptyMonoZip," +
						"EqualsHashCode," +
						"EqualsNaN," +
						"EqualsNull," +
						"EqualsReference," +
						"EqualsWrongThing," +
						"ExplicitArgumentEnumeration," +
						"ExplicitEnumOrdering," +
						"FloggerFormatString," +
						"FloggerLogString," +
						"FloggerLogVarargs," +
						"FloggerSplitLogStatement," +
						"FluxFlatMapUsage," +
						"FluxImplicitBlock," +
						"ForOverride," +
						"FormatString," +
						"FormatStringAnnotation," +
						"FormatStringConcatenation," +
						"FromTemporalAccessor," +
						"FunctionalInterfaceMethodChanged," +
						"FuturesGetCheckedIllegalExceptionType," +
						"FuzzyEqualsShouldNotBeUsedInEqualsMethod," +
						"GetClassOnAnnotation," +
						"GetClassOnClass," +
						"GuardedBy," +
						"GuiceAssistedInjectScoping," +
						"GuiceAssistedParameters," +
						"GuiceInjectOnFinalField," +
						"HashtableContains," +
						"IdentityBinaryExpression," +
						"IdentityConversion," +
						"IdentityHashMapBoxing," +
						"Immutable," +
						"ImmutableEnumChecker," +
						"ImmutablesSortedSetComparator," +
						"ImpossibleNullComparison," +
						"Incomparable," +
						"IncompatibleArgumentType," +
						"IncompatibleModifiers," +
						"IndexOfChar," +
						"InexactVarargsConditional," +
						"InfiniteRecursion," +
						"InjectMoreThanOneScopeAnnotationOnClass," +
						"InjectOnMemberAndConstructor," +
						"InlineMeValidator," +
						"InstantTemporalUnit," +
						"InvalidJavaTimeConstant," +
						"InvalidPatternSyntax," +
						"InvalidTimeZoneID," +
						"InvalidZoneId," +
						"IsInstanceIncompatibleType," +
						"IsInstanceLambdaUsage," +
						"IsInstanceOfClass," +
						"IsLoggableTagLength," +
						"JUnit3TestNotRun," +
						"JUnit4ClassAnnotationNonStatic," +
						"JUnit4SetUpNotRun," +
						"JUnit4TearDownNotRun," +
						"JUnit4TestNotRun," +
						"JUnit4TestsNotRunWithinEnclosed," +
						"JUnitAssertSameCheck," +
						"JUnitClassModifiers," +
						"JUnitMethodDeclaration," +
						"JUnitNullaryParameterizedTestDeclaration," +
						"JUnitParameterMethodNotFound," +
						"JUnitValueSource," +
						"JavaxInjectOnAbstractMethod," +
						"JodaToSelf," +
						"LenientFormatStringValidation," +
						"LexicographicalAnnotationAttributeListing," +
						"LexicographicalAnnotationListing," +
						"LiteByteStringUtf8," +
						"LocalDateTemporalAmount," +
						"LockOnBoxedPrimitive," +
						"LoopConditionChecker," +
						"LossyPrimitiveCompare," +
						"MathRoundIntLong," +
						"MislabeledAndroidString," +
						"MisleadingEmptyVarargs," +
						"MisleadingEscapedSpace," +
						"MisplacedScopeAnnotations," +
						"MissingOverride," +
						"MissingSuperCall," +
						"MissingTestCall," +
						"MisusedDayOfYear," +
						"MisusedWeekYear," +
						"MixedDescriptors," +
						"MockitoMockClassReference," +
						"MockitoStubbing," +
						"MockitoUsage," +
						"ModifyingCollectionWithItself," +
						"MongoDBTextFilterUsage," +
						"MoreThanOneInjectableConstructor," +
						"MustBeClosedChecker," +
						"NCopiesOfChar," +
						"NestedOptionals," +
						"NestedPublishers," +
						"NoCanIgnoreReturnValueOnClasses," +
						"NonCanonicalStaticImport," +
						"NonEmptyMono," +
						"NonFinalCompileTimeConstant," +
						"NonRuntimeAnnotation," +
						"NonStaticImport," +
						"NullArgumentForNonNullParameter," +
						"NullTernary," +
						"NullableOnContainingClass," +
						"OptionalEquality," +
						"OptionalMapUnusedValue," +
						"OptionalOfRedundantMethod," +
						"OptionalOrElseGet," +
						"OverlappingQualifierAndScopeAnnotation," +
						"OverridesJavaxInjectableMethod," +
						"PackageInfo," +
						"ParametersButNotParameterized," +
						"ParcelableCreator," +
						"PeriodFrom," +
						"PeriodGetTemporalUnit," +
						"PeriodTimeMath," +
						"PreconditionsInvalidPlaceholder," +
						"PrimitiveComparison," +
						"PrivateSecurityContractProtoAccess," +
						"ProtoBuilderReturnValueIgnored," +
						"ProtoStringFieldReferenceEquality," +
						"ProtoTruthMixedDescriptors," +
						"ProtocolBufferOrdinal," +
						"ProvidesMethodOutsideOfModule," +
						"RandomCast," +
						"RandomModInteger," +
						"RectIntersectReturnValueIgnored," +
						"RedundantSetterCall," +
						"RedundantStringConversion," +
						"RedundantStringEscape," +
						"RefasterAnyOfUsage," +
						"RequestMappingAnnotation," +
						"RequestParamType," +
						"RequiredModifiers," +
						"RestrictedApi," +
						"ReturnValueIgnored," +
						"SelfAssertion," +
						"SelfAssignment," +
						"SelfComparison," +
						"SelfEquals," +
						"SetUnrecognized," +
						"ShouldHaveEvenArgs," +
						"SizeGreaterThanOrEqualsZero," +
						"Slf4jLogStatement," +
						"Slf4jLoggerDeclaration," +
						"SpringMvcAnnotation," +
						"StaticImport," +
						"StreamToString," +
						"StringBuilderInitWithChar," +
						"StringJoin," +
						"StringJoin," +
						"SubstringOfZero," +
						"SuppressWarningsDeprecated," +
						"TemporalAccessorGetChronoField," +
						"TestParametersNotInitialized," +
						"TheoryButNoTheories," +
						"ThreadBuilderNameWithPlaceholder," +
						"ThrowIfUncheckedKnownChecked," +
						"ThrowNull," +
						"TimeZoneUsage," +
						"TreeToString," +
						"TryFailThrowable," +
						"TypeParameterQualifier," +
						"UnicodeDirectionalityCharacters," +
						"UnicodeInCode," +
						"UnnecessaryCheckNotNull," +
						"UnnecessaryTypeArgument," +
						"UnsafeWildcard," +
						"UnusedAnonymousClass," +
						"UnusedCollectionModifiedInPlace," +
						"VarTypeName," +
						"WrongOneof," +
						"XorPower," +
						"ZoneIdOfZ,"
			)
		}
		val shouldDisableErrorProne = java.toolchain.implementation.orNull == JvmImplementation.J9
		if (name == "compileJava" && !shouldDisableErrorProne) {
			disable(
				// error-prone (https://errorprone.info/bugpatterns)
				"AnnotateFormatMethod", // We don't want to use ErrorProne's annotations.
				"BadImport", // This check is opinionated wrt. which method names it considers unsuitable for import which includes a few of our own methods in `ReflectionUtils` etc.
				"DirectReturn", // https://github.com/junit-team/junit-framework/pull/5006#discussion_r2403984446
				"DoNotCallSuggester",
				"ImmutableEnumChecker",
				"InlineMeSuggester",
				"MissingSummary", // Produces a lot of findings that we consider to be false positives, for example for package-private classes and methods.
				"StringSplitter", // We don't want to use Guava.
				"UnnecessaryLambda", // The findings of this check are subjective because a named constant can be more readable in many cases.
				// error-prone.picnic (https://error-prone.picnic.tech)
				"ConstantNaming",
				"FormatStringConcatenation",
				"IdentityConversion",
				"LexicographicalAnnotationAttributeListing",
				"LexicographicalAnnotationListing",
				"NestedOptionals",
				"NonStaticImport",
				"OptionalOrElseGet",
				"PrimitiveComparison",
				"StaticImport",
				"TimeZoneUsage",
			)
			error("PackageLocation")
		} else {
			disableAllChecks = true
		}
		nullaway {
			if (shouldDisableErrorProne) {
				disable()
			} else {
				enable()
			}
			isJSpecifyMode = true
			customContractAnnotations.add("org.junit.platform.commons.annotation.Contract")
			checkContracts = true
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
