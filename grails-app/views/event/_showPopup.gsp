<div class="eventPopup">

<h2>${eventInstance.title}</h2>

    <form action="update" name="updateForm">
    <g:hiddenField name="id" value="${eventInstance?.id}" />
        Title:<g:textField name="title" value="${eventInstance?.title }"/> <br/>
        Description:<g:textField name="description" value="${eventInstance?.description}" /><br/>
        AllDay:<g:checkBox name="allDay" value="${eventInstance.allDay}" /> <br/>
        StartDate:<g:datePicker name="startDate" precision="day"  value="${eventInstance?.startDate}"  /> <br/>
        EndDate:<g:datePicker name="endDate" precision="day"  value="${eventInstance?.endDate}"  /> <br/>
         <g:submitToRemote  value="Update" action='updateWithJson' onSuccess="alert('update successfully')"></g:submitToRemote>
    <input type="button" value="Delete" onclick="cancelEvent('${eventInstance.id}')"/>

    </form>
    <p>
        <g:link action="list"  >More details »</g:link>


    </p>
</div>