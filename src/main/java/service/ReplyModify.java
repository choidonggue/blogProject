package service;

//댓글 수정 서비스문

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

public class ReplyModify implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		request.setCharacterEncoding("utf-8");
		
		//로그인한 사람만 수정 가능하게 함
		HttpSession session = request.getSession();
		MemberDTO dto = (MemberDTO)session.getAttribute("loginMember");
		
		//json에게 값을 보내기위해서 해시맵 생성
		Map<String,Object> result = new HashMap<String, Object>();
		
		if(dto == null) {
			//로그인 안됬으면 경고 후에 더이상 실행 안되게 함
			result.put("success", false);
			result.put("message", "로그인 하라");
			sendJson(response,result);
			return false;
		}
		int rno = Integer.parseInt(request.getParameter("rno"));
		String content = request.getParameter("content");
		int updateCount = new ReplyDAO().update(rno, content, dto.getId());
		//로그인이 되었으면 해당 댓글들의 내용들을 클라이언트로부터 받아와서 dao에 보내서 처리한다
		
		if(updateCount > 0) {
			//성공적으로 업데이트 처리가 되었으면
			result.put("success", true);
			result.put("content", content);
			//해당 내용을 ajax로 보내서 수정을 완료할 것
			
		}else {
			//수정 실패시
			result.put("success", false);
			result.put("message", "자신의 댓글만 수정 가능");
		}
		
		sendJson(response,result);
		//위의 result값을 json에게 보낸다
		
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
		//맵형식의 데이터를 json형식으로 바꾼다
		
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
