<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="csrf-token" content="<c:out value='${csrfToken}'/>">
  <title>CAVA · Unidades de medida</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,500;0,600;1,500;1,600&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin.css">
  <script defer src="${pageContext.request.contextPath}/js/admin-unidades-medida.js"></script>
</head>
<body>
  <svg width="0" height="0" class="grain-filter" aria-hidden="true">
    <filter id="cavaGrain"><feTurbulence type="fractalNoise" baseFrequency="0.85" numOctaves="2" stitchTiles="stitch" result="noise"/><feColorMatrix in="noise" type="matrix" values="0 0 0 0 0  0 0 0 0 0  0 0 0 0 0  0 0 0 0.06 0"/></filter>
  </svg>
  <div class="grain" aria-hidden="true"></div>

  <header class="top-nav">
    <a class="brand" href="${pageContext.request.contextPath}/inicio" aria-label="Ir al inicio de CAVA">
      <svg class="brand-mark" viewBox="0 0 42 42" fill="none" aria-hidden="true">
        <circle cx="21" cy="21" r="19.5" stroke="#c8973a"/><circle cx="21" cy="21" r="14.5" stroke="#c8973a" opacity=".55"/><circle cx="21" cy="21" r="2.4" fill="#e3bb6d"/><path d="M21 6.5v6M21 29.5v6M6.5 21h6M29.5 21h6" stroke="#c8973a" opacity=".6"/>
      </svg>
      <span class="brand-word"><span class="name">CAVA</span><span class="tag">Chocolatería artesanal</span></span>
    </a>
    <div class="nav-right">
      <div class="nav-admin">
        <span class="avatar" aria-hidden="true">C</span>
        <span class="who"><span class="n"><c:out value="${sessionScope.nombreUsuario}"/></span><span class="r">Administración</span></span>
      </div>
      <form class="logout-form" action="${pageContext.request.contextPath}/logout" method="post">
        <input type="hidden" name="csrfToken" value="<c:out value='${csrfToken}'/>">
        <button class="nav-reset" type="submit">Cerrar sesión</button>
      </form>
    </div>
  </header>

  <div class="ticker" aria-hidden="true">
    <div class="ticker-track"><span>CAVA · PANEL ADMINISTRATIVO</span><span class="dot">✦</span><span>CATÁLOGOS Y PARÁMETROS</span><span class="dot">✦</span><span>CAVA · PANEL ADMINISTRATIVO</span><span class="dot">✦</span><span>CATÁLOGOS Y PARÁMETROS</span><span class="dot">✦</span></div>
  </div>

  <div class="layout">
    <aside class="sidebar" aria-label="Módulos administrativos">
      <p class="side-eyebrow">Panel administrativo</p>
      <a class="side-btn active" href="${pageContext.request.contextPath}/admin/unidades-medida" aria-current="page">
        <svg class="ic" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="3.2"/><path d="M19.4 13.5a7.6 7.6 0 0 0 0-3l2-1.6-2-3.4-2.4.8a7.7 7.7 0 0 0-2.6-1.5L14 2h-4l-.4 2.3a7.7 7.7 0 0 0-2.6 1.5l-2.4-.8-2 3.4 2 1.6a7.6 7.6 0 0 0 0 3l-2 1.6 2 3.4 2.4-.8a7.7 7.7 0 0 0 2.6 1.5L10 22h4l.4-2.3a7.7 7.7 0 0 0 2.6-1.5l2.4.8 2-3.4-2-1.6z"/></svg>
        <span class="lbl"><b>Unidades de medida</b><small>Catálogo activo</small></span>
      </a>
      <button class="side-btn" type="button" disabled>
        <svg class="ic" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" aria-hidden="true"><path d="M3 12h4l2.5-7L14 19l2.5-7H21"/></svg>
        <span class="lbl"><b>Otros módulos</b><small>Próximamente</small></span>
      </button>
      <div class="sidebar-foot"><b>CAVA</b> · administración protegida.<br>Catálogos del sistema.</div>
    </aside>

    <main id="unidades-app" class="main admin-workspace" data-endpoint="${pageContext.request.contextPath}/admin/unidades-medida">
      <section class="section-head admin-heading" aria-labelledby="page-title">
        <p class="eyebrow">Administración · Catálogos</p>
        <h1 id="page-title">Unidades de medida</h1>
        <p>Gestiona las unidades utilizadas para presentar los chocolates y sus formatos.</p>
        <div class="section-divider"></div>
      </section>

      <div id="global-message" class="notice" role="status" aria-live="polite" hidden></div>

      <div class="crud-layout">
        <section class="panel" aria-labelledby="form-title">
          <div class="panel-heading"><div><p class="eyebrow">Edición</p><h2 id="form-title">Nueva unidad</h2></div></div>
          <form id="unidad-form" novalidate>
            <input id="unidad-id" type="hidden">
            <div class="field">
              <label for="unidad-descripcion">Descripción</label>
              <input id="unidad-descripcion" name="descripcion" type="text" maxlength="45" autocomplete="off" required aria-describedby="descripcion-help descripcion-error">
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
            <div><p class="eyebrow">Catálogo</p><h2 id="list-title">Registros</h2></div>
            <button id="reload-button" class="button button-secondary" type="button">Actualizar</button>
          </div>
          <p id="loading-state" class="table-state">Cargando unidades de medida…</p>
          <p id="empty-state" class="table-state" hidden>No hay unidades de medida registradas.</p>
          <div id="table-wrap" class="table-wrap" hidden>
            <table><caption class="sr-only">Listado de unidades de medida</caption><thead><tr><th scope="col">Descripción</th><th scope="col" class="actions-column">Acciones</th></tr></thead><tbody id="unidades-body"></tbody></table>
          </div>
        </section>
      </div>
    </main>
  </div>

  <dialog id="delete-dialog" aria-labelledby="delete-title" aria-describedby="delete-description">
    <form method="dialog" class="dialog-card">
      <p class="eyebrow">Confirmación</p><h2 id="delete-title">Eliminar unidad</h2>
      <p id="delete-description">Esta acción no se puede deshacer.</p><p id="delete-name" class="dialog-record"></p>
      <div class="form-actions"><button class="button button-secondary" value="cancel">Cancelar</button><button id="confirm-delete" class="button button-danger" type="button">Eliminar</button></div>
    </form>
  </dialog>
</body>
</html>
