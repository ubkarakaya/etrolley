$(function() {
    const $document = $(document);

   // Attach event to "View products" button
   $document.on('click', '.supermarket-view-prd-btn', eTrolleyViewProductsClick);

   // Attach event to "View on Map" button
   $document.on('click', '.supermarket-view-map-btn', eTrolleyViewMapClick);

    sessionStorage.removeItem('supermarketId');
   // Perform AJAX request to retrieve supermarkets data
   // Example url /json-example/supermarket-list.json
   const authUser = eTrolleyAuthGetUser();

   const data = {
        latitude: authUser.costumer.latitude,
        longitude: authUser.costumer.longitude
   };

   if(search){
        data.search = search;
   }

   eTrolleyAJAX(baseUrl + "/rest/supermarket", data, true)
        .done(eTrolleyRenderSupermarkets);
});

var urlParams = new URLSearchParams(window.location.search);
var search = urlParams.get('search');

/**
* Renders the supermarket list from JSON
**/
function eTrolleyRenderSupermarkets(responseData){
    const authUser = eTrolleyAuthGetUser();
    $('.costumer-address').text(authUser.costumer.address + ", " + authUser.costumer.city);
    $('#supermarkets-count').text(responseData["resource-list"].length);

    if(search){
        $('.results-number').append(document.createTextNode(" and for keywords '" + search + "'"));
    }

    const surpermarketTpl = document.getElementById('etrolley-supermarket-entry-tpl');

    for(const item of responseData["resource-list"]){
        let supermarket = item.supermarket;
        let $supermarketEntry = $(surpermarketTpl.content.cloneNode(true));

        $supermarketEntry.find('.supermarket-logo img').attr({
            src: baseUrl + "/images/logo/" + supermarket.logo,
            alt: supermarket.name + ' Logo'
        });

        $supermarketEntry.find('.supermarket-name').text(supermarket.name);
        $supermarketEntry.find('.supermarket-address').append(
            document.createTextNode(supermarket.address)
        );
        console.log(supermarket.vatcode);
        let distance = Number((supermarket.distance/0.62137)*1000);
        $supermarketEntry.find('.supermarket-distance-meters').text(distance.toFixed(2));
        $supermarketEntry.find('.supermarket-rating .fa-star').slice(0, supermarket.rating).addClass('star-checked');
        $supermarketEntry.find('.supermarket-view-prd-btn').data('id', supermarket.vatcode);
        $supermarketEntry.find('.supermarket-view-map-btn').data({
            latitude: supermarket.latitude,
            longitude: supermarket.longitude
        });

        $('<li class="supermarket-entry"></li>')
            .append($supermarketEntry)
            .appendTo('#supermarket-ul');
    }
}

/**
* Handles "View products" button click event
**/
function eTrolleyViewProductsClick(e){
    const supermarketId = $(this).data('id').trim();

    // Set the Supermarket ID inside session storage in order to use it in the next page
    sessionStorage.setItem('supermarketId', supermarketId);

    // Make sure the link click go normally
    return true;
}

/**
* Handles "View on Map" button click event
**/
function eTrolleyViewMapClick(e){
    e.preventDefault();

    const $btn = $(this);

    const gMapsBaseUrl = 'https://www.google.com/maps/embed/v1/place?key=' + mapsApiKey;
    const mapUrl = gMapsBaseUrl + '&q=' + $btn.data('latitude') + ',' + $btn.data('longitude');
    $('#map-modal iframe').attr('src', mapUrl);
}