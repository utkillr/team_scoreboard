function reload() {
    $.ajax({
        method: "GET",
        url: 'api/game/' + $("#game").attr("data-id"),
        success: function(game) {
            for (i = 0; i < game["teams"].length; i++) {
                team = game["teams"][i];
                $("#team-name-" + team["id"]).text(team["name"])
                $("#score-" + team["id"]).val(team["score"])
                if (team["current"]) {
                    $("#team-name-" + team["id"]).addClass("current");
                } else {
                    $("#team-name-" + team["id"]).removeClass("current");
                }
                for (j = 0; j < team["players"].length; j++) {
                    player = team["players"][j];
                    $("#player-" + player["id"]).text(player["name"])
                    if (player["current"]) {
                        $("#player-" + player["id"]).addClass("current");
                    } else {
                        $("#player-" + player["id"]).removeClass("current");
                    }
                }
            }
        },
        error: function() {
            alert("Can't get game results");
        }
    });
}

function update() {
    var game = {};
    game["id"] = $("#game").attr("data-id");
    game["teams"] = [];
    $("#game").find("div.team-card").each(function() {
        team = {};
        team["id"] = $(this).attr("data-id");
        team["score"] = $("#score-" + team["id"]).val();
        team["players"] = [];
        $(this).find("li").each(function() {
            player = {};
            player["id"] = $(this).attr("data-id");
            team["players"].push(player);
        });
        game["teams"].push(team);
        game["next"] = true;
    });

    $.ajax({
        method: "PATCH",
        url: 'api/game/' + $("#game").attr("data-id"),
        headers: {
            'Content-Type': "application/json"
        },
        data: JSON.stringify(game),
        success: function() {
            reload();
        },
        error: function() {
            alert("Can't send game results");
        }
    });
}

$(document).ready(() => {
    $("button.btn-inc").on("click", function() {
        id = $(this).attr("data-id");
        score = $("#score-" + id).val();
        $("#score-" + id).val(+score + 1);
    });

    $("button.btn-dec").on("click", function() {
        id = $(this).attr("data-id");
        score = $("#score-" + id).val();
        if (+score > 0) $("#score-" + id).val(+score - 1);
    });

    $("button.btn-start").on("click", function() {
        timer = $("#timer");
        button = $(this);
        button.prop('disabled', true);

        var time = 5 * 1000;
        var prepare = 3;

        var y = setInterval(function() {
            button.text(prepare);
            prepare -= 1;
            if (prepare < 0) {
                clearInterval(y);
                button.text("GO!")
            }
        }, 1000);

        var x = setInterval(function() {
            if (prepare < 0) {
                var m = Math.floor(time / (1000 * 60));
                var s = Math.floor((time % (1000 * 60)) / 1000);
                var ms = Math.floor((time % 1000) / 10);

                if (('' + m).length == 1) m = '0' + m;
                if (('' + s).length == 1) s = '0' + s;
                if (('' + ms).length == 1) ms = '0' + ms;

                timer.val(m + ":" + s + ":" + ms);

                time -= 123

                // If the count down is finished, write some text
                if (time < 0) {
                    clearInterval(x);
                    timer.val("00:00:00");
                    update();
                    button.prop('disabled', false);
                }
            }
        }, 123);
    });
})