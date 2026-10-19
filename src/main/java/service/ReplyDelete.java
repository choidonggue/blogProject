package service;

//댓글 삭제 서비스문

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

public class ReplyDelete implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		request.setCharacterEncoding("utf-8");
		
		//로그인한 사용자만 댓글 제거 가능하게 한다
		
		HttpSession session = request.getSession();
		MemberDTO dto = (MemberDTO)session.getAttribute("loginMember");
		
		Map<String,Object> result = new HashMap<String, Object>();
		//맵형식으로 json에게 주는 이유는 json은 기본적으로 맵형식으로 데이터를 처리하기 때문이고
		//맵 형식으로 데이터를 받게되면 데이터타입을 일일이 붙여가며 처리할 필요가 없다고 한다
		//이유는 json쪽에서 알아서 데이터형에 맞게 바꿔주기 때문이라고 한다.
		
		if(dto == null) {
			//세션이 없다면. 즉 로그인 안되어있으면
			result.put("success", false);
			result.put("message", "로그인 해라");
			sendJson(response,result);
			//해당 Json메서드는 아래에서 처리한다
			return false;
		}
		
		int rno = Integer.parseInt(request.getParameter("rno"));
		//넷상에서 사용자가 댓글 삭제를 누르면 해당 댓글의 고유 번호가 넘어오게된다.
		
		int cnt = new ReplyDAO().delete(rno, dto.getId());
		//무사히 댓글처리 함수가 실행되면 0보다 큰 값이 돌아오게 된다
		
		if(cnt > 0) {
			result.put("success", true);
		}else {
			//작성자가 안맞거나하면 결과적으로 삭제를 거부할테니 0보다 작은값이 돌아오게 된다
			result.put("success", false);
			result.put("message", "자신의 댓글만 삭제 가능");
		}
		
		sendJson(response,result);
		//위에서 나온 success의 값을 밑의 json에게 전송한다
		//이렇게 하는 이유는 json은 실패했을때 콘솔창에 오류가 안나오기에
		//이런식으로 특수한 방법으로 에러를 확인해야한다
		//json은 그냥 빈값이 왔기에 빈창으로 냅뒀다가 제대로 처리했다는 착각을 하기에 생기는 문제다
		
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
