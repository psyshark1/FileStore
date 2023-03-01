<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <jsp:include page="../index.jsp"/>
    <div class="autoz">
        <div class="autoz-form">
            <p>Получить акты</p>
            <form method="post">
                <div class="mb-3">
                    <label for="InputBe" class="form-label">Балансовая единица:</label>
                    <select id="InputBe" class="form-select" name="be">
                    <option disabled selected value="0">Выберите БЕ</option>
                    <option value="WASD">WASD</option>
                    <option value="WCNT">WCNT</option>
                    <option value="WDVS">WDVS</option>
                    <option value="WGSM">WGSM</option>
                    <option value="WKVK">WKVK</option>
                    <option value="WMGF">WMGF</option>
                    <option value="WSBR">WSBR</option>
                    <option value="WURL">WURL</option>
                    <option value="WVLG">WVLG</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="InputNumber" class="form-label">Номер контрагента:</label>
                    <input id="InputNumber" class="form-control" type="text" name="number" aria-describedby="numberHelp">
                    <div id="numberHelp" class="form-text">если поле пусто, то выборка актов проводится по всей БЕ</div>
                </div>
                <div class="mb-3">
                <label for="InputFrom" class="form-label">Дата С:</label>
                    <input id="InputFrom" class="form-control" type="text" name="from" autocomplete="off">
                </div>
                <div class="mb-3">
                <label for="InputTo" class="form-label">Дата По:</label>
                    <input id="InputTo" class="form-control" type="text" name="to" autocomplete="off">
                </div>
                <button type="submit" class="btn btn-success">Submit</button>
            </form>
        </div>
        <%
            if (request.getAttribute("err") != null){
                out.println("<div class=\"form-label\"><p>"+ request.getAttribute("err") +"</p></div>");
            }
        %>
    </div>
    <footer class="footer">
        <div class="cont">
            <span class="foottxt">© ЦК Роботизации 2021</span>
        </div>
    </footer>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/jquery-ui.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/res/js/jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/res/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/res/js/index.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/res/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/res/js/bootstrap.bundle.min.js"></script>

</body>
</html>
