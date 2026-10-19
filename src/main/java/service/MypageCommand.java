package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.BoardDAO;
import model.BoardDTO;
import model.MemberDTO;
import model.ReplyDAO;
import model.ReplyDTO;

public class MypageCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		//세션객체 생성
		//loginMember 속성을 MemberDTO 객체로 형변환
		//loginMember가 null과 같으면 로그인 페이지로 이동
		//loginMember에 있는 id를 변수에 저장하고
		//id를 replyDAO, BoardDAO 객체를 생성하고 메서드를 호출하면서 id를 보낸다
		//메서드를 처리하고 리턴된 가변배열의 값 3개를 속성에 담는다
		
		HttpSession session = request.getSession();
		//로그인 한 사람만 수정 가능하게 세션객체 생성
		
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
		//로그인 유무를 결정하는 객체 생성(세션이 되어있지 않으면 null이 있다)
		
		if(loginMember == null) {
			response.sendRedirect(request.getContextPath() + "/member/login");
			//만약 로그인 안되어있으면 경로를 로그인 페이지로 유도
			return false;
		}
		
		String id = loginMember.getId();
		
		
		
		ReplyDAO dao = new ReplyDAO();
		BoardDAO dao2 = new BoardDAO();

		List<ReplyDTO> myRepliesList = dao.selectListByWriter(id);
		List<BoardDTO> myPostsList = dao2.selectListByWriter(id);
		List<BoardDTO> myLikesList = dao2.selectLikedList(id);

		request.setAttribute("myReplies", myRepliesList);
		//마이페이지에서 내가 쓴 댓글 조회
		request.setAttribute("myPosts", myPostsList);
		//마이페이지의 내가 쓴글을 조회
		request.setAttribute("myLikes", myLikesList);
		//마이페이지의 내가 좋은가요 한 글 조회
		
	

		return true;
		
	
	}

}
