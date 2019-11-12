$(function () {
    $("#button_date").click(function (event) {
        event.preventDefault();
        var submittedDate = $("#_date").val();
        $.ajax({
            type: "GET",
            url: "/temperature",
            data: { value: submittedDate }
        }).done(function (response) {
            $("#temp_table tbody tr").remove();
            var tbody = document.getElementById("table_rows");
            var count = 1;
            $(response).find('temperature').each(function () {
                var date = $('date', this).text();
                var temp = $('temp', this).text();
                tr = document.createElement('tr'), th = document.createElement('th'), td = document.createElement('td'), td1 = document.createElement('td');
                th.setAttribute("scope", "row");
                th.setAttribute("class", "table_col");
                td.setAttribute("class", "table_col");
                td1.setAttribute("class", "table_col");
                th.innerHTML = count;
                td.innerHTML = date;
                td1.innerHTML = temp;
                tr.append(th, td, td1);
                tbody.append(tr);
                count++;
            });
            if (count === 1) {
            var type = $(response).find('Error');
            console.log(type.attr('type'));
                if(type.attr('type') === "InvalidDate"){
                    document.getElementById("dateres").innerHTML = "<h4>Invalid Date </h4>";
                }
                else{
                    document.getElementById("dateres").innerHTML = "<h4> No data found </h4>";
                }
            }
            else {
                document.getElementById("dateres").innerHTML = "<h4> Data loaded </h4>";
            }
        });
    });
});


function createTableFromXML(response) {
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