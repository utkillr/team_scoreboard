
$(document).ready(() => {
    $("#start-btn").on("click", function() {
        $.ajax({
            method: "POST",
            url: "api/game/init?hash=" + getCookie("hash"),
            headers: {
                'Content-Type': "application/json"
            },
            success: function(data) {
                if (data == 0) alert("You're not an admin to do this");
                else window.location.href = "game/" + data + "/admin_lobby"
            },
            error: function() {
                alert("Can't create game");
            }
        });
    });
})