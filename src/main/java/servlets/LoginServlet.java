package servlets;

import entity.UserData;
import model.ModelUserData;
import utils.SecurityUtils;
import utils.appUtils;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userName = request.getParameter("login");
        String NewUserName = request.getParameter("login1");
        String pass = request.getParameter("pass");
        String newpass = request.getParameter("nopass");

        if (newpass != null) {

            if (NewUserName == null || NewUserName.trim().length()==0) {

                request.setAttribute("checkLogin", "Логин нового пользователя не содержит символов!");

                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

                dispatcher.forward(request, response);
                return;

            }else{

                if (CyrillCheck(NewUserName)) {

                    request.setAttribute("checkLogin", "Логин нового пользователя содержит кириллические символы!");

                    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

                    dispatcher.forward(request, response);
                    return;
                }

            }

            ModelUserData mdl = null;
            try {
                mdl = new ModelUserData();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                assert mdl != null;
                if (mdl.checkLogin(NewUserName,true)) {

                    if (mdl.checkLogin(NewUserName,false)) {

                        request.setAttribute("checkLogin", "Пользователь уже зарегистрирован! Используйте ранее присланный пароль.");

                        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

                        dispatcher.forward(request, response);
                        return;

                    }else{

                        String mail = mdl.addUser(NewUserName,"user");

                        request.setAttribute("checkLogin", "Пароль выслан на почту " + mail);

                        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

                        dispatcher.forward(request, response);
                        return;
                    }
                }else{

                    request.setAttribute("checkLogin", "Пользователь не зарегистрирован в системе Пикты.");

                    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

                    dispatcher.forward(request, response);
                    return;
                }
            } catch (SQLException | MessagingException throwables) {
                throwables.printStackTrace();
            }

        }

        if (userName == null || userName.trim().length()==0) {

            request.setAttribute("checkLogin", "Логин не содержит символов!");

            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

            dispatcher.forward(request, response);
            return;

        }else{

            if (CyrillCheck(userName)) {

                request.setAttribute("checkLogin", "Логин содержит кириллические символы!");

                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

                dispatcher.forward(request, response);
                return;
            }

        }

        if (pass == null || pass.trim().length()==0) {

            request.setAttribute("checkLogin", "Пароль не содержит символов!");

            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

            dispatcher.forward(request, response);
            return;

        }else{

            if (CyrillCheck(pass)) {

                request.setAttribute("checkLogin", "Пароль содержит кириллические символы!");

                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

                dispatcher.forward(request, response);
                return;
            }
        }

        ModelUserData mdl = null;
        try {
            mdl = new ModelUserData();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        UserData userAccount = null;
        try {
            assert mdl != null;
            userAccount = mdl.setUserData(userName,pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (userAccount == null) {

            request.setAttribute("checkLoginPass", "Неверный логин или пароль");

            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

            dispatcher.forward(request, response);
            return;
        }else {
            if (userAccount.getActive() != 1) {

                request.setAttribute("checkLoginPass", "Пользователь заблокирован!");

                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");

                dispatcher.forward(request, response);
                return;
            }
        }

        appUtils.storeLoginedUser(request.getSession(), userAccount);

        int redirectId = -1;
        try {
            redirectId = Integer.parseInt(request.getParameter("redirectId"));
        } catch (Exception e) {
        }
        String requestUri = appUtils.getRedirectAfterLoginUrl(request.getSession(), redirectId);
        if (requestUri != null) {
            response.sendRedirect(requestUri);
        } else {
            // По умолчанию после успешного входа в систему
            // перенаправить на страницу /userInfo
            response.sendRedirect(request.getContextPath() + "/userInfo");
        }

    }

    private boolean CyrillCheck(String string)  {

        for(int i = 0; i < string.length(); i++) {
            if(Character.UnicodeBlock.of(string.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return true;
            }
        }
        return false;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        RequestDispatcher reqD = request.getRequestDispatcher("pages/login.jsp");
        //RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/login.jsp");
        reqD.forward(request, response);
    }
}
