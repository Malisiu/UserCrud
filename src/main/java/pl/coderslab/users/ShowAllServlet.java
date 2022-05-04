package pl.coderslab.users;

import pl.coderslab.utils.UserDao;
import pl.coderslab.utils.Users;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/list")
public class ShowAllServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Users> users = UserDao.readAllUsers();
        request.setAttribute("users",users);
        getServletContext().getRequestDispatcher("/users/list.jsp")
                .forward(request, response);
    }
}

