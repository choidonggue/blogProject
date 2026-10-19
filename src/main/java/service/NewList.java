package service;

//인덱스 페이지에 최신글과 인기글을 3개씩 띄워서 보여주는 것

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDAO;
import model.BoardDTO;

public class NewList implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		
		BoardDAO dao = new BoardDAO();
		
		List<BoardDTO> list = dao.selectNewList();
		request.setAttribute("newlist", list);
		//최신글 파트의 dto리스트를 가져온다
		
		List<BoardDTO> popularList = dao.selectPopularList2(); 
	    request.setAttribute("popularlist", popularList);
	    //인기글 파트의 dto리스트를 가져온다
		
		
		
		return true;
	}

}
