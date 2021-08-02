// List of the public pages of the website
const publicPages = ['login.jsp', 'signup.jsp'];

// Google Maps API Key
const mapsApiKey = 'AIzaSyAvC1X1BYfPY_QbWaqYjDC8GCRxPC_hog0';

$(function() {
    /***************************************************************/
    /**** GLOBAL eTrolley document "ready state" event handler  ****/
    /***************************************************************/
    if ( ! storageAvailable('localStorage')) {
        alert('localStorage is not available in your Browser, the website could not work well!');
    }

    if( ! storageAvailable('sessionStorage') ){
        alert('sessionStorage is not available in your Browser, the website could not work well!');
    }

    if ( ! ( 'content' in document.createElement('template') )  ){
        alert('Your browser doesn\'t support <template> tag, the website could not work well!');
    }

    /* Global Ajax Error event handler */
    $( document ).ajaxError(function( event, jqxhr, settings, thrownError ) {
        if ( jqxhr.status == 401 ) {
            return; // Already handled
        }

        alert('Ajax error, check the console');
        console.log(jqxhr);
    });


    eTrolleyPageInit();
});

/* Add object storage feature to localStorage */
Storage.prototype.setObject = function(key, value) {
    this.setItem(key, JSON.stringify(value));
}

Storage.prototype.getObject = function(key) {
    var value = this.getItem(key);
    return value && JSON.parse(value);
}

/*********************************************************/
/**** GLOBAL eTrolley pages initialization functions ****/
/********************************************************/

/**
* Initializes current page
*
**/
function eTrolleyPageInit(){
    let pageName = getPageFilename();
    if( publicPages.includes(pageName) ){
        eTrolleyPublicPageInit();
    }
    else{
        eTrolleyPrivatePageInit();
    }
}

/**
* Initializes a public page
*
**/
function eTrolleyPublicPageInit(){
    let authUser = eTrolleyAuthGetUser();
    if( authUser ){
        // If user is logged in, redirect back to home
        window.location.replace(baseUrl + "/jsp/home.jsp");
        return;
    }
}

/**
* Initializes a private page
*
**/
function eTrolleyPrivatePageInit(){
    let authUser = eTrolleyAuthGetUser();

    if( ! authUser ){
        return eTrolleyAuthNotLoggedInErrorHandler("The page "+ getPageFilename() +" requires to be logged in");
    }

    // Show user data in the header using the template
    let userInfoTpl = document.getElementById('etrolley-tpl-header-auth-info');
    let $userInfoBox = $(userInfoTpl.content.cloneNode(true));
    $userInfoBox.find('.etrolley-user-name').text("WELCOME " + authUser.costumer.name + " " + authUser.costumer.surname);
    $('#etrolley-header-brand').replaceWith($userInfoBox);

    // Attach logout event
    $(document).on('click', '.etrolley-logout-btn', eTrolleyAuthLogoutCall);
}

/********************************/
/**** Auth helper functions ****/
/********************************/

/**
* Returns the user object from localStorage
* or null if it doens't exists
**/
function eTrolleyAuthGetUser(){
    return localStorage.getObject('authUser');
}

/**
* Stores the user from login response to localStorage
*
**/
function eTrolleyAuthStoreUser(responseData){
    localStorage.setObject('authUser', responseData);
}

/**
* Action on "logout" button press
**/
function eTrolleyAuthLogoutCall(e){
    e.preventDefault();
    $(this).html('<i class="fa fa-spinner fa-spin fa-fw"></i> Logging out...').css('text-decoration', 'none');

    // Perform ajax request
    eTrolleyAJAX(baseUrl + "/rest/costumer/login", {}, true ,"DELETE")
    .then(eTrolleyAuthLogoutUser);
}

/**
* Logs out the user (clears the localStorage and redirects to login page)
**/
function eTrolleyAuthLogoutUser(){
    // Clear storages and redirect
    localStorage.clear();
    sessionStorage.clear();
    window.location.href = baseUrl + "/jsp/login.jsp";
}

/**
* eTrolleyAuthNotLoggedInErrorHandler
**/
function eTrolleyUnauthorizedResponseHandler(){
    eTrolleyAuthNotLoggedInErrorHandler('Session is expired');
}

/**
* Performs an action when the user is not logged in
**/
function eTrolleyAuthNotLoggedInErrorHandler(customErrMsg = ''){
    if(! customErrMsg){
        customErrMsg = 'Login is required to see the content'
    }

    localStorage.clear();
    sessionStorage.clear();

    sessionStorage.setItem('loginError', customErrMsg);
    window.location.href = baseUrl + "/jsp/login.jsp";
}

/*************************************/
/**** Helpers & utility functions ****/
/*************************************/

/**
* eTrolley custom AJAX function
* returns a promise
**/
function eTrolleyAJAX(url, data = {}, auth = false, method = "GET"){
    const ajaxSetup = {
        url: url,
        method: method,
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    };

    if( ! $.isEmptyObject(data) ){
        // Do not convert form data to JSON when the request is "GET"
        ajaxSetup.data = ( method == "GET" ? data : JSON.stringify(data) );
    }

    if(auth){
        const authUser = eTrolleyAuthGetUser();
        if(authUser){
            ajaxSetup.headers = {
                "Authorization": "Bearer " + authUser.token.bearer
            }
        }

        ajaxSetup.statusCode = {
            401: eTrolleyUnauthorizedResponseHandler
        }
    }

    let promise = $.ajax(ajaxSetup)
    .done(function (responseData, status, xhr) {
        // Default done function
   })
   .fail(function (xhr, status, err) {
        // Default fail function
   });

   return promise;
}


/**
* Checks if the client storage of a type is available on the browser
* https://developer.mozilla.org/en-US/docs/Web/API/Web_Storage_API/Using_the_Web_Storage_API
**/
function storageAvailable(type) {
    var storage;
    try {
        storage = window[type];
        var x = '__storage_test__';
        storage.setItem(x, x);
        storage.removeItem(x);
        return true;
    }
    catch(e) {
        return e instanceof DOMException && (
            // everything except Firefox
            e.code === 22 ||
            // Firefox
            e.code === 1014 ||
            // test name field too, because code might not be present
            // everything except Firefox
            e.name === 'QuotaExceededError' ||
            // Firefox
            e.name === 'NS_ERROR_DOM_QUOTA_REACHED') &&
            // acknowledge QuotaExceededError only if there's something already stored
            (storage && storage.length !== 0);
    }
}

/**
* Returns the file name of current page
* For example for http://localhost:8082/etrolley-1.00/jsp/login.jsp
* it returns 'login.jsp'
**/
function getPageFilename(){
    let path = window.location.pathname;
    return path.split("/").pop();
}
