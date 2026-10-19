
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%
    // 컨트롤러가 데이터를 담아서 보냈는지 확인합니다.
    Object listCheck = request.getAttribute("newlist");
	//오브젝트라는 최상위 조상으로 변수형을 선언하면 어떤형태로 변수를 담았어도 다 포함하기에
	//오브젝트로써 변수 선언을 한다.
	//newlist라는 속성이 NewList서비스문에서 제대로 담겨져 왔으면 컨트롤러를 거쳤다는 의미다
    
    // 만약 데이터 상자가 비어있다면, 자바 컨트롤러를 안 거쳐왔다는 뜻이므로 토스합니다.
    if (listCheck == null) {
        response.sendRedirect(request.getContextPath() + "/Main/new");
        //위의 서비스문을 거치지 않고 왔으면 강제로 컨트롤러로 보내서 /Main/new를 거치도록 한다
        return; 
    }
    // 데이터가 이미 있다면(listCheck != null) 이 조건문을 통과하므로 무한 루프가 완전히 차단됩니다!
%>
	

<!-- 이걸 실행시키면 링크걸은 부분이 밑줄이 되어있는
	블로그 선택화면이 나올것이다
	인덱스의 특성상 다른데로 이동하게 해주는
	하이퍼링크의 모음을 만드는 jsp같다 -->

<%@ include file="/include/header.jsp" %>

<!-- header를 불러오는 방법 -->

	<section class="visual">
	
		<div id="carouselExample" class="carousel slide">
<div class="carousel-inner">
  <div class="carousel-item active">
    <img src="${pageContext.request.contextPath}/images/visual.jpg" class="d-block w-100" alt="...">
  </div>
  <div class="carousel-item">
    <img src="${pageContext.request.contextPath}/images/visual1.jpg" class="d-block w-100" alt="...">
  </div>
  <div class="carousel-item">
    <img src="${pageContext.request.contextPath}/images/visual2.jpg" class="d-block w-100" alt="...">
  </div>
</div>
<button class="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
  <span class="carousel-control-prev-icon" aria-hidden="true"></span>
  <span class="visually-hidden">Previous</span>
</button>
<button class="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
  <span class="carousel-control-next-icon" aria-hidden="true"></span>
  <span class="visually-hidden">Next</span>
</button>
</div>

	</section>

	<main class="content">
		<!-- 컨텐츠 영역 : 최신글,인기글 들어갈 자리
	헤더 컨텐츠 꼬리말 순으로 작성된다고 한다.
	즉 본문을 작성하는 파트
	
	class에서 .class는 중복 사용이 가능한데 #class는 중복 사용을 허용 안한다 -->
		<section class="content-box">
			<h2 class="section-title">최신글</h2>
			<!-- 본문의 사진 위에 붙이는 제목(맨 위의 사진만 적용된다) -->
			<ul class="post-card-list">
			<!-- 순서가 없는 방식으로 게시판들의 내용들을 관리하기위해 선언 -->
			
			<c:forEach var="b" items="${newlist }">
			
				<li class="post-card">
				
			<c:choose>
			<c:when test="${not empty b.imgPath}">
				<img src="/imgs/${b.imgPath }" alt="샘플이미지" class="post-thumb">
					<%-- ul내의 게시판들을 여러개 만들기위해서 li를 선언하고
					그 게시판 1개마다 이미지를 불러와서 붙이고 장애인을 위한 배려고
					샘플이미지라는 글자를 대체해서 붙여준다(alter)
					 --%>
				</c:when>	
				
				<c:otherwise>
					<img src="${pageContext.request.contextPath}/images/visual.jpg" alt="샘플이미지" class="post-thumb">
				</c:otherwise> 
					 
			 </c:choose>
					 
					<p class="post-title"><a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno }">${b.title}</a></p>
					<%-- p태그는 각 게시판의 내용마다 밑쪽에 다는 제목 요소이다.
					paragragh: 문단 이란 뜻이며
					각 게시글의 제목 영역을 의미한다 --%>
					<p class="post-date">${b.regDate}</p>
					
				</li>
				
				</c:forEach>
				
			</ul>
			<!-- 이것들을 적용하면 화면 헤더 아래에 
			최신글이란 글자와 사진, 샘플 게시글 제목입니다 , 날짜 가 출력되게 된다 
			li를 3개를 적어넣어서 밑으로 연속 세개를 출력되도록 한다-->
		</section>
		<section class="content-box">
		
			<h2 class="section-title">인기글</h2>
			<ul class="post-card-list">
			
			<c:forEach var="p" items="${popularlist}">
			
			<c:choose>
			
			<c:when test="${not empty p.imgPath}">
				<li class="post-card"><img src="/imgs/${p.imgPath }" alt="샘플이미지" class="post-thumb">
				</c:when>
				
				<c:otherwise>
				<li class="post-card"><img src="${pageContext.request.contextPath}/images/visual.jpg" alt="샘플이미지" class="post-thumb">
				</c:otherwise>
				
				</c:choose>
					<p class="post-title"><a href="${pageContext.request.contextPath}/blog/view?bno=${p.bno }">${p.title}</a></p>
					<p class="post-date">${p.regDate}</p>
				</li>
				
				</c:forEach>
			
			</ul>
			<!-- 최신글과 똑같은 형식을 적용해서
			인기글도 똑같이 출력되도록 한다-->
		</section>

	</main>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
   <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
	
	
	<%@ include file="/include/footer.jsp" %>



</body>

</html>








