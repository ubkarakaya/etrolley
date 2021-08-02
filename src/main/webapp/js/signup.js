$(function() {
    let $signupForm = $('.form-signup');

    // Attach submit evento to login form
    $signupForm.on('submit', signupFormSubmitEvent);
});

function signupFormSubmitEvent(e){
    // Prevent browser's submission of the form
    e.preventDefault();
    let $form = $(this);

    const formData = $form.serializeArray();

    const costumerJson = {
        costumer: {}
    };

    for (const field of formData){
        costumerJson.costumer[field.name] = field.value;
    }

    // Parse the zipcode as integer
    costumerJson.costumer.zipcode = parseInt(costumerJson.costumer.zipcode);
    console.log(costumerJson);

    // Perform ajax request
    eTrolleyAJAX(baseUrl + "/rest/costumer", costumerJson, false ,"POST")
    .done(signupFormSuccess)
    .fail(signupFormError);
}

function signupFormSuccess(responseData){
    eTrolleyAuthStoreUser(responseData);
    window.location.replace(baseUrl + "/jsp/home.jsp");
}

function signupFormError(xhr, status, err){
    if(!('responseJSON' in xhr) || ! xhr.responseJSON){
        console.log(status + ' | ' + err);
        alert('Error from the server, check the console');
    }

    const error = xhr.responseJSON;

    if(!('message' in error)){
        console.log(error);
        alert('Unknown error message, check the console');
        return;
    }

    let errormsg = '<strong>Errors occurred</strong><br>';
    errormsg += error.message.message;

    if('error-details' in error.message){
        console.log(error.message['error-details']);
    }

    $('#signup-form-errors').html(errormsg).removeClass('hidden');
}