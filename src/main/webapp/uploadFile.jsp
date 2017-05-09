<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<!-- 
<form action="./agency/departcontroller/uploadPic.do" method="post" enctype="multipart/form-data">  
-->
<form action="http://10.240.34.21:8080/miniProject/personalcenter/uploadportrait.do" method="post" enctype="multipart/form-data"> 
<input type="text" name="id"/>
<input type="text" name="name"/>
<input type="text" name="size"/>
<input type="text" name="rent"/>
<input type="file" name="file" /> <input type="submit" value="Submit" /></form>
</body>
</html>