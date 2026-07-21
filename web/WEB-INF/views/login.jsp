<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Iniciar sesión — CAVA Alta Chocolatería</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/InicioSesion.css">
</head>
<body>
<div class="wrap">
  <div class="side">
    <a class="side-back" href="${pageContext.request.contextPath}/inicio">Volver</a>
    <span class="side-eyebrow">Bienvenido de vuelta</span>
    <h2>CAVA<br>Alta Chocolatería</h2>
    <p>Accede a tu cuenta para continuar.</p>
  </div>
  <div class="form-panel">
    <div class="form-head">
      <h3>Iniciar sesión</h3>
      <p>¿No tienes cuenta? <a href="${pageContext.request.contextPath}/registro">Regístrate</a></p>
    </div>
    <c:if test="${registrationCompleted}"><p class="aviso">Cuenta creada correctamente. Ya puedes iniciar sesión.</p></c:if>
    <c:if test="${not empty loginError}"><p class="aviso"><c:out value="${loginError}"/></p></c:if>
    <form action="${pageContext.request.contextPath}/login" method="post">
      <input type="hidden" name="csrfToken" value="<c:out value='${csrfToken}'/>">
      <div class="field">
        <label class="lbl" for="correo">Correo electrónico</label>
        <input type="email" id="correo" name="correo" required maxlength="100" autocomplete="email" value="<c:out value='${correo}'/>">
      </div>
      <div class="field">
        <label class="lbl" for="clave">Contraseña</label>
        <input type="password" id="clave" name="clave" required maxlength="128" autocomplete="current-password">
      </div>
      <button type="submit" class="btn-cta">Entrar a mi cuenta</button>
    </form>
  </div>
</div>
</body>
</html>
