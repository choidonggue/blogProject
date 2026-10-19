package util;

import java.sql.Connection;
import java.sql.SQLException;

public class DBTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	

		  
		        System.out.println("=== 오라클 데이터베이스 연결 테스트 시작 ===");
		        
		        // 1. DBmanager를 통해 연결 객체(Connection)를 가져옴
		        Connection conn = DBUtil.getInstance();
		        
		        // 2. 연결 결과 확인
		        if (conn != null) {
		            System.out.println("✅ DB 연결 성공! 통로가 정상적으로 개설되었습니다.");
		            
		            // 3. 사용이 끝난 연결은 주석에 쓰신 대로 메모리 절약을 위해 반드시 닫아줍니다.
		            try {
		                conn.close();
		                System.out.println("♻️ DB 연결이 안전하게 종료되었습니다 (자원 반납 완료).");
		            } catch (SQLException e) {
		                System.err.println("❌ DB 연결 종료 중 오류 발생:");
		                e.printStackTrace();
		            }
		            
		        } else {
		            System.out.println("❌ DB 연결 실패! DBmanager의 에러 메시지(상단 콘솔)를 확인하세요.");
		            System.out.println("💡 체크리스트: 오라클 서비스가 켜져 있는지, ID/PW가 맞는지, ojdbc 빌드 패스가 잡혀있는지 확인하세요.");
		        }
		    }
		

	}


