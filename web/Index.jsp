<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CAVA — Alta Chocolatería Colombiana</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
  <div class="ticker-wrap" aria-label="Novedades CAVA">
    <div class="ticker-container">
      <div class="ticker-item">ENVÍO GRATIS EN COMPRAS MAYORES A $100.000 COP</div>
      <div class="ticker-item">✦ CACAO DE ORIGEN FINO Y ÉTICO — 100% COLOMBIANO ✦</div>
      <div class="ticker-item">✦ CHOCOLATE ARTESANAL HECHO EN COLOMBIA ✦</div>
    </div>
  </div>

  <nav aria-label="Navegación principal">
    <a class="nav-logo" href="${pageContext.request.contextPath}/inicio">
      <span class="nav-logo-mark" aria-hidden="true">✦</span> CAVA
    </a>
    <ul class="nav-links">
      <li><a href="${pageContext.request.contextPath}/inicio" class="active">Inicio</a></li>
      <li><a href="#quienes">Quiénes somos</a></li>
      <li><a href="${pageContext.request.contextPath}/productos">La tienda</a></li>
      <li><a href="#servicios">Servicios</a></li>
    </ul>
    <div class="nav-actions">
      <button class="nav-cart is-disabled" type="button" disabled title="Carrito próximamente" aria-label="Carrito próximamente">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
          <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
        </svg>
        <span class="cart-count" id="cart-count" aria-hidden="true">0</span>
      </button>
      <a class="btn-premium-round" href="${pageContext.request.contextPath}/login">Entrar</a>
      <a class="btn-premium-round btn-filled" href="${pageContext.request.contextPath}/registro">Registrarse</a>
    </div>
  </nav>

  <main>
    <section class="panel active" id="panel-inicio" aria-labelledby="hero-title">
      <div class="video-bg-container">
        <video class="video-bg" autoplay muted loop playsinline preload="metadata">
          <source src="${pageContext.request.contextPath}/Videos/CAVA.mp4" type="video/mp4">
        </video>
        <div class="video-overlay"></div>
        <div class="hero-content">
          <span class="hero-tag">Hecho a mano con amor — Bogotá, Colombia</span>
          <div class="hero-rule"></div>
          <h1 class="hero-title" id="hero-title">
            Comer chocolate<br>no es un capricho,<br>
            <em>es una inversión en</em><br>tu felicidad.
          </h1>
          <div class="hero-actions">
            <a class="btn-primary" href="${pageContext.request.contextPath}/productos">Explorar la tienda</a>
            <a class="btn-outline" href="#quienes">Nuestra historia</a>
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
            <a class="btn-premium-round btn-filled" href="#quienes">Conócenos</a>
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
          <a class="btn-premium-round btn-filled btn-on-light" href="${pageContext.request.contextPath}/productos">Conocer la colección</a>
        </div>
      </section>

      <section class="services-layout" id="servicios" aria-labelledby="services-title">
        <div class="section-header">
          <div><span class="hero-tag">Diseño a medida</span><h2 class="section-title" id="services-title">Experiencias y regalos <em>de lujo</em></h2></div>
        </div>
        <div class="services-cards">
          <article class="service-card"><div class="service-icon" aria-hidden="true">✦</div><h3>Regalos corporativos</h3><p>Presentaciones premium para marcas y celebraciones.</p><button type="button" class="btn-premium-round" disabled>Próximamente</button></article>
          <article class="service-card"><div class="service-icon" aria-hidden="true">◇</div><h3>Catas privadas</h3><p>Recorridos sensoriales por el cacao colombiano.</p><button type="button" class="btn-premium-round" disabled>Próximamente</button></article>
          <article class="service-card"><div class="service-icon" aria-hidden="true">◉</div><h3>Ediciones especiales</h3><p>Creaciones artesanales para momentos memorables.</p><button type="button" class="btn-premium-round" disabled>Próximamente</button></article>
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
</body>
</html>
