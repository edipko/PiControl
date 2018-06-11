import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by edipko on 6/18/16.
 */
@WebServlet(name = "controlservlet")
public class ControlServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        System.out.println("Calling readProperties from servlet: button ON state is: " + request.getParameter("on"));
        BackgroundJobManager.readProperties();


        if (request.getParameter("on") != null) {
            String status = BackgroundJobManager.pcc.on();
        } else if (request.getParameter("off") != null) {
            BackgroundJobManager.pcc.off();
        }


        request.setAttribute("pumpStatus", BackgroundJobManager.pcc.isOn());
        request.setAttribute("prop", BackgroundJobManager.prop); // so that you can retrieve in JSP
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

}



