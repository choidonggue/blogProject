package model;

//보드 관련 DAO문 작성장소

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;

public class BoardDAO {
	
	public void insert(BoardDTO dto) {
		
		//글쓰기 파트를 담당하는 인서트문
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "insert into board (bno,title,content,writer,img_path) values(seq_board.nextval,?,?,?,?)";
				//DB에서는 img_path로 만들었기떄문에 저리한다.
				//java서는 관례때문에 _없애고 imgPath로 지정(애초에 _가 안될거다)
		
		//혹여 내용을 담는 과정 실패시 -1을 리턴해서 실패했음을 알림
		//bno는 시퀀스로 인해서 1,2,3,4... 순으로 쭉 담겨간다
		
		try {
			
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2,dto.getContent());
			pstmt.setString(3, dto.getWriter());
			pstmt.setString(4, dto.getImgPath());
			pstmt.executeUpdate();
			//실행한 결과를 bno에 담아서 리턴한다
			//insert가 정상 처리되면 0보다 큰 값이 리턴된다
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		
		
	}

	
	//검색 조건에 맞는 where 절 만들기 (selectCount, selectList 둘 다 같은 조건을 써야 해서 분리)
	private String buildWhere(String searchType, String keyword) {
		//sql에 검색조건을 되돌려주기위한 시발점
		if(keyword == null || keyword.trim().isEmpty()) {
			//만약 아무런 데이터가 넘어오지 않았으면(null)또는 사용자가 공백만 입력했을 시에 그것을 제거하고 아무런 값이 없다면
			return "";
			//그냥 공백을 되돌려줘라
			//만약 입력된 값이 있다면 if문을 통과해서 아래의 where절을 시행해라
		}
		
		
			if("title".equals(searchType)){
				return " where b.title like ? ";
				//만약 제목으로써 검색하라고 한다면 위의 sql문에 해당 내용을 추가해서 검색한다
			}else if("content".equals(searchType)) {
				return " where b.content like ? ";
				//내용으로 검색하라 한다면.
			}else if("writer".equals(searchType)) {
				return " where m.nickname like ? ";
				//작성자로 검색하라 한다면.
		}
			return "";
			
	}
	
