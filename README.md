[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin-flow/Lobby#?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# JDBI-ORM demo using Vaadin

A demo project showing the CRUD capabilities of the [JDBI-ORM](https://gitlab.com/mvysny/jdbi-orm)
ORM library. Requires Java 17+. Uses [jdbi-orm-vaadin](https://gitlab.com/mvysny/jdbi-orm-vaadin).

The [Person](src/main/java/com/vaadin/starter/skeleton/Person.java)
entity is mapped to the database tables; inheriting from Entity and Dao
will make it inherit a bunch of useful methods such as `findAll()` and `save()`. It will also gain means of
providing all of its instances via a `DataProvider`. See the [MainView](src/main/java/com/vaadin/starter/skeleton/MainView.java)
Grid configuration for details.

See the [live demo](https://v-herd.eu/jdbi-orm-vaadin-crud-demo/).

# Documentation

Please see the [Vaadin Boot](https://github.com/mvysny/vaadin-boot#preparing-environment) documentation
on how you run, develop and package this Vaadin-Boot-based app.

## Database

Without the database, we could store the categories and reviews into session only, which would then be gone when the server rebooted.
We will use the [Vaadin-on-Kotlin](http://vaadinonkotlin.eu/)'s SQL database support. To make things easy we'll
use in-memory H2 database which will be gone when the server is rebooted - *touche* :-D

We will use [Flyway](https://flywaydb.org/) for database migration. Check out [Bootstrap](src/main/java/com/vaadin/starter/skeleton/Bootstrap.java)
on how the [migration scripts](src/main/resources/db/migration) are ran when the app is initialized.

### PostgreSQL

You can also use the PostgreSQL database - simply set the `JDBC_URL`, `JDBC_USERNAME` and `JDBC_PASSWORD` env variables
accordingly. To test out, you can start PostgreSQL in docker:

```bash
docker run --rm -ti -e POSTGRES_PASSWORD=mysecretpassword -p 127.0.0.1:5432:5432 postgres:15.2
```

That will create a database named `postgres`, username `postgres` and password `mysecretpassword`.
Then set the env variables as follows:

```bash
export JDBC_URL="jdbc:postgresql://localhost:5432/postgres"
export JDBC_USERNAME="postgres"
export JDBC_PASSWORD="mysecretpassword"
```

To run tests on PostgreSQL, run Maven as follows:

```bash
$ mvn -C test -DargLine="-Dtest.postgresql"
```

The tests will start PostgreSQL in Docker using TestContainers automatically.

## Docker

The easiest way to run the app in Docker is to run the app with the embedded H2 database.
See the [Dockerfile](Dockerfile) for more documentation on how to build the docker image
and run it.

To run the app with PostgreSQL, the easiest way is to run PostgreSQL in a separate docker image,
then connect the images. That's exactly what [docker-compose.yaml](docker-compose.yaml)
is doing: it's starting the app in one Docker container, PostgreSQL in another, and
connects them in a private network. It then configures the app via env variables
to connect to the `postgres` machine running PostgreSQL. To run this setup,
run

```bash
$ docker-compose up
```

## Kubernetes

Please see the [Vaadin app with persistent PostgreSQL in Kubernetes](https://mvysny.github.io/kubernetes-vaadin-app-postgresql/)
article for an explanation how this works. In short, make sure that the necessary plugins are enabled:

```bash
$ microk8s enable dns hostpath-storage registry
```

Then, build the Docker image and push it to the Microk8s internal registry:

```bash
$ docker build --no-cache -t localhost:32000/test/jdbi-orm-vaadin-crud-demo:latest .
$ docker push localhost:32000/test/jdbi-orm-vaadin-crud-demo
```

Then, apply the [Kubernetes config file](kubernetes-app.yaml):

```bash
$ mkctl apply -f kubernetes-app.yaml
```
You should be able to browse to [localhost](http://localhost)
and see the app up-and-running.
