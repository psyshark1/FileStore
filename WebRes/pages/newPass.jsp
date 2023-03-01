<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../index.jsp"></jsp:include>
<div class="autoz">
    <div class="autoz-form">
        <form method="post">
            <div class="mb-3">
                <label for="InputLogin" class="form-label">Введите логин></label>
                <input id="InputLogin" class="form-control" type="text" name="login">
            </div>
            <div class="col-12">
                <button type="submit" class="btn btn-success">Сбросить</button>
            </div>
        </form>
    </div>
    <%
        if (request.getAttribute("mess") != null) {
            out.println("<div class=\"form-label\"><p>" + request.getAttribute("mess") + "</p></div>");
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
