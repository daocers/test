<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 13-6-25
  Time: 下午2:54
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Login page</title>
    <meta name="layout" content="calendar"/>
</head>
<body>
    <g:form action="loginCheck">
        <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'userName', 'error')} required">
            <label for="userName">
                <g:message code="user.userName.label" default="User Name" />
                <span class="required-indicator">*</span>     <br/>
            </label>
            <g:textField name="userName" required="" value="${userInstance?.userName}"/>
        </div>

        <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
            <label for="password">
                <g:message code="user.password.label" default="Password" />
                <span class="required-indicator">*</span>     <br/>
            </label>
            <g:field type="password" name="password" required="" value="${userInstance?.password}"/>
        </div>

        <g:submitButton name="Login" value='loginCheck'/>
        <g:submitButton name="submit" value='register' />
    </g:form>

</body>
</html>