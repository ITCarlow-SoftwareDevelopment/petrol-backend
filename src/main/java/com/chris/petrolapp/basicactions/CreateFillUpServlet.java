package com.chris.petrolapp.basicactions;
 
import com.chris.petrolapp.objects.FillUp;
import com.chris.petrolapp.daos.FillUpDao;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateFillUpServlet extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();
        try {
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = request.getReader().readLine()) != null) {
                sb.append(s);
            }
 
            FillUp fillUp = (FillUp) gson.fromJson(sb.toString(), FillUp.class);
            FillUpDao dao = new FillUpDao();
    		Long id = null;
    		try {
      			id = dao.createFillUp(fillUp);
    	    } catch (Exception ex) {
  	    		throw new ServletException("Error creating fillup", ex);
    		}	
        } catch (Exception ex) {
            throw new ServletException("Error creating a fillUp", ex);
        }
  }
}
