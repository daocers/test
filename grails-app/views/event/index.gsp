<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 13-6-20
  Time: 上午11:16
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="calendar">
  <title>MyCalendar</title>

    <g:javascript>
        $(function(){
            $('#addPopup').hide()
        })
        function addEventSucceed(){


            alert('Event has been saved!')
            $('#addPopup').hide()

            $('#calendar').text("")
            renderCalendar()


        }
    </g:javascript>
</head>
<body>
    This is my web calendar.

    <div id="addPopup" >
         <form id="addForm" action="save">
            Title: <g:textField name="title" value="${eventInstance?.title}" />  <br/>
            Description:<g:textField name="description" value="${eventInstance?.description}" /> <br/>
            AllDay:<g:checkBox name="allDay" value="${eventInstance?.allDay}" />   <br/>
            StartDate:<g:datePicker name="startDate" precision="day"  value="${eventInstance?.startDate}"  />  <br/>
            EndDate:<g:datePicker name="endDate" precision="day"  value="${eventInstance?.endDate}"  />    <br/>
            <g:submitToRemote action="save" value="Create" update="[success:'message', failure: 'error']" onSuccess="addEventSucceed()"></g:submitToRemote>


        </form>

    </div>

</body>
</html>