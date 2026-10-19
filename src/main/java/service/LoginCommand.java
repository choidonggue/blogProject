

package service;

//로그인을 위한 서비스문
//로그인을 성공하면 블로그 홈화면으로 이동되게 된다

//뭔가 서비스파트는 crud문의 메인문과 같은 느낌이 든다

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MemberDAO;
import model.MemberDTO;

public class LoginCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		if("GET".equalsIgnoreCase(request.getMethod())) {
			//전송방식을 get메서드 방식으로 한다
			//이걸하면 http://localhost:8089/blogProject/member/login하면 로그인 화면이 나온다..
			
			
			
			return true;
		}
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		MemberDAO dao = new MemberDAO();
		MemberDTO dto =dao.login(id);
		
		if(dto != null && BCrypt.checkpw(pwd, dto.getPwd())) {
			//입력된 아이디와 비번이 있다면(비번은 입력된 비밀번호와 dto의 암호화된 비밀번호를 비교한다)
			//세션 속성을 만들어서 로그인 정보를 담는다
			//세션 설명은 아래에 존재
			
			HttpSession session = request.getSession();//세션 만들기
			session.setAttribute("loginMember", dto);
			//dto값을 loginMember라는 세션속성에 보낸다 그러면 로그인 완료
			//참고로 이 세션속성을 삭제하면 로그아웃이다.
			response.sendRedirect(request.getContextPath()+"/");
			//해당 값은 로그아웃할떄까지 계속 값이 유지된다(세션)
			
			return false;
			
		}else {
			//입력된 아이디 비번이 없다면
			request.setAttribute("loginFail", "아이디 또는 비번 입력 확인 필요");
			//처리된 값을 jsp로 출력을 하려면 request,session 속성에 저장해서 포워딩 한다
			//리퀘스트 속성(박스)에 해당 값(글자들)들을 저장하라
			//그리고 로그인페일 이라는 jsp로 보내줘라
			//그럼 해당 jsp에서 저 박스를 받으면 글자들이 출력된다
			return true;
			//포워딩 할거면 트루를 한다
			//해당 보내지는값은 증발한다(세션이 아님)
			
			//세션이란것은 로그아웃이 되기전까지 계속 로그인상태를 유지하도록하는 것이다
			//로그아웃을 누르면 세션이 사라져서 로그인상태가 끊긴다
			//한 페이지에서만 값이 유지될거면 request속성을 쓰고
			//다른 페이지로 이동되도 유지되고 싶으면 session속성을 쓴다
			//리퀘스트 = 변수 / 세션 = 객체 / 라고 생각하자
			
			//쿠키는 세션과 성격이 비슷하나 
			//세션은 서버에 정보가 저장되고(로그인 상태를 유지한다 같은)
			//쿠키는 사용자 컴퓨터에 저장된다.(보통 아이디,비번 자동 저장은 내 컴퓨터에 쿠키로써 남는다)
		}
		
		
	}

}




