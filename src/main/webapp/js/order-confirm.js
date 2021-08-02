$(function() {
    // Get selected supermarket id
    const supermarketId = sessionStorage.getItem('supermarketId');

    if(null == supermarketId){
        window.location.replace(baseUrl + '/jsp/supermarket-list.jsp');
        return;
    }

    // Get the selected products
    const sessionKey = `trolley[${supermarketId}]`;
    const cart = sessionStorage.getObject(sessionKey);

    if(null == cart || cart.length == 0){
        $('.order-confirm-container').html('<h1>There are no orders</h1><p>Please select some products of a supermarket</p>');
        return;
    }

    // Clear sessionStorage
    sessionStorage.removeItem('supermarketId');
    sessionStorage.removeItem(sessionKey);

    eTrolleyRenderOrderTable(cart);


});

var shopper = {
              items : [], // Array of current shopping list
              add : function (evt) {
              // add() : add a new item to the list

                // Prevent form submit
                evt.preventDefault();

                // Remove current add item
                item.value = "";

                // Redraw the shopping list
                shopper.draw();

                // Save the shopping list to local storage
                shopper.save();
              },

              draw : function () {
              // draw() : draw the HTML shopping list

                // Reset the current shopping list first
                var container = document.getElementById('shop-list');
                container.innerHTML = "";

                // Rebuild the list
                if (shopper.items.length > 0) {
                  var row = "", button = "";
                  for (let i in shopper.items) {
                    // Item name
                    row = document.createElement("div");
                    row.innerHTML = shopper.items[i].name;
                    // Strike through if item is "done"
                    if (shopper.items[i].done) {
                      row.style = "text-decoration:line-through;";
                    }
                    container.appendChild(row);

                    // Delete button
                    row = document.createElement("div");
                    button = document.createElement("input");
                    button.type = "button";
                    button.value = "Delete";
                    button.dataset.id = i;
                    button.addEventListener("click", shopper.delete);
                    row.appendChild(button);

                    // Completed/Not Yet button
                    button = document.createElement("input");
                    button.type = "button";
                    button.value = shopper.items[i].done ? "Not Yet" : "Got It";
                    button.dataset.id = i;
                    button.addEventListener("click", shopper.toggle);
                    row.appendChild(button);
                    container.appendChild(row);
                  }
                }
              },

              save : function () {
              // save() : save the current shopping list into the local storage

                // Init localstorage
                if (localStorage.items == undefined) { localStorage.items = "[]"; }

                // Save current items list to localstorage
                localStorage.items = JSON.stringify(shopper.items);
              },

              load : function () {
              // load() : retrieve previous shopping cart from local storage

                // Init localstorage
                if (localStorage.items == undefined) { localStorage.items = "[]"; }

                // Load shopping list
                shopper.items = JSON.parse(localStorage.items);

                // Draw
                shopper.draw();
              },


              delete : function () {
              // delete() : delete the selected item

                if (confirm("Remove selected item?")) {
                  // Remove selected item
                  shopper.items.splice(this.dataset.id, 1);

                  // Save
                  shopper.save();

                  // Redraw
                  shopper.draw();
                }
              },

              toggle : function () {
              // toggle() : toggle item between "got it" or "not yet"

                // Toggle item status
                var id = this.dataset.id;
                shopper.items[id].done = !shopper.items[id].done;

                // Save
                shopper.save();

                // Redraw
                shopper.draw();
              }
            };

window.addEventListener("load", function () {

    // Shopping list init
    shopper.load();
    document.getElementById("shop-add").addEventListener("submit", shopper.add);
});

/**
* Renders the selected products table
**/
function eTrolleyRenderOrderTable(cart){
        const $ordTable = $('<table class="pure-table pure-table-bordered etrolley-order-table">');

        $ordTable.append(`
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
                    <th>Tot. Price (€)</th>
                </tr>
            </thead>
        `);

        const $prodTbody = $('<tbody>');
        let totPrice = 0;
        let count = 0;

       for(const prod of cart){
            totPrice += (prod.quantity*prod.price);
            count++;

            $ordTable.append(`
            <tr data-id="${prod.id}">
                <td>
                    ${count}
                </td>
                <td>
                    <img class="pure-img order-product-img" src="${prod.imgUrl}">
                    ${prod.name}
                </td>
                <td>${prod.quantity}</td>
                <td>
                    ${(prod.price*prod.quantity).toFixed(2)}
                </td>
            </tr>
            `);
        }

        $ordTable.append($prodTbody);
        $ordTable.append(`
        <tfoot>
            <tr>
                <th colspan="3">Total</th>
                <td>${totPrice.toFixed(2)}</td>
            </tr>
        </tfoot>
        `);

        $('#order-table').append($ordTable);
        sessionStorage.setItem('totalOrderPrice', totPrice);
}
