package service;

//게시글 수정하는 서비스문

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.BoardDAO;
import model.BoardDTO;
import model.MemberDTO;

public class BoardModify implements Command {
	
	private static final String UPLOAD_DIR = "C:/upload";
	//이미지를 내컴에서 지우기위해서 지정하는 주소

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		//로그인 한 사람만 수정 가능하게 세션객체 생성
		
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
		//로그인 유무를 결정하는 객체 생성(세션이 되어있지 않으면 null이 있다)
		
		if(loginMember == null) {
			response.sendRedirect(request.getContextPath() + "/member/login");
			//만약 로그인 안되어있으면 경로를 로그인 페이지로 유도
			return false;
		}
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		//로그인이 되어있다면 해당 게시글을 찾기위해 bno의 값을 서버로부터 받아옴
		
		BoardDAO dao = new BoardDAO();
		
		BoardDTO board = dao.selectOne(bno);
		//수정 폼에 채울 기존 데이터가 있는 곳
		//기존 데이터를 불러와서 고쳐야하기에 만듬
		
		if(board == null) {
			response.sendRedirect(request.getContextPath() + "/blog/list");
			return false;
			//만약 수정할수있는 내용이 없다면 글목록 화면으로 유도
			
			//참고로 리턴 퍼스해서 서블렛을 종료시킨다는 것은 
			//딱히 다른 jsp에서 더 처리해서 사용자에게 보여줄 이유가 없어 
			//바로 해당 페이지를 보여주기 위함이다
		}
		
		if(!board.getWriter().equals(loginMember.getId())) {
			//글쓴사람과 수정하려는 사람의 id가 다르다면
			request.setAttribute("board", board);
			request.setAttribute("errMsg", "본인 글만 수정 가능");
			//수정 못하게 막음
			
			return true;
			//여기서 리턴 트루를해서 컨트롤러로 떠넘기는 포워딩을 하는 이유는
			//위에서 속성을 저장해서 jsp에서 추가적으로 처리할 일이 있기 때문에
			//해당 속성값을 컨트롤러에게 보내기 위해서 리턴 트루를 한다
		}
		
		
		if("GET".equalsIgnoreCase(request.getMethod())) {
			request.setAttribute("board", board);
			//get 방식으로 요청이 들어오면 수정폼에 기존 값 채워서 보여줌
			//get은 기본적으로 사용자가 읽어온다는 의미가 있다
			//즉 dto에 저장된 기존 글 내용을 불러와서 화면에 보여준다
			
			//참고로 setAttribute는 해당 받아온 값들을 board라는 변수에 저장해서 
			//jsp에서 ${board.title} 같은 el문으로 사용하기위해 선언하는 변수 선언 비슷한 개념이다
			//이 클래스를 벗어나서도 사용할수 있는 변수이나 하나의 요청이 끝나게되면 소멸하게 된다
			return true;
		}
		
		//post 방식으로 요청이 들어오면 실제 수정 처리, 즉 수정완료 버튼을 누름
		//post는 기본적으로 사용자가 서버에 내용을 보내는것에 의미가 있다
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String removeImg = request.getParameter("removeImg");
		//제목 내용 이미지를 불러온다
		//removeImg는 true이면 이미지 제거 요청
		
		String oldImgPath = board.getImgPath();
		//기존 이미지(이미지 없음)의 경로
		String finalImgPath = oldImgPath;
		//기본값 : 기존 이미지 유지
		//이미지 제거시 기존 이미지로 대체하기 위함
		//참고로 수정화면에서 이미지 제거 누르면 이미지 없음이 나오는게 아니고,
		//기존에 저장되었었던 이미지로 되돌아간다고 한다.. 이게 올바른건가?
		
		Part filePart = request.getPart("imgFile");
		//이미지를 바꾼다면 우선 기존 이미지를 제거해야하므로
		//기존 이미지를 객체화
		if(filePart != null && filePart.getSize() > 0) {
			//기존 이미지가 널이 아니고 사이즈가 0보다 크다 
			//= 사용자가 새로 첨부한 이미지가 있다면
			
			deleteOldFile(oldImgPath);
			finalImgPath = saveFile(filePart);
			//기존 이미지를 제거하고 새로운 이미지를 지정하는 과정
			//savaFile메서드는 아래에 있음
			
		}else if("true".equals(removeImg)){
			//새 이미지 없이 이미지 제거만 누른경우 => 기존 이미지만 지우고 null
			deleteOldFile(oldImgPath);
			finalImgPath = null;
		}
		
		
		//둘다 아니면  finalPath는 oldImgPath 그대로 변경없음
		
		BoardDTO updateDto = new BoardDTO();
		//내용들을 사용자가 입력한 값들로 변경하는 과정
		updateDto.setBno(bno);
		updateDto.setTitle(title);
		updateDto.setContent(content);
		updateDto.setWriter(loginMember.getId());
		updateDto.setImgPath(finalImgPath);
		
		dao.update(updateDto);
		response.sendRedirect(request.getContextPath() + "/blog/list");
		//다 되면 리스트 목록을 보여라
		
		
		
		return false;
	}
	
	public String saveFile(Part filePart) throws IOException {
		//첨부파일 이미지 대입하는 방법
		//파일의 경로를 리턴할거라서 스트링을 지정
		String oldFileName = filePart.getSubmittedFileName();
		//전송된 파일의 이름을 가져와라.(사용자가 지정한 첨부파일의 이름)
		String saveFileName = System.currentTimeMillis() + "-" + oldFileName;
		//현재 시간을 밀리초단위로 쪼개서 파일을 저장하는 방법(UUID구성으로 같은 이름의 파일이 중복되지 않게 해준다)
		Path uploadDir = Paths.get(UPLOAD_DIR);
		if(!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
			//만약 c드라이브에 저장 폴더가 없다면 upload폴더 생성
		}
		
		Path targetPath = uploadDir.resolve(saveFileName);
		//업로드 폴더와 저장할 파일을 병합해서(resolve = 합치다) path에 저장
		//이러면 C:/upload/cat_20261007.jpg 같이 만들어진다
		try {
			InputStream input = filePart.getInputStream();
			//서버에서 내 컴퓨터로 오는 첨부파일을 조각조각내서 가져오는 방법
			Files.copy(input,targetPath);
			//위의 병합된 경로를 C의 업로드 폴더에 저장해라
			//저장이란게 곧 카피랑 다를게 없다.
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return saveFileName;
		//파일의 이름을 리턴
		
	}
	
	
	private void deleteOldFile(String fileName) {
		//컴퓨터 드라이브에 혹시라도 남아있을 찌꺼기 이미지파일이 있으면 제거한다
		if(fileName == null) return;
		//만약 찌꺼기가 없다면 그대로 종료
		
		try {
			Files.deleteIfExists(Paths.get(UPLOAD_DIR,fileName));
			//파일이 존재한다면 딜리트 메서드로 컴퓨터의 C:/upload를 찾아가서 직접 제거
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	
	

}










