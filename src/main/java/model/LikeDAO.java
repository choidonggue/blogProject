package model;

//좋은가요 처리 DAO

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBUtil;

public class LikeDAO {
	
	public boolean isLiked(int bno, String memberId) {
		//좋은가요를 회원이 눌렀는지 확인하는 메서드문
		
		
		
		
		String sql= "select 1 from board_like where bno = ? and member_id = ?";
		//해당 조건을 만족하면(게시글 번호와 좋은가요 누른 유저) 1을 출력해서 보여줘라
		
		try (Connection conn = DBUtil.getInstance();
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, bno);
			pstmt.setString(2, memberId);
			
			try (ResultSet rs = pstmt.executeQuery()){
				
				return rs.next();
				//셀렉트 결과로 1이 검색이 되었으면 true를 리턴하여라
						
			}
		
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return false;
		//셀렉트 결과가 없을경우.
		
	}

	
	//좋은가요 토글 : 안 눌렀으면 추가, 이미 눌렀으면 취소
	//board_like 변경 + board.like_cnt 증감을 한 묶음(트랜잭션)으로 처리
	public void toggle(int bno,String memberId) {
		
		String checkSql = "select 1 from board_like where bno = ? and member_id = ?";
		//좋은가요를 눌른 사용자가 이미 이 글에 좋은가요를 눌렀는지 확인. 이래야 토글을 할지 안할지 결정 가능
		String insertSql = "insert into board_like (bno,member_id) values (?,?)";
		//좋은가요 기록 테이블에 좋은가요라는 데이터를 추가
		String deleteSql = "delete from board_like where bno = ? and member_id = ?";
		//좋은가요 기록 테이블에 좋은가요라는 데이터를 삭제
		String plusSql = "update board set like_cnt = like_cnt+1 where bno =?";
		//실제 게시글 테이블(board)에 좋은가요 총 개수를 1 올린다 
		String minusSql = "update board set like_cnt = like_cnt-1 where bno = ?";
//		실제 게시글 테이블(board)에 좋은가요 총 개수를 1 내린다
		
		
		
		//아래 기법은 트랜잭션 기법이라 한다
		//결과가 좋지 않으면 원래대로 되돌리는 기법이라 한다
		try (Connection conn = DBUtil.getInstance()){
			
				conn.setAutoCommit(false);
				//자동 커밋 끄기
				//자바는 원래 sql을 실행마다 즉시 db에 영구 저장을 한다
				//해당 좋은가요 기능은 기록 테이블 변경과 게시글 카운트 변경이라는
				//2가지 작업이 동시에 완성되어야 하기에 하나만 성공하면
				//크게 오류가 날 수 있어서 자동 커밋을 끄고 수동으로 한다
				try {
					boolean exists;
				
				
				
				try (PreparedStatement pstmt = conn.prepareStatement(checkSql)){
					pstmt.setInt(1, bno);
					pstmt.setString(2, memberId);
					try(ResultSet rs = pstmt.executeQuery()){
						exists = rs.next();
						//일단 좋은가요를 이미 눌렀는지 확인해본다
						//눌렀으면 해당 테이블에 1이 있을것이다
						
					}
				}
				
				try(PreparedStatement pstmt = conn.prepareStatement(exists ? deleteSql:insertSql)){
					//3항 연산자로 좋은가요를 이미 누른 상황이면(pstmt에 값이 있어서 true)
					//deleteSql을 시행하여 해당 좋은가요를 삭제하는 과정을 거친다
					//안누른 상황이면 insert문을 실행하여 좋은가요를 추가한다
				pstmt.setInt(1, bno);
				pstmt.setString(2, memberId);
				pstmt.executeUpdate();
				
				} 
				
				try(PreparedStatement pstmt = conn.prepareStatement(exists ? minusSql:plusSql)){
					//마찬가지로 pstmt가 존재한다면 minuSql을 실행해 좋은가요 갯수를 줄인다
					//pstmt가 없다면 좋은가요 갯수를 늘린다
					
					pstmt.setInt(1, bno);
					pstmt.executeUpdate();
					
					}
				
				conn.commit();
				//Db작업과 화면의 카운트 올리기 두 작업이 모두 성공 했을때만 한꺼번에 확정 저장
			
			
		} catch (SQLException e) {
			// TODO: handle exception
			conn.rollback();
			//두 작업 중 하나라도 실패하면 둘 다 없던일로 되돌린다
			throw e;
		}
		
	}catch(SQLException e) {
		e.printStackTrace();
	}
	
	
	
	
	
	}
	
	
	//현재 좋은가요 갯수 조회
	
	public int selectLikeCnt(int bno) {
		
		String sql ="select like_cnt from board where bno = ?";
		//특정 게시글에서 좋은가요 갯수를 뽑아서 보여달라
		
		try(Connection conn = DBUtil.getInstance();
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setInt(1, bno);
			
			try (ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					//특정 게시글을 뽑아내는데 성공했으면
					return rs.getInt("like_cnt");
					//해당 게시글의 좋은가요 갯수를 뽑아서 돌려줘라
				}
			}
				
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return 0;
			//좋은가요가 없거나 실패시 0을 돌려줘라
			
		}
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//class end

