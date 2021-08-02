<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html>
	<head>
        <jsp:include page="partials/head.jsp" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />
		<link rel="stylesheet" href="<c:url value="/css/dhps.css"/>">

        <title>Select products</title>
	</head>

	<body>
        <jsp:include page="partials/header2.jsp" />
                <main class="etrolley-site-main">
                    <div class="pure-g content-center">
                        <div class="pure-u-1-3 leftpane">
                        <p>
                            <img src="../images/trolley.png"  style="vertical-align:middle" width="300" height="300";>
                       <h1> CHIP: <span class="costumer-chip"></span> </h1>

                        </p></div>
                        <div class="pure-u-1-3 middlepane"><p>
                            <img src="../images/card.png"   style="vertical-align:middle" width="300" height="300";>
                            <h1> PAYMENT: <span class="costumer-spending"></span> € </h1>

                            </p></div>
                        <div class="pure-u-1-3 rightpane"><p>
                            <img src="../images/usericon.svg"  style="vertical-align:middle" width="300" height="300";>
                            <h1>Session Count: <span class="costumer-count"></span> </h1>

                            </p></div>

                    </div>
                </main>
        <jsp:include page="partials/footer.jsp" />
        <jsp:include page="partials/footer-scripts.jsp" />
        <script src="<c:url value="/js/dashboard.js"/>"></script>
	</body>
</html>