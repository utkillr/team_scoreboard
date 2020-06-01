function getWords() {
    count = 10;
    $.ajax({
        method: "GET",
        url: 'api/word?game=' + $("#words").attr("data-id") + '&count=' + count,
        success: function(words) {
            html = ""
            for (i = 0; i < words.length; i++) {
                html += $("#template").html().replace(/{{id}}/g, words[i]["id"]).replace(/{{word}}/g, words[i].word);
            }
            $("#words-container").html(html)
        },
        error: function() {
            alert("Can't get words");
        }
    });
}

function useWords() {
    game = $("#words").attr("data-id");
    words = [];
    $("#words").find("input").each(function() {
        done = $(this).prop("checked");
        if (done) words.push({
            "wordId": $(this).attr("data-id"),
            "gameId": game
        });
    });
    if (words.length == 0) {
        getWords();
        return;
    } else {
        console.log(words)
        $.ajax({
            method: "PATCH",
            url: "api/word",
            headers: {
                'Content-Type': "application/json"
            },
            data: JSON.stringify(words),
            success: function(data) {
                getWords();
            },
            error: function() {
                alert("Can't use words");
            }
        });
    }
}

$(document).ready(() => {
    $("#submit-btn").on("click", useWords);
})