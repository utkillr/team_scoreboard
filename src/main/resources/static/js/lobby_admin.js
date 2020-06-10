
$(document).ready(() => {
    $("#btn-start").on("click", function() {
        var game = {}
        game["id"] = $("#game").attr("data-id");
        game["next"] = false;
        game["teams"] = [];

        $("#game").find("div.team-card").each(function() {
            var team = {}
            team["id"] = $(this).attr("data-id");
            team["name"] = $("#team-name-" + team["id"]).val();
            team["score"] = 0;
            team["players"] = [];

            $(this).find("li").each(function() {
                var player = {};
                player["id"] = $(this).attr("data-id");
                player["name"] = $(this).text();
                player["hash"] = "";
                team["players"].push(player);
            })

            game["teams"].push(team);
        });

        $.ajax({
            method: "POST",
            url: "api/game/" + game["id"] + "/start?hash=" + getCookie("hash"),
            headers: {
                'Content-Type': "application/json"
            },
            data: JSON.stringify(game),
            success: function(data) {
                if (data == 0) alert("You're not an admin to do this");
                else window.location.href = "game/" + data + "?hash=" + getCookie("hash");
            },
            error: function() {
                alert("Can't start game");
            }
        });
    });
})
