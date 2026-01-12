# Allows you to run this app easily as a docker container.
# See README.md for more details.
#
# 1. Build the image with: docker build -t test/jdbi-orm-vaadin-crud-demo:latest .
# 2. Run the image with: docker run --rm -ti -p8080:8080 test/jdbi-orm-vaadin-crud-demo
#
# Uses Docker Multi-stage builds: https://docs.docker.com/build/building/multi-stage/

# The "Build" stage. Copies the entire project into the container, into the /app/ folder, and builds it.
FROM eclipse-temurin:21 AS BUILD
COPY . /app/
WORKDIR /app/
RUN --mount=type=cache,target=/root/.m2 --mount=type=cache,target=/root/.vaadin ./mvnw -C clean test package
WORKDIR /app/target/
RUN ls -la
RUN mkdir app && tar xvzf *.tar.gz -C app
# At this point, we have the app (executable bash scrip plus a bunch of jars) in the
# /app/target/app/ folder.

# The "Run" stage. Start with a clean image, and copy over just the app itself, omitting gradle, npm and any intermediate build files.
FROM eclipse-temurin:21
COPY --from=BUILD /app/target/app /app/
WORKDIR /app/bin
EXPOSE 8080
ENTRYPOINT ./app
