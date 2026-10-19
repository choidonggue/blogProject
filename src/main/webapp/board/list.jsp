
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- 이걸 실행시키면 링크걸은 부분이 밑줄이 되어있는
	블로그 선택화면이 나올것이다
	인덱스의 특성상 다른데로 이동하게 해주는
	하이퍼링크의 모음을 만드는 jsp같다 -->

<%@ include file = "/include/header.jsp" %>
<!-- 헤더를 불러오는 방법 -->

<!-- 위쪽의 헤더는 다른데서 가져왔고, 아래의 메인파트만 
검색, 글쓰기 버튼을 만들으려고 한다 -->
	
<!-- 검색창, 글쓰기 버튼 -->
	<main class="list-wrap">
	<!-- 메인파트의 모든 스타일 적용위해 list-wrap을 선언 -->
		<div class = "list-top">
		<!-- 위쪽의 선택~글쓰기 파트를 책임지는 파트 -->
			<form class = "search-form" action="${pageContext.request.contextPath}/blog/list" method = "get">
			<!-- blog/list를 하는 이유가 board/list를하면 list가 두번 실행이 되어서 그렇다네 -->
				<!-- 모든 입력받는 태그는 submit으로 보내기 떄문에 
				문서를 보내는 형식인 form을 만들어야 한다 -->
					<select name="searchType" class="search-select">
						<option value="">선택</option>
						<option value="title" ${searchType == 'title' ? 'selected' : ''}>제목</option>
						<!-- selected를 사용하면 선택박스에서 해당 내용이 먼저 선택되어있다.(현 상황에선 제목이 기본값) 
						여기서는 선택박스가 title이 선택되어 있으면 searchType변수에 해당 내용을 집어넣고
						선택이 안되어있으면 공백을 집어넣으란 말이다-->
						<option value="content" ${searchType == 'content' ? 'selected' : ''}>내용</option>
						<option value="writer" ${searchType == 'writer' ? 'selected' : ''}>글쓴이</option>
					</select>
					<!-- 선택박스를 만든다 -->
					<input type="text" name="keyword" value="${keyword}" placeholder="검색어를 입력하세요" 
					class="search-input">
					<!-- 검색어 입력칸을 만들고 연하게 검색어를 입력하세요란 글씨를 박스안에 보이도록 한다
					(place holder가 장소를 고정한단 의미인듯한데 그래서 글자가 고정되어 있나보다) 
					사용자가 입력한 내용도 keyword에 저장해서 보여주게 한다-->
					
					
					<button type="submit" class="btn btn-outline">검색</button>
					<!--검색 글쓰기 버튼을 만드는 방법(제출형식 버튼)
					스타일 적용시 btn스타일 시트도 적용하고 btn-outline스타일 시트도 적용하라는 의미
					한줄에 스타일을 2개 적용시킬수 있도록 할수 있다 -->
					<!-- 하나는 제출형식 하나는 하이퍼링크로 이동형식으로 지정위해 하나는 a태그를 사용 -->
			</form>
			
			
			
					<a href="${pageContext.request.contextPath}/blog/write" class="btn btn-primary">글쓰기</a>
					<!-- form안에 a태그가 들어가면 안된다고 한다
					이 방법을 써야지 글쓰기 버튼이 오른쪽으로 붙는 justify를 적용 가능 -->
		</div>
		
		
		<!-- 게시글 카드 목록 (이제부터 조인한 리스트 값들을 출력할것이다)-->
		<ul class = "board-grid">
		
		
		<c:forEach var = "b" items = "${boardList}">
		<%--예전에 했떤 강화 for문 비슷한거다 가져온 리스트의 dto를(여기선 dto = b로 대입) 뜯어서 출력한다 --%>
				
		
	<%-- 게시글 카드 목록  --%>
	<li class="board-card">
	
	<c:choose>
	<%--choose와 when,otherwise는 한세트로 엮여다니는 조건문이다 
	여기서 그림을 첨부하거나 첨부 안했을경우를 분리해준다--%>
	
	<c:when test="${not empty b.imgPath}">
		<img src="/imgs/${b.imgPath}" alt="게시글 썸네일" class="board-thumb">
		<%-- 클래스 이름 선언시 앞쪽에는 무엇의 클래스인가를 유추 가능한 이름도 붙여주자 
		이미지 경로는 imgs같은 임의로 변수명을 만들고 설정파일을 연결해줘야 출력된다
		servers폴더의 server.xml의 아랫부분의 경로를 수정해줘야 하더라
		
		여기서 이미지를 프로젝트가 아니고 내 컴퓨터의 C드라이브를 찾아가기 때문에 pagecontext같은것을 붙이면 안됨
		--%>
		</c:when>
		
		<c:otherwise>
		<img src="${pageContext.request.contextPath}/images/default.jpg" alt="게시글 썸네일" class="board-thumb">
		</c:otherwise>
		
		</c:choose>
		
		<div class="board-info">
		<%-- 그림에다가 링크를 걸면 list파일인데(인라인요소 = 문장요소)
		 왜 박스 형식인 그림을 감싸냐고 경고가 나온다고 한다 
		 그래서 밑의 제목쪽으로 링크를 내렸다 --%>
		  
			<p class="board-title"><a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">${b.title}</a></p>
			<!-- get 방식으로 bno를 넘겨받아서 각 게시글을 bno의 번호로써 인덱스 연결해줘야 한다 -->
			<p class="board-meta">작성자 ${b.writerNickname} | ${b.regDate}</p>
			<p class="board-meta">조회수 ${b.viewCnt} | 좋은가요 ${b.likeCnt}</p>
			<%--달러 표시안에 들은것들은 dto에 들어있었던 값들을 꺼내서 출력하는 것 --%>
		</div>
	</li>
	
