$(function() {
    const $document = $(document);

    const authUser = eTrolleyAuthGetUser();

    $('#name').val(authUser.costumer.name);
    $('#surname').val(authUser.costumer.surname);
    $('#email').val(authUser.costumer.email);
    $('#zipcode').val(authUser.costumer.zipcode);
    $('#city').val(authUser.costumer.city);
    $('#address').val(authUser.costumer.address);

});


