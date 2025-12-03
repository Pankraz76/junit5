plugins {
	id("org.openrewrite.rewrite")
}

dependencies {
	rewrite("org.openrewrite.recipe:rewrite-static-analysis:2.22.0")
	rewrite("org.openrewrite.recipe:rewrite-testing-frameworks:3.22.0")
}

rewrite {
	activeRecipe("org.junit.openrewrite.SanityCheck")
	configFile = project.getRootProject().file("gradle/config/rewrite.yml")
	exclusion(
		"**AggregatorIntegrationTests.java",
		"**AnnotationConsumerInitializerTests.java",
		"**Assert**AssertionsTests.java", // trivial import fix.
		"**BeforeAndAfterSuiteTests.java",
		"**BridgeMethods.java",
		"**DiscoverySelectorResolverTests.java",
		"**DiscoveryTests.java",
		"**DisplayNameGenerationTests.java",
		"**DynamicNodeGenerationTests.java",
		"**DynamicTestTests.java",
		"**ExceptionHandlingTests.java",
		"**ExecutionCancellationTests.java",
		"**ExtensionRegistrationViaParametersAndFieldsTests.java",
		"**InvocationInterceptorTests.java",
		"**IsTestMethodTests.java",
		"**IsTestTemplateMethodTests.java",
		"**JupiterTestDescriptorTests.java",
		"**LifecycleMethodUtilsTests.java",
		"**MultipleTestableAnnotationsTests.java",
		"**NestedContainerEventConditionTests.java",
		"**ParallelExecutionIntegrationTests.java",
		"**ParameterResolverTests.java",
		"**ParameterizedTestIntegrationTests.java",
		"**RepeatedTestTests.java",
		"**StaticPackagePrivateBeforeMethod.java",
		"**SubclassedAssertionsTests.java",
		"**TempDirectoryCleanupTests.java",
		"**TestCase.java",
		"**TestCases.java",
		"**TestExecutionExceptionHandlerTests.java",
		"**TestInstanceFactoryTests.java",
		"**TestTemplateInvocationTestDescriptorTests.java",
		"**TestTemplateInvocationTests.java",
		"**TestTemplateTestDescriptorTests.java",
		"**TestWatcherTests.java",
		"**TimeoutExtensionTests.java",
		"**UniqueIdTrackingListenerIntegrationTests.java",
		"**WorkerThreadPoolHierarchicalTestExecutorServiceTests.java",
		"**documentation/src/test/java/example**",
		"**org/junit/jupiter/engine/bridge**",
		"**testFixtures/java/org/junit/vintage/engine/samples**",
	)
	setExportDatatables(true)
	setFailOnDryRunResults(true)
}
