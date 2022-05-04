package pl.coderslab.users;

import pl.coderslab.utils.UserDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/user/edit")
public class EditServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        String[] info = UserDao.readUser(id);
        request.setAttribute("email", info[0]);
        request.setAttribute("username", info[1]);
        request.setAttribute("id",id);
        getServletContext().getRequestDispatcher("/users/edit.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        UserDao.update(email,username,password,id);
        response.sendRedirect("/user/list");


    }
}

/*
<div class="card-body">
    <table class="table">
        <tr>
            <td>Id</td>
            <td>${id}</td>
        </tr>
        <tr>
            <td>Nazwa użytkownika</td>
            <td>${username}</td>
        </tr>
        <tr>
            <td>Email</td>
            <td>${email}</td>
        </tr>
    </table>



</div>
 */
