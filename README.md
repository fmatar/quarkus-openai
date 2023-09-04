# OpenAI Quarkus Driver

## Overview

This OpenAI driver is built using Quarkus, a full-stack, Kubernetes-native Java framework. The library provides
easy-to-use API endpoints for OpenAI's Completion and Chat Completion services. It allows seamless integration into any
Quarkus-based application and supports native compilation using GraalVM.

![Build Status] (https://github.com/fmatar/quarkus-openai/actions/workflows/build.yml/badge.svg)


## Features

- Model Information: Fetch details about OpenAI models.
- Completion: Perform text completion using OpenAI's GPT models.
- Chat Completion: Enables conversational agents with OpenAI's Chat models.
- Streamed Responses: Supports streaming of both completion and chat completion responses.

## Installation

Add the following dependency to your pom.xml:

```xml

<dependency>
  <groupId>org.slixes.platform</groupId>
  <artifactId>openai-quarkus-driver</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Usage

Initialize OpenAI Client

```java

@ApplicationScoped
public class YourService {

  @Inject
  OpenAI openAI;
  // ... Your code
}
```

**Fetch Available Models**

```java
Uni<Set<Model>>availableModels=openAI.models();
```

**Generate Completion**

```java
  CompletionRequest request=new CompletionRequest(/* your parameters */);
  Uni<CompletionResult> result=openAI.createCompletion(request);
```

**Generate Chat Completion**

```java
  ChatCompletionRequest request=new ChatCompletionRequest(/* your parameters */);
  Uni<ChatCompletionResult> result=openAI.createChatCompletion(request);
```

**Streamed Completion**

```java
  CompletionRequest request=new CompletionRequest(/* your parameters */);
  Multi<CompletionChunk> resultStream=openAI.createStreamedCompletion(request);
```

## Developer Guide

Prerequisites

- JDK 17 or higher
- Maven 3.9 or higher
- GraalVM (optional for native compilation)

### Clone the Repository

```bash
git clone https://github.com/fmatar/quarkus-openai.git
cd openai-quarkus-driver
```

### Build the Library

**JVM Mode**
To compile and package the library for JVM, execute:

``` bash
mvn clean package
```

This will produce a JAR file in the target directory that you can include in your project.

**Native Mode (GraalVM)**
If you have GraalVM installed and wish to compile the library into a native executable, execute:

```bash
mvn package -Pnative
```

This will produce a native executable in the target directory.

### Running Tests

To run unit tests, execute:

```bash
mvn test
```

### Contribution

- Fork the repository.
- Create a new branch for each feature, fix, or documentation improvement.
- Push your changes to your fork.
- Create a pull request against the main branch.

For detailed contribution guidelines, please refer to CONTRIBUTING.md.

### Native Compilation with GraalVM

This library is fully compatible with GraalVM, enabling you to compile your Quarkus application into a native
executable.

## License

This project is open-sourced under the MIT License.

## Contributing

We welcome contributions from the community. Please refer to the CONTRIBUTING.md for more details.

For more information on the API and more advanced features, please refer to the official documentation.

