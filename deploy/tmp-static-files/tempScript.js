$(function () {
    $("button").click(function (event) {
        event.preventDefault();
        var submittedDate = $("#_date").val();
        $.ajax({
            type: "GET",
            url: "/temperature",
            data: { value: submittedDate }
        }).done(function (response) {
            $('#temp_table').empty();
            if (response.startsWith("Error")) {
                document.getElementById("dateres").innerHTML = "<h4>Error:</h4>" + response;
            }
            else {
                var count = 1;
                var data = response.split("<temperatures>")[1];
                var items = data.split("<temperature");
                items.shift();
                var thead = document.getElementById("temp_table").appendChild(document.createElement('thead'));
                tr = document.createElement('tr'), th = document.createElement('th'), th1 = document.createElement('th'), th2 = document.createElement('th');
                th.setAttribute("scope", "col");
                th1.setAttribute("scope", "col");
                th2.setAttribute("scope", "col");
                th.innerHTML = "entry";
                th1.innerHTML = "Date";
                th2.innerHTML = "Temperature [°C]";
                tr.append(th, th1, th2);
                thead.append(tr);
                var tbody = document.createElement('tbody');
                tbody.setAttribute("id", "table_rows");
                document.getElementById("temp_table").appendChild(tbody);
                var tbody_ele = document.getElementById('table_rows');
                items.forEach(function (element) {
                    var oneDataSet = element.split("<date>");
                    var date = oneDataSet[1].split("</date>")[0];
                    var temp = oneDataSet[1].split("<temp>")[1].split("</temp>")[0];
                    tr = document.createElement('tr'), th = document.createElement('th'), td = document.createElement('td'), td1 = document.createElement('td');
                    th.setAttribute("scope", "row");
                    th.innerHTML = count;
                    td.innerHTML = date;
                    td1.innerHTML = temp;
                    tr.append(th, td, td1);
                    tbody.append(tr);
                    count++;
                });
            }
        });
    });
});