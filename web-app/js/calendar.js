$(document).ready(function() {
    renderCalendar();
//    setupDatePickers();
//    setupRecurOptions();
//    setupRecurSavePopups();
});

function renderCalendar() {
    $("#calendar").fullCalendar({
        events: function(start, end, callback){
            $.getJSON("listAsJson",
                {
                    start: start.getTime(),
                    end: end.getTime()
                } ,

                function(result){
                    for(var i = 0; i < result; i++){
//                        alert(result)
                        result[i].start = $.fullCalendar.parseISO8601(result[i].start, false);
                        result[i].end = $.fullCalendar.parseISO8601(result[i].end, false);

                    }
                    callback(result);
                }
            )
        }
        ,    //从list action中获取json类型的event数据
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
        },

        dayClick: function(date, allDay, jsEvent, view){
            var dateOfClick = date
            $("form[id='addForm'] > input[name]").val("")
            $("input[name='allDay']").removeAttr("value")
            $("input[name='startDate']").attr("value","date.struct")
            $("input[name='endDate']").attr("value","date.struct")
            $('select[name$="_day"]').val(dateOfClick.getDate())
            $('select[name$="_month"]').val(dateOfClick.getMonth() + 1)
            $('select[name$="_year"]').val(dateOfClick.getYear() + 1900)

            $('#addPopup').show()


        },
        eventRender: function(event, element) { //element从哪里找？
        //当描绘事件时触发     event为该事件，element为用来渲染该事件的div元素
            //event 企图被渲染的日程对象， element是一个新建的jquery<div>被用来渲染的，已经在恰当的时间被创建
//            alert(event.id)
            $(element).addClass(event.cssClass);

            var occurrenceStart = event.startDate;
            var occurrenceEnd = event.endDate;

//            var data = {id: event.id, occurrenceStart: occurrenceStart, occurrenceEnd: occurrenceEnd};
            var data = {id: event.id}
            $(element).qtip({   //点击事件时候使用qtip，ajax获取事件后显示到showPopup页面
                content: {
                    text: ' ',
                    ajax: {          //使用ajax获取远程信息作为内容
                        url: "show",
                        type: "GET",
                        data: data
                    }
                },
                show: {    //点击时候出现模态对话框
                    event: 'click',
                    solo: true
                },
                hide: {   //点击时候隐藏对话框
                    event: 'click'
                },
                style: {     //设置样式
                    width: '500px',
                    backgroudColor: '333',
                    widget: true
                },
                position: {   //设置位置
                    my: 'bottom middle',
                    at: 'top middle',
                    viewport: true
                }
            });
        },



        eventMouseover: function(event, jsEvent, view) {
            $(this).addClass("active");    //获取鼠标焦点时候添加样式

        },
        eventMouseout: function(event, jsEvent, view) {
           $(this).removeClass("active");    //失去鼠标焦点时候移除样式
        }
//        dayClick(dayDate, allDay, jsEvent, view){
//
//    }
    });
}

function cancelEvent(eventId){
    $.ajax({
        url: 'deleteWithJson',
        type:'GET',
        data:{id: eventId},
        dataType:'json',
        timeout: 1000,

        success: function(data){
            if(data.result == 'fail'){
                alert('error delete event in database')
            }else{
                alert('success')
                $('#calendar').fullCalendar('removeEvents', eventId);
            }
        },
        error: function(){
            alert('Error can not connect to server')
        }
    })
}

function ajaxDelete(eventId){
    $.getJSON("ajaxDelete", {id: eventId}, function(data){
        $('#message').text(data.msg);

        $("tr[id= ${eventId}]").remove()
    })
}

function onChange(){
    alert("aaa")
    var name = $(this).attr("name");
    alert(name);
}

function setupDatePickers() {
    $("input.datetime").datetimepicker({
        ampm: true,
        stepMinute: 15
    });
}


