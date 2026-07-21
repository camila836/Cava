<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CAVA · Acceso administrativo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin.css">
</head>
<body>
  <main class="main">
    <p class="eyebrow">CAVA · Administración</p>
    <h1>Bienvenida, <c:out value="${sessionScope.nombreUsuario}"/></h1>
    <p>El acceso administrativo está protegido. Los módulos de gestión pertenecen a fases posteriores.</p>
    <form action="${pageContext.request.contextPath}/logout" method="post">
      <input type="hidden" name="csrfToken" value="<c:out value='${csrfToken}'/>">
      <button type="submit">Cerrar sesión</button>
    </form>
  </main>
</body>
</html>
