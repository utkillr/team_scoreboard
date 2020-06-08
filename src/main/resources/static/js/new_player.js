
$(document).ready(() => {
    $("#login-btn").on("click", function() {
        gameId = $("#game").attr("data-id");
        player = {};
        player["name"] = $("#input-player").val();
        player["hash"] = "";

        $.ajax({
            method: "POST",
            url: "api/player/game/" + gameId,
            headers: {
                'Content-Type': "application/json"
            },
            data: JSON.stringify(player),
            success: function(data) {
                setCookie("hash", data, 2);
                window.location.href = "game/" + gameId;
            },
            error: function() {
                alert("Can't create player");
            }
        });
    });
})
