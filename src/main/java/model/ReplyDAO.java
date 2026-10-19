package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;

//댓글 처리 메서드문

public class ReplyDAO {
	
	public int insert(ReplyDTO dto) {
		//댓글을 서버에 저장하고 해당 댓글의 고유 번호를 가져오는 방법
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "insert into reply (rno,bno,writer,content) values (seq_reply.nextval,?,?,?)";
		
		int rno = -1;
		//성공 여부를 위한 rno선언
		
		
		try {
			
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBno());
			pstmt.setString(2, dto.getWriter());
			pstmt.setString(3, dto.getContent());
			pstmt.executeUpdate();
			//이러면 db에 댓글이 저장됨
			
			
			
			try {
				//클라이언트에게 비동기식으로 댓글을 보여주기 위한 트라이문
				PreparedStatement pstmt2 = conn.prepareStatement("select seq_reply.currval from dual");
				//sql문을 프리페어문에 직접 적어넣는 방법을 사용
				//reply의 현재 시퀀스값을 추출해서 dual테이블에서 보여달라 
				//(dual : 오라클에서 제공하는 가상 테이블 / currval : current value)
				ResultSet rs =pstmt2.executeQuery();
				if(rs.next()) {
					rno = rs.getInt(1);
					//무조건 1개 행만 가져올텐데 여기서 첫번째 컬럼의 값을 가져와라.(bno의 값만 가져올 것이다)
					
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		
		return rno;
		
	}
	
	
	
	
	//댓글 전체 조회
	
	public List<ReplyDTO> selectList(int bno){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select r.rno, r.bno, r.writer, m.nickname, r.content, to_char(r.reg_date,'YYYY-MM-DD') as reg_date\r\n"
				+ "from reply r\r\n"
				+ "join member m\r\n"
				+ "on r.writer = m.id\r\n"
				+ "where r.bno = ?\r\n"
				+ "order by r.rno asc";
				
		
		List<ReplyDTO> list = new ArrayList<ReplyDTO>();
		
		try {
			
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,bno);
			rs= pstmt.executeQuery();
			
			while(rs.next()) {
				
				ReplyDTO dto = new ReplyDTO();
				
				dto.setBno(rs.getInt("bno"));
				dto.setContent(rs.getString("content"));
				dto.setRegDate(rs.getString("reg_date"));
				dto.setNickName(rs.getString("nickname"));
				dto.setRno(rs.getInt("rno"));
				dto.setWriter(rs.getString("writer"));
				
				list.add(dto);
				
			}
		
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			
		
	}
	
		return list;
	
	
	
	
	
	}
	
	
	
	
	
	//댓글 삭제
	//댓글의 고유 번호와 작성자만 지울 수 있도록 writer를 같이 받아옴
	
	public int delete(int rno,String writer) {
		
		String sql = "delete from reply where rno = ? and writer = ?";
		
		int result = 0;
		
		try(Connection conn = DBUtil.getInstance();
				PreparedStatement pstmt = conn.prepareStatement(sql)
						){
			
			pstmt.setInt(1, rno);
			pstmt.setString(2, writer);
			result = pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		return result;
		
	}
	
	
	
	//댓글 수정
	
	public int update(int rno, String content, String writer) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "update reply set content = ? where rno = ? and writer = ?";
		
		int result = 0;
		
		try {
			
			conn=DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content);
			pstmt.setInt(2, rno);
			pstmt.setString(3, writer);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		
	}
	
		
	
	return result;
	
	
	
	
	}
	
	
	public List<ReplyDTO> selectListByWriter(String writer){
		//마이페이지에서 내가 쓴 댓글 조회하는 메서드


		String sql = "select r.rno,r.bno,b.title as board_title,r.content,\r\n"
				+ "        to_char(r.reg_date,'YYYY-MM-DD') as reg_date \r\n"
				+ "from reply r\r\n"
				+ "join board b\r\n"
				+ "on r.bno = b.bno\r\n"
				+ "where r.writer = ?\r\n"
				+ "order by r.bno desc";
		
		List<ReplyDTO> list = new ArrayList<ReplyDTO>();
		
		try (Connection conn = DBUtil.getInstance();
				PreparedStatement pstmt = conn.prepareStatement(sql)
				
				){
			
			pstmt.setString(1, writer);
			
			try(ResultSet rs = pstmt.executeQuery()){
			
				while(rs.next()) {
					
					ReplyDTO dto = new ReplyDTO();
					
					//select r.rno,r.bno,b.title as board_title,r.content,\r\n"
					//+ "        to_char(r.reg_date,'YYYY-MM-DD') as reg_date 
					
					dto.setRno(rs.getInt("rno"));
					dto.setBno(rs.getInt("bno"));
					dto.setBoardTitle(rs.getString("board_title"));
					dto.setContent(rs.getString("content"));
					dto.setRegDate(rs.getString("reg_date"));
					
					
					
					list.add(dto);
					
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return list;
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
}	
	


