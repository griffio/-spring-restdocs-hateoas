# Kollchap Restful Dungeon spring hateoas rest-docs

Building up example with [Spring REST Docs 2.0.6](https://github.com/spring-projects/spring-restdocs)

* Added javax.validator constraints to be pulled out
* Added subsection to pull out DnD character stats

Verify Restful Api and generate docs from Tests [kotlin/griffio/kollchap/demo/DemoApplicationTests.kt](https://github.com/griffio/spring-restdocs-hateoas/blob/master/src/test/kotlin/griffio/kollchap/demo/DemoApplicationTests.kt)

Spring Boot 2.7.1

Kotlin 1.7.10

Asciidoc template located in [/src/docs/asciidoc/index.adoc](https://github.com/griffio/spring-restdocs-hateoas/blob/master/src/docs/asciidoc/index.adoc)

Use GitHub action to build asciidoc and publish html to GitHubPages

The published restdoc can be viewed here [https://griffio.github.io/spring-restdocs-hateoas](https://griffio.github.io/spring-restdocs-hateoas/) 

![image](https://user-images.githubusercontent.com/346896/187029634-f1417b98-0a49-42e0-8e7b-9143d8910dee.png)

The Dungeons Of Kollchap - from https://en.wikipedia.org/wiki/What_Is_Dungeons_%26_Dragons%3F

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

HAL explorer client
~~~
http://localhost:8080/webjars/hal-explorer/1.1.0/index.html#uri=/
~~~
