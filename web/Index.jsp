<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CAVA — Alta Chocolatería Colombiana</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body class="cava-home">
  <a class="skip-link" href="#contenido-principal">Saltar al contenido principal</a>

  <header class="site-header" data-site-header>
    <nav class="cava-nav" aria-label="Navegación principal">
      <a class="nav-logo" href="${pageContext.request.contextPath}/inicio" aria-label="CAVA, ir al inicio">
        <span class="nav-logo-mark" aria-hidden="true">✦</span>
        <span>CAVA</span>
      </a>

      <button class="nav-toggle" type="button" aria-expanded="false" aria-controls="navegacion-principal" data-nav-toggle>
        <span class="sr-only">Abrir menú principal</span>
        <span class="nav-toggle-line" aria-hidden="true"></span>
        <span class="nav-toggle-line" aria-hidden="true"></span>
        <span class="nav-toggle-line" aria-hidden="true"></span>
      </button>

      <div class="nav-menu" id="navegacion-principal" data-nav-menu>
        <ul class="nav-links">
          <li><a href="${pageContext.request.contextPath}/inicio" class="active" aria-current="page">Inicio</a></li>
          <!-- AV-02 conserva Origen y Servicios como destinos reales dentro de Inicio; no crea rutas nuevas. -->
          <li><a href="#quienes">Origen</a></li>
          <li><a href="${pageContext.request.contextPath}/productos">Tienda</a></li>
          <li><a href="#servicios">Servicios</a></li>
        </ul>

        <div class="nav-actions">
          <!-- Destinos funcionales pendientes: regalo, favoritos y bolsa. Se muestran deshabilitados sin rutas ficticias. -->
          <button class="cava-button cava-button--primary cava-button--compact nav-gift" type="button" disabled aria-label="Crea tu regalo, próximamente" aria-describedby="acciones-pendientes">
            Crea tu regalo
          </button>
          <button class="nav-action nav-action--pending" type="button" disabled aria-label="Favoritos, próximamente">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <path d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.6l-1-1a5.5 5.5 0 0 0-7.8 7.8l1 1L12 21l7.8-7.6 1-1a5.5 5.5 0 0 0 0-7.8Z"/>
            </svg>
            <span>Favoritos</span>
          </button>
          <a class="nav-action" href="${pageContext.request.contextPath}/login" aria-label="Cuenta, iniciar sesión">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <circle cx="12" cy="8" r="4"/><path d="M4 21a8 8 0 0 1 16 0"/>
            </svg>
            <span>Cuenta</span>
          </a>
          <button class="nav-action nav-action--pending" type="button" disabled aria-label="Bolsa, próximamente">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <path d="M6 8h12l1 13H5L6 8Z"/><path d="M9 9V6a3 3 0 0 1 6 0v3"/>
            </svg>
            <span>Bolsa</span>
          </button>
          <span class="sr-only" id="acciones-pendientes">Disponible en una subfase futura.</span>
        </div>
      </div>
    </nav>
  </header>

  <main id="contenido-principal">
    <section class="panel active" id="panel-inicio" aria-labelledby="hero-title">
      <div class="video-bg-container">
        <video class="video-bg" autoplay muted loop playsinline preload="metadata" aria-hidden="true">
          <source src="${pageContext.request.contextPath}/Videos/CAVA.mp4" type="video/mp4">
        </video>
        <div class="video-overlay" aria-hidden="true"></div>
        <div class="hero-content">
          <span class="hero-tag">Hecho a mano con amor — Bogotá, Colombia</span>
          <div class="hero-rule"></div>
          <h1 class="hero-title" id="hero-title">
            Comer chocolate<br>no es un capricho,<br>
            <em>es una inversión en</em><br>tu felicidad.
          </h1>
          <div class="hero-actions">
            <a class="cava-button cava-button--primary" href="${pageContext.request.contextPath}/productos">Explorar la tienda</a>
            <a class="cava-button cava-button--secondary-dark" href="#quienes">Conocer nuestro origen</a>
          </div>
        </div>
        <a class="scroll-hint" href="#calidad" aria-label="Descubrir más">
          <span>Descubrir</span>
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true"><path d="M12 5v14M6 13l6 6 6-6"/></svg>
        </a>
      </div>

      <section class="quality-section" id="calidad" aria-labelledby="quality-title">
        <div class="quality-grid">
          <div class="quality-text">
            <span class="hero-tag">Nuestro compromiso</span>
            <h2 id="quality-title">Calidad que se siente</h2>
            <p>Seleccionamos cada ingrediente para ofrecer un chocolate auténtico y artesanal, del grano a la barra.</p>
            <a class="cava-button cava-button--secondary-dark" href="#quienes">Conócenos</a>
          </div>
          <div class="quality-icons" aria-label="Valores de CAVA">
            <div class="quality-icon-card"><div class="icon-glyph" aria-hidden="true">✦</div><span>Ingredientes naturales</span></div>
            <div class="quality-icon-card"><div class="icon-glyph" aria-hidden="true">◇</div><span>Hecho a mano</span></div>
            <div class="quality-icon-card"><div class="icon-glyph" aria-hidden="true">◉</div><span>Comercio justo</span></div>
          </div>
        </div>
      </section>

      <section class="featured-strip" aria-labelledby="collections-title">
        <div class="section-header">
          <div>
            <span class="hero-tag">Selección artesanal</span>
            <h2 class="section-title" id="collections-title">Descubre nuestras <em>colecciones</em></h2>
          </div>
          <a class="section-link" href="${pageContext.request.contextPath}/productos">Ver catálogo</a>
        </div>
        <div class="cat-grid">
          <a class="cat-card" href="${pageContext.request.contextPath}/productos">
            <div class="cat-card-bg"></div><div class="cat-card-overlay"></div>
            <div class="cat-card-body"><h3 class="cat-card-title">Chocolate <em>con leche</em></h3><span class="cat-card-cta">Explorar</span></div>
          </a>
          <a class="cat-card" href="${pageContext.request.contextPath}/productos">
            <div class="cat-card-bg"></div><div class="cat-card-overlay"></div>
            <div class="cat-card-body"><h3 class="cat-card-title">Cacao de <em>origen</em></h3><span class="cat-card-cta">Explorar</span></div>
          </a>
          <a class="cat-card" href="${pageContext.request.contextPath}/productos">
            <div class="cat-card-bg"></div><div class="cat-card-overlay"></div>
            <div class="cat-card-body"><h3 class="cat-card-title">Regalos <em>premium</em></h3><span class="cat-card-cta">Explorar</span></div>
          </a>
        </div>
      </section>

      <section class="about-luxury-grid" id="quienes" aria-labelledby="about-title">
        <h2 class="about-manifesto" id="about-title">Creamos experiencias que conectan el origen ancestral del cacao con el diseño contemporáneo.</h2>
        <div class="about-body">
          <p>En CAVA seleccionamos cacao colombiano de origen fino y ético para convertir cada creación en una experiencia artesanal.</p>
          <p>Trabajamos con respeto por el ingrediente, el oficio y las personas que hacen posible cada pieza.</p>
          <a class="cava-button cava-button--secondary-light" href="${pageContext.request.contextPath}/productos">Conocer la colección</a>
        </div>
      </section>

      <section class="services-layout" id="servicios" aria-labelledby="services-title">
        <div class="section-header">
          <div><span class="hero-tag">Diseño a medida</span><h2 class="section-title" id="services-title">Experiencias y regalos <em>de lujo</em></h2></div>
        </div>
        <div class="services-cards">
          <article class="service-card"><div class="service-icon" aria-hidden="true">✦</div><h3>Regalos corporativos</h3><p>Presentaciones premium para marcas y celebraciones.</p><button type="button" class="cava-button cava-button--secondary-light" disabled>Próximamente</button></article>
          <article class="service-card"><div class="service-icon" aria-hidden="true">◇</div><h3>Catas privadas</h3><p>Recorridos sensoriales por el cacao colombiano.</p><button type="button" class="cava-button cava-button--secondary-light" disabled>Próximamente</button></article>
          <article class="service-card"><div class="service-icon" aria-hidden="true">◉</div><h3>Ediciones especiales</h3><p>Creaciones artesanales para momentos memorables.</p><button type="button" class="cava-button cava-button--secondary-light" disabled>Próximamente</button></article>
        </div>
      </section>
    </section>
  </main>

  <footer>
    <div class="footer-grid">
      <div class="footer-col"><div class="footer-brand">CAVA</div><p class="footer-desc">Chocolate artesanal elaborado con cacao colombiano de origen fino y ético.</p></div>
      <div class="footer-col"><h4>Navegación</h4><ul><li><a href="${pageContext.request.contextPath}/inicio">Inicio</a></li><li><a href="#quienes">Quiénes somos</a></li><li><a href="${pageContext.request.contextPath}/productos">La tienda</a></li></ul></div>
      <div class="footer-col"><h4>Cuenta</h4><ul><li><a href="${pageContext.request.contextPath}/login">Iniciar sesión</a></li><li><a href="${pageContext.request.contextPath}/registro">Crear cuenta</a></li><li><span>Pedidos · Próximamente</span></li></ul></div>
      <div class="footer-col"><h4>Origen</h4><ul><li><span>Bogotá, Colombia</span></li><li><span>Cacao 100% colombiano</span></li><li><span>Elaboración artesanal</span></li></ul></div>
    </div>
    <div class="footer-bottom"><span>© <%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) %> CAVA Alta Chocolatería.</span><span>Hecho con cacao colombiano</span></div>
  </footer>
  <script src="${pageContext.request.contextPath}/js/inicio.js" defer></script>
</body>
</html>
