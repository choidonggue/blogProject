package service;

//list.jsp에서 (글목록) 선택한 글을 조회수 증가시키고 여는 서비스문

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDAO;
import model.BoardDTO;
import model.ReplyDAO;
import model.ReplyDTO;

public class BoardView implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		request.setCharacterEncoding("utf-8");
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		//가져오는 값을 정수로써 가져오기위해서 정수변환
		//html은 기본적으로 다 string으로 온다
		
		BoardDAO dao = new BoardDAO();
		
		dao.increViewCnt(bno);
		//조회수를 먼저 증가시키고 선택한 페이지를 보여줄 것이다
		
		BoardDTO dto = dao.selectOne(bno);
		//선택한 페이지를 보여준다
		
		List<ReplyDTO> list = new ReplyDAO().selectList(bno);
		//댓글 전체 내용을 보여주기 메서드
		
		request.setAttribute("view", dto);
		request.setAttribute("replyList", list);
		//게시글 내용과 댓글의 내용 전체를 보여줄 수 있는 속성을 만들어서
		//jsp로 그 내용을 보낸다
		
		
		
		
		
		
		
		
		
		
		
		return true;
		
		
	}

}
