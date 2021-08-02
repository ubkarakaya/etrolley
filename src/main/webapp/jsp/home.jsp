<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html>
	<head>
        <jsp:include page="partials/head.jsp" />

        <title>eTrolley</title>
	</head>

	<body>
        <jsp:include page="partials/header2.jsp" />

        <div class="splash-container">
            <div class="splash">
                <h1 class="splash-head">Grocery Home delivery</h1>
                <p class="splash-subhead">
                    You can buy all needs just with a simple click
                </p>
                <p>
                    <a href="supermarket-list.jsp" class="pure-button pure-button-primary start-btn">Start Shopping</a>
                </p>
            </div>
        </div>

        <div class="content-wrapper">
            <div class="content">
                <h2 class="content-head is-center">Simple and efficient</h2>

                <div class="pure-g">
                    <div class="l-box pure-u-1 pure-u-md-1-2 pure-u-lg-1-4">

                        <h3 class="content-subhead">
                            <i class="fa fa-rocket"></i>
                            Find the Nearest Shops
                        </h3>
                        <p>
                            You can find all supermarkets which are located near to your address
                        </p>
                    </div>
                    <div class="l-box pure-u-1 pure-u-md-1-2 pure-u-lg-1-4">
                        <h3 class="content-subhead">
                            <i class="fa fa-mobile"></i>
                            Select Products
                        </h3>
                        <p>
                            Create your own shopping list and fill your trolley
                        </p>
                    </div>
                    <div class="l-box pure-u-1 pure-u-md-1-2 pure-u-lg-1-4">
                        <h3 class="content-subhead">
                            <i class="fa fa-th-large"></i>
                            Order
                        </h3>
                        <p>
                            After your shopping go to trolley and make the payment and confirm it.
                        </p>
                    </div>
                    <div class="l-box pure-u-1 pure-u-md-1-2 pure-u-lg-1-4">
                        <h3 class="content-subhead">
                            <i class="fa fa-check-square-o"></i>
                            Confirm Your Order
                        </h3>
                        <p>
                            After your confirmation, we shall approve your delivery
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="partials/footer.jsp" />
        <jsp:include page="partials/footer-scripts.jsp" />
	</body>
</html>