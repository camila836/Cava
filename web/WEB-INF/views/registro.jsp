<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Crear cuenta — CAVA Alta Chocolatería</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/RegistrarUsuario.css">
</head>
<body>
<div class="wrap">
  <div class="side">
    <a class="side-back" href="${pageContext.request.contextPath}/inicio">Volver</a>
    <span class="side-eyebrow">Bienvenido a</span>
    <h2>CAVA<br>Alta Chocolatería</h2>
    <p>Crea tu cuenta para descubrir nuestras creaciones artesanales premium.</p>
  </div>
  <div class="form-panel">
    <div class="form-head">
      <h3>Crear cuenta</h3>
      <p>¿Ya tienes cuenta? <a href="${pageContext.request.contextPath}/login">Inicia sesión</a></p>
    </div>
    <c:if test="${not empty errors.formulario}"><p class="aviso"><c:out value="${errors.formulario}"/></p></c:if>
    <form action="${pageContext.request.contextPath}/registro" method="post">
      <input type="hidden" name="csrfToken" value="<c:out value='${csrfToken}'/>">
      <div class="g2">
        <div class="field">
          <label class="lbl" for="nombres">Nombres</label>
          <input type="text" id="nombres" name="nombres" required maxlength="45" autocomplete="given-name" value="<c:out value='${nombres}'/>">
          <c:if test="${not empty errors.nombres}"><small class="aviso"><c:out value="${errors.nombres}"/></small></c:if>
        </div>
        <div class="field">
          <label class="lbl" for="apellidos">Apellidos</label>
          <input type="text" id="apellidos" name="apellidos" required maxlength="45" autocomplete="family-name" value="<c:out value='${apellidos}'/>">
          <c:if test="${not empty errors.apellidos}"><small class="aviso"><c:out value="${errors.apellidos}"/></small></c:if>
        </div>
      </div>
      <div class="field">
        <label class="lbl" for="correo">Correo electrónico</label>
        <input type="email" id="correo" name="correo" required maxlength="100" autocomplete="email" value="<c:out value='${correo}'/>">
        <c:if test="${not empty errors.correo}"><small class="aviso"><c:out value="${errors.correo}"/></small></c:if>
      </div>
      <div class="g2">
        <div class="field">
          <label class="lbl" for="clave">Contraseña</label>
          <input type="password" id="clave" name="clave" required minlength="15" maxlength="128" autocomplete="new-password">
          <c:if test="${not empty errors.clave}"><small class="aviso"><c:out value="${errors.clave}"/></small></c:if>
        </div>
        <div class="field">
          <label class="lbl" for="confirmarClave">Confirmar contraseña</label>
          <input type="password" id="confirmarClave" name="confirmarClave" required minlength="15" maxlength="128" autocomplete="new-password">
          <c:if test="${not empty errors.confirmarClave}"><small class="aviso"><c:out value="${errors.confirmarClave}"/></small></c:if>
        </div>
      </div>
      <label class="consent">
        <input type="checkbox" name="autorizacionTratamientoDatos" value="true" required>
        Autorizo el tratamiento de mis datos personales para gestionar mi cuenta CAVA.
      </label>
      <c:if test="${not empty errors.consentimiento}"><small class="aviso"><c:out value="${errors.consentimiento}"/></small></c:if>
      <button type="submit" class="btn-cta">Crear cuenta</button>
    </form>
  </div>
</div>
</body>
</html>
