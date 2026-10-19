
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    <%@ include file = "/include/header.jsp"%>
    
    <!-- 인기글 파트의 내용을 모아둔 곳이다 -->
    
    
    <main class="popular-wrap">

        <h2 class="popular-page-title">인기글</h2>
        <p class="popular-sub">좋아요를 가장 많이 받은 글 순서로 보여줍니다</p>

     
        <ul class="popular-grid">
        <!-- 나중에 격자형식으로 그림들을 보여줄 장소 -->
        
        <c:forEach var="b" items="${poplist }" varStatus = "status">
        <%-- 인기글을 여러번 반복하면서 출력한다(여기선 6번)
         배지를 위해서 status를 선언했는데 이것은 반복문을 1바퀴 돌떄마다 1씩 증가하는 증가문이다(초기치는 1)--%>
            <li class="popular-card">
            
                <span class="rank-badge rank-${status.count }">${status.count }</span>
                <%--class명을 아주 길게 적었는데, 이게 이유가 css에서 rank-1 의 경우 rank-2의 경우
                이런식으로해서 모든 배치의 색깔을 다르게 배정했기 떄문이다
                그리고 해당 배지 내부에 1,2,3이란 글자를 넣기 위해서 다시하면 status.count를 적는다
                 --%>
                <a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno }">
                <!-- 인기글의 게시글을 누르면 해당 게시글로 연결되게 된다 -->
                
                <c:choose>
                	<%--첨부파일의 처리 방법 --%>
                	
                	<c:when test="${b.imgPath != null}">
                	<%--첨부된 이미지가 있다면 이것을 선택 --%>
                		<img src="/imgs/${b.imgPath}" alt="게시글 썸네일" class="popular-thumb">
                		<%--서버내에 imgs라는 특수 주소를 만들어놔서 contextpath가 필요 없다 --%>
                	</c:when>
                	
                	<%--첨부된 이미지가 없다면 이것을 선택(기본 이미지) --%>
                	<c:otherwise>
                    	<img src="${pageContext.request.contextPath}/images/visual.jpg" alt="게시글 썸네일" class="popular-thumb">
                    </c:otherwise>
                    </c:choose>
                    
                    <div class="popular-info">
                    <%--게시글의 제목, 작성자의 정보와 좋은가요의 갯수를 보여준다 --%>
                        <p class="popular-title">${b.title }</p>
                        <p class="popular-meta">작성자 ${b.writerNickname } - 좋은가요 ${b.likeCnt }</p>
                        <!-- 결과적으로는 썸네일과 썻던 글 파트가 일부 보이게 된다
                        이 아래는 같은것의 반복 -->
                    </div>
                </a>
            </li>
          </c:forEach>
        </ul>
        
        

    </main>
    
    
    
    
    
    
    
    
    <%@ include file = "/include/footer.jsp"%>
