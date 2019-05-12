
package com.vishal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet(urlPatterns = "/database/*",asyncSupported = true)
public class SimpleServlet extends HttpServlet {
	static String collectionsFromDb ;
	
   private static final long serialVersionUID = 1L;
   @Override
   protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) 
         throws ServletException, IOException {
      resp.setContentType("text/plain");
      PrintWriter out = resp.getWriter();
     
      final long startTime = System.nanoTime();
      final AsyncContext asyncContext = req.startAsync(req, resp);

      new Thread() {

        @Override
        public void run() {
          try {
            ServletResponse response = asyncContext.getResponse();
            resp.setContentType("text/plain");
            String pathInfo = req.getPathInfo(); 
            System.out.println("pathInfo"+pathInfo);
            String[] parts = pathInfo.split("/");
            System.out.println("parts0"+parts[0]);
            System.out.println("parts1"+parts[1]);
            System.out.println("parts2"+parts[2]);
            System.out.println("parts2"+parts[3]);
            String param1 = parts[3];
            PrintWriter out = response.getWriter();
            Thread.sleep(2000);
            MongoDb db=new MongoDb();
            try {
            	System.out.println(param1);
      		collectionsFromDb = db.getCollectionsFromDb(param1);
      		 resp.getWriter().write("Hello World! Maven Web Project Example.");
      	} catch (InterruptedException e) {
      		// TODO Auto-generated catch block
      		e.printStackTrace();
      	}
           out.println(collectionsFromDb.replaceAll("_", ""));
            out.print("Work completed. Time elapsed: " + (System.nanoTime() - startTime));
            out.flush();
            asyncContext.complete();
          } catch (IOException e) {
            throw new RuntimeException(e);
          } catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        }
      }.start();

      
   
   }
   
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
	         throws ServletException, IOException {
	      resp.setContentType("text/plain");
	      StringBuffer jb = new StringBuffer();
	      String line = null;
	      try {
	        BufferedReader reader = req.getReader();
	        while ((line = reader.readLine()) != null)
	          jb.append(line);
	      } catch (Exception e) { /*report an error*/ }

	      
	      PrintWriter out = resp.getWriter();
	      String json=req.getParameter("");
	      MongoDb db=new MongoDb();
	      System.out.println("jsonString"+jb.toString());
	      db.insertIntoCollection(jb.toString().replace("id", "_id"));
	      resp.getWriter().write("Hello World! Maven Web Project Example.");
	   }
   
   
}