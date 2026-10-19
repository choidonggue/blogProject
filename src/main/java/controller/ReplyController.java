package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.Command;
import service.ReplyDelete;
import service.ReplyModify;
import service.ReplyWrite;

/**
 * Servlet implementation class ReplyController
 */
@WebServlet("/reply/*")
public class ReplyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReplyController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

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
		
		Command command = null;
		
		switch(pathInfo) {
		case "/write":
			//댓글달기
			command = new ReplyWrite();
			//모든 reply기능은 Ajax라 forward할 page가 없다
			//댓글달기는 바로 그화면에 비동기식으로 나오기때문에 포워딩이 필요없다. 
			//(화면을 새로 띄울 필요가 없어서 page = null; 같은것도 필요없다)
			
			break;
			
		
		case "/delete":
			//댓글 삭제
			command = new ReplyDelete();
			break;
			
		
		case "/modify":
			//댓글 수정
			command = new ReplyModify();
			break;
			
		}
		
		
		command.doCommand(request, response);
		//모든 reply 기능은 ajax라(비동기 처리)
		//포워딩이 없기떄문에 이거 하나만 적어줘도 된다
		
		
		
}
	
}
