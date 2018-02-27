<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>tagR - Turbine front end</h1>
        
        <br><br>
        <%
        java.util.Properties prop = new java.util.Properties();
        prop.load(this.getServletConfig().getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF"));
        for (String name: prop.stringPropertyNames() )
        {%>
          <%=name%> value=<%=prop.getProperty(name)%>
          <br>
        <%
        }
        %>
    </body>
</html>
