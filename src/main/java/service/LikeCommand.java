package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.LikeDAO;
import model.MemberDTO;
import model.ReplyDAO;

public class LikeCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		MemberDTO dto = (MemberDTO)session.getAttribute("loginMember");
		
		Map<String,Object> result = new HashMap<String,Object>();
				
				if(dto == null) {
					//로그인 안됬으면 경고 후에 더이상 실행 안되게 함
					result.put("success", false);
					result.put("message", "로그인 하라");
					sendJson(response,result);
					return false;
				}
				int bno = Integer.parseInt(request.getParameter("bno"));
				//해당 게시글의 번호를 받아와라
				
				LikeDAO dao = new LikeDAO();
				dao.toggle(bno, dto.getId());
				//사용자가 좋은가요 버튼을 이미 눌렀으면 취소 아니라면 좋은가요 상태로 만들어준다(toggle = 전환)
				//게시글 번호와 유저를 받아서 toggle메서드를 실행(DAO에 존재)
				
				result.put("success", true);
				result.put("liked", dao.isLiked(bno, dto.getId()));
				result.put("likedCount",dao.selectLikeCnt(bno));
				//무사히 마쳤으면 성공했단 말과 isLiked에서 처리된 결과(좋은가요 취소나 적용)를 적용
				//좋은가요 갯수를 증가시키거나 감소시키는 selectLikeCnt함수를 실행해서 
				//result 객체에 저장시켜서 view.jsp의 ajax에 전달위해 대기
				
				sendJson(response,result);
				//view.jsp의 ajax에 전달
		
		
		
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
