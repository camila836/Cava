<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CAVA · Panel Administrativo</title>
    
    <%-- Enlaces a recursos externos --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,500;0,600;1,500;1,600&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.5/gsap.min.js"></script>
    <link rel="stylesheet" href="css/Admin.css">
    <%-- Aquí puedes incluir tus estilos CSS usando: <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css"> --%>
</head>
<body>

    <svg width="0" height="0" style="position:absolute">
      <filter id="cavaGrain">
        <feTurbulence type="fractalNoise" baseFrequency="0.85" numOctaves="2" stitchTiles="stitch" result="noise"/>
        <feColorMatrix in="noise" type="matrix" values="0 0 0 0 0  0 0 0 0 0  0 0 0 0 0  0 0 0 0.06 0"/>
      </filter>
    </svg>
    <div class="grain"></div>

    <!-- ============================ NAV ============================ -->
    <header class="top-nav">
      <div class="brand">
        <svg class="brand-mark" viewBox="0 0 42 42" fill="none">
          <circle cx="21" cy="21" r="19.5" stroke="#c8973a" stroke-width="1"/>
          <circle cx="21" cy="21" r="14.5" stroke="#c8973a" stroke-width="1" opacity=".55"/>
          <circle cx="21" cy="21" r="2.4" fill="#e3bb6d"/>
          <path d="M21 6.5v6M21 29.5v6M6.5 21h6M29.5 21h6" stroke="#c8973a" stroke-width="1" opacity=".6"/>
        </svg>
        <div class="brand-word">
          <span class="name">Cava</span>
          <span class="tag">Chocolatería Artesanal</span>
        </div>
      </div>
      <div class="nav-right">
        <button class="nav-reset" id="btnReset" title="Restaurar los datos de ejemplo">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"><path d="M20 12a8 8 0 1 1-2.6-5.9"/><path d="M20 4v5h-5"/></svg>
          Reiniciar datos
        </button>
        <div class="nav-admin">
          <div class="avatar">C</div>
          <div class="who">
            <%-- Ejemplo de cómo inyectar un nombre desde el backend --%>
            <span class="n"><%= request.getAttribute("nombreUsuario") != null ? request.getAttribute("nombreUsuario") : "Camila" %></span>
            <span class="r">Administradora</span>
          </div>
        </div>
      </div>
    </header>

    <div class="ticker">
      <div class="ticker-track" id="tickerTrack"></div>
    </div>

    <!-- ============================ LAYOUT ============================ -->
    <div class="layout">
      <aside class="sidebar">
        <p class="side-eyebrow">Panel administrativo</p>
        <button class="side-btn active" data-group="config" id="navConfig">
          <svg class="ic" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="3.2"/><path d="M19.4 13.5a7.6 7.6 0 0 0 0-3l2-1.6-2-3.4-2.4.8a7.7 7.7 0 0 0-2.6-1.5L14 2h-4l-.4 2.3a7.7 7.7 0 0 0-2.6 1.5l-2.4-.8-2 3.4 2 1.6a7.6 7.6 0 0 0 0 3l-2 1.6 2 3.4 2.4-.8a7.7 7.7 0 0 0 2.6 1.5L10 22h4l.4-2.3a7.7 7.7 0 0 0 2.6-1.5l2.4.8 2-3.4-2-1.6z"/></svg>
          <span class="lbl"><b>Configuración</b><small>Catálogos y parámetros</small></span>
        </button>
        <button class="side-btn" data-group="procesos" id="navProcesos">
          <svg class="ic" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M3 12h4l2.5-7L14 19l2.5-7H21"/></svg>
          <span class="lbl"><b>Estado / Procesos</b><small>Operación en curso</small></span>
        </button>
        <div class="sidebar-foot">
          <b>Cava</b> · panel funcional de demostración.<br>Conectado a: <%= application.getServerInfo() %>
        </div>
      </aside>

      <main class="main">
        <div class="section-head">
          <p class="eyebrow" id="sectionEyebrow">Panel administrativo · Configuración</p>
          <h1 id="sectionTitle">Configuración</h1>
          <p id="sectionSubtitle">Catálogos y parámetros base que sostienen la operación de Cava.</p>
          <div class="section-divider"></div>
        </div>
        <div class="card-grid" id="cardGrid"></div>
      </main>
    </div>

    <div class="toast-wrap" id="toastWrap"></div>

    <%-- Aquí puedes incluir scripts JS: <script src="${pageContext.request.contextPath}/js/app.js"></script> --%>
    <script src="javaScript/Admin.js"></script>
</body>
</html>