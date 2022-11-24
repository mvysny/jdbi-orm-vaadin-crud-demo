[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin-flow/Lobby#?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# JDBI-ORM demo using Vaadin

A demo project showing the CRUD capabilities of the [JDBI-ORM](https://gitlab.com/mvysny/jdbi-orm)
ORM library.

Both the development and production modes are supported. Also, the project
demoes packaging itself both into a flatten uberjar and a zip file containing
a list of jars and a runner script. See "Packaging for production" below
for more details.

# Documentation

Please see the [Vaadin Boot](https://github.com/mvysny/vaadin-boot#preparing-environment) documentation
on how you run, develop and package this Vaadin-Boot-based app.

## Database

Without the database, we could store the categories and reviews into session only, which would then be gone when the server rebooted.
We will use the [Vaadin-on-Kotlin](http://vaadinonkotlin.eu/)'s SQL database support. To make things easy we'll
use in-memory H2 database which will be gone when the server is rebooted - *touche* :-D

We will use [Flyway](https://flywaydb.org/) for database migration. Check out [Bootstrap](src/main/java/com/vaadin/starter/skeleton/Bootstrap.java)
on how the [migration scripts](src/main/resources/db/migration) are ran when the app is initialized.

The [Person](src/main/java/com/vaadin/starter/skeleton/Person.java)
entity is mapped to the database tables; inheriting from Entity and Dao
will make it inherit a bunch of useful methods such as `findAll()` and `save()`. It will also gain means of
providing all of its instances via a `DataProvider`. See the [MainView](src/main/java/com/vaadin/starter/skeleton/MainView.java)
Grid configuration for details.
