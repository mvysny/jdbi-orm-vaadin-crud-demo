[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin-flow/Lobby#?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# JDBI-ORM demo using Vaadin

A demo project showing the CRUD capabilities of the [JDBI-ORM](https://gitlab.com/mvysny/jdbi-orm)
ORM library. Requires Java 17+.

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
