<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html>
	<head>
        <jsp:include page="partials/head.jsp" />
		<link rel="stylesheet" href="<c:url value="/css/payment.css"/>">
        <title>Select payment method</title>
	</head>
	<body>

          <main class="etrolley-site-main checkout-panel">
            <div class="panel-body">
              <h1 class="title">Checkout</h1>

              <div class="progress-bar">
                <div class="step active"></div>
                <div class="step active"></div>
                <div class="step"></div>
                <div class="step"></div>
              </div>

              <div class="payment-method">
                <label for="card" class="method card">
                  <div class="card-logos">
                    <img src="../img/visa_logo.png"/>
                    <img src="../img/mastercard_logo.png"/>
                  </div>

                  <div class="radio-input">
                    <input id="card" type="radio" name="payment">
                    Pay €<span class="tot-price-count">0</span> with credit card
                  </div>
                </label>

                <label for="paypal" class="method paypal">
                  <img src="../img/paypal_logo.png"/>
                  <div class="radio-input">
                    <input id="paypal" type="radio" name="payment">
                    Pay €<span class="tot-price-count">0</span> with PayPal
                  </div>
                </label>
              </div>

              <div class="input-fields">
                <div class="column-1">
                  <label for="cardholder">Cardholder's Name</label>
                  <input type="text" id="cardholder" />

                  <div class="small-inputs">
                    <div>
                      <label for="date">Valid thru</label>
                      <input type="text" id="date" placeholder="MM / YY" />
                    </div>

                    <div>
                      <label for="verification">CVV / CVC *</label>
                      <input type="password" id="verification"/>
                    </div>
                  </div>

                </div>
                <div class="column-2">
                  <label for="cardnumber">Card Number</label>
                  <input type="password" id="cardnumber"/>

                  <span class="info">* CVV or CVC is the card security code, unique three digits number on the back of your card separate from its number.</span>
                </div>
              </div>
            </div>

            <div class="panel-footer">
              <a href="home.jsp" class="pure-button pure-button-active">Quit</a>
              <a href="finalize.jsp" class="pure-button pure-button-primary start-btn">Pay</a>

            </div>
          </main>
        <jsp:include page="partials/footer.jsp" />
        <jsp:include page="partials/footer-scripts.jsp" />
        <script src="<c:url value="/js/payment.js"/>"></script>
	</body>
</html>