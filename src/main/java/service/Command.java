
package service;

//서비스 부분을 작성한다

//서비스는 DB > Dao > service > servlet순으로 오는 처리 과정중 3번째에 해당한다
//(요청을 받아서(Servlet) -> 실제 기능(Service)을 처리하고 -> DB(DAO)에 접근한다)
//서블렛에 메서드, 함수같은것을 적는데 한계가 존재해서 서비스 파트를 만든다고 한다

//보통 서블렛 부분에는 다형성의 성질을 가진 문장 한개만 적어넣는다고 한다.
//그 문장이 이쪽으로 연결되는것

//아래의 내용은 모든 기능(로그인,글쓰기,댓글쓰기...)이 공통으로 구현할 인터페이스
//컨트롤러는 이 인터페이스만 다룬다

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {

	boolean doCommand(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException,IOException;
	//docommand 메서드를 호출하면 불린형식으로 리턴해라
	//위에 적힌것은 추상메서드다
	//이 방법을 쓰면 서블렛을 껐다 키는 return값을 지정할수 있는 모양이다(false면 서블렛이 꺼진다)

}
