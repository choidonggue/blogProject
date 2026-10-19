package service;

//게시글 삭제 서비스문

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.BoardDAO;
import model.MemberDTO;

public class BoardDelete implements Command {

	private static final String UPLOAD_DIR = "C:/upload";
	//이미지를 내컴에서 지우기위해서 지정하는 주소 그렇다
	
	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
		
		if(loginMember == null) {
			//게시글을 쓴사람만 해당 게시물을 지울수 있게 로그인이 안되어있으면 로그인 창이 나오도록 유도
			response.sendRedirect(request.getContextPath()+"/member/login");
			return false;
		}
		
		//로그인이 완료된 상태로 삭제버튼 누를 시 아래의 과정을 거친다
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		String imgPath = new BoardDAO().delete(bno, loginMember.getId());
		//bno의 값을 delete메서드에 전달해서 해당 게시글을 제거
		//Id = writer다
		
		if(imgPath != null) {
			//게시글에서 받아온 이미지 주소값으로 내 컴퓨터에서 이미지를 제거하는 방법
			try {
				Path target = Paths.get(UPLOAD_DIR, imgPath);
				//맨 위에서 UPLOAD_DIR = c의 upload폴더라고 지정함
				Files.deleteIfExists(target);
				//해당 폴더에 이미지 주소값을 가진 이미지가 있으면 지운다
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		response.sendRedirect(request.getContextPath()+"/blog/list");
		//게시글이 지워지면 list.jsp가 열리도록 함
		
		
		
		return false;
	}

}
