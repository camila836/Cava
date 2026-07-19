<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Registro — CAVA Alta Chocolatería</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/RegistrarUsuario.css">
  <link rel="stylesheet" href="css/RegistrarUsuario.css">
</head>
<body data-error="${requestScope.error}" data-msg="${param.msg}">

<div class="wrap">
  <div class="side">
    <a class="side-back" href="Index.jsp">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
      Volver
    </a>
    <span class="side-eyebrow">Bienvenido a</span>
    <h2>CAVA<br>Alta Chocolatería</h2>
    <p>Crea tu cuenta y empieza a disfrutar de nuestras creaciones artesanales con entrega en toda Colombia.</p>
  </div>

  <div class="form-panel">
    <div class="form-head">
      <h3>Crear cuenta</h3>
      <p>¿Ya tienes cuenta? <a href="InicioSesion.jsp">Inicia sesión aquí</a></p>
    </div>

    <div class="aviso" id="mensajeRegistro" aria-live="polite"></div>

    <form id="formularioRegistro" action="${pageContext.request.contextPath}/RegistrarUsuario" method="post">
      <div class="g2">
        <div class="field">
          <label class="lbl" for="nombres">Nombres</label>
          <input type="text" id="nombres" name="nombres" required autocomplete="given-name">
        </div>
        <div class="field">
          <label class="lbl" for="apellidos">Apellidos</label>
          <input type="text" id="apellidos" name="apellidos" required autocomplete="family-name">
        </div>
      </div>

      <div class="g2">
        <div class="field">
          <label class="lbl" for="idTipoDocumento">Tipo de documento</label>
          <select id="idTipoDocumento" name="idTipoDocumento" required>
            <option value="1">Cedula de ciudadania</option>
            <option value="2">Cedula de extranjeria</option>
            <option value="3">Pasaporte</option>
            <option value="4">NIT</option>
          </select>
        </div>
        <div class="field">
          <label class="lbl" for="identificacion">Identificacion</label>
          <input type="text" id="identificacion" name="identificacion" required autocomplete="off">
        </div>
      </div>

      <div class="field">
        <label class="lbl" for="correo">Correo electronico</label>
        <input type="email" id="correo" name="correo" required autocomplete="email" placeholder="correo@ejemplo.com">
      </div>

      <div class="g2">
        <div class="field">
          <label class="lbl" for="telefono">Telefono</label>
          <input type="tel" id="telefono" name="telefono" autocomplete="tel">
        </div>
        <div class="field">
          <label class="lbl" for="fechaNacimiento">Fecha de nacimiento</label>
          <input type="date" id="fechaNacimiento" name="fechaNacimiento">
        </div>
      </div>

      <div class="field">
        <label class="lbl" for="direccion">Direccion</label>
        <input type="text" id="direccion" name="direccion" autocomplete="street-address">
      </div>

      <div class="field">
        <label class="lbl" for="idCiudades">Ciudad</label>
        <select id="idCiudades" name="idCiudades" required>
          <option value="1">Bogota</option>
          <option value="2">Medellin</option>
          <option value="3">Cartagena</option>
          <option value="4">Cali</option>
        </select>
      </div>

      <div class="g2">
        <div class="field">
          <label class="lbl" for="clave">Clave</label>
          <input type="password" id="clave" name="clave" required autocomplete="new-password" minlength="6">
        </div>
        <div class="field">
          <label class="lbl" for="confirmarClave">Confirmar clave</label>
          <input type="password" id="confirmarClave" name="confirmarClave" required autocomplete="new-password" minlength="6">
        </div>
      </div>

      <input type="hidden" name="idRoles" value="2">
      <label class="consent">
        <input type="checkbox" name="autorizacionTratamientoDatos" value="true" required>
        Autorizo el tratamiento de mis datos personales para gestionar mi cuenta, pedidos y comunicaciones de CAVAChocolateria.
      </label>

      <button type="submit" class="btn-cta">Crear cuenta</button>
    </form>
  </div>
</div>

<script src="javascript/RegistrarUsuario.js"></script>
</body>
</html>
