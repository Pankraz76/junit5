pluginManagement {
	includeBuild("../base")
}

plugins {
	id("junitbuild.dsl-extensions") apply false
}

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("../libs.versions.toml"))
		}
	}
	repositories {
		gradlePluginPortal()
	}
}

rootProject.name = "plugins"

include("backward-compatibility")
include("build-parameters")
include("code-generator")
include("common")
include("publishing")
include("rewrite")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