</c:forEach>
	
</ul>

	<div class="pagination">
	
	<!-- 이전 
	만약 내가 6페이지라면 5페이지로 갈 수 있는 링크를 열어주고 
	1페이지라면 이전으로 갈 수 없도록 막는다-->
	<c:choose>
		<c:when test="${startPage > 1 }">
		<%--시작 페이지가 1보다 크다면 --%>
			<c:url var="preUrl" value="/blog/list">
			<%-- 이동할 주소를 조립해서(list.jsp)preUrl에 담는다--%>
				<c:param name="page" value = "${startPage -1 }"/>
				<%--만약 6페이지였다면 5페이지로 이동 --%>
				<c:param name="searchType" value = "${searchType }"/>
				<c:param name="keyword" value = "${keyword}"/>
				<%-- 검색조건과 검색어 유지--%>
				</c:url>
				<a href = "${preUrl }" class = "page-nav">이전</a>
				<%-- 클릭가능한 하이퍼 링크 생성--%>
		</c:when>
		<c:otherwise>
		<%--만약 페이지가 1번이라면 이전 버튼을 누를 수 없도록 막는다 --%>
			<span class = "page-nav disabled">이전</span>
		</c:otherwise>
		
	</c:choose>
	
	<!-- 페이지 번호 
	1~5까지로 설정되었으면 1~5까지 반복하면서 페이지 번호를 찍어낸다-->
	<c:forEach var = "p" begin = "${startPage }" end = "${endPage }">
		<c:url var="pageUrl" value="/blog/list">
				<c:param name="page" value = "${p}"/>
				<%-- 클릭한 그 페이지 숫자로 이동
				일단 5페이지까지 다 출력을 하는데, for문을 도는동안 사용자가 누른 숫자를 대입해서
				해당 페이지에 불을 켜는 방법을 쓴다
				currentPage에 저장된 숫자를 가져와서 여기에 적용시킨다
				
				param은 parameter의 약자인데
				지금 온 데이터의 값을 빼서 쓰겠다는 의미로 받아들이라고 한다--%>
				<c:param name="searchType" value = "${searchType }"/>
				<c:param name="keyword" value = "${keyword}"/>
		</c:url>
		<a href="${pageUrl }"
			class = "page-num ${p==currentPage?'active':'' }">${p }</a>
			<%--3항 연산자를 써서 내가 누른 페이지만 불이 들어오도록 한다 --%>
	</c:forEach>
	
	
		<!-- 다음 
		마지막 페이지를 넘어가면 안되기에 방어를 한다-->
	<c:choose>
	<c:when test="${endPage < totalPage }">
	<%-- 현재 끝 페이지가 최종 페이지 갯수보다 작다면--%>
			<c:url var="nextUrl" value="/blog/list">
			<%--이동할수 있는 경로를 열어서 nextUrl에 넣어준다 --%>
				<c:param name="page" value = "${endPage +1 }"/>
				<%--5페이지라면 6페이지로 갈 수 있게 해준다 --%>
				<c:param name="searchType" value = "${searchType }"/>
				<c:param name="keyword" value = "${keyword}"/>
				<%-- 검색조건을 넣어준다--%>
				</c:url>
				<a href = "${nextUrl }" class = "page-nav">다음</a>
				<%--다음 버튼에 링크를 만들어준다 --%>
		</c:when>
		<c:otherwise>
		<%-- 만약 맨 마지막 페이지라면 다음을 누를 수 없도록 한다--%>
			<span class = "page-nav disabled">다음</span>
		</c:otherwise>
	

	</c:choose>
	
	</div>


	</main>
	
	<!-- footer도 똑같이 쓸거라서 그대로 복붙 -->
	
	<footer class="site-footer">
	<!-- 꼬릿말 태그
	헤더 > 메인 > 컨텐츠 > 꼬릿말 순으로 만들어 진다 -->
	
	<div class= "footer-inner">
		<p>&copy; 2026 MY BLOG. All right reserved.</p>
		<!-- &copy는 제어문자라고 한다 
		이걸 쓰면 화면 맨 아래쪽에 동그라미안에 C가 적힌것을 볼수 있다고 한다-->
		<p class = "footer-sub">Sevlet,&nbsp;&nbsp;JSP,Oracle로 만든 개인 블로그 프로젝트</p>
		<!--웹 문서는 space bar를 많이 눌러도 한칸으로 인식.
		 &nbsp라는 것을 사용하게 되면 한개당 스페이스바 1개씩 넣는것이 가능하다 -->
	</div>
	
	</footer>
	



</body>

</html>


