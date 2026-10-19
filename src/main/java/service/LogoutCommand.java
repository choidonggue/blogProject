package service;

//로그아웃용 세션제거 클래스
//loginMember라는 것이 세션이다 이것을 지울거다
//페이지에 member/logout을 입력 시 블로그 홈화면(blogProject/)가 나온다면 로그아웃이 된거라고..

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		session.invalidate();
		//세션안의 모든값 제거(invalid : 무효로 하다)
		
		response.sendRedirect(request.getContextPath() + "/");
		//리퀘스트 컨텍스트 패스를 쓰면 blogProject가 실행되도록 한다
		//즉 로그아웃 누르면 블로그 메인화면 나오도록 한다 (/ 하나만 하면 가장 위의 폴더)
		
		return false;
		//forward를 안시키기 위해서 서블렛을 종료위해 false반환 (이미 갈 방향을 response로 마쳤다)
	}

}
