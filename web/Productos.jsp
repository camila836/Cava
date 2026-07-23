<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Chocolates — CAVA</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
  <div class="ticker-wrap" aria-label="Novedades CAVA">
    <div class="ticker-container">
      <div class="ticker-item">CACAO DE ORIGEN FINO Y ÉTICO — 100% COLOMBIANO</div>
      <div class="ticker-item">✦ CHOCOLATES ARTESANALES CAVA ✦</div>
      <div class="ticker-item">✦ HECHOS A MANO EN COLOMBIA ✦</div>
    </div>
  </div>

  <nav aria-label="Navegación principal">
    <a class="nav-logo" href="${pageContext.request.contextPath}/inicio"><span class="nav-logo-mark" aria-hidden="true">✦</span> CAVA</a>
    <ul class="nav-links">
      <li><a href="${pageContext.request.contextPath}/inicio">Inicio</a></li>
      <li><a class="active" href="${pageContext.request.contextPath}/productos">La tienda</a></li>
    </ul>
    <div class="nav-actions">
      <a class="btn-premium-round" href="${pageContext.request.contextPath}/login">Entrar</a>
      <a class="btn-premium-round btn-filled" href="${pageContext.request.contextPath}/registro">Registrarse</a>
    </div>
  </nav>

  <header class="shop-hero">
    <span class="hero-tag">Selección artesanal</span>
    <h1>Nuestros chocolates</h1>
    <p>Cacao colombiano, ingredientes honestos y el cuidado de un oficio hecho a mano.</p>
  </header>

  <main class="shop-layout">
    <aside class="shop-filters" aria-label="Información del catálogo">
      <span class="hero-tag">CAVA</span>
      <h3>Catálogo público</h3>
      <p>Chocolate artesanal elaborado con cacao colombiano de origen fino y ético.</p>
      <a class="btn-premium-round btn-filled btn-on-light" href="${pageContext.request.contextPath}/inicio">Conocer CAVA</a>
    </aside>

    <section aria-labelledby="products-title">
      <div class="shop-section-head">
        <div><span class="hero-tag">Colección actual</span><h2 id="products-title">Piezas disponibles</h2></div>
        <span class="shop-note">Elaboración artesanal</span>
      </div>
      <c:choose>
        <c:when test="${empty productos}">
          <div class="service-card products-empty" id="products-empty-state" role="status">
            <div class="service-icon" aria-hidden="true">✦</div>
            <h3>Próximamente nuevos chocolates</h3>
            <p>El catálogo está vacío por el momento. Vuelve pronto para descubrir nuestras creaciones artesanales.</p>
          </div>
        </c:when>
        <c:otherwise>
          <div class="products-grid-premium">
            <c:forEach items="${productos}" var="producto">
              <article class="product-card-premium">
                <div class="product-graphic" aria-hidden="true">◆</div>
                <div class="product-body">
                  <div class="product-tag-row"><span class="hero-tag">CAVA artesanal</span></div>
                  <h3><c:out value="${producto.descripcionProductos}"/></h3>
                  <p class="product-desc">Chocolate artesanal elaborado con cacao colombiano.</p>
                  <div class="product-card-footer"><span class="product-price">$ <c:out value="${producto.precioProductos}"/> COP</span><button type="button" class="btn-premium-round" disabled>Próximamente</button></div>
                </div>
              </article>
            </c:forEach>
          </div>
        </c:otherwise>
      </c:choose>
    </section>
  </main>

  <footer>
    <div class="footer-grid">
      <div class="footer-col"><div class="footer-brand">CAVA</div><p class="footer-desc">Alta chocolatería artesanal colombiana.</p></div>
      <div class="footer-col"><h4>Navegación</h4><ul><li><a href="${pageContext.request.contextPath}/inicio">Inicio</a></li><li><a href="${pageContext.request.contextPath}/productos">La tienda</a></li></ul></div>
      <div class="footer-col"><h4>Cuenta</h4><ul><li><a href="${pageContext.request.contextPath}/login">Iniciar sesión</a></li><li><a href="${pageContext.request.contextPath}/registro">Crear cuenta</a></li></ul></div>
      <div class="footer-col"><h4>Origen</h4><ul><li><span>Bogotá, Colombia</span></li><li><span>Cacao 100% colombiano</span></li></ul></div>
    </div>
    <div class="footer-bottom"><span>© <%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) %> CAVA Alta Chocolatería.</span><span>Hecho con cacao colombiano</span></div>
  </footer>
</body>
</html>
