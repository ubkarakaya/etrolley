<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html>
	<head>
        <jsp:include page="partials/head.jsp" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />
		<link rel="stylesheet" href="<c:url value="/css/supermarket-list.css"/>">

        <title>Select products | eTrolley</title>
	</head>

	<body>
        <jsp:include page="partials/header2.jsp" />

		<main class="supermarket-list-container etrolley-site-main">
			<section class="supermarket-list-titles">
				<div class="pure-g">
					<div class="pure-u-18-24">
						<h1>Where do you want to shop?</h1>
						<p class="results-number">
							Found <strong><span id="supermarkets-count"></span> supermarkets</strong> near <span class="costumer-address"></span>
						</p>
					</div>
					<div class="pure-u-6-24">
						<img class="pure-img" src="<c:url value="/images/supermarket.png"/>" alt="">
					</div>
				</div>
			</section>

			<section class="supermarket-list">
				<ul id="supermarket-ul">
				</ul>
			</section>

			<div id="map-modal" class="modal">
				<h2><i class="fa fa-map-pin" aria-hidden="true"></i> Supermarket Position</h2>
				<iframe src="about:blank" width="600" height="450" frameborder="0" style="border:0;" allowfullscreen="" aria-hidden="false" tabindex="0"></iframe>
			</div>
		</main>

		<template id="etrolley-supermarket-entry-tpl">
            <div class="pure-g">
                <div class="pure-u-6-24">
                    <figure class="supermarket-logo">
                        <img src="" class="pure-img" alt="">
                    </figure>
                </div>
                <div class="pure-u-12-24 supermarket-description">
                    <h1 class="supermarket-name"></h1>
                    <address class="supermarket-address">
                        <i class="fa fa-map-marker" aria-hidden="true"></i>
                    </address>
                    <p class="supermarket-distance">
                        <i class="fa fa-arrows-h" aria-hidden="true"></i>
                        <strong class="supermarket-distance-meters"></strong> meters from you
                    </p>
                    <div class="supermarket-rating">
                        <span class="fa fa-star"></span>
                        <span class="fa fa-star"></span>
                        <span class="fa fa-star"></span>
                        <span class="fa fa-star"></span>
                        <span class="fa fa-star"></span>
                    </div>
                </div>
                <div class="pure-u-6-24 supermarket-btn-container">
                    <a class="pure-button supermarket-btn supermarket-view-prd-btn" href="<c:url value="/jsp/product-select.jsp"/>">
                        <i class="fa fa-shopping-cart fa-lg"></i>
                        View Products
                    </a>

                    <a class="pure-button supermarket-btn supermarket-view-map-btn" href="#map-modal" rel="modal:open">
                        <i class="fa fa-map" aria-hidden="true"></i>
                        View on map
                    </a>
                </div>
            </div>
		</template>

        <jsp:include page="partials/footer.jsp" />
        <jsp:include page="partials/footer-scripts.jsp" />
        <!-- jQuery Modal -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
        <script src="<c:url value="/js/supermarket-list.js"/>"></script>
	</body>
</html>