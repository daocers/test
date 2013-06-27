<%@ page import="co.bugu.User" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="calendar">
	</head>
	<body>
		<div id="create-user" class="content scaffold-create" role="main">
			<h3>Register</h3>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${userInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${userInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form action="regist" >
                <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'userName', 'error')} required">
                    <label for="userName">
                        <g:message code="user.userName.label" default="User Name" />
                        <span class="required-indicator">*</span>
                    </label>
                    <g:textField name="userName" required="" value="${userInstance?.userName}"/>
                </div>

                <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
                    <label for="password">
                        <g:message code="user.password.label" default="Password" />
                        <span class="required-indicator">*</span>
                    </label>
                    <g:field type="password" name="password" required="" value="${userInstance?.password}"/>
                </div>

                <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
                    <label for="passwordAgain">
                        <g:message code="user.password.label" default="PasswordAgain" />
                        <span class="required-indicator">*</span>
                    </label>
                    <g:field type="password" name="passwordAgain" required="" value="${cmd?.passwordAgain}"/>
                </div>

				<fieldset class="buttons">
					<g:submitButton name="Regist" class="register" value="regist"  />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
