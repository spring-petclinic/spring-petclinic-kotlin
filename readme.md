# Kotlin version of the Spring PetClinic Sample Application [![Build Status](https://travis-ci.org/spring-petclinic/spring-petclinic-kotlin.png?branch=master)](https://travis-ci.org/spring-petclinic/spring-petclinic-kotlin/)

This is a [Kotlin](https://kotlinlang.org/) version of the [spring-petclinic][] Application. 

## Technologies used

* Language: Kotlin
* Core framework: Spring Boot 2.0 with Spring Framework 5 Kotlin support
* Server: Netty
* Web framework: Spring MVC
* Templates: Thymeleaf and Bootstrap
* Persistence : Spring Data JPA
* Databases: H2 and MySQL both supported
* Build: Gradle Script with the Kotlin DSL
* Testing: Junit 5, Mockito and AssertJ

## Running petclinic locally
```
	git clone https://github.com/spring-petclinic/spring-petclinic-kotlin.git
	cd spring-petclinic-kotlin
	./gradlew bootRun
```

You can then access petclinic here: http://localhost:8080/

<img width="1042" alt="petclinic-screenshot" src="https://user-images.githubusercontent.com/838318/29994372-7f85f6da-8fce-11e7-8896-b5aa075ac0d7.png">

## In case you find a bug/suggested improvement for Spring Petclinic
Our issue tracker is available here: https://github.com/spring-petclinic/spring-petclinic-kotlin/issues


## Database configuration

In its default configuration, Petclinic uses an in-memory database (HSQLDB) which
gets populated at startup with data. A similar setup is provided for MySql in case a persistent database configuration is needed.
Note that whenever the database type is changed, the data-access.properties file needs to be updated and the mysql-connector-java artifact from the pom.xml needs to be uncommented.

You could start a MySql database with docker:

```
docker run -e MYSQL_ROOT_PASSWORD=petclinic -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:5.7
```


## Looking for something in particular?

| Features                           | Class, files or Java property files  |
|------------------------------------|---|
|The Main Class                      | [PetClinicApplication](https://github.com/spring-petclinic/spring-petclinic-kotlin/blob/master/src/main/kotlin/org/springframework/samples/petclinic/PetClinicApplication.kt) |
|Properties configuration file       | [application.properties](https://github.com/spring-petclinic/spring-petclinic-kotlin/blob/master/src/main/resources) |
|Gradle build script with Kotlin DSL | [build.gradle.kts](https://github.com/spring-petclinic/spring-petclinic-kotlin/blob/master/build.gradle.kts) |
|Caching Configuration               | [CacheConfig](https://github.com/spring-petclinic/spring-petclinic-kotlin/blob/master/src/main/kotlin/org/springframework/samples/petclinic/system/CacheConfig.kt) |


## Import and run the project in IntelliJ IDEA
   
* Make sure you have at least IntelliJ IDEA 2017.2 and IDEA Kotlin plugin 1.1.60+ (menu Tools -> Kotlin -> configure Kotlin Plugin Updates -> make sure "Stable" channel is selected -> check for updates now -> restart IDE after the update)
* Import it in IDEA as a Gradle project
  * Go to the menu "File -> New -> Project from Existing Sources... "
  * Select the spring-petclinic-kotlin directory then choose "Import the project from Gradle"
  * Select the "Use gradle wrapper task configuration" radio button
* In IntelliJ IDEA, right click on PetClinicApplication.kt then "Run..." or "Debug..."
* Open http://localhost:8080/ in your browser


## Documentation

* [Migrez une application Java Spring Boot vers kotlin](http://javaetmoi.com/2017/09/migrez-application-java-spring-boot-vers-kotlin/) (french)
* [Migration Spring Web MVC vers Spring WebFlux](http://javaetmoi.com/2017/12/migration-spring-web-mvc-vers-spring-webflux/) (french)

## Interesting Spring Petclinic forks

The Spring Petclinic master branch in the main
[spring-projects](https://github.com/spring-project/spring-petclinic)
GitHub org is the "canonical" implementation, currently based on
Spring Boot and Thymeleaf. 

This [spring-petclinic-kotlin][] project is one of the several forks hosted
in a special GitHub org: [spring-petclinic](https://github.com/spring-petclinic).
If you have a special interest in a different technology stack that could
be used to implement the Pet Clinic then please join the community
there.

| Link                               | Main technologies |
|------------------------------------|-------------------|
| [spring-petclinic][]               | "Canonical" implementation of Spring Petclinic based on Spring Boot and Thymeleaf |
| [spring-framework-petclinic][]     | Spring Framework XML configuration, JSP pages, 3 persistence layers: JDBC, JPA and Spring Data JPA |
| [javaconfig branch][]              | Same frameworks as the [spring-framework-petclinic][] but with Java Configuration instead of XML |
| [spring-petclinic-angularjs][]     | AngularJS 1.x, Spring Boot and Spring Data JPA |
| [spring-petclinic-angular][]       | Angular 4 front-end of the Petclinic REST API [spring-petclinic-rest][] |
| [spring-petclinic-microservices][] | Distributed version of Spring Petclinic built with Spring Cloud |
| [spring-petclinic-reactjs][]       | ReactJS (with TypeScript) and Spring Boot |
| [spring-petclinic-graphql][]       | GraphQL version based on React Appolo, TypeScript and GraphQL Spring boot starter |
| [spring-petclinic-kotlin][]        | Kotlin version of [spring-petclinic][] |
| [spring-petclinic-rest][]          | Backend REST API |


## Interaction with other open source projects

One of the best parts about working on the Spring Petclinic application is that we have the opportunity to work in direct contact with many Open Source projects. We found some bugs/suggested improvements on various topics such as Spring, Spring Data, Bean Validation and even Eclipse! In many cases, they've been fixed/implemented in just a few days.
Here is a list of them:

| Name | Issue |
|------|-------|
| Spring JDBC: simplify usage of NamedParameterJdbcTemplate | [SPR-10256](https://jira.springsource.org/browse/SPR-10256) and [SPR-10257](https://jira.springsource.org/browse/SPR-10257) |
| Bean Validation / Hibernate Validator: simplify Maven dependencies and backward compatibility |[HV-790](https://hibernate.atlassian.net/browse/HV-790) and [HV-792](https://hibernate.atlassian.net/browse/HV-792) |
| Spring Data: provide more flexibility when working with JPQL queries | [DATAJPA-292](https://jira.springsource.org/browse/DATAJPA-292) |


# Contributing

The [issue tracker][] is the preferred channel for bug reports, features requests and submitting pull requests.

For pull requests, editor preferences are available in the [editor config](.editorconfig) for easy use in common text editors. Read more and download plugins at <http://editorconfig.org>.


[issue tracker]: https://github.com/spring-petclinic/spring-petclinic-kotlin/issues
[spring-petclinic]: https://github.com/spring-projects/spring-petclinic
[spring-framework-petclinic]: https://github.com/spring-petclinic/spring-framework-petclinic
[spring-petclinic-angularjs]: https://github.com/spring-petclinic/spring-petclinic-angularjs 
[javaconfig branch]: https://github.com/spring-petclinic/spring-framework-petclinic/tree/javaconfig
[spring-petclinic-angular]: https://github.com/spring-petclinic/spring-petclinic-angular
[spring-petclinic-microservices]: https://github.com/spring-petclinic/spring-petclinic-microservices
[spring-petclinic-reactjs]: https://github.com/spring-petclinic/spring-petclinic-reactjs
[spring-petclinic-graphql]: https://github.com/spring-petclinic/spring-petclinic-graphql
[spring-petclinic-kotlin]: https://github.com/spring-petclinic/spring-petclinic-kotlin
[spring-petclinic-rest]: https://github.com/spring-petclinic/spring-petclinic-rest




