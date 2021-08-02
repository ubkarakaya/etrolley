<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html>
	<head>
        <jsp:include page="partials/head.jsp" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />
		<link rel="stylesheet" href="<c:url value="/css/product-select.css"/>">

        <title>Select products</title>
	</head>

	<body>
        <jsp:include page="partials/header2.jsp" />

		<main class="etrolley-site-main product-select-container">
			<section id="product-page-headers">
					<div class="pure-g">
						<div class="pure-u-4-24 supermarket-logo-container">

						</div>
						<div class="pure-u-16-24 supermarket-info-container">
							<h1 id="supermarket-name"></h1>
							<address class="supermarket-address">
								<i class="fa fa-map-marker" aria-hidden="true"></i>
							</address>
							<p class="results-number">
								<span id="total-products-count">0</span> products available
							</p>
						</div>
						<div class="pure-u-4-24 cart-container">
							<div class="cart-box">
								<h3>My cart</h3>
								<p>Edit your products or end shopping</p>
								<a class="pure-button cart-manage-button" href="#trolley-modal" rel="modal:open">
									<i class="fa fa-shopping-cart fa-fw" aria-hidden="true"></i>
									<span id="trolley-products-count">0</span> items
								</a>
							</div>
						</div>
					</div>
			</section>
			<div class="pure-g">
				<aside id="product-category-selector" class="pure-u-6-24">
					<div class="pure-menu custom-restricted-width">
						<span class="pure-menu-heading">Select product category</span>

						<ul class="pure-menu-list" id="product-category-list">
						</ul>
					</div>
				</aside>

				<section id="product-list" class="pure-u-18-24">
					<div id="product-filters">
						<p id="product-found-count">
							Found <strong><span id="product-count-nr">0</span> Products</strong> for the selected category
						</p>
						<form action="" class="pure-form pure-form-aligned" id="product-filter-form" method="get">
						    <input name="category" type="hidden" id="etrolley-category-hidden">
							<div class="pure-control-group">
								<input type="text" id="etrolley-product-search" placeholder="Search product" name="search">
								<label for="order-by">Order by</label>
								<select id="etrolley-order-by" name="orderby">
									<option value="price-desc">Price descending &darr;</option>
									<option value="price-asc">Price ascending &uarr;</option>
								</select>
							</div>
						</form>
					</div>

					<div id="product-items">
						<ul class="product-list-ul">
						</ul>
					</div>
				</section>


			</div>

			<div id="trolley-modal" class="modal">
				<h2><i class="fa fa-shopping-cart" aria-hidden="true"></i> Your trolley</h2>

                <div class="etrolley-trolley-container"></div>

				<div class="cart-actions-container">
					<a class="pure-button" id="empty-trolley-btn" href="#">
						<i class="fa fa-trash-o fa-fw" aria-hidden="true"></i>
						Empty trolley
					</a>

					<a class="pure-button" id="order-continue-btn" href="#">
						<i class="fa fa-arrow-right fa-fw" aria-hidden="true"></i>
						Continue with the order
					</a>
				</div>
			</div>
		</main>

        <template id="etrolley-product-item-tpl">
            <div class="product-entry" data-id="0">
                <h2 class="product-title"></h2>
                <figure class="product-img">
                    <img class="pure-img" src="" alt="">
                </figure>
                <p class="product-description">
                </p>

                <div class="price-cart-container">
                    <div class="price-value">
                        <p></p>
                    </div>

                    <div class="add-to-cart-wrap">
                        <button class="pure-button add-to-cart-btn" title="Add to cart" data-id="0"><i class="fa fa-cart-plus" aria-hidden="true"></i></button>
                    </div>
                </div>
            </div>
        </template>

        <jsp:include page="partials/footer.jsp" />
        <jsp:include page="partials/footer-scripts.jsp" />
        <!-- jQuery Modal -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
        <script src="<c:url value="/js/product-select.js"/>"></script>
	</body>
</html>