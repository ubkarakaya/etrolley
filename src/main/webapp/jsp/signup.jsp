<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html>
	<head>
        <jsp:include page="partials/head.jsp" />
		<link rel="stylesheet" href="<c:url value="/css/login_signup.css"/>">

        <title>Signup | eTrolley</title>
	</head>

	<body>
        <jsp:include page="partials/header.jsp" />

        <main class="etrolley-site-main">
            <div class="overlay">
                <form class="form-signup">
                    <!--   con = Container  for items in the form-->
                    <div class="con">
                        <!--     Start  header Content  -->
                        <header class="head-form">
                            <h2>Sign Up</h2>
                            <!--     A welcome message or an explanation of the login form -->
                            <p>Welcome to eTrolley, sign up here in less than three minutes and do your online shopping!</p>
                        </header>

                        <!--     End  header Content  -->
                        <div class="field-set">
                            <center><i class="fa fa-pencil-square-o fa-4x" aria-hidden="true"></i></center>

                            <section id="signup-form-errors" class="hidden">
                                Test
                            </section>

                            <input class="form-input" name="name" id="txt-input-1" type="text" placeholder="Name" required>
                            <input class="form-input" name="surname" id="txt-input-1" type="text" placeholder="Surname" required>
                            <input class="form-input" name="email" id="txt-input-1" type="text" placeholder="Email" required>
                            <input class="form-input" name="password" type="password" placeholder="Password" id="pwd" name="password" required>
                            <input class="form-input" name="address" id="txt-input-4" type="text" placeholder="Address" required>
                            <input class="form-input" name="city" id="txt-input-2" type="text" placeholder="City" required>
                            <input class="form-input" name="zipcode" id="txt-input-3" type="text" placeholder="Zip Code" required>

                            <input type="checkbox" id="cb" name="privacy" value="privacy" required>
                            <label for=cb> Accept the Terms and Conditions </label>

                            <br>

                            <div class="content-center">
                                <button class="btn-signup" type="submit"> Sign up </button>
                            </div>
                            <p class="account">If you already have an account, please <a href="login.jsp">Login</a>.</p>

                        </div>

                        <!--   End Conrainer  -->
                    </div>

                    <!-- End Form -->
                </form>
            </div>
        </main>

        <jsp:include page="partials/footer.jsp" />
        <jsp:include page="partials/footer-scripts.jsp" />
        <script src="<c:url value="/js/signup.js"/>"></script>

	</body>
</html>