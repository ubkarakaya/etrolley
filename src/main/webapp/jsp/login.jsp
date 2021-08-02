<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html>
	<head>
        <jsp:include page="partials/head.jsp" />
		<link rel="stylesheet" href="<c:url value="/css/login_signup.css"/>">

        <title>Login | eTrolley</title>
	</head>

	<body>
        <jsp:include page="partials/header.jsp" />
        <main class="etrolley-site-main">
            <div class="overlay">
                <form class="login" id="etrolley-login-form">
                    <!--   con = Container  for items in the form-->
                    <div class="con">
                        <!--     Start  header Content  -->
                        <header class="head-form">
                            <h2>Log In</h2>
                            <!--     A welcome message of the login form -->
                            <p>Welcome to eTrolley, login here using your email and password!</p>
                        </header>
                        <!--     End  header Content  -->
                        <div class="field-set">

                            <center> <i class="fa fa-user-circle fa-4x" aria-hidden="true"></i></center>

                            <section id="login-form-errors" class="hidden">
                                Test
                            </section>

                            <input name="email" class="form-input" id="txt-input" type="text" placeholder="Email" required>
                            <input name="password" class="form-input" type="password" placeholder="Password" id="pwd-login" required>
                            <input type="checkbox" id="cb" name="remember" value="1">
                            <label for=cb> Keep me logged in </label>

                            <br>
                            <!--        buttons -->
                            <!--      button LogIn -->
                            <button class="log-in" type="submit"> Login </button>
                        </div>

                        <!--   End Container  -->
                    </div>
                    <!-- End Form -->
                </form>
            </div>
        </main>

        <jsp:include page="partials/footer.jsp" />
        <jsp:include page="partials/footer-scripts.jsp" />
        <script src="<c:url value="/js/login.js"/>"></script>
	</body>
</html>