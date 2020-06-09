$(document).ready(() => {
    $("#start-btn").on("click", function() {

        game = {};
        game["id"] = 0;
        game["next"] = true;
        game["teams"] = [];
        for (i = 0; i < 4; i++) {
            team = {};
            team["id"] = 0;
            team["name"] = $("#input-team-" + (i + 1)).val()
            team["score"] = 0;
            team["players"] = [];
            for (j = 0; j < 4; j++) {
                player = {};
                player["id"] = 0;
                player["name"] = $("#input-player-" + (i + 1) + "-" + (j + 1)).val()
                team["players"].push(player);
            }
            game["teams"].push(team);
        }

        $.ajax({
            method: "POST",
            url: "api/game",
            headers: {
                'Content-Type': "application/json"
            },
            data: JSON.stringify(game),
            success: function(data) {
                window.location.href="game/" + data;
            },
            error: function() {
                alert("Can't create game");
            }
        });
    });
})