function setupRecurSavePopups() {
    $(".delete.recurring").live('click', function() {
        $("#deletePopup").dialog({
            title: "Delete recurring event",
            width: 400,
            modal: true
         });

        return false;
    });

    var editPopup = $("#editPopup").dialog({
        title: "Update recurring event",
        width: 400,
        modal: true,
        autoOpen: false
    });

    $(".save.recurring").click(function() {
        var editTypeField = $("#editType");

        if ($(editTypeField).val() == "") {
            $(editPopup).dialog('open');
            return false;
        }
        else {
            return true;
        }
    });

    $("#editPopup button").click(function() {
        $("#editType").val($(this).val());
        $(editPopup).dialog('close');
        $(".save.recurring").trigger('click');
    });

}


function setupRecurOptions() {

    $("#isRecurring").change(function() {
        if ($(this).is(":checked")) {
            showRecurPopup();
            $("#editRecurringLink").show();
        }
        else {
            $("#editRecurringLink").hide();
        }

        updateRecurDescriptions();
    });


    $("#editRecurringLink").click(function() {
        showRecurPopup();
    });

    $("#recurType").change(function() {

        if ($(this).val() == "WEEKLY") {
            $("#weeklyOptions").show();
        }
        else {
            $("#weeklyOptions").hide();
        }

        updateRecurDescriptions();
    });

    $("#recurInterval, input[name='recurDaysOfWeek']").change(function() {
        updateRecurDescriptions();
    });


    $("input[name='recurEndOption']").click(function() {
        if ($(this).val() == "never") {
            $("#recurUntil").val('');
            $("#recurCount").val('');
        }
        if ($(this).val() == "occurrences") {
            $("#recurUntil").val('');
        }
        else {
            $("#recurCount").val('');
        }
        updateRecurDescriptions();
    });

    $("#recurUntil, #recurCount").focusout(function() {
        // Make sure correct option is checked
        var checkboxId = + $(this).parent("label").attr("for");
        $("#" + checkboxId).attr("checked", true);

        updateRecurDescriptions();
    });

    $("#recurUntil").datetimepicker({
        ampm: true,
        onSelect: function(dateText, inst) {
            // Make sure correct option is checked
            var checkboxId = + $(this).parent("label").attr("for");
            $("#" + checkboxId).attr("checked", true);

            updateRecurDescriptions();
        }
    });

    updateRecurDescriptions();
}

function showRecurPopup() {

    var recurPopup = $("#recurPopup").dialog({
        title: 'Repeat',
        width: 400,
        modal: true,
        open: function(event, ui) {
          $("#recurOptions").show().appendTo("#recurPopup");
        },
        close: function(event, ui) {
          $("#recurOptions").hide().appendTo("form.main");
        },
        buttons: {
            Ok: function() {
                $( this ).dialog( "close" );
            }
        }
    });

}


function getRecurDescription() {
    var description = ' ';
    var recurType = $("#recurType option:selected").text();
    var recurUntil = $("#recurUntil").val();
    var recurCount = $("#recurCount").val();
    var recurInterval = $("#recurInterval").val();

    if ($("#isRecurring").is(":checked")) {

        if (recurInterval == 1) {
            description += recurType;
        }
        else {
            description += "Every " + recurInterval + " " + getRecurTypeUnit(recurType);
        }

        if (recurType == "Weekly") {
            description += " on ";
            $("input[name='recurDaysOfWeek']:checked").each(function() {
                description += " " + $(this).attr("title") + ",";
            });

            // Remove last comma
            description = description.replace(/,$/,'');
        }

        if (recurCount) {
            description += ", " + recurCount + " times" ;
        }
        else if (recurUntil) {
            description += ", until " + recurUntil;
        }

    }
    else {
        description = "..." ;
    }

    return description;
}

function getRecurTypeUnit(recurType) {
    var result = "";

    switch(recurType)
    {
        case "Daily":
            result = "days";
            break;
        case "Weekly":
            result = "weeks";
            break;
        case "Monthly":
            result = "months";
            break;
        case "Yearly":
            result = "years";
    }
    return result;
}


function updateRecurDescriptions() {
    var recurType = $("#recurType option:selected").text();

    var description = getRecurDescription();
    $("#recurDescription").html(description);
    $("#recurSummary").html(description);

    var repeatType = getRecurTypeUnit(recurType);
    $("#repeatLabel").html(repeatType);
}