	//검색 조건에 맞는 전체 글 개수
	public int selectCount(String searchType, String keyword) {
		String where = buildWhere(searchType, keyword);
		String sql = "select count(*) from board b join member m on b.writer = m.id" + where;
		//만약 검색 조건을 걸었으면 where절이 붙게된다
		try (Connection conn = DBUtil.getInstance();
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			if (!where.isEmpty()) {
				//만약 where절이 비어있지 않으면
				pstmt.setString(1, "%"+keyword+"%");
				//물음표 부분에 키워드를 집어넣어라
				//앞뒤에 어떤 내용이 오더라도 검색할수 있게 %도 붙임
			}
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					return rs.getInt(1);
				
			}
		}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}
	//게시글 목록 조회(검색 + 페이징)
	//startRow 가져올 시작 순번(1부터)
	//endRow 가져올 끝 번호
	//한 페이지 9개 일 시 2페이지 -> startRow=10,endRow=18
	//시작번호가 10부터고 1페이지에 9개 들어가게 2페이지란 의미인듯
	
	
	public List<BoardDTO> selectList(String searchType, String keyword,int startRow, int endRow) {
		
		//list.jsp의 게시글 여러개를 띄우는 방법과
		//특정 조건으로 검색하는 방법(제목,작성자,내용 검색 같은)
		//그리고 특정조건으로 검색시 페이징 처리를 위한 row값도 받아온다
		
		String where = buildWhere(searchType,keyword);
		//사용자가 특정 검색 조건을 입력시 그 매개변수를 가져다 쓴다
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from (\r\n"
				+ "    select rownum as rn, t.* from (  \r\n"
				+ "        select b.bno,b.title,b.writer,m.nickname as writer_nickname,\r\n"
				+ "        to_char(b.reg_date,'YYYY-MM-DD') as reg_date,b.view_cnt,b.like_cnt,b.img_path\r\n"
				+ "        from board b\r\n"
				+ "        join member m\r\n"
				+ "        on b.writer = m.id\r\n"
				+ where
				+ "        order by b.bno desc    \r\n"
				+ "        ) t where ROWNUM <= ?    \r\n"
				+ "    ) where rn >= ?";
		//위에서 적어둔 검색조건(where)을 대입해서 각 페이징 번호를 대입하기위한 sql문
		//참고로 where절안에도 ?가 있기에 아래 대입해야할 물음표는 3개가 된다
		
		
			
			
		
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		
		try {
			
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			
			//?부분 대입
			int idx = 1;
			//초기값 1선언
			if(!where.isEmpty()) {
				
				pstmt.setString(idx++, "%"+keyword+"%");
				//초기치는 1로 지정되어 있고 후위 연산자라 
				//첫번째 ?자리에는 키워드란 값을 where에 대입하고
				//idx를 1증가
			}
			pstmt.setInt(idx++, endRow);
			//증가되어 idx값이 2가 되었고, 2번째 ?부분에 endRow의 값을 대입하라
			pstmt.setInt(idx, startRow);
			//마지막으로 증가된 idx값 3을 사용해서 3번째 ?에 startRow값을 대입해라
			
			//만약 검색조건이 없으면 where절에 값을 대입하지 않고 if문을 건너뛰어서
			//endRow부분에 1이 대입되게 될것이다
			
			
			
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setBno(rs.getInt("bno"));
				dto.setWriter(rs.getString("writer"));
				dto.setTitle(rs.getString("title"));
				dto.setWriterNickname(rs.getString("writer_nickname"));
				dto.setImgPath(rs.getString("img_path"));
				dto.setRegDate(rs.getString("reg_date"));
				dto.setViewCnt(rs.getInt("view_cnt"));
				dto.setLikeCnt(rs.getInt("like_cnt"));
				list.add(dto);
				
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		finally {
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

public BoardDTO selectOne(int bno) {
	//list.jsp의 게시글에서 1개의 게시글 선택시 그 세부내용 창을 띄우는 방법
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select b.bno, b.title,b.content, b.writer, m.nickname as writer_nickname,\r\n"
				+ "        to_char(b.reg_date,'YYYY-MM-DD') as reg_date, b.view_cnt, b.like_cnt, b.img_path\r\n"
				+ "from board b\r\n"
				+ "join member m\r\n"
				+ "on b.writer = m.id\r\n"
				+ "where bno = ?\r\n";
		
		//bno의 값만 대입하는 sql문
		
		BoardDTO dto = null;
		//전역변수로 dto를 선언해야 가비지 컬렉터에 의해서 dto가 증발 안하므로 여기서 생성해준다
		//딱 한개만 옮기는것이기에 list안쓴다
		
		try {
			
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto = new BoardDTO();
				dto.setBno(rs.getInt("bno"));
				dto.setWriter(rs.getString("writer"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setWriterNickname(rs.getString("writer_nickname"));
				dto.setImgPath(rs.getString("img_path"));
				dto.setRegDate(rs.getString("reg_date"));
				dto.setViewCnt(rs.getInt("view_cnt"));
				dto.setLikeCnt(rs.getInt("like_cnt"));
				
				
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			
			}
		
	}
	
	return dto;
	
	
}


//view_cnt(조회수 증가)

public void increViewCnt(int bno) {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	
	String sql = "update board set view_cnt = view_cnt + 1 where bno = ?";
	
	try {
		
		conn = DBUtil.getInstance();
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bno);
		pstmt.executeUpdate();
		
	
		
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}finally {
		try {
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
			
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		
		}
	
	
	}
	
}


//view에서 게시글 삭제 메서드

public String delete(int bno, String writer) {
	//지울 게시글 번호와, 글쓴사람만 지울수 있도록 글쓴이를 받아온다
	
	String sql2 = "select img_path from board where bno = ? and writer = ?";
	//내 컴퓨터의 이미지를 삭제하기 위한 sql문
	String sql = "delete from board where bno = ? and writer = ?";
	//게시글을 삭제하기 위한 sql문
	
	String imgPath = null;
	
	try(Connection conn = DBUtil.getInstance()) {
		//DB접속문은 전역으로 사용하겠다
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql2)){
			//내 컴퓨터의 이미지를 삭제하는 sql2를 실행
		
		pstmt.setInt(1, bno);
		pstmt.setString(2, writer);
		//해당 게시글 이름과 작성자를 찾는다
		
		try (ResultSet rs = pstmt.executeQuery()){
			//일단 조회를 해서 그 조회된 게시글에서 이미지 경로를 추출하는 과정
			if(rs.next()) {
				//한개만 추출될거다. 즉 추출된게 있다면 경로를 추출해내라
				imgPath = rs.getString("img_path");
			}else {
				return null;
				//딱히 찾아진게 없으면 null을 되돌려줘서 오류는 막음
			}
		}
		}
	
	try(PreparedStatement pstmt = conn.prepareStatement(sql)){
		pstmt.setInt(1, bno);
		pstmt.setString(2, writer);
		pstmt.executeUpdate();
		//위에서 이미지 경로를 추출해냈으니 이제 게시글을 삭제하는 과정을 거친다
	}
		
		
		
	}catch(Exception e){
		e.printStackTrace();
		
	}
	return imgPath;
	
	
	
}




//작성글 수정하기 메서드


public void update(BoardDTO dto) {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	
	
	
	//dto에 저장된 값을 불러와서 업데이트하면 수정된다
	
	String sql = "update board set title = ?,content = ?, img_path = ?, mod_date = sysdate where bno = ? and writer = ?";
	//제목,내용,그림 저장 경로, 수정 날짜를 수정할건데, 작성자만 수정할수 있게끔하고 게시글의 고유 번호도 가져와서 어떤 게시글을 고칠건지 안다
	
	
	try {
		
		conn = DBUtil.getInstance();
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,dto.getTitle());
		pstmt.setString(2,dto.getContent());
		pstmt.setString(3,dto.getImgPath() );
		pstmt.setInt(4,dto.getBno() );
		pstmt.setString(5,dto.getWriter() );
		pstmt.executeUpdate();
		
		
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



}


//인기글 파트

public List<BoardDTO> selectPopularList(){
	//인기글을 대략 6개정도 뽑아서 출력하는 방법
	
	String sql = "select * from \r\n"
			+ "    (select b.bno,b.title,m.nickname as writer_nickname,\r\n"
			+ "    to_char(b.reg_date,'YYYY-MM-DD') as reg_date,\r\n"
			+ "    b.view_cnt,b.like_cnt,b.img_path\r\n"
			+ "    from board b\r\n"
			+ "    join member m\r\n"
			+ "    on b.writer = m.id\r\n"
			+ "    order by b.like_cnt desc, b.view_cnt desc,b.bno desc) \r\n"
			+ "where rownum <= 6";
	//우선 괄호안부터 처리해서 전체 글들을 조회수 순으로 내림차순으로 전부 뽑아서 정렬 후
	//그것중에서 상위 6개만 보여준단 의미
	
	
	List<BoardDTO> list = new ArrayList<BoardDTO>();
	
	try (Connection conn = DBUtil.getInstance();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()){
		
		while(rs.next()) {
			
			BoardDTO dto = new BoardDTO();
			
			
			dto.setBno(rs.getInt("bno"));
			dto.setTitle(rs.getString("title"));
			dto.setWriterNickname(rs.getString("writer_nickname"));
			dto.setRegDate(rs.getString("reg_date"));
			dto.setViewCnt(rs.getInt("view_cnt"));
			dto.setLikeCnt(rs.getInt("like_cnt"));
			dto.setImgPath(rs.getString("img_path"));
			
			list.add(dto);
			
			
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	
	return list;
	
	
	
}


public List<BoardDTO> selectNewList(){
	//최신글을 3개정도 출력하는 방법(인덱스 페이지에서 보인다)
	
	String sql = "select * from \r\n"
			+ "    (select b.bno,b.title,\r\n"
			+ "    to_char(b.reg_date,'YYYY-MM-DD') as reg_date,\r\n"
			+ "    b.img_path\r\n"
			+ "    from board b\r\n"
			+ "    join member m\r\n"
			+ "    on b.writer = m.id\r\n"
			+ "    order by reg_date desc) \r\n"
			+ "where rownum <= 3\r\n";
	
	List<BoardDTO> list = new ArrayList<BoardDTO>();
	
	try (Connection conn = DBUtil.getInstance();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()
			){
		
		while(rs.next()) {
		
		BoardDTO dto = new BoardDTO();
		
		dto.setBno(rs.getInt("bno"));
		dto.setTitle(rs.getString("title"));
		dto.setRegDate(rs.getString("reg_date"));
		dto.setImgPath(rs.getString("img_path"));
		
		list.add(dto);
		
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	
	return list;
	
	
}


public List<BoardDTO> selectPopularList2(){
	//인기글을 대략 3개정도 뽑아서 출력하는 방법
	
	String sql = "select * from \r\n"
			+ "    (select b.bno,b.title,m.nickname as writer_nickname,\r\n"
			+ "    to_char(b.reg_date,'YYYY-MM-DD') as reg_date,\r\n"
			+ "    b.view_cnt,b.like_cnt,b.img_path\r\n"
			+ "    from board b\r\n"
			+ "    join member m\r\n"
			+ "    on b.writer = m.id\r\n"
			+ "    order by b.like_cnt desc, b.view_cnt desc,b.bno desc) \r\n"
			+ "where rownum <= 3";
	//우선 괄호안부터 처리해서 전체 글들을 조회수 순으로 내림차순으로 전부 뽑아서 정렬 후
	//그것중에서 상위 6개만 보여준단 의미
	
	
	List<BoardDTO> list = new ArrayList<BoardDTO>();
	
	try (Connection conn = DBUtil.getInstance();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()){
		
		while(rs.next()) {
			
			BoardDTO dto = new BoardDTO();
			
			
			dto.setBno(rs.getInt("bno"));
			dto.setTitle(rs.getString("title"));
			dto.setWriterNickname(rs.getString("writer_nickname"));
			dto.setRegDate(rs.getString("reg_date"));
			dto.setViewCnt(rs.getInt("view_cnt"));
			dto.setLikeCnt(rs.getInt("like_cnt"));
			dto.setImgPath(rs.getString("img_path"));
			
			list.add(dto);
			
			
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	
	return list;
	
	
	
}





public List<BoardDTO> selectListByWriter(String writer){
	//마이페이지의 내가 쓴글을 조회하는 메서드
	
	String sql = "select bno,title,to_char(reg_date,'YYYY-MM-DD') as reg_date,\r\n"
			+ "        view_cnt,like_cnt,img_path\r\n"
			+ "from board\r\n"
			+ "where writer = ?\r\n"
			+ "order by bno desc";
	
	List<BoardDTO> list = new ArrayList<BoardDTO>();
	
	try (Connection conn = DBUtil.getInstance();
			PreparedStatement pstmt = conn.prepareStatement(sql)
			//try 바로 옆에 괄호를 붙여서 처리하는 방법을 쓰는데
					//만약 매개변수로 값을 받아와서 sql문에 대입하는 방법을 쓴다면
					//한방에 rs까지 선언하면 안되고 우선 pstmt까지만 선언 후에
					//?에 들어갈 자리를 만드는 pstmt문을 선언후에
					//아래쪽에 다시 try를 선언해서 rs를 집어넣는 과정을 거쳐야한다
					//이유는 sql문을 실행하는 execute는 완성된 sql문장에 적용해야
					//제대로 돌아가기 때문에 순서상 sql문에 매개변수를 대입해 완성후에
					//execute를 선언해야 하는것이다
			){
			
		pstmt.setString(1, writer);
		
		
		try(ResultSet rs = pstmt.executeQuery()){
			while(rs.next()) {
				
				BoardDTO dto = new BoardDTO();
				
				//select bno,title,to_char(reg_date,'YYYY-MM-DD') as reg_date,\r\n"
				//+ "        view_cnt,like_cnt,img_path\
				
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setRegDate(rs.getString("reg_date"));
				dto.setViewCnt(rs.getInt("view_cnt"));
				dto.setLikeCnt(rs.getInt("like_cnt"));
				dto.setImgPath(rs.getString("img_path"));
				
				list.add(dto);
				
			}
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	
	return list;
	
	
	
}



public List<BoardDTO> selectLikedList(String memberId){
//마이페이지의 내가 좋은가요 한 글 조회 메서드

	
	String sql = "select b.bno,b.title,m.nickname as writer_nickname,b.like_cnt,b.img_path\r\n"
			+ "from board_like l\r\n"
			+ "join board b\r\n"
			+ "on l.bno = b.bno\r\n"
			+ "join member m\r\n"
			+ "on b.writer = m.id\r\n"
			+ "where l.member_id = ?\r\n"
			+ "order by l.like_date desc";
	
	List<BoardDTO> list = new ArrayList<BoardDTO>();
	
	try (Connection conn = DBUtil.getInstance();
			PreparedStatement pstmt = conn.prepareStatement(sql)
			
			){
		
		pstmt.setString(1, memberId);
		
		try(ResultSet rs = pstmt.executeQuery()){
			
			
			while(rs.next()) {
				
				BoardDTO dto = new BoardDTO();
				
				//select b.bno,b.title,m.nickname as writer_nickname,b.like_cnt,b.img_path\r\n
				
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setWriterNickname(rs.getString("writer_nickname"));
				dto.setLikeCnt(rs.getInt("like_cnt"));
				dto.setImgPath(rs.getString("img_path"));
			
				
				list.add(dto);
				
			}
			
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	
	return list;
	
	
}







}//class


















