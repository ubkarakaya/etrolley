
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<!DOCTYPE html>
<html>
  <head>
          <jsp:include page="partials/head.jsp" />
          <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />

          <title>Select products</title>
  	</head>
    <script>
        var shopper = {
          items : [], // Array of current shopping list
          add : function (evt) {
          // add() : add a new item to the list

            // Prevent form submit
            evt.preventDefault();

            // Add new entry to shopper.items
            var item = document.getElementById('add-item');
            shopper.items.push({
              name : item.value, // Item name
              done : false // True for "completed" item, false for not
            });

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
          shopper.load();
          document.getElementById("shop-add").addEventListener("submit", shopper.add);
        });

    </script>
  <body>
            	<body>
                    <jsp:include page="partials/header2.jsp" />

                   <!-- [SHOPPING LIST] -->
                   <div class="splash-container">
                   <div class="splash">
                       <div align="center">
                       <h1>Your Shopping List</h1>
                       <div id="shop-list"></div>

                       <!-- [ADD NEW ITEM] -->
                       <h1>Please add your orders</h1>
                       <form id="shop-add">
                         <input type="text" id="add-item" placeholder="to do list"/>
                         <input type="submit" class="pure-button pure-button-primary" value="Add to list"/>
                       </form>
                        </div>
                        </div>
                        </div>


                    <jsp:include page="partials/footer.jsp" />
                    <jsp:include page="partials/footer-scripts.jsp" />
            	</body>
  </body>
</html>