<%@ page import="entity.UserData" %>
<%@ page contentType="text/html;charset=UTF-8" session="false" language="java" %>
<html>
  <head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/bootstrap.min.css" />
      <%%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/css/index.css" />
    <title><%
      switch (request.getServletPath()) {
        case "/index.jsp":
          out.println("Акты сверки по ФДФО 2.25");
          break;
        case "/pages/login.jsp":
          out.println("Вход");
          break;
        case "/pages/forAdmin.jsp":
          out.println("Администратор");
          break;
        case "/pages/forManager.jsp":
          out.println("Менеджер");
          break;
        case "/pages/getActs.jsp":
          out.println("Получить акты");
          break;
        case "/pages/userInfo.jsp":
          out.println("Информация пользователя");
          break;
        case "/pages/logout.jsp":
          out.println("Выйти");
          break;
        case "/pages/newPass.jsp":
          out.println("Сброс пароля");
          break;
      }%></title>
  </head>
  <body class="bd">
  <div class="overlay"></div>
  <nav class="navbar navbar-expand-lg fixed-top ">

  <div class="collapse navbar-collapse " id="navbarSupportedContent">
    <ul class="navbar-nav mr-4">
    <% UserData loginedUser = (UserData) request.getSession().getAttribute("loginedUser");
       if (loginedUser != null) {
         if (loginedUser.getRole().contains("admin")) {
           out.println("<li class=\"nav-item\"><a class=\"nav-link\" href=\""+ request.getContextPath() +"/forAdmin\">" +
            "Администратор</a></li>");
         }else{
           out.println("<li class=\"nav-item\"><a class=\"nav-link\" href=\""+ request.getContextPath() +"/forManager\">" +
           "Менеджер</a></li>");
         }
         out.println("<li class=\"nav-item\">" +
          "<a class=\"nav-link\" href=\""+ request.getContextPath() +"/getActs\">" +
          "Получить акты</a></li>" +
          "<li class=\"nav-item\">" +
          "<a class=\"nav-link\" href=\""+ request.getContextPath() +"/userInfo\">" +
          "Информация пользователя</a></li>" +
                 "<li class=\"nav-item\">" +
                 "<a class=\"nav-link\" href=\""+ request.getContextPath() + "/logout\">" +
                 "Выйти" +
                 "</a></li>"+ System.lineSeparator() +
         "<span class=\"usr-info\" style=\"color:#A500BF\">["+ loginedUser.getLogin() +"]</span>");
       }else{
         out.println("<li class=\"nav-item\">" +
          "<a class=\"nav-link\" href=\""+ request.getContextPath() +"/login\">" +
           "Войти" +
            "</a></li>");
       }
    %>
      </ul>
    </div>
  </nav>
  <%
    if(request.getServletPath().equals("/index.jsp")){
      out.println("<footer class=\"footer\">\n<div class=\"cont\">\n<span class=\"foottxt\">© ЦК Роботизации 2021</span>\n</div>\n</footer>\n"+
              "<script type=\"text/javascript\" src=\""+ request.getContextPath() +"/res/js/jquery.js\"></script>\n" +
              "<script type=\"text/javascript\" src=\""+ request.getContextPath() +"/res/js/bootstrap.min.js\"></script>\n" +
              "<script type=\"text/javascript\" src=\""+ request.getContextPath() +"/res/js/bootstrap.bundle.min.js\"></script>\n" +
              "</body>\n</html>");
    }
  %>
