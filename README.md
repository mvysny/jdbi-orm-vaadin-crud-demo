[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin-flow/Lobby#?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# JDBI-ORM demo using Vaadin

A demo project showing the CRUD capabilities of the [JDBI-ORM](https://gitlab.com/mvysny/jdbi-orm)
ORM library.

Both the development and production modes are supported. Also, the project
demoes packaging itself both into a flatten uberjar and a zip file containing
a list of jars and a runner script. See "Packaging for production" below
for more details.

## Running

Clone this github repository and import the project to the IDE of your choice as a Maven project. You need to have Java 11+ installed.

To run quickly from the command-line in development mode:

1. Run `./mvnw -C clean package exec:java`
2. Your app will be running on [http://localhost:8080](http://localhost:8080).

To run the app from your IDE:

1. Import the project into your IDE
2. Run `mvn -C clean package` in the project, to configure Vaadin for npm mode.
3. Run/Debug the `Main` class as an application (run the `main()` method).
   The app will use npm to download all javascript libraries (will take a long time)
   and will start in development mode.
4. Your app will be running on [http://localhost:8080](http://localhost:8080).
   
See [Main.java](src/main/java/com/vaadin/starter/skeleton/Main.java)
for details on how Jetty is configured for embedded mode.

### Missing `/src/main/webapp`?

Yeah, since we're not packaging to WAR but to uberjar/zip+jar, the `webapp` folder needs to be
served from the jar itself, and therefore it needs to reside in `src/main/resources/webapp`.

## Packaging for production

To package in production mode:

1. `mvn -C clean package -Pproduction`

The project packages itself in two ways:

1. As a flatten uberjar (a jar with all dependencies unpacked inside, which you can simply launch with `java -jar`).
   Please read below regarding inherent issues with flat uberjars.
   The deployable file is in `target/vaadin14-embedded-jetty-1.0-SNAPSHOT-uberjar.jar`
2. As a zip file with dependencies. The file is in `target/vaadin14-embedded-jetty-1.0-SNAPSHOT-zip.zip`

## Running in production mode

To build&run the flat uberjar:

1. `mvn -C clean package -Pproduction`
2. `cd target`
3. `java -jar jdbi-orm-vaadin14-crud-demo-1.0-SNAPSHOT-uberjar.jar`

To build&run the zip file:

1. `mvn -C clean package -Pproduction`
2. `cd target`
3. `unzip jdbi-orm-vaadin14-crud-demo-1.0-SNAPSHOT-zip.zip`
4. `./run`

Head to [localhost:8080/](http://localhost:8080).

## Warning Regarding Flat Uberjar

There is an inherent problem with flat uberjar (everything unpacked, then packed as a single jar):
it disallows repeated resources or duplicate files. That can be problematic especially for Java Service API
property files located under `META-INF/services/`, since the flat uberjar will simply
throw away any duplicate property files, which can cause certain libraries to remain unconfigured.
You should therefore always prefer the zip+jar distribution; if you keep using
flat uberjar then please keep these limitations in mind.

Another inherent issue is that it's impossible to see the dependencies of the app
as a list of jars in the `lib/` folder, since everything is unpacked into one huge jar file.

## About The Project

How this works:

1. The app is initialized in the `Bootstrap` class. Jetty (run via `ManualJetty` class)
   will call the `Bootstrap` class since it's a WebListener
   (google for "Servlet WebListener" for more info on how this standard Servlet machinery works).
2. `Bootstrap` will configure the database: it will create [HikariCP](https://github.com/brettwooldridge/HikariCP) (a JDBC connection
   pool which keeps certain amount of JDBC connections around since they're expensive
   to construct), it will configure HikariCP to use the in-memory H2 database, and
   it will set the DataSource to the [JDBI-ORM](https://gitlab.com/mvysny/jdbi-orm) library. Done - the database layer is ready.
3. `Bootstrap` will also create the database tables for us. Generally you should use
   FlyWay to migrate your database to newer version, but I wanted to keep things simple here.
4. Since the database is now configured, `Bootstrap` can now simply access the database
   and generate a set of sample data for us. See how easy is to use JDBI to
   manage transactions for us - no interceptors needed.
5. Done - the application is now configured. You can now navigate to [localhost:8080/](http://localhost:8080)
   for Vaadin to do its job. Your Vaadin code can now simply call `Person.dao` directly to fetch the data -
   no dependency injection needed.

Testing:

1. Typically we would have to laborously mock out the database in order to test the UI, but
   we really don't have to: it's very easy to bootstrap the application including the database.
   And so we can simply perform a full system testing right away very fast.
   To initialize the app, simply call `new Bootstrap().contextInitialized(null)`
   to start the app in the current JVM. That's exactly what `AbstractAppLauncher` is doing.
2. Selenium-based tests are very slow, fail randomly, they are hard to maintain,
   error-prone and require a server up-and-running.
   Why bother then, when we can simply use [Karibu-Testing](https://github.com/mvysny/karibu-testing)
   instead? This approach is demoed in `MainViewTest` class.
3. Done - to run the tests simply run `mvn clean test`, or simply run the `MainViewTest` class
   from your IDE.

Let's look at all files that this project is composed of, and what are the points where you'll add functionality:

| Files | Meaning
| ----- | -------
| [pom.xml](pom.xml) | Maven 2 build tool configuration files. Maven is used to compile your app, download all dependency jars and build a war file
| [.travis.yml](.travis.yml) | Configuration file for [Travis-CI](http://travis-ci.org/) which tells Travis how to build the app. Travis watches your repo; it automatically builds your app and runs all the tests after every commit.
| [.gitignore](.gitignore) | Tells [Git](https://git-scm.com/) to ignore files that can be produced from your app's sources - be it files produced by Gradle, Intellij project files etc.
| [Procfile](Procfile) | Configures Heroku on how your application is launched in the cloud.
| [webpack.config.js](webpack.config.js) | TODO
| [src/main/java](src/main/java) | Place the sources of your app here.
| [MainView.java](src/main/java/com/vaadin/starter/skeleton/MainView.java) | The main view, shown when you browse for http://localhost:8080/
| [Person.java](src/main/java/com/vaadin/starter/skeleton/Person.java) | The `Person` entity mapped to the SQL database table `person`. Includes DAO (data access object) helper methods.
| [PersonForm.java](src/main/java/com/vaadin/starter/skeleton/PersonForm.java) | A form component which edits the `Person` entity. Uses [Vaadin Binder](https://vaadin.com/docs/flow/binding-data/tutorial-flow-components-binder.html) to populate form components with data from the `Person` entity.
| [Bootstrap.java](src/main/java/com/vaadin/starter/skeleton/Bootstrap.java) | Configures the database and creates the `Person` database table.
| [CreateEditPersonDialog.java](src/main/java/com/vaadin/starter/skeleton/CreateEditPersonDialog.java) | A dialog which edits the `Person` entity. Uses `PersonForm`.
| [ManualJetty.java](src/main/java/com/vaadin/starter/skeleton/ManualJetty.java) | Launches the Embedded Jetty; just run the `main()` method.
| [src/main/resources/](src/main/resources) | A bunch of static files not compiled by Java in any way; see below for explanation.
| [simplelogger.properties](src/main/resources/simplelogger.properties) | Configures the logging engine; this demo uses the SLF4J logging library with slf4j-simple logger.
| [src/main/webapp/](src/main/webapp) | Static web files served as-is by the web container.
| [src/test/java/](src/test/java) | Your unit & integration tests go here.
| [MainViewTest.java](src/test/java/com/vaadin/starter/skeleton/MainViewTest.java) | Tests the Vaadin UI; uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) UI test library.
| [frontend/](frontend) | TODO
| `node_modules` | populated by `npm` - contains sources of all JavaScript web components.

## Packaging for production

To package in production mode:

1. `mvn -C clean package -Pproduction`
2. `cd target`
3. `java -jar jdbi-orm-vaadin14-crud-demo-1.0-SNAPSHOT-uberjar.jar`

Head to [localhost:8080/](http://localhost:8080).

## Heroku Integration

See the [Live Demo of the app running on Heroku](https://jdbi-orm-vaadin14-crud-demo.herokuapp.com/).

To integrate with Heroku, you need to activate two Maven profiles:

1. The `production` profile which packages Vaadin in production mode
2. The `heroku` profile which uses the `frontend-maven-plugin` to install local node+npm in order for Vaadin Maven build to successfully run webpack to package for production.

> Note: unfortunately adding the `heroku:nodejs` buildpack in Heroku project settings did not worked for me,
I had to use the `frontend-maven-plugin`

Both profiles are activated by [heroku-settings.xml](heroku-settings.xml) Maven Settings file. To use the settings
file during Heroku build, set the `MAVEN_SETTINGS_PATH` config var to `heroku-settings.xml` in Heroku project settings tab.
See [Using a Custom Maven Settings File](https://devcenter.heroku.com/articles/using-a-custom-maven-settings-xml) and
[Stack Overflow: Activate Maven Profile On Heroku](https://stackoverflow.com/questions/11162194/triggering-maven-profiles-from-heroku-configured-environment-variables) for more details.

## More info

For a full Vaadin application example, there are more choices available also from [vaadin.com/start](https://vaadin.com/start) page.
