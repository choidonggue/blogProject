package util;

import java.sql.Connection;
import java.sql.DriverManager;

// DB에 접속하는 클래스
public class DBUtil {

    // DB접속위해 java와 DB간의 연결점을 설정하는 과정
    public static Connection getInstance() {
        // 연결부터 시작해서 가장 먼저 실행할 것이기 때문에 static을 붙임
        // jar파일(오라클 연동)에 들어있는 인터페이스인 Connection을 임포트
        Connection conn = null;
        
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String id = "ai2gi";
        String pw = "1234";

        try {
            Class.forName(driver);
            // 오라클 드라이버 로딩
            conn = DriverManager.getConnection(url, id, pw);
            // DB접속
        } catch (Exception e) {
            // 에러 내용을 콘솔에 상세히 출력하도록 수정
            e.printStackTrace(); 
        }
        
        return conn;
    }
}