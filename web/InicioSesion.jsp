<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Iniciar Sesión — CAVA Alta Chocolatería</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,500;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/InicioSesion.css">
  <link rel="stylesheet" href="css/InicioSesion.css">
</head>
<body data-error="${requestScope.error}" data-msg="${param.msg}">

<div class="wrap">
  <div class="side">
    <a class="side-back" href="Index.jsp">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round">
        <path d="M19 12H5M12 5l-7 7 7 7"/>
      </svg>
      Volver
    </a>
    <div class="side-spheres">
      <div class="sphere s1"></div>
      <div class="sphere s2"></div>
    </div>
    <span class="side-eyebrow">Bienvenido de vuelta</span>
    <h2>CAVA<br>Alta Chocolatería</h2>
    <p>Accede a tu cuenta para revisar tus pedidos y descubrir nuestras nuevas creaciones.</p>
    <div class="side-tag">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round">
        <path d="M12 3l7 3v6c0 4.5-3 8-7 9-4-1-7-4.5-7-9V6l7-3z"/>
      </svg>
      Acceso seguro y cifrado
    </div>
  </div>

  <div class="form-panel">
    <div class="form-head">
      <h3>Iniciar sesión</h3>
      <p>¿No tienes cuenta aún? <a href="RegistrarUsuario.jsp">Regístrate gratis</a></p>
    </div>

    <form action="${pageContext.request.contextPath}/InicioSesion" method="post">
      <div class="field">
        <label class="lbl">Correo electrónico</label>
        <input type="email" name="correo" placeholder="correo@ejemplo.com" required autocomplete="email">
      </div>
      <div class="field">
        <label class="lbl">Contraseña</label>
        <div class="input-wrap">
          <input type="password" name="clave" id="loginPass" placeholder="Tu contraseña" required autocomplete="current-password">
          <button type="button" class="eye-btn" onclick="togglePw('loginPass')" aria-label="Mostrar/ocultar contraseña">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7"><path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7-11-7-11-7z"/><circle cx="12" cy="12" r="3"/></svg>
          </button>
        </div>
      </div>
      <div class="row-util">
        <label class="remember">
          <input type="checkbox" name="recordarme" checked> Recordarme
        </label>
        <a href="#" class="link-forgot">¿Olvidaste tu contraseña?</a>
      </div>
      <button type="submit" class="btn-cta">Entrar a mi cuenta</button>
    </form>
    <!-- ... (resto del código igual) ... -->
  </div>
</div>
<script src="javascript/InicioSesion.js"></script>
</body>
</html>