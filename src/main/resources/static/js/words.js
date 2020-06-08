
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
