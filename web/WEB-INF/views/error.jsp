<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:choose>
  <c:when test="${not empty errorStatus}">
    <c:set var="safeStatus" value="${errorStatus}"/>
    <c:set var="safeTitle" value="${errorTitle}"/>
    <c:set var="safeMessage" value="${errorMessage}"/>
  </c:when>
  <c:otherwise>
    <c:set var="safeStatus" value="${requestScope['jakarta.servlet.error.status_code']}"/>
    <c:choose>
      <c:when test="${safeStatus == 404}">
        <c:set var="safeTitle" value="Página no encontrada"/>
        <c:set var="safeMessage" value="No encontramos el recurso solicitado."/>
      </c:when>
      <c:when test="${safeStatus == 405}">
        <c:set var="safeTitle" value="Método no permitido"/>
        <c:set var="safeMessage" value="La operación solicitada no está disponible."/>
      </c:when>
      <c:otherwise>
        <c:set var="safeTitle" value="Ocurrió un error"/>
        <c:set var="safeMessage" value="No pudimos completar tu solicitud en este momento."/>
      </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><c:out value="${safeStatus}"/> — CAVA</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
  <main class="public-message" aria-labelledby="error-title">
    <p class="public-message-code"><c:out value="${safeStatus}"/></p>
    <h1 id="error-title"><c:out value="${safeTitle}"/></h1>
    <p><c:out value="${safeMessage}"/></p>
    <a class="btn-primary" href="${pageContext.request.contextPath}/inicio">Volver al inicio</a>
  </main>
</body>
</html>
