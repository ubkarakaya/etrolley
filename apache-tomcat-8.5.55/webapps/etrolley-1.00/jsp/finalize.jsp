<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html>
	<head>
        <jsp:include page="partials/head.jsp" />
		<link rel="stylesheet" href="<c:url value="/css/payment.css"/>">
        <title>Order successful</title>
	</head>
	<body>
          <main class="etrolley-site-main checkout-panel">
            <div class="panel-body">
              <h1 class="title">Checkout</h1>

              <div class="progress-bar">
                <div class="step active"></div>
                <div class="step active"></div>
                <div class="step active"></div>
                <div class="step active"></div>
              </div>
                <h1> THANKS FOR CHOOSE US</h1>
          </div>
          </main>
        <jsp:include page="partials/footer.jsp" />
        <jsp:include page="partials/footer-scripts.jsp" />
	</body>
</html>