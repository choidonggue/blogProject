
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
    <%@ taglib uri = "jakarta.tags.core" prefix = "c" %>
    <%@ taglib uri = "jakarta.tags.functions" prefix = "fn" %>
    <!--JSP파일에서 JSTL(JSP Standard Tag Library)의 핵심(Core)기능들을
    사용하겠다고 선언하는 코드다
    
    lib폴더에서 jakarta jar파일을 불러오는 방법이다. 이걸 쓰면 %가 들어가는 이상한 형식자 안써도 된다
    prefix는 접두사인데 c로 시작하는 코어 태그를 사용하겠다. 라는 의미다. 
    참고로 url이 아니고 uri인데 jakarta의 주소를 가리키는 말이라한다
     -->
     
    
<html>

<head>

<title>블로그 프로젝트</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/mystyle.css" rel="stylesheet">

<!-- 부트스트랩이란걸 사용하기위해 viewport와 css? 란걸 붙였다 선언되는 순서가 중요해서
부트스트랩부터 선언하고 나의 css가 적용되도록함-->

<!-- 경로가 바뀌어서 한단계 위에서 css폴더를 찾아갈수 있게끔 ..같이 점을 2번 찍었다.
기본적으로 아무것도 입력안하면(css/mystyle.css)./이 앞에 붙어서 현재 위치부터로 
진행된다고 한다(./css/mystyle.css) -->
<!-- common.css의 스타일과 mystyle을 불러와서 index.jsp에 적용시키는 방법

link:외부 자원을 현재 문서와 연결하겠다
href : Hypertext Reference(하이퍼 텍스트 참조)
rel : relation(관계)

css폴더내의 common.css를 불러와라

rel="stylesheet"
이는 불러오는 파일의 성격은 스타일시트 형식이다(css형식을 적용시킬 거니까) -->

<%-- <script>const contextPath = "${pageContext.request.contextPath}";</script>--%>
<!-- join.js에서 url이 꼬이는 오류를 막기위해 이쪽에다가 변수 선언을해서 직접 경로를 적어넣는다. 

이렇게 하는 이유는 톰캣은 $로 시작하는 문법을 읽을수 있으나 jsp파일만 읽을수 있다는 제한이 있다
그래서 자바스크립트 내에다가 $경로를 집어넣으면 톰캣은 자바스크립트를 읽을수 없으므로
$가 붙은 문장 그대로 서버에게 넘겨주게 되고 서버는 저걸 읽을수가 없어서
url관련 오류를 띄워버린다.
jsp에 저런식으로 $로 된 경로를 집어넣으면 비로소 톰캣이 ${pageContext.request.contextPath} = blogproject
라는 결과로 치환해서 contextPath변수에 저장해 주기 때문에
비로소 자바스크립트에서 서버가 올바른 url을 읽을수 있게 된다.-->

</head>

<body>
	<header class="site-header">
		<div class="header-inline">
			<!-- 사이트 윗부분을 보여주는게 헤더(본문중에 윗부분) 
	헤더 태그에 클래스 이름(별칭)을 붙인다
	site-header를 검색시 header가 호출되는 변수 선언과 비슷한 개념이다-->
			<h1>
				<a href="${pageContext.request.contextPath}/Main/new">MY Blog</a>
			</h1>
			<!-- a(앵커)태그라는것은 하이퍼링크를 만드는 태그다. 마우스를 갖다대면
		손가락 모양으로 나올것이다.
		무조건 href와 같이 다닌다고 한다
		닻을 내리듯이 저쪽에 연결을 박아놓겠다 라고 인식하라고 한다 -->

			<nav class="gnb">
				<!-- nav 네비게이션 태그다. 여기는 다른 페이지로 이동하는 메뉴(링크)들이 모여 있는 곳입니다"라고 알려주는 태그
	페이지 상단에 링크들이 모여있을 것이다
	div와는 달리 의미가 있는 태그라고 한다 -->
				<ul>
					<!--
		ul(Unordered List: 순서가 없는 목록)
		> 목록 옆에 점이 찍혀서 나온다
		
		ol(Ordered List: 순서가 있는 목록)
		>목록 옆에 1.2.3.같은게 붙어서 나온다
		
		li(list item):ul과 ol에 채워넣을 목록 리스트 값들이다 
		 
	
		ul,ol,li태그는 블록 요소라서 가로 100%를 차지하기 때문에
		글자를 적으면 세로로 출력된다
		
		div태그는 블럭이 한정된 크기로 있기 때문에 글자들이 가로로 출력된다
		
		nav > ol > li 순으로 부모 자식으로 묶이는 구조를 돔구조라고 한다
		이 돔구조를 가지고 제어를 할수있다-->
					<li><a href="${pageContext.request.contextPath}/blog/list">글목록</a></li>
					<!-- 헤더에서 글목록을 모르면 list.jsp가 실행되도록 경로 설정 -->
					<!-- 만약 컨트롤러로써 jsp를 찾아가면 컨트롤러의 경로를 적는데 맨 마지막에 .jsp를 안붙여야 된다 -->
					<li><a href="${pageContext.request.contextPath}/blog/popular">인기글</a></li>
					
					
					
				</ul>
			</nav>
			<div class="member-menu">
				<!-- div(division:분할 나누기 구역)는 의미가 없는 영역을 만들 시 사용하는 태그
	여러 요소를 묶음으로 만들 시 사용
	 
	가로폭 100%의 범위를 묶어서 표현한다
	
	모든 태그는 가로영역을 가지고 있는데 이것을 이용해서
	이것저것 묶어서 그룹화해서 관리하는데 사용
	(특정 그룹을 페이지 가운데 정렬을 한다던가)
	-->
				<c:choose>
				<%-- 로그인하면 로그인, 회원가입 이란 글자가 없어지고 로그아웃과 마이페이지 글자가 나오도록하는 방법 
				참고로 c:안에는 !로 시작하는 주석넣으면 에러나니까 %주석 사용--%> 
				
					<c:when test="${empty sessionScope.loginMember}">
					<%-- 기본 화면으로써 로그인과 회원가입이 보이도록 한다
					loginMember클래스의 session값이 비어있다면 로그인하도록 글자를 띄운다
					참고로 scope는 범위이다. --%>
					
					
						<a href="${pageContext.request.contextPath}/mem/login.jsp">로그인</a>
						 <a href="${pageContext.request.contextPath}/mem/join.jsp">회원가입</a>	
					</c:when>
				
				<c:otherwise>
				
				<%-- 로그인하면 로그아웃과 마이페이지가 보이도록 한다 --%>
					<span>${sessionScope.loginMember.nickname}님</span>
					<%--로그인 멤버안에 있는 nickname을 읽어와서 로그아웃 글자 옆에
					홍길동님 같은 메시지를 띄울것이다. 내가 무사히 로그인했다라는 안심감을 주는 효과가 있다 --%>
					
				<a href="${pageContext.request.contextPath}/member/logout">로그아웃</a>
				<a href="${pageContext.request.contextPath}/mem/mypage.jsp">마이페이지</a>
				</c:otherwise>
				
				</c:choose>
				
			</div>
		</div>
	</header>
	
	
	
	
	
	
	
	
	
	
