
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- 회원 가입 파트 -->

<%@ include file="/include/header.jsp"%>
<!-- 인크루드 파일은 ${pageContext.request.contextPath}를 못쓴다 -->


<main class="login-wrap">

        <h2 class="login-page-title">로그인</h2>
        
       <c:if test="${not empty loginFail}">
       <!-- 자카르타 형식으로 %자바문법 쓰는법. 저렇게 c:한뒤에 컨트롤 스페이스하면 내가 원하는걸 골라서
        함수를 실행 가능. 
        loginFail의 변수가 비어있지 않으면(값이 있단 얘기니까 결국 로그인에 실패했단 얘기)
        안쪽의 p태그를 실행해라. -->
        
       <p class="login-error-msg">${loginFail}</p>
       <!-- 로그인화면에서 아이디와 비번을 다 입력 후 로그인이 실패하면 나오는 ajax메시지(알럿이 아니고 화면에 글자가 나옴) -->
       
       </c:if>
                
		
		
		
        <form id="loginForm" class="login-form"
              action="${pageContext.request.contextPath}/member/login" method="post">

            <div class="form-row">
                <label for="id">아이디</label>
                <input type="text" id="id" name="id" placeholder="아이디를 입력하세요" class="form-input">
            </div>

            <div class="form-row">
                <label for="pwd">비밀번호</label>
                <input type="password" id="pwd" name="pwd" placeholder="비밀번호를 입력하세요" class="form-input">
            </div>

            <button type="submit" class="btn btn-primary login-submit-btn">로그인</button>

            <p class="login-footer-link">
                아직 계정이 없으신가요?
                <a href="${pageContext.request.contextPath}/member/join">회원가입</a>
            </p>

        </form>
        
        </main>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src = "${pageContext.request.contextPath}/js/login.js"></script>
<!-- 위에 두 문장의 순서를 지켜야 한다. 먼저 jquery의 라이브러리를 불러오고
나의 javascript경로를 지정해줘야 된다 -->



<%@ include file="/include/footer.jsp"%>

