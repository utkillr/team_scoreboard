
function reload() {

    var playerId = getPlayerId($("#game").attr("data-id"));

    $.ajax({
        method: "GET",
        url: 'api/game/' + $("#game").attr("data-id"),
        success: function(game) {
            if (game["startDate"] != null) window.location.href = "game/" + game["id"]
            $("ul.list-group").each(function() {
                $(this).html("");
            });
            var template = $("#template");
            for (i = 0; i < game["teams"].length; i++) {
                var team = game["teams"][i];
                if (team["isLobby"]) continue;
                if (!isAdmin(game["id"])) $("#team-name-" + team["id"]).val(team["name"])
                for (j = 0; j < team["players"].length; j++) {
                    var player = team["players"][j];
                    var ul = $("#team-" + team["id"] + " ul");
                    var li = template.clone();
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
        var player = {};
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
})
