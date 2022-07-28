# Kollchap spring hateoas rest-docs

Building up example with [Spring REST Docs 2.0.6](https://github.com/spring-projects/spring-restdocs)

Verify Restful Api and generate docs from Tests [kotlin/griffio/kollchap/demo/DemoApplicationTests.kt](https://github.com/griffio/spring-restdocs-hateoas/blob/master/src/test/kotlin/griffio/kollchap/demo/DemoApplicationTests.kt)

Spring Boot 2.7.1

Kotlin 1.7.0

Asciidoc template located in [/src/docs/asciidoc/index.adoc](https://github.com/griffio/spring-restdocs-hateoas/blob/master/src/docs/asciidoc/index.adoc)

Sample output doc can be [viewed](https://griffio.github.io/spring-restdocs-hateoas/) 

---

Tasks

Run tests and output completed docs in `build/docs/asciidoc/index.html`
~~~
./gradlew asciidoctor
~~~

Run tests only - does not generate index.html
~~~
./gradlew test
~~~

Run all tasks and create deployable jar
~~~
./gradlew bootJar
~~~

Execute jar with compatible Java 11
~~~
java -jar build/libs/kollchap-0.0.1-SNAPSHOT.jar
~~~

Restful Api index
~~~
http://localhost:8080/
~~~

Restdocs index
~~~
http://localhost:8080/docs/index.html
~~~

