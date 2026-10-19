package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BoardDelete;
import service.BoardList;
import service.BoardModify;
import service.BoardView;
import service.BoardWrite;
import service.Command;
import service.LikeCommand;
import service.NewList;
import service.PopularCommand;
import service.ReplyDelete;

import java.io.IOException;

/**
 * Servlet implementation class MainController
 */
@WebServlet("/Main/*")
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;



    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainController() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doAction(request,response);
		//get이든 post든 들어오면 doAction을 실행시키도록 한다
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request,response);
	}
	
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pathInfo = request.getPathInfo();
		//액션을 실행하면 경로를 받는다(해당 경로는 http://localhost:8089/blogproject/blog/list할듯) 
		System.out.println("pathInfo : " + pathInfo);
		//http://localhost:8089/blogproject/blog/list 요청이 들어오면 마지막 /list가 구해진다
		String page = null;
		
		Command command = null;
		
		switch(pathInfo) {
		
		case "/new":
			//
			command = new NewList();
			page = "/index.jsp";
			break;
			
				
			}
		
		boolean needForward = command.doCommand(request, response);
		//위에서 커맨드 명령을 받고 내려오면 doCommand를 실행하는데, 이것은 오버라이딩이 된 doCommand이기떄문에
		//BoardList같은데 찾아가보면 doCommand가 보이는것을 확인 가능
		
		if(needForward && page != null) {
			
			request.getRequestDispatcher(page).forward(request, response);
			//서버의 요청을 받아서 page변수에 저장된 jsp를 실행시킨다(forward) 
		
		}
		
		
	}
	
	
	
	
	
	
	

}
