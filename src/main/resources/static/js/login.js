
$(document).ready(() => {
    $("#login-btn").on("click", function() {
        var admin = {};
        admin["login"] = $("#login").val();
        admin["pwd"] = $("#password").val();
        $.ajax({
            method: "POST",
            url: "api/admin",
            headers: {
                'Content-Type': "application/json"
            },
            data: JSON.stringify(admin),
            success: function(data) {
                if (data == null || data == "") alert("You're not authorized");
                else {
                    setCookie("hash", data, 2);
                    window.location.href = "";
                }
            },
            error: function() {
                alert("Can't login");
            }
        });
    });
})