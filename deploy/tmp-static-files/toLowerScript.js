$(function () {
    $("button").click(function (event) {
        event.preventDefault();
        var post_url = "/tolower?tolower_plugin=true";
        var request_method = "POST";
        var submittedText = $("#_text").val();
        $("#upload-progress .progress-bar").css("width", + 0);
        $.ajax({
            type: "POST",
            url: "/tolower?tolower_plugin=true",
            transformRequest: function (obj) {
                var str = [];
                for (var p in obj)
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                return str.join("&");
            },
            data: { value: submittedText },
            contentType: "application/x-www-form-urlencoded",
            xhr: function () {
                var xhr = $.ajaxSettings.xhr();
                if (xhr.upload) {
                    xhr.upload.addEventListener('progress', function (event) {
                        var percent = 0;
                        var position = event.loaded || event.position;
                        var total = event.total;
                        if (event.lengthComputable) {
                            percent = Math.ceil(position / total * 100);
                        }
                        $("#upload-progress .progress-bar").css("width", + percent + "%");
                    }, true);
                }
                return xhr;
            }

        }).done(function (response) {
            if (response === "Not found: Bitte geben Sie einen Text ein") {
                $("#upload-progress .progress-bar").css("width", + 0);
            }
            document.getElementById("tolowerres").innerHTML = "<h4>Result:</h4>" + response;
        });
    });
});