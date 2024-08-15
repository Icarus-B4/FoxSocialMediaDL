

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Cuberto/liquid-swipe-android")
            credentials {
                username = System.getenv("GPR_USER") ?: "Icarus-B4"
                password = System.getenv("GPR_API_KEY") ?: "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"
            }
        }
    }
}

rootProject.name = "Fox-SocialMediaDL"
include(":app")
