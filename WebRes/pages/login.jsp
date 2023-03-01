<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <jsp:include page="../index.jsp"></jsp:include>
    <div class="autoz">
        <div class="autoz-form">
            <form method="post">
                <div class="mb-3">
                <label for="InputLogin" class="form-label">Логин></label>
                    <input id="InputLogin" class="form-control" type="text" name="login">
                </div>
                <div class="mb-3">
                <label for="InputPass" class="form-label">Пароль></label>
                    <input id="InputPass" class="form-control" type="password" name="pass" aria-describedby="passHelp">
                    <div id="passHelp" class="form-text"><a href="${pageContext.request.contextPath}/newPass">Забыли пароль?</a></div>
                </div>
                <div class="col-12">
                <button type="submit" class="btn btn-success">Войти</button>
                </div>
            </form>
        </div>
        <div class="autoz-form">
            <p><u>Новый пользователь</u></p>
            <form method="post">
                <div class="mb-3">
                <label for="InputEmail1" class="form-label">Укажите логин нового пользователя></label>
                    <input id="InputEmail1" class="form-control" type="text" name="login1"><input type="checkbox" name="nopass" hidden="true" checked>
                </div>
                <div class="col-12">
                <button type="submit" class="btn btn-success">Создать</button>
                </div>
            </form>
        </div>

        <%
            if (request.getAttribute("checkLogin") != null) {
                out.println("<div class=\"form-label\"><p>" + request.getAttribute("checkLogin") + "</p></div>");
            }
            if (request.getAttribute("checkLoginPass") != null) {
                out.println("<div class=\"form-label\"><p>" + request.getAttribute("checkLoginPass") + "</p></div>");
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
