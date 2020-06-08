
function showWords() {
    $.ajax({
        method: "GET",
        url: 'api/word?game=' + $("#game").attr("data-id") + "&hash=" + getCookie("hash"),
        success: function(words) {
            if (words.length == 0) {
                hideWords();
                return;
            };
            $("#words").removeAttr("hidden");
            ul = $("#words ul");
            ul.html("");
            for (i = 0; i < words.length; i++) {
                word = words[i];
                li = $("#template").clone();
                li.find("input").attr("id", "word-" + word["id"]);
                li.find("input").attr("data-id", word["id"]);
                li.find("label").attr("for", "word-" + word["id"]);
                li.find("label").text(word["word"]);
                li.removeAttr("hidden");
                li.appendTo(ul);
            }
            if (isAdmin($("#game").attr("data-id"))) {
                $("#submit-btn").removeAttr("hidden");
            }
        },
        error: function() {
            // alert("Can't get words");
            console.log("Can't get words");
        }
    })
}

function hideWords() {
    $("#words").attr("hidden", true);
    $("#submit-btn").attr("hidden", true);
    $("#words ul").html("");
}

function reload() {
    $.ajax({
        method: "GET",
        url: 'api/game/' + $("#game").attr("data-id"),
        success: function(game) {
            if ((game["active"] || isAdmin) && $("#words ul").html() == "") showWords();
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
            setCurrent($("#game").attr("data-id"));

            // Let's update words
            showWords();

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
    game["next"] = true;
    $("#game").find("div.team-card").each(function() {
        team = {};
        team["id"] = $(this).attr("data-id");
        team["score"] = $("#score-" + team["id"]).val();
        team["players"] = [];
        team["name"] = $(this).find("#team-name-" + team["id"]).text()
        $(this).find("li").each(function() {
            player = {};
            player["id"] = $(this).attr("data-id");
            player["name"] = $(this).text();
            player["hash"] = "";
            team["players"].push(player);
        });
        game["teams"].push(team);
    });

    $.ajax({
        method: "PATCH",
        url: 'api/game/' + $("#game").attr("data-id") + "?hash=" + getCookie("hash"),
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

function setActive() {
    $("#btn-start").prop('disabled', false);
}

function setInactive() {
    $("#btn-start").prop('disabled', true);
}

var playing = false;
function setCurrent(game) {
    current = isCurrent(game);
    if (current && !playing) {
        setActive();
    } else {
        setInactive();
    }
}

$(document).ready(() => {

    setCurrent($("#game").attr("data-id"));

    setInterval(reload, 3000);

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
        playing = true;
        timer = $("#timer");
        button = $(this);
        button.prop('disabled', true);

        var time = 5 * 1000;
        var prepare = 3;

        $.ajax({
            method: "PATCH",
            url: 'api/game/' + $("#game").attr("data-id") + "/activate?hash=" + getCookie("hash"),
            headers: {
                'Content-Type': "application/json"
            },
            error: function() {
                alert("Can't activate game");
            }
        });

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
                    // hideWords();
                    timer.val("00:00:00");
                }
            }
        }, 123);
    });

    // TODO Update score!
    $("button.btn-submit").on("click", function() {
        $.ajax({
            method: "PATCH",
            url: 'api/game/' + $("#game").attr("data-id") + "/deactivate?hash=" + getCookie("hash"),
            headers: {
                'Content-Type': "application/json"
            },
            success: function() {
                playing = false;
                useWords();
                update();
            },
            error: function() {
                alert("Can't deactivate game");
            }
        });
    });
})