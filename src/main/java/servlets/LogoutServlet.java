package servlets;

import entity.UserData;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LogoutServlet")
public class LogoutServlet extends HttpServlet {

    UserData user = UserData.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        request.getSession().invalidate();

        user.destroy();

        RequestDispatcher reqD = request.getRequestDispatcher("pages/logout.jsp");
        reqD.forward(request, response);
        // Redirect to Home Page.
        //response.sendRedirect(request.getContextPath() + "/");
    }
}
