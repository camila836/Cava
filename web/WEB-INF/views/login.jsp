<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Iniciar sesión — CAVA Alta Chocolatería</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,500;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/InicioSesion.css">
</head>
<body>
<div class="wrap">
  <div class="side">
    <a class="side-back" href="${pageContext.request.contextPath}/inicio">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" aria-hidden="true"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
      Volver
    </a>
    <div class="side-spheres" aria-hidden="true"><div class="sphere s1"></div><div class="sphere s2"></div></div>
    <span class="side-eyebrow">Bienvenido de vuelta</span>
    <h2>CAVA<br>Alta Chocolatería</h2>
    <p>Accede a tu cuenta para continuar y descubrir nuestras creaciones artesanales.</p>
    <div class="side-tag">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" aria-hidden="true"><path d="M12 3l7 3v6c0 4.5-3 8-7 9-4-1-7-4.5-7-9V6l7-3z"/></svg>
      Acceso protegido
    </div>
  </div>
  <div class="form-panel">
    <div class="form-head">
      <h3>Iniciar sesión</h3>
      <p>¿No tienes cuenta? <a href="${pageContext.request.contextPath}/registro">Regístrate aquí</a></p>
    </div>
    <div class="form-messages" aria-live="polite">
      <c:if test="${registrationCompleted}"><p class="aviso aviso-correcto">Cuenta creada correctamente. Ya puedes iniciar sesión.</p></c:if>
      <c:if test="${not empty loginError}"><p class="aviso aviso-error"><c:out value="${loginError}"/></p></c:if>
    </div>
    <form action="${pageContext.request.contextPath}/login" method="post">
      <input type="hidden" name="csrfToken" value="<c:out value='${csrfToken}'/>">
      <div class="field">
        <label class="lbl" for="correo">Correo electrónico</label>
        <input type="email" id="correo" name="correo" required maxlength="100" autocomplete="email" placeholder="correo@ejemplo.com" value="<c:out value='${correo}'/>">
      </div>
      <div class="field">
        <label class="lbl" for="clave">Contraseña</label>
        <input type="password" id="clave" name="clave" required maxlength="128" autocomplete="current-password" placeholder="Tu contraseña">
      </div>
      <button type="submit" class="btn-cta">Entrar a mi cuenta</button>
    </form>
  </div>
</div>
</body>
</html>
