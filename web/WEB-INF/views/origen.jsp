<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="Conoce la historia, el propósito y el recorrido del cacao que inspiran a CAVA.">
  <title>Origen — CAVA Alta Chocolatería Colombiana</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body class="cava-home cava-origin">
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
          <li><a href="${pageContext.request.contextPath}/inicio">Inicio</a></li>
          <li><a href="${pageContext.request.contextPath}/origen" class="active" aria-current="page">Origen</a></li>
          <li><a href="${pageContext.request.contextPath}/productos">Tienda</a></li>
          <li><a href="${pageContext.request.contextPath}/inicio#servicios">Servicios</a></li>
        </ul>

        <div class="nav-actions">
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
    <section class="origin-hero" aria-labelledby="origin-title">
      <img class="origin-hero-image" src="${pageContext.request.contextPath}/img/cacao-origen.svg" alt="" width="1536" height="1024">
      <div class="origin-hero-overlay" aria-hidden="true"></div>
      <div class="origin-hero-content">
        <span class="hero-tag">Cacao colombiano · Historia y propósito</span>
        <div class="hero-rule"></div>
        <h1 id="origin-title">Del origen <em>al placer</em></h1>
        <p>Una historia inspirada en el cacao de Landázuri, el cuidado de cada etapa y el deseo de convertir el chocolate en una experiencia cálida y memorable.</p>
        <a class="cava-button cava-button--primary" href="#nuestra-historia">Conocer la historia</a>
      </div>
    </section>

    <section class="origin-section origin-story" id="nuestra-historia" aria-labelledby="history-title">
      <div class="origin-story-image">
        <img src="${pageContext.request.contextPath}/img/cosecha-cacao.svg" alt="Mazorcas de cacao maduras creciendo en el árbol" width="1536" height="1024" loading="lazy" decoding="async">
      </div>
      <div class="origin-story-copy">
        <span class="origin-eyebrow">Nuestra historia</span>
        <h2 id="history-title">Chocolate con <em>raíces</em></h2>
        <p>La historia de CAVA está unida a las montañas de Landázuri, Santander, donde el cacao forma parte de la vida rural y del conocimiento de familias que cuidan la tierra y cada cosecha.</p>
        <p>La idea nació del deseo de reconocer el valor del cacao de nuestra región y transformarlo en una experiencia que conecte origen, calidad y placer.</p>
        <p>CAVA es una propuesta de chocolate artesanal pensada para disfrutar, compartir y regalar. Su nombre está acompañado por una idea sencilla: <strong>Absoluto Placer</strong>, una expresión de la experiencia especial y cercana que queremos crear alrededor del chocolate.</p>
      </div>
    </section>

    <section class="origin-process" aria-labelledby="process-title">
      <div class="origin-section-heading">
        <span class="origin-eyebrow">Del cacao al chocolate</span>
        <h2 id="process-title">Siete etapas, <em>un mismo cuidado</em></h2>
        <p>El recorrido comienza en la tierra y continúa con decisiones cuidadosas que buscan conservar las cualidades del cacao hasta su presentación final.</p>
      </div>

      <div class="origin-process-gallery" aria-label="Cultivo y preparación del cacao">
        <figure>
          <img src="${pageContext.request.contextPath}/img/cultivo-cacao.svg" alt="Recolección manual de una mazorca de cacao" width="1536" height="1024" loading="lazy" decoding="async">
          <figcaption>El cuidado comienza en el cultivo.</figcaption>
        </figure>
        <figure>
          <img src="${pageContext.request.contextPath}/img/secado-cacao.svg" alt="Granos de cacao extendidos durante su secado" width="1536" height="1024" loading="lazy" decoding="async">
          <figcaption>Cada etapa requiere tiempo y atención.</figcaption>
        </figure>
      </div>

      <ol class="origin-process-list">
        <li class="origin-process-card">
          <span class="origin-process-number" aria-hidden="true">01</span>
          <h3>Cultivo</h3>
          <p>La selección de plantas, la preparación del terreno y el cuidado del árbol construyen la base de un cacao con buenas condiciones.</p>
        </li>
        <li class="origin-process-card">
          <span class="origin-process-number" aria-hidden="true">02</span>
          <h3>Cosecha y selección</h3>
          <p>Las mazorcas maduras se recolectan con cuidado y los granos se revisan para continuar el proceso con una materia prima bien elegida.</p>
        </li>
        <li class="origin-process-card">
          <span class="origin-process-number" aria-hidden="true">03</span>
          <h3>Fermentación</h3>
          <p>Los granos permanecen durante un periodo controlado en el que comienzan a desarrollarse aromas y sabores propios del cacao.</p>
        </li>
        <li class="origin-process-card">
          <span class="origin-process-number" aria-hidden="true">04</span>
          <h3>Secado</h3>
          <p>El secado gradual ayuda a conservar la calidad y prepara los granos para su almacenamiento y transformación.</p>
        </li>
        <li class="origin-process-card">
          <span class="origin-process-number" aria-hidden="true">05</span>
          <h3>Tostado</h3>
          <p>En la transformación, el tostado acompaña el desarrollo del perfil aromático que dará carácter al chocolate.</p>
        </li>
        <li class="origin-process-card">
          <span class="origin-process-number" aria-hidden="true">06</span>
          <h3>Molienda y refinado</h3>
          <p>El cacao se trabaja hasta alcanzar una mezcla de textura fina y uniforme, preparada para convertirse en una creación CAVA.</p>
        </li>
        <li class="origin-process-card">
          <span class="origin-process-number" aria-hidden="true">07</span>
          <h3>Moldeado y presentación</h3>
          <p>El chocolate toma forma y recibe una presentación cuidada, pensada para disfrutar, compartir o regalar.</p>
        </li>
      </ol>
    </section>

    <section class="origin-quality" aria-labelledby="ingredients-title">
      <div class="origin-quality-copy">
        <span class="origin-eyebrow">Ingredientes y calidad</span>
        <h2 id="ingredients-title">El cuidado se percibe <em>en cada detalle</em></h2>
        <p>La calidad se construye a lo largo de todo el recorrido: en la selección responsable, la atención al proceso y el respeto por el ingrediente.</p>
      </div>
      <div class="origin-quality-values">
        <article><span aria-hidden="true">✦</span><h3>Selección</h3><p>Elegimos con cuidado los elementos que forman parte de cada creación.</p></article>
        <article><span aria-hidden="true">◇</span><h3>Experiencia sensorial</h3><p>Buscamos equilibrio en textura, aroma y sabor, sin perder la cercanía del oficio artesanal.</p></article>
        <article><span aria-hidden="true">◉</span><h3>Presentación</h3><p>Cada detalle visual acompaña la experiencia de disfrutar y compartir chocolate.</p></article>
      </div>
    </section>

    <section class="origin-purpose" aria-labelledby="purpose-title">
      <div class="origin-purpose-card">
        <span class="origin-eyebrow">Quiénes somos y propósito</span>
        <h2 id="purpose-title">Crecer con el <em>origen</em></h2>
        <p>CAVA busca crear experiencias y detalles alrededor del chocolate: productos para disfrutar, compartir y regalar, presentados con una identidad cálida y cuidadosa.</p>
        <p>Nuestro propósito es crecer junto a quienes cultivan el cacao. A medida que CAVA avance, queremos ampliar la compra a productores locales, construir relaciones responsables y contribuir de forma progresiva a que más familias encuentren en el cacao una oportunidad sostenible.</p>
        <p>Ese compromiso expresa una dirección de futuro; no presupone alianzas o programas que todavía no hayan sido consolidados.</p>
      </div>
    </section>

    <section class="origin-cta" aria-labelledby="origin-cta-title">
      <span class="origin-eyebrow">El placer continúa</span>
      <h2 id="origin-cta-title">Descubre nuestras <em>creaciones</em></h2>
      <p>Encuentra una forma especial de disfrutar, compartir o regalar chocolate.</p>
      <div class="origin-cta-actions">
        <a class="cava-button cava-button--primary" href="${pageContext.request.contextPath}/productos">Explorar la tienda</a>
        <a class="cava-button cava-button--secondary-dark" href="${pageContext.request.contextPath}/inicio">Volver al inicio</a>
      </div>
    </section>
  </main>

  <footer>
    <div class="footer-grid">
      <div class="footer-col"><div class="footer-brand">CAVA</div><p class="footer-desc">Chocolate artesanal inspirado en el cacao colombiano y en el cuidado de cada detalle.</p></div>
      <div class="footer-col"><h4>Navegación</h4><ul><li><a href="${pageContext.request.contextPath}/inicio">Inicio</a></li><li><a href="${pageContext.request.contextPath}/origen">Origen</a></li><li><a href="${pageContext.request.contextPath}/productos">La tienda</a></li></ul></div>
      <div class="footer-col"><h4>Cuenta</h4><ul><li><a href="${pageContext.request.contextPath}/login">Iniciar sesión</a></li><li><a href="${pageContext.request.contextPath}/registro">Crear cuenta</a></li><li><span>Pedidos · Próximamente</span></li></ul></div>
      <div class="footer-col"><h4>Origen</h4><ul><li><span>Landázuri, Santander</span></li><li><span>Cacao colombiano</span></li><li><span>Elaboración artesanal</span></li></ul></div>
    </div>
    <div class="footer-bottom"><span>© <%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) %> CAVA Alta Chocolatería.</span><span>Hecho con cacao colombiano</span></div>
  </footer>
  <script src="${pageContext.request.contextPath}/js/inicio.js" defer></script>
</body>
</html>
