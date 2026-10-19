package service;

//인기글을 출력하는 서비스문

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDAO;
import model.BoardDTO;

public class PopularCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		
		List<BoardDTO> list = new BoardDAO().selectPopularList();
		//인기글 출력을 위해서 리스트를 받는다
		
		request.setAttribute("poplist", list);
		//리스트를 다른 장소에 포워딩 해준다
		
		
		
		
		return true;
	}

}
