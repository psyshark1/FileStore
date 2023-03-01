<%@ page import="java.util.Map" %>
<%@ page import="entity.UserData" %>
<%@ page import="java.lang.ref.PhantomReference" %>
<%@ page import="java.lang.ref.ReferenceQueue" %>
<%@ page import="java.util.WeakHashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <jsp:include page="../index.jsp"/>
    <div class="autoz">
        <%
            UserData changeUser = (UserData) request.getAttribute("changeUser");
            if (changeUser == null) {
                out.println("<div class=\"autoz-form\"><form method=\"post\">" +
                        "<div class=\"mb-3\"><label for=\"InputLogin\" class=\"form-label\">Логин пользователя</label>" +
                        "<input id=\"InputLogin\" class=\"form-control\" type=\"text\" name=\"login\"></div><button type=\"submit\" class=\"btn btn-success\">Найти</button></form></div>");

            }else {

                out.println("<div class=\"autoz-form\"><form method=\"post\">" +
                            "<div class=\"mb-3\"><label for=\"InputLogin\" class=\"form-label\">Логин пользователя</label>" +
                            "<input id=\"InputLogin\" class=\"form-control\" type=\"text\" name=\"login\" value=\"" + changeUser.getLogin() + "\"></div>" +
                        "<input type=\"checkbox\" name=\"findUser\" hidden=\"true\" checked>" +
                        "<button type=\"submit\" class=\"btn btn-success\">Найти</button></form></div>");

                if (changeUser.getLogin() != null) {

                    final Map<String, String> accessRoles = new WeakHashMap<>();
                    accessRoles.put("admin","Администратор");accessRoles.put("manager","Менеджер");accessRoles.put("user","Пользователь");
                    //final String[] accessRoles  = {"admin","manager","user"};
                    final String[] beRoles  = {"wasd","wcnt","wdvs","wgsm","wkvk","wmgf","wsbr","wurl","wvlg"};

                    out.println("<div class=\"autoz-form\"><form method=\"post\"><b>Доступные роли:</b><input type=\"text\" name=\"login\" hidden=\"true\" value=\"" + changeUser.getLogin() + "\">");
                    for (Map.Entry<String, String> item : accessRoles.entrySet()) {
                        if (!changeUser.getRole().contains(item.getKey())) {
                            out.println("<div class=\"mb-3\"><div class=\"form-check\"><input class=\"form-check-input\" type=\"radio\" id=\"" +
                                    item.getKey() + "\" name=\"accessRole\" value=\"" + item.getKey() + "\">" +
                                    "<label class=\"form-check-label\" for=\"" + item.getKey() + "\">" + item.getValue() + "</label></div></div>");
                        }else {
                            out.println("<div class=\"mb-3\"><div class=\"form-check\"><input class=\"form-check-input\" type=\"radio\" id=\"" +
                                    item.getKey() + "\" name=\"accessRole\" value=\"" + item.getKey() + "\" checked>" +
                                    "<label class=\"form-check-label\" for=\"" + item.getKey() + "\">" + item.getValue() + "</label></div></div>");
                        }
                    }
                    out.println("<b>Текущие роли БЕ:</b>");
                    for (String role : beRoles) {
                        out.println("<div class=\"mb-3\"><div class=\"form-check\"><input class=\"form-check-input\" name=\"roles\" type=\"checkbox\"");

                        //if (changeUser.getRole() != null) {
                            if (changeUser.getRole().contains(role)) {
                                out.println("value=\"" + role + "\" checked>" + role + "</div></div>");
                            } else {
                                out.println("value=\"" + role + "\">" + role + "</div></div>");
                            }
                        //} else {
                        //    out.println(">" + role + "<br>");
                        //}
                    }
                    out.println("<b>Статус пользователя:</b>");
                    if (changeUser.getActive() == 1){
                        out.println("<div class=\"mb-3\"><div class=\"form-check\"><input class=\"form-check-input\" name=\"block\" type=\"checkbox\">Пользователь заблокирован</div></div>");
                    }else{
                        out.println("<div class=\"mb-3\"><div class=\"form-check\"><input class=\"form-check-input\" name=\"block\" type=\"checkbox\" checked>Пользователь заблокирован</div></div>");
                    }

                    out.println("<button type=\"submit\" class=\"btn btn-success\">Сохранить</button></form></div>");
                }else {
                    out.println("<button type=\"submit\" class=\"btn btn-success\">Найти</button></form></div>");
                }
            }
            ReferenceQueue<UserData> ref = new ReferenceQueue<>();
            PhantomReference<UserData> PhRef = new PhantomReference<>(changeUser,ref);
            changeUser = null;
            PhRef.clear();
            if (request.getAttribute("checkLogin") != null) {
                out.println("<div class=\"form-label\"><p>" + request.getAttribute("checkLogin") + "</p></div>");
            }
            if (request.getAttribute("message") != null) {
                out.println("<div class=\"form-label\"><p>" + request.getAttribute("message") + "</p></div>");
            }
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
</body>
</html>
