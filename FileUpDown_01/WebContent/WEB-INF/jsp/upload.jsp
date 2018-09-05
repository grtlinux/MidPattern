<%--
  Created by IntelliJ IDEA.
  User: kangmac
  Date: 2018. 5. 14.
  Time: AM 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.FileItem"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <title>Insert title here</title>
</head>
<body>
<%
    System.out.println("############## upload.jsp [START] ##############");

    boolean isMultipart = ServletFileUpload.isMultipartContent(request);

    if (isMultipart) {
		String baseDir = "/Users/kangmac/VirtualBox VMs/_VB_SHARE" + "/tomcat7";
        File dir = new File(baseDir + "/tmp/");
        File realDir = new File(baseDir + "/upload/");

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(100 * 1024 * 1024);   // 100 MB
        factory.setRepository(dir);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(15 * 1024 * 1024 * 1024);   // 15 GB
        List<FileItem> items = upload.parseRequest(request);

        System.out.println(">>>>> upload.jsp [REQUEST OK]");

        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem fileItem = iter.next();

            if (fileItem.isFormField()) {
                // Parameter
                System.out.println(">>>>> Parameter: " + fileItem.getFieldName() + "=" + fileItem.getString("euc-kr"));
            } else {
                if (fileItem.getSize() > 0) {
                    String fieldName = fileItem.getFieldName();
                    String fileName = fileItem.getName();
                    String contentType = fileItem.getContentType();
                    boolean isInMemory = fileItem.isInMemory();
                    long sizeInBytes = fileItem.getSize();

                    // check fileName
                    int idx;
                    if ((idx = fileName.lastIndexOf('\\')) < 0)
                    	idx = fileName.lastIndexOf('/');
                    if (idx >= 0)
                    	fileName = fileName.substring(idx + 1);

                    System.out.println(">>>>> FILE [fieldName] : " + fieldName);
                    System.out.println(">>>>> FILE [fileName] : " + fileName);
                    System.out.println(">>>>> FILE [contentType] : " + contentType);
                    System.out.println(">>>>> FILE [isInMemory] : " + isInMemory);
                    System.out.println(">>>>> FILE [sizeInBytes] : " + sizeInBytes);

                    try {
                        File uploadedFile = new File(realDir, fileName);
                        fileItem.write(uploadedFile);
                        fileItem.delete();
                    } catch(IOException ex) {}
                }
            }
        }
    } else {
        System.out.println("ERROR: It's not the type 'multipart/form-data'.");
    }

    System.out.println("############## upload.jsp [END] ##############");
%>

<hr>
<a href="/form.html">FILEUPLOAD</a>

</body>
</html>
