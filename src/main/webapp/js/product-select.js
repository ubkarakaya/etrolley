$(function() {
    const supermarketId = sessionStorage.getItem('supermarketId');

    if(null == supermarketId){
        window.location.replace(baseUrl + '/jsp/supermarket-list.jsp');
        return;
    }

    // Init trolley object
    trolley = new eTrolleyTrolley($('.etrolley-trolley-container'), supermarketId);

    // Perform AJAX request to init the page
    eTrolleyAJAX(baseUrl + "/rest/supermarket/" + supermarketId, {}, true)
        .done(eTrolleyProductSelectInit);
    //eTrolleyAJAX(baseUrl + "/json-example/product-select.json").done(eTrolleyProductSelectInit);
});

var trolley;

/**
* Initializes the page with supermarket data and category list
**/
function eTrolleyProductSelectInit(responseData){
    let superMrkData = responseData.supermarket;

    $('<img alt="" id="supermarket-logo">')
        .attr('src', baseUrl + '/images/logo/' + superMrkData.logo)
        .appendTo('.supermarket-logo-container');

    $('#supermarket-name').text(superMrkData.name);
    $('.supermarket-address').append(document.createTextNode(superMrkData.address));
    $('#total-products-count').text(superMrkData.product_number);

    // Call to init the categories
    eTrolleyAJAX(baseUrl + "/rest/category", {}, true)
        .done(eTrolleyCategoryListInit);
}

function eTrolleyCategoryListInit(responseData){
    const $categoryList = $('#product-category-list');
    const categoryTpl = '<li class="pure-menu-item"><a href="#" class="pure-menu-link etrolley-category"></a></li>';

    // Generate "All categories" item
    const $allCategoriesItem = $(categoryTpl).addClass('pure-menu-selected');
    $allCategoriesItem.find('.etrolley-category').text('All').data('id', '0');
    $categoryList.append($allCategoriesItem);

    // Generate category list from the response data
    for(const catObj of responseData['resource-list']){
        let cat = catObj.category;
        let $catItem = $(categoryTpl);
        $catItem.find('.etrolley-category').text(cat.name).data('id', cat.id);
        $categoryList.append($catItem);
    }

    // Add category switch on click event
    $(document).on('click', '.etrolley-category', eTrolleyCategorySwitch);

    // Add product filter events
    $('#product-filter-form').on('submit', eTrolleyProductFilter);
    $('#etrolley-order-by').on('change', eTrolleyProductFilter);

    // Init product list
    eTrolleyProductFilterCall(0);
}

/**
* Filters the products by selected category, free search, and order by value
*
**/
function eTrolleyProductFilterCall(category, search = '', orderBy = ''){
    let params = {
        category: category
    };

    if(search){
        params.search = search;
    }

    if(orderBy){
        params.orderBy = orderBy;
    }
    const supermarketVat = sessionStorage.getItem('supermarketId').trim();
    //eTrolleyAJAX(baseUrl + "/json-example/product-list.json", params).done(eTrolleyProductsRender);
    eTrolleyAJAX(baseUrl + "/rest/product/supermarket/" + supermarketVat, params, true)
        .done(eTrolleyProductsRender);

}

/**
* Renders the product list from the response
*
**/
function eTrolleyProductsRender(responseData){
    $('#product-count-nr').text(responseData.length);

    const productTpl = document.getElementById('etrolley-product-item-tpl');
    const $productContainer = $('.product-list-ul');
    $('#product-count-nr').text(responseData['resource-list'].length);

    // Clean old products
    $productContainer.empty();

    for(const productObj of responseData['resource-list']){
        let product = productObj.product;
        let $productEntry = $(productTpl.content.cloneNode(true));

        $productEntry.find('.product-entry').data('id', product.id);
        $productEntry.find('.product-title').text(product.name);
        $productEntry.find('.product-img img').attr('src', baseUrl + product.photo);
        $productEntry.find('.product-description').text(product.description);
        $productEntry.find('.price-value p').text('€ ' + parseFloat(product.unit_price).toFixed(2));
        $productEntry.find('.add-to-cart-btn').data('id', product.id);

        $('<li></li>')
            .append($productEntry)
            .hide()
            .appendTo($productContainer)
            .show(800);
    }
}

/**
* Category switch function
*
**/
function eTrolleyCategorySwitch(e){
    e.preventDefault();

    const $selectedCat = $(this);
    const categoryId = $selectedCat.data('id');

    $('#product-category-list > .pure-menu-selected').removeClass('pure-menu-selected');
    $selectedCat.parent().addClass('pure-menu-selected');

    // Reset filter form
    $('#product-filter-form').trigger('reset');

    // Add hidden category id
    $('#etrolley-category-hidden').val(categoryId);

    // Load products of new category
    eTrolleyProductFilterCall(categoryId);
}

/**
* Filters the products using input values
**/
function eTrolleyProductFilter(e){
    e.preventDefault();
    const categoryId = $('#etrolley-category-hidden').val();
    const productName = $('#etrolley-product-search').val();
    const orderBy = $('#etrolley-order-by').val();

    eTrolleyProductFilterCall(categoryId, productName, orderBy);
}

