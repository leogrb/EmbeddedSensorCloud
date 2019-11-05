$(function () {
    $("button").click(function (event) {
        event.preventDefault();
        var submittedDate = $("#_date").val();
        $.ajax({
            type: "GET",
            url: "/temperature",
            data: { value: submittedDate }
        }).done(function (response) {
            document.getElementById("dateres").innerHTML = "<h4>Result:</h4>" + response;
        });
    });
});