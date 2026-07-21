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

  <!-- MARQUESINA -->
  <div class="ticker-wrap">
    <div class="ticker-container">
      <div class="ticker-item">ENVÍO GRATIS EN COMPRAS MAYORES A $100.000 COP</div>
      <div class="ticker-item">✦ CACAO DE ORIGEN FINO Y ÉTICO — 100% COLOMBIANO ✦</div>
      <div class="ticker-item">✦ EXPERIENCIAS CORPORATIVAS Y REGALOS DE LUJO ✦</div>
    </div>
  </div>

  <!-- NAVEGACIÓN -->
  <nav>
    <a class="nav-logo" href="${pageContext.request.contextPath}/inicio">
      <span class="nav-logo-mark">✦</span> CAVA
    </a>

    <ul class="nav-links">
      <li><a href="${pageContext.request.contextPath}/inicio" class="active">Inicio</a></li>
      <li><a onclick="navigate('quienes')" data-panel="quienes">Quiénes Somos</a></li>
      <li><a href="${pageContext.request.contextPath}/productos">La Tienda</a></li>
      <li><a onclick="navigate('servicios')" data-panel="servicios">Servicios</a></li>
      <li><a onclick="navigate('historial')" data-panel="historial">Mis Pedidos</a></li>
    </ul>

    <div class="nav-actions">
      <button class="nav-cart" onclick="toggleCart()" title="Ver carrito" aria-label="Abrir carrito">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
          <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
        </svg>
        <span class="cart-count" id="cart-count">0</span>
      </button>
      <a class="btn-premium-round" href="${pageContext.request.contextPath}/login">Entrar</a>
      <a class="btn-premium-round btn-filled" href="${pageContext.request.contextPath}/registro">Registrarse</a>
      <button class="btn-premium-round" onclick="navigate('contacto')">Asesoría</button>
    </div>
  </nav>

  <!-- PANEL: INICIO -->
  <section class="panel active" id="panel-inicio">
    <div class="video-bg-container">
      <video class="video-bg" autoplay muted loop playsinline>
        <source src="${pageContext.request.contextPath}/Videos/CAVA.mp4" type="video/mp4">
      </video>
      <div class="video-overlay"></div>
      <div class="hero-content">
        <span class="hero-tag">Hecho a mano con amor — Bogotá, Colombia</span>
        <div class="hero-rule"></div>
        <h1 class="hero-title">
          Comer chocolate<br>no es un capricho,<br>
          <em>es una inversión en</em><br>tu felicidad.
        </h1>
        <div class="hero-actions">
          <a class="btn-primary" href="${pageContext.request.contextPath}/productos">Explorar la tienda</a>
          <button class="btn-outline" onclick="navigate('quienes')">Nuestra historia</button>
        </div>
      </div>
    </div>

    <!-- ... resto del contenido reemplazando las menciones de marca ... -->

    <footer>
      <div class="footer-grid">
        <div class="footer-col">
          <div class="footer-brand">CAVA</div>
          <p class="footer-desc">Chocolate artesanal elaborado con cacao de origen fino y ético, 100% colombiano. El placer del chocolate, hecho con arte.</p>
        </div>
        <!-- ... resto del footer ... -->
        <div class="footer-bottom">
          <span>© <%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) %> CAVA Alta Chocolatería. Todos los derechos reservados.</span>
        </div>
      </div>
    </footer>
  </section>

</body>
</html>
