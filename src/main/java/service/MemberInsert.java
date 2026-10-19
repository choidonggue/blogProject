
package service;

//아마도 가입하기 버튼을 누르면 데이터가 입력되는 과정
//실제 회원가입 데이터(ID, 비밀번호, 닉네임, 이메일)를 추출하고
//데이터베이스에 저장하는 핵심 비즈니스 로직(Service) 이라고한다

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MemberDAO;
import model.MemberDTO;

public class MemberInsert implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//get 방식으로 요청이 들어오면 다음 코딩을 실행을 안하게 한다
		if("get".equalsIgnoreCase(request.getMethod())) {
			//request.getMethod() 요청 방식을 알고 싶을시
			//이퀄 이그노어 케이스는 대문자 소문자를 똑같이 취급한다(GET = get)
			return true;
			/*
			 * 대강의 정리
			 * 여기서 get방식은 회원가입 폼을 그저 볼뿐인 장면이고
			 * post방식은 가입하기를 눌러서 정보를 전송시 쓴다고 한다
			 * 이렇게 귀찮은 짓을 하는 이유는 하나의 경로(/mem/join.jsp)로 회원가입 폼 보기와 전송까지
			 * 다 쓰려고하다보니 이렇게 boolean으로 리턴방식을 true false로 쪼개서
			 * get이 들어오면 true로 적용시켜서 회원가입 폼 화면을 보도록
			 * post로 들어오면 false로 적용시켜서 회원가입하기 버튼을 누르도록하게끔 한것이다. 
			 * 
			 * 이 if가 get이면 이 true가 실행되도록 한거고
			 * post면 아래의 전송을 하는 식을 거친다
			 */
		}
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
				//Bcrypt라는 jar을 이용해서 패스워드를 암호화 시키는 방법.
		//이 방법을 쓰면 1234라는 비번이 엄청나게 긴 숫자+영어의 형태로 만들어진다
		//비밀번호에 소금을쳐서 마구 뒤섞는다는 영어단어의 조합..
		
		//뒤섞을때 쳤던 소금을 서버는 기억해두는데
		//사용자가 비번을 치면 똑같은 소금을 쳐서 뒤섞으면
		//가입할떄 썼던 비번의 형태와 똑같은 믹스가 나오게되어
		//비교해서 같다. 라는 판명이 나오게 된다고 한다
		String nickname = request.getParameter("nickname");
		String email = request.getParameter("email");
		//서버에서 입력된 값을 가져와서
		
		MemberDTO dto = new MemberDTO();
		dto.setEmail(email);
		dto.setId(id);
		dto.setNickname(nickname);
		dto.setPwd(hashedPwd);
		//dto에 집어넣고
		
		MemberDAO dao = new MemberDAO();
		dao.memberInsert(dto);
		//dto를 dao로 보내서 값을 인서트하게끔 한다
		
		response.sendRedirect(request.getContextPath()+"/member/login");
		//회원 가입이 끝나면 로그인 화면이 나오도록 해라
		
		
		
		
		return false;
		//위에서 리다이렉트 하면 false를 리턴하고 포워딩이 필요하면 true를 리턴한다
		//즉 위에서 이미 이동경로를 만들었기때문에 별다른 이동경로를 안만들기위해서 퍼스 리턴
		//더이상 컨트롤러가 간섭 못하게(화면이동 못하게) 리턴을 퍼스로 한다고 한다
	}

}







