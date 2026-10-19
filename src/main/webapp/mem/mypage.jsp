

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%
    // 컨트롤러가 데이터를 담아서 보냈는지 확인합니다.
    Object listCheck = request.getAttribute("myPosts");
    
    // 데이터 상자가 비어있다면 컨트롤러를 안 거쳐왔다는 뜻이므로 강제로 토스합니다.
    if (listCheck == null) {
        response.sendRedirect(request.getContextPath() + "/member/mypage");
        return;
    }
%>

    <%@ include file = "/include/header.jsp" %>
    
    <main class="mypage-wrap">

        <h2 class="mypage-page-title">마이페이지</h2>
        <p class="mypage-sub">홍길동님, 환영합니다</p>
		
		<!-- 내가 좋은가요를 눌렀던 글이나 내가 작성했던 글 들이 보이는 장소
		그래서 마이페이지다 -->
        <!-- 탭 메뉴: jQuery로 클릭한 탭만 보이게 전환 -->
        <div class="mypage-tabs">
            <button type="button" class="mypage-tab-btn active" data-tab="myPosts">내 글</button>
            <button type="button" class="mypage-tab-btn" data-tab="myReplies">내 댓글</button>
            <button type="button" class="mypage-tab-btn" data-tab="myLikes">좋아요한 글</button>
            <!-- 나중에 자바스크립트로 연결시켜서 탭이 전환되도록 만들거다 -->
        </div>

        <!-- 탭 ① 내 글: 지금까지 만든 카드형 리스트와 같은 구조 -->
        <div id="myPosts" class="mypage-panel active">
            <ul class="mypage-list">
            
            <c:forEach var="b" items="${myPosts}">
            <a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">
            
            
                <li class="mypage-item">
                
                
                
                <c:choose>
                <c:when test = "${empty b.imgPath}">
                    <img src="${pageContext.request.contextPath}/images/default-thumb.jpg" alt="게시글 썸네일" class="mypage-thumb">
                    </c:when>
                    
                    <c:otherwise>
                    <img src="/imgs/${b.imgPath}" alt="게시글 썸네일" class="mypage-thumb">
                    </c:otherwise>
                    </c:choose>
                    
                    <div class="mypage-item-info">
                    
                    
                        <p class="mypage-item-title">${b.title}</p>
                        <p class="mypage-item-meta">${b.regDate} · 조회 ${b.viewCnt} · 좋아요 ${b.likeCnt}</p>
                    </div>
                    <!-- 내가 썻던 1번째 글과 사진이 보이게 된다. -->
                </li>
                    </a>
               </c:forEach>
            </ul>
        </div>

        <!-- 탭 ② 내 댓글: 이미지 없이 텍스트 위주 리스트 -->
        
        <div id="myReplies" class="mypage-panel">
            <ul class="mypage-list">
            
            
            
             <c:forEach var="b" items="${myReplies}">
                <li class="mypage-reply-item">
                <a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">
                    <p class="mypage-reply-content">${b.content}</p>
                    <p class="mypage-item-meta">"${b.boardTitle}" · ${b.regDate}</p>
                    </a>
                </li>
               
               
                </c:forEach>
                
                <!-- 내가 썻던 3번째 글이 보이게 된다.(여긴 사진은 없음) -->
            </ul>
        </div>

        <!-- 탭 ③ 좋아요한 글: 내 글과 같은 카드 구조 재사용 -->
        <div id="myLikes" class="mypage-panel">
            <ul class="mypage-list">
            
            
            
             <c:forEach var="b" items="${myLikes}">
             <a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">
                <li class="mypage-item">
                
              
                
                <c:choose>
                <c:when test = "${empty b.imgPath}">
                    <img src="${pageContext.request.contextPath}/images/default-thumb.jpg" alt="게시글 썸네일" class="mypage-thumb">
                    </c:when>
                    
                    <c:otherwise>
                    <img src="/imgs/${b.imgPath}" alt="게시글 썸네일" class="mypage-thumb">
                    </c:otherwise>
                    </c:choose>
                    
                    
                    <div class="mypage-item-info">
                        <p class="mypage-item-title">${b.title}</p>
                        <p class="mypage-item-meta">${b.writerNickname} · 좋은가요 ${b.likeCnt}</p>
                    </div>
                    <!-- 내가 다른사람에 좋은가요 버튼을 눌렀을때 그 내용이 보이게 된다 -->
                </li>
                </a>
                </c:forEach>
            </ul>
        </div>

    </main>

   <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/mypage.js"></script>
    
    
    
    
    
    <%@ include file = "/include/footer.jsp" %>
