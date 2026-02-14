import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class CreateAccountServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AccountManager accountManager = (AccountManager) request.getServletContext().getAttribute("accountManager");
        if (accountManager.accountExists(username)) {
            request.setAttribute("username", username);
            request.getRequestDispatcher("/nameInUse.jsp").forward(request, response);
        }
        else {
            accountManager.createNewAccount(username, password);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/userWelcome.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/createNewAccount.jsp").forward(request, response);
    }
}
