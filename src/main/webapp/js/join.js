
/**
 * 회원가입파트 자바 스크립트
 유효성검사 진행
 */

$(function() {
	let idChecked = false;
	$("#idCheckBtn").on("click", function() {

		const id = $("#id").val().trim();
		if (id === '') {
			alert("아이디 입력");
			return;
		}
		//실제 연동시 비동기식 방식으로 id존재여부 확인 방법
		//비동기식 : 화면 전환을 하지않고 id존재여부를 확인하는 방법
		//		서버에서 데이터 처리하고 다시 되돌아 오는 방법이라 한다
		//이 방법을 사용하기때문에 화면전체가 새로고침되서 지금까지 작성했떤
		//아이디,비번,주소같은것이 새로 다 날아가는 비극이 벌어지지 않고
		//원하는 부분만 새로고침되어 해당 진행상황을 알려주는게 가능하다고 한다

		$.ajax({
		
		//유저가 행동을 입력 시 해당 명령이 서블렛으로 갔다가 서비스 구간(command)에서 처리되게 된다
		//처리가 끝나면 서블렛으로 거치치않고 바로 이쪽으로 돌아와서 유저에게 출력된다
		
			 url: contextPath +'/member/idCheck',
			//contextPath(프로젝트 이름)에다가 멤버의 idcheck를 요구
			//contextPath는 헤더쪽에다가 선언해논것을 가져왔다
			//직접 $형식으로해서 pathInfo를 집어넣으면 충돌이 일어나서 저렇게 변수 형식으로 
			//끌고와야 오류가 안난다고 한다
			
			//내 프로젝트 밑에있는 해당 서블릿(멤버 컨트롤러)으로 찾아가라
			type: 'get',
			//데이터 전송 방식
			data: { id: id },
			//전송하는 데이터{키:값} => 자바스크립트 객체 {"id":abcd}
			//id라는 키값에 abcd라는 문자열이 넘어간다
			//즉 서버에갈때 사용자가 입력한 아이디를 가져가란말
			dataType : 'json',
			//되돌아 오는 값은 반드시 json 형식으로 받아라
			//자바 서버가 나한테 응답줄때는 무조건 json방식으로 해라
			success: function(result) {
			//서버와 통신이 성공하면 함수를 실행(json이 무사히 돌아왔을때)
			//서비스에서 처리된 결과값이 result에 리턴되어 저장된다
				if (result.isDuplicate) {
				//만약 실행된 리저트 함수가 중복된다면(아이디가 중복된다면)
				$("#idCheckMsg").text('중복된 아이디').css('color','#f00');
				}else {
				//빨간 글씨로 아래쪽에 저런 문장이 출력(화면 새로고침 없음)
				//지금 db에 java01이 입력되어 있으므로 java01을 입력시 중복된 아이디라고 메시지가 나올거다
			$("#idCheckMsg").text('사용 가능 아이디').css('color','#00f');
				}
				//여기까지 제대로 진행되었으면 콘솔창에 
				//pathInfo : /idCheck라고 적혀나올것이다. 
				//서블렛이 난 최종적으로 여기로 연결시켜줬어. 라고 보여주는 것이다
				},error: function(){
				alert("통신 오류 발생")
				//exception경우를 대비한 에러코드 작성
				}
				
				})//ajax

		

	})//idCheck
	//아이디를 다시 수정하면 중복확인을 다시 해야한다
	$("#id").on('input', function() {

		idChecked = false;
		$("#idCheckMsg").text('');

	})
	//비밀번호 확인 일치 검사
	$("#pwdCheck").on('input',function(){
	
		const pwd = $("#pwd").val();
		const pwdCheck = $(this).val();
		if(pwdCheck === ''){
			$("#pwdCheckMsg").text('');
			}else if(pwd === pwdCheck){
			
				$("#pwdCheckMsg")
				.text("비밀번호가 일치 함").css('color','#3b6ff2');
			}else{
				$("#pwdCheckMsg")
				.text("비밀번호가 일치 안 함").css('color','#ff3b5c');
			}
			
	
	})//pwdCheck
	
	
	
	//가입하기전 최종 점검
	$("#joinForm").on('submit',function(e){
	if(!idChecked){
		alert("아이디 중복 확인 해라");
		e.preventDefault();
		return;
	}
	if($('#pwd').val() !== $("#pwdCheck").val()){
		alert("비밀번호 일치 안함");
		e.preventDefault();
		return;
	
	}
	
	
	})
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


})//end

