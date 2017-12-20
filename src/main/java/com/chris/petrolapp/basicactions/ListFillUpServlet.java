package com.chris.petrolapp.basicactions;

import com.chris.petrolapp.daos.FillUpDao;
import com.chris.petrolapp.objects.FillUp;
import com.chris.petrolapp.objects.Result;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ListFillUpServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException,
      ServletException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "GET");
    FillUpDao dao = new FillUpDao(); 
    String startCursor = req.getParameter("cursor");	//fix Chris:should be from hson paramter 
    Result<FillUp> result;
    try {
      result = dao.listFillUps(startCursor);
    } catch (Exception e) {
      throw new ServletException("Error listing fillUps", e);
    }
    Gson gson = new Gson();
    response.getWriter().print(gson.toJson(result));
    response.getWriter().flush();
  }
}