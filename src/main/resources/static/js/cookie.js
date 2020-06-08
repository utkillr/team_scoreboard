function setCookie(key, value, hours) {
    var d = new Date();
    d.setTime(d.getTime() + (hours * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = key + "=" + value + ";" + expires // + ";path=/";
}

function removeCookie(key) {
    document.cookie = key + "=" + ";" + expires // + ";path=/";
}

function getCookie(key) {
    var name = key + "=";
    var cookies = document.cookie.split(';');
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        while (cookie.charAt(0) == ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(key) == 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return "0";
}

function hasCookie(key) {
    return getCookie(key) != "" && getCookie(key) != "0";
}

function getPlayerId(game) {
    res = 0;
    $.ajax({
        method: "GET",
        url: 'api/player/' + getCookie("hash") + '/id?game=' + game,
        headers: {
            'Content-Type': "application/json"
        },
        async: false,
        success: function(data) {
            res = data;
        },
        error: function() {
            alert("Can't get id");
        }
    });
    return res;
}

function isCurrent(game) {
    res = false;
    $.ajax({
        method: "GET",
        url: 'api/player/' + getCookie("hash") + '/current?game=' + game,
        headers: {
            'Content-Type': "application/json"
        },
        async: false,
        success: function(data) {
            res = data;
        },
        error: function() {
            alert("Can't get current");
        }
    });
    return res;
}

function isAdmin(game) {
    res = false;
    $.ajax({
        method: "GET",
        url: 'api/player/' + getCookie("hash") + '/admin?game=' + game,
        headers: {
            'Content-Type': "application/json"
        },
        async: false,
        success: function(data) {
            res = data;
        },
        error: function() {
            alert("Can't get admin");
        }
    });
    return res;
}
