package service;

//조인한 board를 출력하는 서비스문 

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDAO;
import model.BoardDTO;

public class BoardList implements Command {
	
	private static final int PAGE_SIZE = 9;
	//한페이지에 보여줄 게시글 갯수 9개
	private static final int PAGE_BLOCK = 5;
	//하단에 보여줄 페이지 번호 1 2 3 4 5 까지

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		
		String searchType = request.getParameter("searchType");
		String keyword = request.getParameter("keyword");
		//사용자로부터 검색어를 입력받았으면(작성자 : 청길동 검색) 해당 검색어와 선택박스의 값을 가져온다
		
		
		//현재 페이지 번호 (없거나 이상한 값이면 1페이지)
		int currentPage =1 ;
		//기본 페이지는 1페이지를 연다
		String pageParam = request.getParameter("page");
		//사용자에게 원하는 페이지 번호를 받아온다
		if(pageParam != null) {
			//넘어온 페이지 값이 존재한다면
			try {
				currentPage = Integer.parseInt(pageParam);
				//해당 페이지를 정수형으로 변환해서 사용한다
			} catch (NumberFormatException e) {
				currentPage = 1;
				// TODO: handle exception
				//만약 넘어온값이 비정상적이면 1페이지로 고정한다
			}
		}
		
		BoardDAO dao = new BoardDAO();
		//전체페이지 수 계산
		int totalCount = dao.selectCount(searchType, keyword);
		//전체 페이지수 계산(만약 사용자가 검색조건을 넣었으면 매개변수로 키워드같은것을 넘겨주고 계산된 페이지수를 받음)
		int totalPage = (int)Math.ceil((double)totalCount / PAGE_SIZE);
		//전체 페이징 갯수를 구한다(아래쪽에 1 2 3 4 5같은 링크가 페이징이다)
		//만약 게시글이 105개고 10개씩 페이지내에 보여준다면 105/10을 한뒤 10.5가 되면
		//소숫점을 반올림하여 11페이징 이라는 결과가 나오게 된다
		if(totalPage == 0) totalPage = 1;
		//페이지가 0페이지가 되는것을 방지. 0이되면 자동으로 1페이지로 연결
		if(currentPage < 1) currentPage = 1;
		//음수로 페이지가 되는것을 방지
		if(currentPage > totalPage) currentPage = totalPage;
		//마지막 페이지를 넘어갈수 없도록 방지. 가장 마지막 페이지로 고정되도록 한다
		
		//이번 페이지 가져올 구간
		//만약 1페이지에서 10개까지의 게시글을 볼 수 있는데 다음 페이지(2페이지)를 누르게되면
		//기존 1~10까지의 게시글은 없애고 11~20까지의 게시글을 가져와야한다. 
		int startRow = (currentPage -1) * PAGE_SIZE + 1;
		//시작점 페이지를 구한다. 만약 2페이지를 누르면 게시글이 11번째 게시글부터 나와야 한다
		//2번째 페이지이고 10개의 게시글을 보인다면 (2-1) * 10 + 1 = 11이 나오게 된다
		int endRow = currentPage * PAGE_SIZE;
		//끝나는점 페이지를 구한다. 2페이지를 누르면 20번쨰 게시글까지 와야하 한다
		//2페이지이고 10개의 게시글을 보인다면 2*20해서 20번째 게시글까지 보인다
		
		List<BoardDTO> list = dao.selectList(searchType, keyword, startRow, endRow);
		//dao문에서 페이징을 처리하고 list를 리턴받는다
		
		//하단 페이지 번호 구간 계산		
		int startPage = ((currentPage - 1) / PAGE_BLOCK) * PAGE_BLOCK + 1;
		//페이징의 시작 번호를 정한다
		//내가 1~5페이지를 보고있다면 어떤 페이지를 누르더라도 시작점은 1페이징이어야 한다
		//수학적 방법으로는 페이징의 최대 범위가 5라 하고, 현재 페이지 번호가 5라면
		//(5-1)/5 = 0.8이 나오게 되는데 결과적으로 강제 정수형을 하게되면 소숫점 이하는 날아가서 0이 나온다
		//0*5 = 0이고 여기에 1을 더하면 결과적으로 첫 시작 페이지는 1로 되게된다.
		//만약 6페이지라면 1*5+1을 하게되어서 시작점이 6이 될것이다. 
		int endPage = Math.min(startPage + PAGE_BLOCK - 1, totalPage);
		//페이징의 마지막 번호를 정한다
		//만약 12페이지가 나오게된다면 15페이지까지 출력하면 안되므로 Math.min을 사용한다
		//Math.min(A,B)는 A와 B중 더 작은것을 선택하는 성질이 있다
		//만약 7페이지까지 있다 가정하고 최대 5페이지까지 보일 수 있을 시에
		//1~5페이지를 보인다면
		//1+5-1 = 5가 나와서 
		//Math.min(5,7)이라는 결과가 나오므로 5개를 보이는 선택이 가능하다
		//그런데 만약 6페이지를 띄웠다면 6+5-1 = 10이 나오는데
		//Math.min(10,7)이란 결과가 나오게되어 7을 선택해서 7페이징까지만 가능하도록 한다
		
		request.setAttribute("boardList", list);
		request.setAttribute("searchType", searchType);
		request.setAttribute("keyword", keyword);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		//위의 값들을 모두 list.jsp로 던져준다
		
		
		
		
		
		return true;
		//포워딩을 하기 위해서 트루
		//이러면 서블렛에서 알아서 처리해준다
	}

}
