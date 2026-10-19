
package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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


@WebServlet("/blog/*")
@MultipartConfig(
		//파일을 전송받기 위해서 어노테이션을 하나 선언
		maxFileSize = 10 * 1024 * 1024, //파일 1개 저장시 최대 10MB까지 가능하도록 지정
		maxRequestSize = 20 * 1024 * 1024 //전체 요청 최대 20MB
		)


public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardController() {
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
		case "/list":
			//조인된 board 리스트를 출력하는 방법
			command = new BoardList();
			
			page="/board/list.jsp";
			//해당 jsp경로를 page 변수에 저장
			break;
			
			
		case "/write":
			//글쓰기 작성 화면을 띄우는 케이스
			command = new BoardWrite();
		page="/board/write.jsp";
		break;
		
		
		case "/view":
			//글 목록에서 글 1개를 선택했을때 여는 방법
			command = new BoardView();
			page = "/board/view.jsp";
			break;
			
			
		case "/delete":
			//게시글 목록에서 글 1개 삭제
			//내 게시글이 아니라면 삭제 버튼을 눌러도 안사라진다
			command = new BoardDelete();
			page = null;
			//이미 저쪽에서 포워딩 끝나고 리턴을 false를 해서 닫아버려서 컨트롤러가 종료되었기에 
			//딱히 page로 연결시켜줄 jsp를 할 필요가 없다
			break;
			
			
		case "/update":
			//게시글 수정 요청
			command = new BoardModify();
			page = "/board/modify.jsp";
			//포워딩 요청이 오면 modify.jsp로 연결시켜서 setAttribute의 저장된 값을 사용하도록 한다
			break;
			
		case "/like":
			//좋은가요를 처리하는 커맨드
			command = new LikeCommand();
			page = null;
			break;
			
			
		case "/popular":
			//인기글 처리하는 커맨드
			command = new PopularCommand();
			page = "/board/popular.jsp";
			break;
			
		case "/new":
			//최신글 처리하는 커맨드
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

