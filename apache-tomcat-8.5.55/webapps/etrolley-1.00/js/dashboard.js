$(function() {
   const authUser = eTrolleyAuthGetUser();

   $('.costumer-chip').text(authUser.costumer.chip);
   $('.costumer-spending').text(authUser.costumer.spending.toFixed(2));
   $('.costumer-count').text(authUser.costumer.session_count);

});


