<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html>
	<head>
        <jsp:include page="partials/head.jsp" />
		<link rel="stylesheet" href="<c:url value="/css/order-confirm.css"/>">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />

        <title>Thank you for the order</title>
	</head>
	<body>
        <jsp:include page="partials/header2.jsp" />

		<main class="etrolley-site-main order-confirm-container">
            <section class="order-list-titles">
                <div class="pure-g">
                    <div class="pure-u-18-24">
                        <h1>Your Final Trolley </h1>
                        <p class="order-thanks">
                            Thank you for shopping with us
                        </p>
                        <a href="home.jsp">
                            <i class="fa fa-arrow-circle-left" aria-hidden="true"></i>
                            Return to homepage
                        </a>
                    </div>
                    <div class="pure-u-6-24">
                        <img class="pure-img" src="<c:url value="/images/order-confirm.jpg"/>" alt="">
                    </div>
                </div>
            </section>

            <div id="order-table">
            </div>
                        <div class="shopping-list-container">

            					<h2> Your List</h2>
                               <div id="shop-list"></div>

                                   </div>
                                 <a class="pure-button pure-button-primary" id="order-continue-btn" href="payment.jsp">
                                    <i class="fa fa-arrow-right fa-fw" aria-hidden="true"></i>
                                    Confirm and pay your order
                                  </a>
        </main>

        <jsp:include page="partials/footer.jsp" />
        <jsp:include page="partials/footer-scripts.jsp" />
        <script src="<c:url value="/js/order-confirm.js"/>"></script>
	</body>
</html>