<%@ page import="java.util.Map" %>
<%@ page import="java.util.WeakHashMap" %>
<jsp:useBean id="loginedUser" scope="session" type="entity.UserData"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <jsp:include page="../index.jsp"/>
    <div class="autoz">
        <h3>${loginedUser.login}</h3>
        <p><b>Роль доступа</b></p>
        <%
            final Map<String, String> accessRoles = new WeakHashMap<>();
            accessRoles.put("admin","Администратор");accessRoles.put("manager","Менеджер");accessRoles.put("user","Пользователь");
            final String[] beRoles  = {"wasd","wcnt","wdvs","wgsm","wkvk","wmgf","wsbr","wurl","wvlg"};
            for (Map.Entry<String, String> item : accessRoles.entrySet()) {
                if (loginedUser.getRole().contains(item.getKey())) {
                    out.println("<ul><li>" + item.getValue() + "</li></ul>");
                    break;
                }
            }
            out.println("<p><b>Текущие роли БЕ:</b></p><ul>");
            for (String role : beRoles) {
                if (loginedUser.getRole().contains(role)) {
                    out.println("<li>" + role + "</li>");
                }
            }
            out.println("</ul>");
            //UserData changeUser = loginedUser;
        %>
    </div>
    <footer class="footer">
        <div class="cont">
            <span class="foottxt">© ЦК Роботизации 2021</span>
        </div>
    </footer>
    <script type="text/javascript" src="${pageContext.request.contextPath}/res/js/jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/res/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/res/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/res/js/index.js"></script>
</body>
</html>
