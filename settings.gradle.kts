rootProject.name = "spring-petclinic-kotlin"

pluginManagement {
	repositories {
		maven { url = uri("https://repo.spring.io/snapshot") }
		maven { url = uri("https://repo.spring.io/milestone") }
		gradlePluginPortal()
	}
}
