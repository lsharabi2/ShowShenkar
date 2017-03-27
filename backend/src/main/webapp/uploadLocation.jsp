<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>


<html>
    <head>
        <title>Upload Test</title>
    </head>
    <body>
        <form action="<%= blobstoreService.createUploadUrl("/uploadLocaition") %>" method="post" enctype="multipart/form-data">
            <label>id</label><input type="text" name="id"><br><br>
            <label>lat</label><input type="text" name="lat"><br><br>
            <label>lng</label><input type="text" name="lng"><br><br>
            <label>description</label><input type="text" name="description"><br><br>
            <label>myFile</label><input type="file" name="myFile"><br><br>
            <input type="submit" value="Submit">
        </form>
    </body>
</html>


