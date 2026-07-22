<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="csrf-token" content="<c:out value='${csrfToken}'/>">
  <title>CAVA · Unidades de medida</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin.css">
  <script defer src="${pageContext.request.contextPath}/js/admin-unidades-medida.js"></script>
</head>
<body>
  <header class="admin-topbar">
    <a class="admin-brand" href="${pageContext.request.contextPath}/inicio"
       aria-label="Ir al inicio de CAVA">
      <span class="admin-brand-name">CAVA</span>
      <span>Chocolate artesanal premium</span>
    </a>
    <div class="admin-session">
      <span>Hola, <c:out value="${sessionScope.nombreUsuario}"/></span>
      <form action="${pageContext.request.contextPath}/logout" method="post">
        <input type="hidden" name="csrfToken" value="<c:out value='${csrfToken}'/>">
        <button class="button button-secondary" type="submit">Cerrar sesión</button>
      </form>
    </div>
  </header>

  <main id="unidades-app" class="admin-shell"
        data-endpoint="${pageContext.request.contextPath}/admin/unidades-medida">
    <section class="admin-heading" aria-labelledby="page-title">
      <p class="eyebrow">Administración · Catálogos</p>
      <h1 id="page-title">Unidades de medida</h1>
      <p>Gestiona las unidades utilizadas para presentar los chocolates y sus formatos.</p>
    </section>

    <div id="global-message" class="notice" role="status" aria-live="polite" hidden></div>

    <div class="crud-layout">
      <section class="panel" aria-labelledby="form-title">
        <div class="panel-heading">
          <div>
            <p class="eyebrow">Edición</p>
            <h2 id="form-title">Nueva unidad</h2>
          </div>
        </div>
        <form id="unidad-form" novalidate>
          <input id="unidad-id" type="hidden">
          <div class="field">
            <label for="unidad-descripcion">Descripción</label>
            <input id="unidad-descripcion" name="descripcion" type="text"
                   maxlength="45" autocomplete="off" required
                   aria-describedby="descripcion-help descripcion-error">
            <small id="descripcion-help">Entre 1 y 45 caracteres.</small>
            <small id="descripcion-error" class="field-error" aria-live="polite"></small>
          </div>
          <div class="form-actions">
            <button id="save-button" class="button button-primary" type="submit">Guardar</button>
            <button id="cancel-edit" class="button button-secondary" type="button" hidden>Cancelar edición</button>
          </div>
        </form>
      </section>

      <section class="panel panel-list" aria-labelledby="list-title">
        <div class="panel-heading">
          <div>
            <p class="eyebrow">Catálogo</p>
            <h2 id="list-title">Registros</h2>
          </div>
          <button id="reload-button" class="button button-secondary" type="button">Actualizar</button>
        </div>

        <p id="loading-state" class="table-state">Cargando unidades de medida…</p>
        <p id="empty-state" class="table-state" hidden>No hay unidades de medida registradas.</p>
        <div id="table-wrap" class="table-wrap" hidden>
          <table>
            <caption class="sr-only">Listado de unidades de medida</caption>
            <thead>
              <tr>
                <th scope="col">Descripción</th>
                <th scope="col" class="actions-column">Acciones</th>
              </tr>
            </thead>
            <tbody id="unidades-body"></tbody>
          </table>
        </div>
      </section>
    </div>
  </main>

  <dialog id="delete-dialog" aria-labelledby="delete-title" aria-describedby="delete-description">
    <form method="dialog" class="dialog-card">
      <p class="eyebrow">Confirmación</p>
      <h2 id="delete-title">Eliminar unidad</h2>
      <p id="delete-description">Esta acción no se puede deshacer.</p>
      <p id="delete-name" class="dialog-record"></p>
      <div class="form-actions">
        <button class="button button-secondary" value="cancel">Cancelar</button>
        <button id="confirm-delete" class="button button-danger" type="button">Eliminar</button>
      </div>
    </form>
  </dialog>
</body>
</html>
