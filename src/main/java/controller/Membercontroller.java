

package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.Command;
import service.IdCheckCommand;
import service.LoginCommand;
import service.LogoutCommand;
import service.MemberInsert;
import service.MypageCommand;


@WebServlet("/member/*")
//URL을 통해서 요청하는 주소가 /member로 시작하는 모든요청(*)은 이 클래스가 자동으로 실행
//멤버로 시작하는 jsp는 다 이 서블렛을 거친후 화면을 띄우던가 한다
//이제부터는 jsp로 실행하면 안되고 url에 주소쳐서 가야한다(제대로 만들어진다면 어노테이션을 이용해서 접근이 가능할것으로 보인다)

//실제 클라이언트는 member/join.jsp라는 주소로 시작되는 url을 보게되는데
//실제로는 저런 주소가 아니고 mem/join.jsp로 접근하게된다
//이렇게 하는 이유는 제대로된 주소를 사용자에게 보여주지 않아서 보안을 향상시키기 위해서라고 한다
//즉 사용자는 항상 어노테이션의 가상 주소만 보게된다
//만약 직접 mem/join.jsp로 쳐서 간다면 어노테이션의 주소는 나타나지 않는다

//현재 페이지의 대강의 흐름은 컨트롤러에서 명령 받으면 서비스쪽으로 연결한 후에 관련 jsp를 연결시켜 주는 과정을 거친다.
//리턴값에 따라서 jsp를 연결 안하고 다른 과정을 처리할수도 있다

//화면구현 > dao 작성 > 서비스 작성 > 컨트롤러 작성 순으로 간다.
//(서비스 + 컨트롤러를 crud의 메인문으로 취급하는듯 하다)
//(그리고 dao는 메서드, 화면구현은 콘솔창)

public class Membercontroller extends HttpServlet {
	//http를 상속받아서 원하는 클래스를 만듬
	private static final long serialVersionUID = 1L;
	//1L이라는 변수를 serialVersionUID에 저장
	//스태틱 성질로 저장했기에 객체생성 없이 변수 사용 가능
	//그 외에도 long(8byte 정수형)성질과 final(수정 불가), private(여기서만 쓰임)같은것도 단번에 다 지정했다
	
	//직렬화 관련 버전 번호라고 하는데
	//자동으로 이클립스가 서블릿 만들때 만든다고 한다
	
       
    public Membercontroller() {
    	//기본 생성자
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request,response);
	}
	
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doAction(request,response);
	}

	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//doGet요청이 이쪽으로 넘어온다
		//get으로 요청받아서 여기에서 처리하는 셈. service영역이라고 한다
		
		String pathInfo = request.getPathInfo();
		//localhost:8089/blogProject/member/join
		//요청된 주소가 저거라면 가장 마지막에 join을 구하는게 getPathInfo()함수다
		System.out.println("pathInfo : " + pathInfo);
		//자바에서 잘되는지 확인용 콘솔 출력 위함
		String page = null;
		//아래에서 페이지 경로 쓰려고 선언
		//ai는 저리 쓰면 오히려 500오류가 나온다고 한다. 아래에 page != null이 성립이 안된다고.
		//""를 제거하는게 맞는것같다.
		//제대로 null로써 연결되면 흰 화면만 나오는데 서버에 접속하는건 성공했으나
		//출력될 내용을 캐치 못하는 상황이라고 한다
		
		Command command = null;
		//커맨드 전역변수 생성
		//이 커맨드로 어떤 case를 실행했을때 실행되는 메서드를 결정할수 있다.
		//이 메서드로 서비스파트에 가서 if문으로 리턴이 트루냐 퍼스냐를 결정시키는 과정을 거친다

		
		
		switch(pathInfo) {
		
		case "/join":
			//위의 request.getPathInfo()의 결과가 /join이라면
			
			command = new MemberInsert();
			//다형성
			//실제 회원가입을 처리할 객체 생성
			//service폴더 가보면 이 클래스가 보인다. 컨트롤러와 서비스를 연결시키는 과정이다.
			
			page = "/mem/join.jsp";
			//해당 경로를 진행하라.(join.jsp가 열림)
			//굳이 경로를 member가 아니라 mem으로 폴더 이름을 바꾼 이유가
			//위의 서블렛 경로로 member를 지정해놔서 맨아래의 disPatcher를 실행하면
			//두번 jsp가 실행되기 떄문이라고 한다. 
			//그래서 mem으로 폴더를 변경해서 다시 경로를 지정해준다.
			
			//작업 완료 후 이동할 jsp화면 지정
			//내가 보기에는 리턴값으로 퍼스가 돌아오면 해당 페이지가 실행되도록 하는것으로 유추된다
			
			break;
			
		case "/idCheck": 
			//localhost:8088/blogProject/member/idCheck
			//join.js에서 idCheck라는 요청이 들어오면
			//아이디 중복검사시 연결되는 객체
			command = new IdCheckCommand();
			//아이디 중복검사를 처리할 객체 생성
			//null포인트 예외 없애기위해서 커맨드를 변수에 값을 할당
			//서비스 폴더에서 idcheckcommand클래스를 가져와서 컨트롤러와 연결
			page = null;
			//보통 중복 체크는 Ajax 비동기 통신을 하므로
			//이동할 화면이 필요 없다(보통 아래에 글자로 아이디중복됨. 같은거 나오고 끝)
			break;

		
		
	case "/login":
		//localhost:8088/blogProject/member/login
		//로그인을 할때 연결되는 객체
		command = new LoginCommand();
		//서비스의 logincommand클래스와 컨트롤러 연결
		page = "/mem/login.jsp";
		//해당 페이지로 연결
		break;
		//만들어놓은 로그인 jsp실행시키기 위함
		
	case "/logout":
		command = new LogoutCommand();
		page = null;
		//로그아웃 후에는 false가 리턴되어와서 갈데가 없으니 page는 null로 지정(딱히 갈 주소가 없으면 null로 기본값지정)
		break;
	
	
		
	case "/mypage":
		//마이페이지를 여는 커맨드
		page="/mem/mypage.jsp";
		command = new MypageCommand();
		break;
		
	}
	
	boolean needForward = command.doCommand(request, response);
	//해당 주소에 맞는 비즈니스 로직(회원등록 등)을 실제로 수행한다
	//불리언으로 하는 이유는 true가 나오면 화면이동을 하고 false가 나오면 
	//ajax로 처리하는등의 과정을 거치기 위함이다.
	
	if(needForward && page != null) {
		//command의 값이 true이고 페이지값이 존재한다면 아래의 리퀘스트를 실행.
		//만약 화면 이동이 필요하고 이동할 페이지 경로(page)가 존재 한다면
		//즉 response를 안하고 서블렛을 아직까지 살려 왔으면(리턴값 true) 해당 if문을 시행
		RequestDispatcher rd = request.getRequestDispatcher(page);
		//지정된 jsp화면으로 사용자를 이동(포워드)시킨다
		//(page는 위의 case에 배정된 jsp를 찾아간다)
		//세션은 서버에 데이터가 저장되어 있어서 포워드할 필요가없는데
		//리퀘스트는 포워드를 반드시 해줘야한다
		//결과가 false가 나오면 서블렛을 꺼버리는데, true가 나오면 서블렛이
		//최종 경로를 지정해주도록 하기위해 이런 짓을 한다
		rd.forward(request, response);
		//포워드가 되면 위의 page에 저장되어있던 jsp경로로 이동되게 된다
	}
}

}



