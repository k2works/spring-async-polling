$(function () {
    var POLLLING_INVERVAL_TIME_IN_MILLIS = 1000;//1s
    (function polling() {
        getCountUp();
        window.setTimeout(polling, POLLLING_INVERVAL_TIME_IN_MILLIS);
    }());
    function getCountUp() {
        $.ajax({
            type: "GET",
            url: CONTEXT_PATH + "api/tasks",
            dataType: "json",
        }).done(function (data) {
            $("tbody tr:not(:first)").remove();

            var updatedList = "";
            data.forEach(function (element, index, array) {
                updatedList += `<tr><td>${element.id}</td><td>${element.amount}</td><td>${element.done}</td></tr>`;
            });

            $("tbody tr:first").after(updatedList);
        }).fail(function (jqXHR, textStatus) {
            $("button").after().remove();
            $("button").after("<p>error occured</p>");
        });
    }

    $("#createBtn").on("click", function () {
        $.ajax({
            type: "POST",
            url: CONTEXT_PATH + "api/tasks",
            dataType: "json"
        }).done(function (data) {
            getCountUp();
        }).fail(function () {
            window.alert("登録に失敗しました");
        });
    });

    $("#execBtn1").on("click", function () {
        $("#result").text("処理中");
        $.ajax({
            type: "PUT",
            url: CONTEXT_PATH + "api/tasks/heavy1",
            dataType: "json"
        }).done(function (data) {
            console.log(data)
            $("#result1").text(data.message)
        }).fail(function (jqXHR, textStatus) {
            $("#result1").text("失敗")
        })
    });


    $("#execBtn2").on("click", function () {
        $("#result2").text("処理中");
        $.ajax({
            type: "PUT",
            url: CONTEXT_PATH + "api/tasks/heavy2",
            dataType: "json"
        }).done(function (data) {
            console.log(data)
            $("#result2").text(data.message)
        }).fail(function (jqXHR, textStatus) {
            $("#result2").text("失敗")
        })
    })

    $("#execBtn3").on("click", function () {
        $("#result3").text("処理中");
        $.ajax({
            type: "PUT",
            url: CONTEXT_PATH + "api/tasks/heavy3",
            dataType: "json"
        }).done(function (data) {
            console.log(data)
            var updatedList = "";
            data.forEach(function (element, index, array) {
                updatedList += `<tr><td>${element}</td><td>`;
            });
            $("#result3").text("非同期処理を完了しました");
            $("#result3").after(updatedList);
        }).fail(function (jqXHR, textStatus) {
            $("#result3").text("失敗")
        })
    })

    $("#execBtn4").on("click", function () {
        $("#result4").text("処理中");
        $.ajax({
            type: "PUT",
            url: CONTEXT_PATH + "api/tasks/heavy4",
            dataType: "json"
        }).done(function (data) {
            console.log(data)
            $("#result4").text(data.message)
        }).fail(function (jqXHR, textStatus) {
            $("#result4").text("失敗")
        })
    });

    const sse = new EventSource('api/tasks/greeting');

    sse.onmessage = function (event) {
        console.log(event.data);
        // ここで受け取ったデータを処理します。
        $("#greeting").text(event.data);
    };

    sse.onerror = function (error) {
        console.log('SSE error:', error);
        sse.close();
    };

});
