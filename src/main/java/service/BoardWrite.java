package service;

//로그인 한 사람만 글쓰기 가능
//첨부파일(그림)을 저장하는 과정이 포함된다

//그림 파일을 저장하는 과정은
//1) 중복 없는 파일명 생성 ➡️ 2) 폴더가 없으면 자동 생성 ➡️ 3) 안전하게 파일 복사 ➡️ 4) 저장된 이름(경로)을 DTO로 전달
//같은 과정을 거치게 된다
//만약 집에서 하게된다면 어떤 드라이브에 저장되는지 확인하도록 하자

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.BoardDAO;
import model.BoardDTO;
import model.MemberDTO;

public class BoardWrite implements Command {

	private static final String UPLOAD_DIR = "C:/upload";
	//전역변수 하나를 선언
	//아마도 파일이 저장될 장소를 선언한것 같다
	
	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		//세션 객체 생성
		MemberDTO memberdto = (MemberDTO)session.getAttribute("loginMember");
		//세션 속성을 가져온다(LoginCommand 클래스에서 가져왔다)
		//로그인 멤버의 형태가 dto타입이라서 MemberDTO타입으로 형변환을 하라고 해서 왼쪽의 dto변수에 저장한다
		if(memberdto == null) {
			//dto가 null이면 로그인이 안되었다는 말이다
			response.sendRedirect(request.getContextPath()+"/member/login");
			//로그인이 안되었으면 login.jsp를 열도록 시킨다(로그인하라고 화면 띄움)
			return false;
			//이미 리스폰스로 다른데 보냈으니 컨트롤러를 꺼버린다(포워딩 안함)
		}
		
		//get 요청 시 글쓰기 화면 보여주기
		//이 경우는 로그인이 되어있어야 나올수 있는 장면이다
		if("get".equalsIgnoreCase(request.getMethod())) {
			
			return true;
			//이 방법을 쓰면 write.jsp가 나오도록해서 글쓰기 화면이 출력된다
			
		}
		
		
		
		//글쓰기 전송을 누를 시 서버의 dto에 저장하는 과정 
		BoardDTO dto = new BoardDTO();
		dto.setTitle(request.getParameter("title"));
		dto.setContent(request.getParameter("content"));
		dto.setWriter(memberdto.getId());
		//작성자 = id니까 memberdto에서 ID를 가져오면 그게 곧 작성자다
		
		System.out.println(request.getParameter("writer"));
		
		//이미지 요청 - 이미지를 첨부했을 때만 서버에 저장
		Part filePart = request.getPart("imgFile");
		//jsp/servlet에서 파일 업로드를 처리 시 사용하는 servlet 표준 API
		//part는 업로드된 파일 하나를 표현하는 객체(이전에 배운 file 객체 같은것)
		//request.getPart("imgFile") http요청으로 전송된 데이터 중에서 name = "imgFile"인 파일을 가져와라
		//넘어오는 이미지 파일을 part라는 객체타입을 이용해서 filePart객체에 저장해라
		if(filePart != null && filePart.getSize() > 0){
			//filePart에 값이있고(파일이 있고) 그 용량이 0보다 크다면
			//굳이 0보다 큰지 비교하는 이유는 사용자가 파일을 안넣고 보내버리는 이상한 경우도 있기 때문
			
			String saveFileName = saveFile(filePart);
			//파일 객체를 저장하기 위한 객체 생성
			//일단 서버 디렉토리(폴더)에 파일을 저장한다
			//내가보기엔 saveFIle()이란 함수가 저장하는 과정같다
			//단순히 파일이 저장된 경로를 저장하는거라 문자열로써 저장한다
			//바로 아래쪽에 해당 함수가 있다.
			dto.setImgPath(saveFileName);
			//서버 디렉토리에서 저장된 파일 주소(일반적으로 db에는 파일의 주소를 기억한다)를 DB에 저장하는 과정
			//이렇게 하는 이유는 DB는 전세계 사람들이 다쓰는거라 모든 파일을 다 집어넣으면 터지므로
			//텍스트 파일만 받기때문에 이리 된것이다.
			//실제로 파일이 저장되는곳은 회사마다 가지고있는 사설 서버에 저장되고 그 경로가 DB에 저장되는것
		}
		
		
		BoardDAO dao = new BoardDAO();
		dao.insert(dto);
		//썼던 내용들을 dto에 저장하는 과정
		
