# ARM64 Docker Spring Petclinic Kotlin
This repository is a fork of the official spring-petclinic-kotlin repository. It has been modified to include a GitHub
Action workflow that builds and pushes a Docker image for ARM64, AMD64 and ARM/v7 to Docker Hub.

This was created so I would have a simple SpringBoot JVM application to test and demo with on my ARM64 Kubernetes cluster.

No guarantees I will keep it up to date though 😅

## Running petclinic with Docker

```
docker run -p 8080:8080 wrdle/spring-petclinic-kotlin
```





