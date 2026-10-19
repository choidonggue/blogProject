
package service;

//아이디가 제대로 입력 안되었으면 비동기식으로 알려주는 방법
//아이디와 비번 확인하세요 라는 문자가 보인다

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MemberDAO;

public class IdCheckCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		//사용자가 입력한 데이터의 한글깨짐 방지
		
		String id = request.getParameter("id");
		//화면에서 입력한 아이디 값을 가져옴
		MemberDAO dao = new MemberDAO();
		
		boolean isDuplicate = dao.idCheck(id);
		//db에 물어보고 아이디 중복여부 판단
		
		//json형태로 직접 응답(join.js에 가면 ajax라고 있음)
		//json은 데이터를 서버와 클라이언트가 주고받기위해 사용하는 문자열
		//화면전환이 없이 비동기식으로 데이터를 처리가능
		//대표적으로 핸드폰의 처리방식인데 좋은가요버튼이라던가 댓글달기라던가
		//눌러도 화면 깜빡임없이 수치가 변화한다. 이게 json방식
		
		/*
		 * JSON형식의 데이터 "키:값"
		 * 
		 *  {
		 * "name" : "흑길동",
		 * "age" : 70,
		 * "phone":"010"
		 * }
		 * 
		 * 무슨 db테이블 제작같이 생겼다
		 * */
		
		
		   response.setContentType("application/json, charset=utf-8");
		//응답의 종류를 알려주는 것. 
		//브라우저에게 내가 보내는 문서는 html이 아니고 JSON형식이니 문자는 UTF-8형식으로 가공해서 읽으라고 명령
		
		PrintWriter out = response.getWriter();
		//서버가 응답할 통로 객체. HTML,JSON 같은 문자열을 클라이언트에게 보내는 도구
		//브라우저에게 보낼 글자 전송 통로를 연다
		  out.print("{\"isDuplicate\":"+ isDuplicate+"}");
		//{키값,밸류값} 순서
		//JSON형식의 데이터. 실제로 만들어지는 문자열 {"isDuplicate":true}
		//\" 같이 붙여줘야지 "가 제어문자로써 적용된다. 그냥 "만 붙이면 문자열로 취급되기에 저런직을 한다
		//\n과 비슷한 원리이다
		
		
		
		// TODO Auto-generated method stub
		return false;
		//이게 있는 이유는 true라면 해당 jsp파일을 다시 새로고침해서 열지만
		//지금 이건 비동기방식이라 화면을 새로고침하면 안되므로 false를 선언해서
		//화면의 새로고침을 막는 방법이다
		
	}

}
