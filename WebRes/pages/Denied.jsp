<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <jsp:include page="../index.jsp"></jsp:include>
    <div class="autoz">
        <p>Отсутствуют разрешения на промотр данной страницы</p>
        <button class="btn btn-success" onclick="location.href='${pageContext.request.contextPath}/'">На главную</button>
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
