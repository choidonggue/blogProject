
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBUtil;

public class MemberDAO {
	
	/*public boolean idCheck(String id) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		String sql = "select id from member where id=?";
		
		boolean isDuplicate = false;
		
		try {
			
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery(sql);
			if(rs.next()) {
				//검색결과가 있으면 트루를 출력
				return true;
				
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return isDuplicate;
		
	}*/
	
	
	public boolean idCheck(String id) {
		
		String sql = "select id from member where id=?";
		//아이디 테이블에서 ?의 값을 가져온다
		
	
		boolean isDuplicate = false;
		//우선 중복이냐? 란 대답을 거짓으로 지정
		
		try (Connection conn = DBUtil.getInstance();
				PreparedStatement pstmt = conn.prepareStatement(sql)){
				pstmt.setString(1, id);
				//사용자가 입력한 id를 db에서 가져온다
				try(ResultSet rs = pstmt.executeQuery()){
					//rs라는곳에 db에서 가져온 값을 대입한다
					isDuplicate = rs.next();
					//rs.next()라는것은 불린형식을 가지게 되면 select한 데이터가 db에 있으면 true를 리턴한다
					//그래서 true 가 리턴된단건 똑같은 id가 있다는 말이니 아이디가 중복된단것을 의미한다
					//아이디가 없다면 기본상태인 false가 리턴될것
				}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return isDuplicate;
		//트루와 퍼스중 하나가 리턴
	}
	
	
	public void memberInsert(MemberDTO dto) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "insert into member (ID,PWD,NICKNAME,EMAIL) values (?,?,?,?)";
		
		
		try {
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2,dto.getPwd());
			pstmt.setString(3,dto.getNickname());
			pstmt.setString(4, dto.getEmail());
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
	        // [여기가 추가된 finally 블록입니다]
	        // 실행 역순으로 자원을 안전하게 닫아줍니다 (pstmt -> conn)
	        
	        try {
	            if (pstmt != null) {
	                pstmt.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		
	}
	
	

	public MemberDTO login(String id) {
		//로그인시 아이디와 비밀번호를 조회해서 비교하는 과정을 거친다
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select id,pwd,nickname,email,to_char(join_date,'YYYY--MM--DD') as join_date from member where id = ?";
		//to_char(join_date,'YYYY--MM--DD') as join_date는 날짜값을 문자열로 바꿔서 가져오기 위함이다
		
		MemberDTO dto = null;
		
		try {
			
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				//while을 안쓰는 이유는 딱 한개의 데이터만 찾을것이기 떄문
				
				dto = new MemberDTO();
				dto.setEmail(rs.getString("email"));
				dto.setId(rs.getString("id"));
				dto.setJoin_date(rs.getString("join_date"));
				dto.setNickname(rs.getString("nickname"));
				dto.setPwd(rs.getString("pwd"));
				//암호화된 비밀번호가 저장되게 된다
			}
			
			
		}catch(Exception e) {
			
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null)conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return dto;
		
	}
	
	
	
	
}






	
	
	
	
	
	
	
	