/**
* Class for trolley management (add product etc)
**/
class eTrolleyTrolley{
    constructor($container, supermarketId) {
        // The document selector
        this.$document = $(document);
        // The container where the cart table is rendered
        this.$container = $container;
        // The selected supermarket id
        this.supermarketId = supermarketId;
        // The selected products array
        this.cart = [];
        // Element where to print the number of products inside the trolley
        this.$productCountEl = $('#trolley-products-count');

        // Some event handlers
        this.$document.on('click', '.add-to-cart-btn', {_self: this}, this.addToCartBtnPress);
        this.$document.on('click', '.cart-remove', {_self: this}, this.removeFromCartBtnPress);
        this.$document.on('click', '#empty-trolley-btn', {_self: this}, this.emptyCartBtnPress);
        this.$document.on('click', '#order-continue-btn', {_self: this}, this.orderContinueBtnPress);
        this.$document.on('change', '.product-qt-select', {_self: this}, this.updateProdQuantity);

        // Render the trolley
        this.renderCart();
    }

   /**
   * Handles the "add to cart" button click
   **/
   addToCartBtnPress(e){
        e.preventDefault();

        // Make button shake
        $(e.target).addClass('btn-shake');
        const $productEntry = $(e.target).closest('.product-entry');

       // Call addToCart function
       e.data._self.addToCart(
            $productEntry.data('id'),
            $productEntry.find('.product-title').text(),
            $productEntry.find('.product-img img').attr('src'),
            1,
            parseFloat( $productEntry.find('.price-value p').text().substr(1) ) // Remove "€" symbol
        );
   }

   /**
   * Handles the "remove product" button click
   **/
    removeFromCartBtnPress(e){
        const prodId = $(e.target).closest('tr').data('id');
        e.data._self.removeFromCart(prodId);
    }

    /**
    * Handles the "Empty trolley" button click
    **/
    emptyCartBtnPress(e){
        e.preventDefault();
        e.data._self.emptyCart();
    }

    /**
    * Finds a product inside the trolley by id and returns it
    * Returns undefined if it can't find anything
    **/
   findProduct(productId){
        return this.cart.find(function(el){
                           return el.id == productId;
       });
   }

    /**
    * Adds a new product to the trolley
    * If there is another product with the same id, it will update the quantity
    **/
   addToCart(productId, productName, productImgUrl, productQuantity, productPrice){
        const productInCart = this.findProduct(productId);

        if(!( typeof productInCart === 'undefined')){
            productInCart.quantity += productQuantity;
            this.renderCart();
            return;
        }

        this.cart.push({
            id: productId,
            name: productName,
            imgUrl: productImgUrl,
            quantity: productQuantity,
            price: productPrice
        });

        this.updateProdCount();
        this.renderCart();
    }

    /**
    * Removes a product from the trolley by id and updates the html table
    **/
    removeFromCart(productId){
        this.cart = this.cart.filter(function(el){
            return el.id != productId;
        });

        this.updateProdCount();
        this.renderCart();
    }

    /**
    * Removes all products from the trolley
    **/
    emptyCart(){
        this.cart = [];
        this.updateProdCount();
        this.renderCart();
    }

    /**
    * Updates the html element which shows the number of products in the trolley
    **/
    updateProdCount(){
        this.$productCountEl.text(this.cart.length);
    }

    /**
    * Handles the 'product quantity' input change
    **/
    updateProdQuantity(e){
        const $btn = $(e.target);
        const prodId = $btn.closest('tr').data('id');
        let productQuantity = parseInt( $btn.val() );

        if(productQuantity < 1){
            productQuantity = 1;
            $btn.val('1');
        }

        let product = e.data._self.findProduct(prodId);
        product.quantity = productQuantity;

        e.data._self.renderCart();
    }

    /**
    * Handles the 'continue with order' button click
    **/
    orderContinueBtnPress(e){
        e.preventDefault();

        if(0 == e.data._self.cart.length){
            return false;
        }

        e.data._self.toSessionStorage();
        window.location.replace(baseUrl+'/jsp/order-confirm.jsp');
    }

    /**
    * Saves the selected products array to sessionStorage
    **/
    toSessionStorage(){
        sessionStorage.setObject(`trolley[${this.supermarketId}]`, this.cart);
    }

    /**
    * Renders the product table inside this.$container
    **/
    renderCart(){
        const $c = this.$container;

        $c.empty();

        if(! this.cart.length){
            $c.html('<p>There is nothing in the Trolley</p>');
            return;
        }

        const $prodTable = $('<table class="pure-table pure-table-bordered etrolley-product-table">');

        $prodTable.append(`
            <colgroup>
                <col span="1" style="width: 20%;">
                <col span="1" style="width: 40%;">
                <col span="1" style="width: 25%;">
                <col span="1" style="width: 15%;">
            </colgroup>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Quantity</th>
                    <th>Price (€)</th>
                </tr>
            </thead>
        `);

        const $prodTbody = $('<tbody>');
        let totPrice = 0;

        for(const prod of this.cart){
            totPrice += (prod.quantity*prod.price);

            $prodTbody.append(`
            <tr data-id="${prod.id}">
                <td>
                    <button class="pure-button cart-remove" data-id="${prod.id}">
                        <i class="fa fa-times" aria-hidden="true"></i>
                    </button>
                </td>
                <td>
                    <img class="pure-img trolley-product-img" src="${prod.imgUrl}">
                    ${prod.name}
                </td>
                <td><input class="product-qt-select" type="number" step="1" min="0" value="${prod.quantity}"></td>
                <td>
                    ${prod.quantity} x ${prod.price.toFixed(2)} = <br>
                    ${(prod.price*prod.quantity).toFixed(2)}
                </td>
            </tr>
            `);
        }

        $prodTable.append($prodTbody);
        $prodTable.append(`
        <tfoot>
            <tr>
                <th colspan="3">Total</th>
                <td>${totPrice.toFixed(2)}</td>
            </tr>
        </tfoot>
        `);

        $c.append($prodTable);
    }
}