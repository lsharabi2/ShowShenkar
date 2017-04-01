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
        <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
            <label>id</label><input type="text" name="id"><br><br>
            <label>type</label><input type="text" name="type"><br><br>
            <label>video url</label><input type="text" name="url"><br><br>
            <label>myFile</label><input type="file" name="myFile"><br><br>
            <input type="submit" value="Submit">
        </form>
    </body>
</html>


