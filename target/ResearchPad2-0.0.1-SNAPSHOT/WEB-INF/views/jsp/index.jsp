<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=iso-8859-1">
  	<meta http-equiv="X-UA-Compatible" content="IE=edge">
  	<meta name="description">
  	<meta name="keywords">
  	<title>research</title>
</head>
<body>
<H1>Header tablet</H1>

<div>
<form:form action="save" commandName="jerre" method="POST">

<form:label id="firstName" path=""> First name: </form:label>
<form:input path="firstName" size="20" maxlength="20" />

<form:label id="lastName" path=""> Last name: </form:label>
<form:input path="lastName" size="20" maxlength="20" />

<input name="btnSubmit"  type="submit" value="Post" />

</form:form>

</div>
</body>
</html>