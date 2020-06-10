
function deactivate(game) {
    $.ajax({
        method: "PATCH",
        url: 'api/game/' + game + "/deactivate?hash=" + getCookie("hash"),
        headers: {
            'Content-Type': "application/json"
        },
        async: false,
        success: function() {
            useWords();
            update();
            hideWords();
        },
        error: function() {
            alert("Can't deactivate game");
        }
    });
}

$(document).ready(() => {
    $("button.btn-inc").on("click", function() {
        var id = $(this).attr("data-id");
        var score = $("#score-" + id).val();
        $("#score-" + id).val(+score + 1);
    });

    $("button.btn-dec").on("click", function() {
        var id = $(this).attr("data-id");
        var score = $("#score-" + id).val();
        if (+score > 0) $("#score-" + id).val(+score - 1);
    });

    $("button.btn-submit").on("click", function() {
        deactivate($("#game").attr("data-id"))
    });
});