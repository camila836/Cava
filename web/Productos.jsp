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
  <div class="ticker-wrap">
    <div class="ticker-container">
      <div class="ticker-item">CACAO DE ORIGEN FINO Y ÉTICO — 100% COLOMBIANO</div>
      <div class="ticker-item">✦ CHOCOLATES ARTESANALES CAVA ✦</div>
    </div>
  </div>

  <nav>
    <a class="nav-logo" href="${pageContext.request.contextPath}/inicio">
      <span class="nav-logo-mark">✦</span> CAVA
    </a>
    <ul class="nav-links">
      <li><a href="${pageContext.request.contextPath}/inicio">Inicio</a></li>
      <li><a class="active" href="${pageContext.request.contextPath}/productos">La Tienda</a></li>
    </ul>
    <div class="nav-actions">
      <a class="btn-premium-round" href="${pageContext.request.contextPath}/InicioSesion.jsp">Entrar</a>
      <a class="btn-premium-round btn-filled" href="${pageContext.request.contextPath}/RegistrarUsuario.jsp">Registrarse</a>
    </div>
  </nav>

  <main class="shop-layout">
    <aside class="shop-filters" aria-label="Información del catálogo">
      <h3>Catálogo público</h3>
      <p>Chocolate artesanal elaborado con cacao colombiano de origen fino y ético.</p>
    </aside>

    <section aria-labelledby="products-title">
      <h1 id="products-title">Nuestros chocolates</h1>
      <c:choose>
        <c:when test="${empty productos}">
          <div class="service-card" id="products-empty-state" role="status">
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
                  <h3><c:out value="${producto.descripcionProductos}"/></h3>
                  <p class="product-desc">Chocolate artesanal CAVA.</p>
                  <div class="product-card-footer">
                    <span class="product-price">$ <c:out value="${producto.precioProductos}"/> COP</span>
                  </div>
                </div>
              </article>
            </c:forEach>
          </div>
        </c:otherwise>
      </c:choose>
    </section>
  </main>
</body>
</html>
