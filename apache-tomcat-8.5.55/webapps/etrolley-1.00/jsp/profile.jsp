<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html>
	<head>
        <jsp:include page="partials/head.jsp" />
		<link rel="stylesheet" href="<c:url value="/css/profile.css"/>">

        <title>Profile | eTrolley</title>
	</head>
	<body>
        <jsp:include page="partials/header2.jsp" />

        <main class="etrolley-site-main">
                <div class="profile-container">
                    <h1 class=".splash-head">Account Information</h1>
                    <form class="pure-form pure-form-aligned" id="etrolley-update-form">
                        <img class="user-image-info" src="../images/user.png" alt="User" align="left">
                        <fieldset>
                            <div class="pure-control-group">
                                <label for="name">Name</label>
                                <input id="name" type="text" placeholder="Name" disabled>
                            </div>

                            <div class="pure-control-group">
                                <label for="surname">Surname</label>
                                <input id="surname" type="text" placeholder="Surname" disabled>
                            </div>

                            <div class="pure-control-group">
                                <label for="email">Email Address</label>
                                <input id="email" type="email" placeholder="Email Address" disabled>
                            </div>

                            <div class="pure-control-group">
                                <label for="email">Zipcode</label>
                                <input id="zipcode" type="number" placeholder="Zipcode" disabled>
                            </div>
                            <div class="pure-control-group">
                                <label for="city">City</label>
                                <input id="city" type="text" placeholder="City" disabled>
                            </div>
                            <div class="pure-control-group">
                                <label for="address">Address</label>
                                <input id="address" type="text" placeholder= "Address" disabled>
                            </div>

                        </fieldset>
                    </form>
                </div>
        </main>

        <jsp:include page="partials/footer.jsp" />
        <jsp:include page="partials/footer-scripts.jsp" />
        <script src="<c:url value="/js/profile.js"/>"></script>
	</body>
</html>