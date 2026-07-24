#!/usr/bin/env sh
set -eu

ASADMIN="/opt/glassfish7/glassfish/bin/asadmin"
DOMAIN_DIR="/opt/glassfish7/glassfish/domains/domain1"
PASSWORD_FILE=""

cleanup() {
    if [ -n "${PASSWORD_FILE}" ] && [ -f "${PASSWORD_FILE}" ]; then
        rm -f "${PASSWORD_FILE}"
    fi
}

shutdown() {
    "${ASADMIN}" stop-domain domain1 >/dev/null 2>&1 || true
    exit 0
}

trap cleanup EXIT
trap shutdown INT TERM

require_value() {
    variable_name="$1"
    variable_value="$(printenv "${variable_name}" 2>/dev/null || true)"
    if [ -z "${variable_value}" ]; then
        echo "Missing required runtime secret: ${variable_name}" >&2
        exit 64
    fi
}

require_safe_property() {
    variable_name="$1"
    variable_value="$(printenv "${variable_name}")"
    case "${variable_value}" in
        *:*|*=*)
            echo "Invalid JDBC property value: ${variable_name}" >&2
            exit 65
            ;;
    esac
}

for required_secret in CAVA_DB_HOST CAVA_DB_PORT CAVA_DB_NAME CAVA_DB_USER CAVA_DB_PASSWORD; do
    require_value "${required_secret}"
done

for jdbc_property in CAVA_DB_HOST CAVA_DB_PORT CAVA_DB_NAME CAVA_DB_USER; do
    require_safe_property "${jdbc_property}"
done

case "${CAVA_DB_PORT}" in
    *[!0-9]*|'')
        echo "CAVA_DB_PORT must be numeric" >&2
        exit 65
        ;;
esac

# Azure Database for MySQL Flexible Server requires encrypted connections.
if [ "${CAVA_DB_TLS_MODE:-VERIFY_IDENTITY}" != "VERIFY_IDENTITY" ]; then
    echo "CAVA_DB_TLS_MODE must be VERIFY_IDENTITY" >&2
    exit 65
fi

"${ASADMIN}" start-domain domain1 >/dev/null

# GlassFish stores only an encrypted password alias in its ephemeral runtime
# domain. The plaintext is removed before the application is deployed.
PASSWORD_FILE="$(mktemp)"
umask 077
printf 'AS_ADMIN_PASSWORD=\nAS_ADMIN_ALIASPASSWORD=%s\n' "${CAVA_DB_PASSWORD}" > "${PASSWORD_FILE}"
"${ASADMIN}" create-password-alias --passwordfile "${PASSWORD_FILE}" cava.mysql.password >/dev/null
rm -f "${PASSWORD_FILE}"
PASSWORD_FILE=""
unset CAVA_DB_PASSWORD

"${ASADMIN}" create-jdbc-connection-pool \
    --datasourceclassname com.mysql.cj.jdbc.MysqlDataSource \
    --restype javax.sql.DataSource \
    --property "serverName=${CAVA_DB_HOST}:portNumber=${CAVA_DB_PORT}:databaseName=${CAVA_DB_NAME}:user=${CAVA_DB_USER}:password=\${ALIAS=cava.mysql.password}:sslMode=VERIFY_IDENTITY" \
    CavaPool >/dev/null

"${ASADMIN}" create-jdbc-resource --connectionpoolid CavaPool jdbc/CavaDS >/dev/null
"${ASADMIN}" ping-connection-pool CavaPool >/dev/null
"${ASADMIN}" deploy --force --name Cava --contextroot /Cava /opt/cava/Cava.war >/dev/null

echo "CAVA deployed at /Cava with jdbc/CavaDS."

# Port 4848 is not exposed. The container only receives traffic through 8080.
tail -F "${DOMAIN_DIR}/logs/server.log" &
LOG_PID="$!"
wait "${LOG_PID}"
