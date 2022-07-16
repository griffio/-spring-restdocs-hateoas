# Kollchap spring hateoas rest-docs

Building up example with [Spring REST Docs 2.0.6](https://github.com/spring-projects/spring-restdocs)

Kotlin 1.7.0

~~~
./gradlew test
~~~

Create deployable jar
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

