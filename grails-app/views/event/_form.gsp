<%@ page import="co.bugu.Event" %>



<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="event.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${eventInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'allDay', 'error')} ">
	<label for="allDay">
		<g:message code="event.allDay.label" default="All Day" />
		
	</label>
	<g:checkBox name="allDay" value="${eventInstance?.allDay}" />
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'startDate', 'error')} required">
	<label for="startDate">
		<g:message code="event.startDate.label" default="Start Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="startDate" precision="day"  value="${eventInstance?.startDate}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'endDate', 'error')} required">
	<label for="endDate">
		<g:message code="event.endDate.label" default="End Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="endDate" precision="day"  value="${eventInstance?.endDate}"  />
</div>


<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="event.title.label" default="Title" />
		
	</label>
	<g:textField name="title" value="${eventInstance?.title}"/>
</div>

