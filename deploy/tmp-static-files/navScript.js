$(function () {
    $("#navsub").click(function (event) {
        event.preventDefault();
        var submittedText = $("#_text").val();
        $.ajax({
            type: "POST",
            url: "/navigation?navigation_plugin=true",
            transformRequest: function (obj) {
                var str = [];
                for (var p in obj)
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                return str.join("&");
            },
            data: { value: submittedText },
            contentType: "application/x-www-form-urlencoded"
        }).done(function (response) {
            document.getElementById("navres").innerHTML = "<h4>Result:</h4>" + response;
        });
    });
});