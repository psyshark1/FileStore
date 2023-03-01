package servlets;

import entity.UserData;
import model.ModelUserData;
import utils.appUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet(name = "forManagerServlet")
public class forManagerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userName = request.getParameter("login");
        String[] roles = request.getParameterValues("roles");
        String accessRole = request.getParameter("accessRole");
        String block = request.getParameter("block");
        String findUser = request.getParameter("findUser");

        if (userName == null || userName.trim().length() == 0) {

            errors(request,response,"Логин не содержит символов!");
            return;

        }

        ModelUserData mdl = null;
        try {
            mdl = new ModelUserData();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (accessRole == null || findUser != null) {

            try {
                assert mdl != null;
                if (mdl.checkLogin(userName, false)) {
                    UserData changeUser = mdl.setUserData(userName);

                    request.setAttribute("changeUser",changeUser);

                    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/forManager.jsp");

                    dispatcher.forward(request, response);
                    return;

                }

                errors(request,response,"Логин не найден!");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            //doGet(request, response);
        }

        try {
            assert mdl != null;
            UserData changeUser = new UserData();

            List<String> roles1 = changeUser.getRole();

            if (roles1.contains("admin")){
                request.setAttribute("changeUser",changeUser);
                errors(request,response,"Невозможно изменить ролевую модель Администратора!");
                return;
            }

            //changeUser.destroy();

            mdl.updateUser(userName,accessRole,roles,block, appUtils.getLoginedUser(request.getSession()).getLogin());

            changeUser.setLogin(userName);
            assert accessRole != null;
            StringBuilder chRoles = new StringBuilder(accessRole);

            for(String role : roles){
                chRoles.append(",").append(role);
            }
            changeUser.setRole(chRoles.toString());

            if (block!=null) {
                if (block.equals("on")) {
                    changeUser.setActive(0);
                } else {
                    changeUser.setActive(1);
                }
            }else{
                changeUser.setActive(1);
            }

            request.setAttribute("changeUser",changeUser);
            request.setAttribute("message","Изменения сохранены");

            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/forManager.jsp");

            dispatcher.forward(request, response);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void errors(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {

        request.setAttribute("checkLogin", errorMessage);
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/forManager.jsp");
        dispatcher.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        RequestDispatcher reqD = request.getRequestDispatcher("pages/forManager.jsp");
        reqD.forward(request, response);
    }
}