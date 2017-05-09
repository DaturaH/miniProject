<%@ page contentType="text/html;charset=utf-8" %> 
<!DOCTYPE html>
<html lang="zh-CN">
 <head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
</head> 
<body>

<form name="lform" action="./user/test.do" method="post"  >
  <p>First name: <input type="text" name="a" /></p>
  <p>Last name: <input type="text" name="b" /></p>
  <input type="submit" value="提交" />
</form>

<form method="POST" action="./house/uploadPics.do" enctype="multipart/form-data">
        File to Upload: <input type="files" name="files"><br /> 
        File to Upload: <input type="files" name="files"><br /> 
         File to Upload: <input type="file" name="files"><br /> 
        File to Upload: <input type="file" name="files"><br /> 
         File to Upload: <input type="file" name="files"><br /> 
        File to Upload: <input type="file" name="files"><br /> 
        Name: <input type="text" name="houseId" value="302fb8f0-15bf-4f4a-9f41-26e2cae5ed2e"><br /> <br /> 
        <input type="submit" value="Upload">
 </form>

</body>
</html>
