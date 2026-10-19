package service;

//댓글 작성 관련 서비스문

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MemberDTO;
import model.ReplyDAO;
import model.ReplyDTO;

public class ReplyWrite implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
		//로그인한 사람만 댓글을 달 수 있도록 세션 객체 생성
		
		Map<String, Object> result = new HashMap<String, Object>();
		//키는 문자열 타입 값은 객체 타입으로 웹데이터를 처리하겠다
		//Gson을 사용하기 위해서 해당 Map방식을 선언한다
		
		if(loginMember == null) {
			//로그인이 안되어 있다면
			
			//이상한 json으로 처리시는 아래를 사용
			//sendJson(response, "{\"success\":false,\"message\":\"로그인이 필요하다\"}");
			//Json언어로 html에게 로그인이 필요하다는 내용을 보내는 과정. 
			//이런 이상한 방법으로 적는 이유가 json에선 ""가 문장을 끝낸다는 의미가 있어서
			// \"같은 방법으로 이것은 끝맺음이 아니라 전송할 내용이라고 알려주는 방법이다.
			//실제로는  "success": false,"message": "로그인이 필요하다" 라는 내용이 전송된다
			
			
			
			//gson이란걸 사용하는 방법
			//gson은 기본적으로 Map방식으로 전송해야 하기떄문에 Map객체를 불러올 필요가 있다
			result.put("success", false);
			result.put("message","로그인이 필요 하다");
			//result라는 바구니에다가 로그인일 실패했다는 내용들을 담는다
			sendJson(response,result);
			
			
			return false;
			//Json에게 해당 내용들을 보내고 더이상 진행 못하게 false를 반환
			//사실 댓글은 비동기처리라 false만 리턴된다
		}
		
		//로그인에 성공했으면 사용자로부터 댓글의 내용을 받아오는 과정을 거침
		int bno = Integer.parseInt(request.getParameter("bno"));
		String content = request.getParameter("content");
		
		//dto에 값을 저장하고
		ReplyDTO dto = new ReplyDTO();
		dto.setBno(bno);
		dto.setWriter(loginMember.getId());
		dto.setContent(content);
		
		//dao의 처리를 거친뒤에
		ReplyDAO dao = new ReplyDAO();
		int rno = dao.insert(dto);
		
		//json에게 해당 댓글 내용을 보내기위해 result에 내용을 담는다
		result.put("success", true);
		//로그인 성공
		result.put("rno", rno);
		//해당 댓글 고유 번호
		result.put("writeNickname", loginMember.getNickname());
		//글쓴이
		result.put("content", content);
		//댓글 내용
		
		//Json에게 전송
		sendJson(response,result);
		
		
		return false;
	}
	
	
	
	
	
	private void sendJson(HttpServletResponse response, Map<String, Object> result) {
		//sendJson에 들어갈 내용
		
		response.setContentType("application/json; charset=utf-8");
		//브라우저에게 지금 보낼 데이터는 utf-8로 인코딩된 json 데이터라고 알려준다
		
		Gson gson = new Gson();
		//구글에서 만든 gson객체 생성
		
		String json = gson.toJson(result);
		//result 객체를 json 형식으로 데이터를 변환
		//result안에 담겨있는 내용들을 알아서 \" 같은게 붙은 json언어로 변환해주는 과정
		
		try {
			PrintWriter out = response.getWriter();
			//브라우저로 데이터를 출력할수 있는 스트림(통로)를 연다
			
			out.print(json);
			//호출한 클라이언트 변환된 json 데이터를 비동기식으로 보낸다
			//사용자에게 비동기식으로 해당 내용들이 보이게 된다
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	
	
	
	

}
