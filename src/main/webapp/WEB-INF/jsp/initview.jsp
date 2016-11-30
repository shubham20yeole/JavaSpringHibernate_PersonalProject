<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
<title>Upload Multiple File Request Page</title>
</head>
<body>

<div style="margin: auto; text-align: center;">
<a href="initmethod">Start Over</a>
<br><br>
	<form method="POST" action="uploadImage" enctype="multipart/form-data">
		<input type="file" name="file"><br><br>
 
		<input type="text" name="name" value="shubhamYeole"><br><br>
 
 

		<input type="submit" value="Upload">
	</form>
	</div>
</body>
</html>