
function reload() {

    playerId = getPlayerId($("#game").attr("data-id"));

    $.ajax({
        method: "GET",
        url: 'api/game/' + $("#game").attr("data-id"),
        success: function(game) {
            if (game["startDate"] != null) window.location.href = "game/" + game["id"]
            $("ul.list-group").each(function() {
                $(this).html("");
            });
            template = $("#template");
            for (i = 0; i < game["teams"].length; i++) {
                team = game["teams"][i];
                if (team["isLobby"]) continue;
                if (!isAdmin()) $("#team-name-" + team["id"]).val(team["name"])
                for (j = 0; j < team["players"].length; j++) {
                    player = team["players"][j];
                    ul = $("#team-" + team["id"] + " ul");
                    li = template.clone();
                    li.attr("id", "player-" + player["id"]);
                    li.attr("data-id", player["id"]);
                    li.removeAttr("hidden");
                    li.text(player["name"]);
                    li.appendTo(ul);
                }
            }
        },
        error: function() {
            alert("Can't get lobby status");
        }
    });
}

$(document).ready(() => {

    reload();

    setInterval(reload, 2000);

    $(".btn-join").on("click", function() {
        player = {};
        player["hash"] = getCookie("hash");

        $.ajax({
            method: "PATCH",
            url: "api/team/" + $(this).attr("data-id") + "/join",
            headers: {
                'Content-Type': "application/json"
            },
            data: JSON.stringify(player),
            success: function() {
                reload();
            },
            error: function() {
                alert("Can't join team");
            }
        });
    });

    $("#btn-start").on("click", function() {
        game = {}
        game["id"] = $("#game").attr("data-id");
        game["next"] = false;
        game["teams"] = [];

        $("#game").find("div.team-card").each(function() {
            team = {}
            team["id"] = $(this).attr("data-id");
            team["name"] = $("#team-name-" + team["id"]).val();
            team["score"] = 0;
            team["players"] = [];

            $(this).find("li").each(function() {
                player = {};
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
