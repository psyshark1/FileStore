package filter;

import entity.UserData;
import servlets.UserRoleRequestWrapper;
import utils.SecurityUtils;
import utils.appUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@javax.servlet.annotation.WebFilter(filterName = "WebFilter", urlPatterns = {"/*"})
public class WebFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String servletPath = request.getServletPath();
        // Информация пользователя сохранена в Session
        // (После успешного входа в систему).
        UserData loginedUser;
        if (!servletPath.equals("/index.jsp")) {
            loginedUser = appUtils.getLoginedUser(request.getSession());
        }else{
            loginedUser = null;
        }
        HttpServletRequest wrapRequest = request;
        if (loginedUser != null) {

            if (servletPath.equals("/login")) {
                response.sendRedirect(request.getContextPath() + "/userInfo");
                return;
            }

            // User Name
            String userName = loginedUser.getLogin();

            // Роли (Role).
            List<String> roles = loginedUser.getRole();

            // Старый пакет request с помощью нового Request с информацией userName и Roles.
            wrapRequest = new UserRoleRequestWrapper(userName, roles, request);
        }

        // Страницы требующие входа в систему.
        if (SecurityUtils.isSecurityPage(request)) {

            // Если пользователь еще не вошел в систему,
            // Redirect (перенаправить) к странице логина.
            if (loginedUser == null) {

                String requestUri = request.getRequestURI();

                // Сохранить текущую страницу для перенаправления (redirect) после успешного входа в систему.
                int redirectId = appUtils.storeRedirectAfterLoginUrl(request.getSession(false), requestUri);

                response.sendRedirect(wrapRequest.getContextPath() + "/login?redirectId=" + redirectId);
                return;
            }

            // Проверить пользователь имеет действительную роль или нет?
            boolean hasPermission = SecurityUtils.hasPermission(wrapRequest);
            if (!hasPermission) {

                RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/pages/Denied.jsp");

                dispatcher.forward(request, response);
                return;
            }
        }

        chain.doFilter(wrapRequest, response);
    }

    public void init(FilterConfig config) {

    }

}
