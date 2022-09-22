package servlets;

import entity.UserData;
import model.ModelUserData;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "NewPassServlet")
public class NewPassServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userName = request.getParameter("login");

        if (userName == null || userName.trim().length()==0) {

            setMessage(request,response, "Логин не содержит символов!");

            return;

        }else{

            if (CyrillCheck(userName)) {

                setMessage(request,response, "Логин содержит кириллические символы!");
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
            if (mdl.checkLogin(userName,true)) {

                if (mdl.checkLogin(userName,false)) {

                    UserData userAccount = mdl.resetPass(userName);
                    setMessage(request,response,"Пароль выслан на почту " + userAccount.getMail());

                }else{

                    setMessage(request,response,"Пользователь не проходил процедуру регистрации! Воспользуйтесь формой создания нового пользователя.");

                }
            }else{

                setMessage(request,response,"Пользователь не зарегистрирован в системе Пикты.");

                return;
            }
        } catch (SQLException | MessagingException throwables) {
            throwables.printStackTrace();
        }

        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        RequestDispatcher reqD = request.getRequestDispatcher("pages/newPass.jsp");
        reqD.forward(request, response);
        // Redirect to Home Page.
        //response.sendRedirect(request.getContextPath() + "/");
    }

    private void setMessage(HttpServletRequest request, HttpServletResponse response, String mess) throws ServletException, IOException {

        request.setAttribute("mess", mess);
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/newPass.jsp");
        dispatcher.forward(request, response);

    }

    private boolean CyrillCheck(String string)  {

        for(int i = 0; i < string.length(); i++) {
            if(Character.UnicodeBlock.of(string.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return true;
            }
        }
        return false;
    }
}