		response.sendRedirect(request.getContextPath() + "/blog/list");
		//글쓰기에서 저장이 완료되면 저장된 글목록 화면이나오도록 하자
		
		
		return false;
		//서블렛 종료
		
	}//do Command
	
	
	public String saveFile(Part filePart) throws IOException{
		//첨부 파일 저장하는 과정 (캐치를 안쓰려고 쓰로우를 미리 만듬)
		

		/*핵심 클래스
		 *Path : 파일이나 디렉터리의 경로 정보를 나타내는 객체
		 *Files : 파일 생성,삭제,읽기,쓰기,복사 등 실제 동작을 처리하는 정적 메서드 집합 
		*/
		
		
		String oriFileName = filePart.getSubmittedFileName();
		//업로드한(첨부) 원래 파일명을 구한다(위에서 서버에 저장된filePart)
		System.out.println("original file : " + oriFileName);
		//실제로 어떤 파일이 저장되어있나 콘솔로 확인 방법
		
		//String saveFileName = System.currentTimeMillis() + "_" + oriFileName;
		//currentTimeMillis() : 현재 시간을 밀리초 단위로 반환하는 메서드
		//밀리초 시간에 뒤에다가 _ 를 붙인후 그뒤에 파일 이름을 붙여준다
		//이런 행동을 하는 이유가 전세계에서 똑같은 이름으로 저장을 하게 된다면 덮어쓰기가 일어난다
		//그래서 시간을 아주 미세하게 쪼개서 고유한 파일을 만들려고 하는 행동
		
		String saveFileName = UUID.randomUUID() + "_" + oriFileName;
		//UUID라는것도 위의 millis()함수같이 밀리초 시간을 붙이는 방법이다. 16진수형식으로 쪼개서 보여준다고
		//이것도 중복을 막기위한 방법중 하나
		System.out.println("UUID : " + saveFileName);
		//시간이 잘 붙었나 확인하는 콘솔 출력문
		
		Path uploadDir = Paths.get(UPLOAD_DIR);
		//문자열로 되어 있는 경로를 Path 객체로 바꿔라
		//UPLOAD_DIR은 위에서 선언했던 C:/upload를 가리키는 변수다
		//이것을 가지고 컴퓨터에 폴더가 존재하는지를 판단한다
		
		//Path : 경로를 표현하는 객체
		//Paths : Path객체를 만들어 주는 클래스
		
		if(!Files.exists(uploadDir)) {
			//C드라이브에 uploadDir 폴더가 존재하지 않으면(저장 장소가 없다면)
			Files.createDirectories(uploadDir);
			//저장할 폴더를 uploadDir이란 이름으로 생성해라
			//createDirectories는 상위폴더부터 하위폴더까지 전부 만드는 함수다
			//C:/upload/form/dir 같이 써놓으면 한방에 3개다 만든다
		}
		
		Path targetPath = uploadDir.resolve(saveFileName);
		//resolve() : 폴더 경로 + 파일 이름을 합쳐서 완전한 파일 경로를 만들어라
		//C:/upload/1712345678901_cat.png 같이 저장장소와 경로가 합쳐져서 보이게 된다
		
		try(InputStream input = filePart.getInputStream()){
			//InputStream은 데이터를 읽어오는 통로를 개설하는 과정
			//파일을 저장하기 위해서 일단 통로를 개설한다
			
			Files.copy(input, targetPath);
			//input에서 파일 데이터를 읽어서 targetPath로 지정한 위치로 복사 시행
			//파일이 복사된다가 결국 저장되는 과정이다
			
		}//이 과정을 거치면 서버에 물리적으로 파일이 저장된다
		
		
		
		return saveFileName;
		//DB에 저장하기 위해서 DTO로 경로_파일이름 으로 저장된 파일의 경로만 리턴한다
		//
		
	}

}











