<%@ page import="org.gnk.selectintrigue.Plot"%>
<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>

<meta name="layout" content="main">
<g:set var="entityName"
	value="${plotInstance.name}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-datetimepicker.min.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css', file: 'redactIntrigue.css')}" type="text/css">
</head>
<body>
<g:javascript src="redactIntrigue/bootstrap-confirmation.js"/>
<g:javascript src="redactIntrigue/bootstrap-datetimepicker.min.js"/>
<g:javascript src="redactIntrigue/bootstrap-datetimepicker.fr.js"/>
<g:javascript src="redactIntrigue/redactIntrigue.js"/>
<g:javascript src="redactIntrigue/role.js"/>
<g:javascript src="redactIntrigue/event.js"/>
<g:javascript src="redactIntrigue/pastScene.js"/>
<g:javascript src="redactIntrigue/genericPlace.js"/>
<g:javascript src="redactIntrigue/genericResource.js"/>
<g:javascript src="redactIntrigue/relation.js"/>
	<div id="edit-plot" class="content scaffold-list">
		<h1>
			<g:message code="default.edit.label" args="[entityName]" />
		</h1>
        <g:render template="testMenuSlide" model="['right': right]" />
	</div>
</body>
</html>
