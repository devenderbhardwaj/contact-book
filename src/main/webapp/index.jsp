<%@page import="entities.User,jakarta.servlet.RequestDispatcher" %>
<% 
    User user = (User) session.getAttribute("user");
    {
        if (user==null) {
            response.sendRedirect("/contacts/login.jsp");
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contacts</title>
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    <script type="module" src="/contacts/index.js"></script>
    <link rel="stylesheet" href="/contacts/style.css">
</head>
<body>
    <div id="root"></div>
</body>
</html>