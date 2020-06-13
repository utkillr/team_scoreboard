
var POLLING_PERIOD = 2000;

function showWords() {
    $.ajax({
        method: "GET",
        url: 'api/word?game=' + $("#game").attr("data-id") + "&hash=" + getCookie("hash"),
        success: function(words) {
            if (words.length == 0) {
                hideWords();
                return;
            };
            $("#words").prop("hidden", false);
            ul = $("#words ul");
            ul.html("");
            for (i = 0; i < words.length; i++) {
                word = words[i];
                li = $("#template").clone();
                li.find("input").attr("id", "word-" + word["id"]);
                li.find("input").attr("data-id", word["id"]);
                li.find("label").attr("for", "word-" + word["id"]);
                li.find("label").text(word["word"]);
                li.prop("hidden", false);
                li.appendTo(ul);
            }
        },
        error: function() {
            // alert("Can't get words");
            console.log("Can't get words");
        }
    })
}

function hideWords() {
    $("#words").prop("hidden", true);
    $("#words ul").html("");
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function runTimer(delay) {
    var timer = $("#timer");
    var time = 60 * 1000;
    await sleep(delay);
    if (timer.val() != "00:00:00") return;
    var x = setInterval(function() {
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
        }
    }, 123);
    return x;
}

function reload() {
    $.ajax({
        method: "GET",
        url: 'api/game/' + $("#game").attr("data-id"),
        success: function(game) {

            // Sync timer
            if ((game["diff"] != null) && (game["diff"] > 0) && (game["diff"] < POLLING_PERIOD)) {
                runTimer(game["diff"]);
            }

            current = isCurrentAndRunning(game["id"], true);
            if (current) admin = false;
            else admin = isAdmin(game["id"]);

            if (admin) {
                if ($("#words ul").children().length == 0) showWords();
                if (game["running"]) return;
            } else {
                if (current) showWords();
                else hideWords();
            }
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
            setCurrent(game["id"]);
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

function setCurrent(game) {
    if (isCurrentAndRunning(game, false)) {
        setActive();
    } else {
        setInactive();
    }
}

function activate(game) {
    $.ajax({
        method: "PATCH",
        url: 'api/game/' + game + "/activate?hash=" + getCookie("hash"),
        headers: {
            'Content-Type': "application/json"
        },
        error: function() {
            alert("Can't activate game");
        }
    });
}

$(document).ready(() => {
    setCurrent($("#game").attr("data-id"));
    reload()
    setInterval(reload, POLLING_PERIOD);

    $("button.btn-start").on("click", function() {
        var button = $(this);
        var prepare = 3;
        button.prop('disabled', true);
        button.text(prepare);
        prepare -= 1;

        activate($("#game").attr("data-id"));

        var y = setInterval(function() {
            button.text(prepare);
            prepare -= 1;
            if (prepare < 0) {
                clearInterval(y);
                button.text("GO!")
            }
        }, 1000);
    });
})