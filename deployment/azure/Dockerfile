# syntax=docker/dockerfile:1
# The local dist/ directory is excluded from the build context. The image
# always generates a new WAR from the checked-out source.
FROM eclipse-temurin:17-jdk-jammy AS glassfish

ARG GLASSFISH_VERSION=7.0.25
ARG CONNECTOR_J_VERSION=9.7.0
ARG NETBEANS_VERSION=20

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends ca-certificates curl mysql-client unzip \
    && rm -rf /var/lib/apt/lists/* \
    && curl --fail --location --silent --show-error \
        --output /tmp/glassfish.zip \
        "https://github.com/eclipse-ee4j/glassfish/releases/download/${GLASSFISH_VERSION}/glassfish-${GLASSFISH_VERSION}.zip" \
    && unzip -q /tmp/glassfish.zip -d /opt \
    && rm /tmp/glassfish.zip \
    && curl --fail --location --silent --show-error \
        --output /opt/glassfish7/glassfish/domains/domain1/lib/mysql-connector-j.jar \
        "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/${CONNECTOR_J_VERSION}/mysql-connector-j-${CONNECTOR_J_VERSION}.jar" \
    && curl --fail --location --silent --show-error \
        --output /tmp/netbeans.zip \
        "https://archive.apache.org/dist/netbeans/netbeans/${NETBEANS_VERSION}/Apache-NetBeans-${NETBEANS_VERSION}-bin.zip" \
    && unzip -q /tmp/netbeans.zip -d /opt \
    && mv /opt/netbeans /opt/netbeans-${NETBEANS_VERSION} \
    && rm /tmp/netbeans.zip

FROM glassfish AS build

WORKDIR /workspace
# Copy only the NetBeans project inputs. Database dumps, local artifacts,
# environment files and the legacy Connector/J copies never enter the build.
COPY build.xml ./
COPY nbproject ./nbproject
COPY src ./src
COPY test ./test
COPY web ./web

# NetBeans supplies the project-specific Ant tasks.  compile-jsps is deliberately
# not invoked here: NetBeans 20's JspC rejects compilerSourceVM=17 (PWC6309).
# The runtime deployment below is the authoritative JSP validation for Java 17.
RUN /opt/netbeans-${NETBEANS_VERSION}/netbeans/extide/ant/bin/ant \
        -Dj2ee.server.home=/opt/glassfish7/glassfish \
        clean compile compile-test dist

FROM glassfish AS runtime

LABEL org.opencontainers.image.title="CAVA test deployment" \
      org.opencontainers.image.description="GlassFish 7 / Java 17 runtime for the CAVA test environment" \
      org.opencontainers.image.licenses="EPL-2.0"

COPY --from=build /workspace/dist/Cava.war /opt/cava/Cava.war
COPY deployment/azure/entrypoint.sh /opt/cava/entrypoint.sh
COPY database/00_create_database.sql database/01_schema.sql database/02_indexes.sql database/03_seed_catalogs.sql /opt/cava/sql/

RUN chmod 0555 /opt/cava/entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/opt/cava/entrypoint.sh"]
