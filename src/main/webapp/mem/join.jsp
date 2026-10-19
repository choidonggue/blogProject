
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- 회원 가입 파트 -->

<%@ include file="/include/header.jsp"%>
<!-- 인크루드 파일은 ${pageContext.request.contextPath}를 못쓴다 -->

<main class="join-wrap">

	<h2 class="join-page-title">회원가입</h2>
	
	<form id="joinForm" class="join-form"
              action="${pageContext.request.contextPath}/member/join" method="post">
              <!-- 아마도 회원가입을 처리하면 member폴더의 join.jsp로 사용자 정보가 넘어가나보다
              join.jsp라고 안하는 이유는 보안상의 이유때문에 직접 jsp에 접근시키는것을 막기 위해서라고함
              단순히 join만 적어도 컨트롤러가 알아서 jsp에 값을 전달한다고 한다 -->

            <!-- 아이디 + 중복확인 버튼: 같은 줄에 배치 -->
            <div class="form-row">
                <label for="id">아이디</label>
                <div class="id-check-group">
                    <input type="text" id="id" name="id" placeholder="영문/숫자 4~12자" class="form-input">
                    <button type="button" id="idCheckBtn" class="btn btn-outline id-check-btn">중복확인</button>
                </div>
                <!-- 아이디를 입력하고 중복확인 버튼을 누를수 있도록 하는 장소
                id라는 파트를 사용해서 나중에 자바스크립트 함수를 적용시켜서 중복 검사를 넣을거다 -->
                
                <p id="idCheckMsg" class="field-msg"></p>
                <!-- p태그는 '아이디가 중복되고 있습니다' 같은 아래에 문자를 띄운다거나 하는데 쓰이는
                자바스크립트를 적용시키기위해서 사용한다.  
                p태그는 단순히 글자만 출력하는 기능을 가졌기에 적합한 기능이다-->
            </div>

            <div class="form-row">
                <label for="pwd">비밀번호</label>
                <input type="password" id="pwd" name="pwd" placeholder="비밀번호를 입력하세요" class="form-input">
            </div>
            <!-- 비밀번호 입력장소 -->

            <div class="form-row">
                <label for="pwdCheck">비밀번호 확인</label>
                <input type="password" id="pwdCheck" placeholder="비밀번호를 다시 입력하세요" class="form-input">
                <p id="pwdCheckMsg" class="field-msg"></p>
            </div>
            <!-- 비밀번호 확인을 입력하는 장소 
           위와 마찬가지로 p태그로써 비번이 일치 안합니다라는 글자를 출력시 사용-->

            <div class="form-row">
                <label for="nickname">닉네임</label>
                <input type="text" id="nickname" name="nickname" placeholder="닉네임을 입력하세요" class="form-input">
            </div>
            <!-- 닉네임 입력 장소 -->

            <div class="form-row">
                <label for="email">이메일</label>
                <input type="email" id="email" name="email" placeholder="example@email.com" class="form-input">
            </div>
            <!-- 이메일 입력 장소 -->

            <div class="join-btns">
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline">취소</a>
                <button type="submit" class="btn btn-primary">가입하기</button>
            </div>
            <!-- 서브밋 버튼으로써 가입과 취소를 처리하는 장소
            취소를 누르면 블로그 홈 화면으로 돌아가게끔 해놨다 -->

        </form>
	
	
	
	
	<!-- 제목이라는 글자를 누르면 커서가 input 박스에 위치하는데
    	이게 label태그다 참고로 for은 반복문이 아니다-->

</main>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<%-- <script src="${pageContext.request.contextPath}/js/join.js"></script> --%>
<!-- 위의 퍼센트--는 jsp에서 자바문법을 주석처리 하는 방법 -->

<script>

$(function() {
	let idChecked = false;
	$("#idCheckBtn").on("click", function() {
		const id = $("#id").val().trim();
		if(id === '') {
			alert("아이디를 입력하세요");
			return;
		}
		//실제 연동시 비동기식 방식으로 id존재여부 확인 방법
		
		 $.ajax({
			url:'${pageContext.request.contextPath}/member/idCheck',
			//요청하는 주소
			type:'get',
			//전송방식
			data:{id:id}, //{"id":"abcd"}
			//전송하는 데이터 {키:값} => 자바스크립트 객체
			dataType:'json',
			//되돌아 오는 값은 반드시 json 형식으로 받아라
			success:function(result) {
				//서버와 통신이 성공하면 함수를 실행
				if(result.isDuplicate) {
					$("#idCheckMsg").text('이미 사용중인 아이디 입니다').css('color', '#ff3b5c');
				}else{
					$("#idCheckMsg").text('사용가능한 아이디 입니다').css('color', '#3bcff2');
					idChecked = true;
					//
				}
			},error: function() {
				alert("통신 오류 발생!")
			}
		}) //ajax
		
		
	}) //idCheck
	//아이디를 다시 수정하면 중복확인을 다시 받아야 한다
	$("#id").on('input', function() {
		idChecked =false;
		$("#idCheckMsg").text('');
	})
	//비밀번호 확인 일치 검사
	$("#pwdCheck").on('input', function() {
		const pwd = $("#pwd").val();
		const pwdCheck = $(this).val();
		if(pwdCheck === '') {
			$("#pwdCheckMsg").text('');
		}else if(pwd === pwdCheck) {
			$("#pwdCheckMsg")
			.text("비밀번호가 일치 합니다")
			.css('color','#3b6ff2');
		}else{
			$("#pwdCheckMsg")
			.text("비밀번호가 일치 하지 않습니다")
			.css('color','#ff3b5c');
		}
	}) //pwdCheck
	
	//가입하기전 최종 점검
	$("#joinForm").on('submit', function(e) {
		if(!idChecked) {
			alert("아이디 중복확인을 해 주세요");
			e.preventDefault();
			return;
		}
		if($('#pwd').val() !== $("#pwdCheck").val()) {
			alert("비밀번호가 일치하지 않습니다");
			e.preventDefault();
			return;
		}
	})
}) //end

//위의 과정을 거치면 ${pageContext.request.contextPath}/member/idCheck'같은
//자바 스크립트가 읽을수 없는 경로도 읽을수 있다. 왜냐면 jsp파일안에 들어있기 떄문












</script>

<%@ include file="/include/footer.jsp"%>









