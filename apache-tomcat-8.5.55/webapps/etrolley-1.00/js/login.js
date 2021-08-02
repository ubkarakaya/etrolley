$(function() {
    let $loginForm = $('#etrolley-login-form');

    // Attach submit evento to login form
    $loginForm.on('submit', loginFormSubmitEvent);

    // Show login errors from sessionStorage
    const loginError = sessionStorage.getItem('loginError')
    if(loginError){
        loginFormErrorPrint(loginError);
        sessionStorage.removeItem('loginError');
    }
});

function loginFormSubmitEvent(e){
    // Prevent browser's submission of the form
    e.preventDefault();

    let $loginForm = $(this);
    const data = {
        costumerLogin: {
            email: $loginForm.find('input[name="email"]').val(),
            password: $loginForm.find('input[name="password"]').val(),
            remember: $loginForm.find('input[name="remember"]')[0].checked
        }
    };

    // Perform ajax request
    eTrolleyAJAX(baseUrl + "/rest/costumer/login", data, false ,"POST")
    .done(loginFormSuccess)
    .fail(loginFormError);
}

function loginFormSuccess(responseData, status, xhr){
    eTrolleyAuthStoreUser(responseData);
    window.location.replace(baseUrl + "/jsp/home.jsp");
}

function loginFormError(xhr, status, err){
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

    if('error-details' in error.message){
        console.log(error.message['error-details']);
    }

    loginFormErrorPrint(error.message.message)
}

function loginFormErrorPrint(errormsg){
    $('#login-form-errors').html('<strong>Errors occurred</strong><br>' + errormsg)
    .removeClass('hidden');
}