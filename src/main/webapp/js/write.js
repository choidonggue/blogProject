
$(function() {//jquery 시작
	//이미지 선택시 미리보기

	const defaultImg = $("#imgPreview").attr('src');
	//프로필 사진을 바꾸기 전에 원래쓰던 기본 이미지(이미지없음 사진)이 무엇이었는지 기억해두는 변수
	//나중에 이미지 삭제를 누르면 원래 이미지로 되돌아온다
	//imagPreview의 속성중 src의 값만 가져오라는 의미이다(경로를 가져오게 된다)
	//attribute: 속성 
	//.attr()은 태그의 속성을 "가져오거나(Get) 변경하는(Set)" 역할을 한다
	$("#imgFile").on("change", function() {
	//사용자가 이미지를 선택해서 바꿨을때 시행되는 함수
		const file = this.files[0];
		//사용자가 가져온 첫번째 사진을 file이라는 변수에 담는다
		//첨부파일은 배열의 형태로 저장이 된다고 한다
		if (!file) return;
		//사진을 고르려다가 취소를 눌러서 아무것도 안골랐으면 코드를 실행하지말고 끝내라는 얘기
		const previewUrl = URL.createObjectURL(file);
		//서버에 사진을 올리려면 시간이 걸리므로
		//브라우저 메모리 안에서 임시 url을 만들어 미리보기에 반영하여 사진을 미리 보여준다
		
		
		$("#imgPreview").attr("src", previewUrl);
		//imgPreview라는 속성중 src가 있는데 거기에
		//previewUrl을 대체해서 넣어줘라
		 //그러면 화면에 내가 선택한 사진이 썸네일로써 보여지게 된다
		 $("#removeImg").val("false");
		 //이미지 제거 눌렀다가 다시 새 파일 선택하면 제거 요청 취소
		 //



	})

	//이미지 제거
	$("#imgRemoveBtn").on("click", function() {
	//이미지 리무브 버튼을 클릭시
		$("#imgFile").val('');
		//이미지 파일에 있는 값을 공백으로 전환하여 사진을 제거
		$("#imgPreview").attr("src", defaultImg);
		//맨위에서 저장해두었던 기본 사진(이미지없음 사진)을 다시 불러온다
		$("#removeImg").val('true');
		//modify.jsp에만 있는 hidden,write.jsp엔 없다
		//참고로 자바스크립트에서는 ''와 ""가 똑같이 취급된다
	

	})
	
	$("#writeForm").on('submit',function(e){
	//데이터 유효성 검사
	//제출 버튼 누르면 발생
	//function에 매개변수 e를 지정해서 아래에서
	//e.preventDefault() 함수를 실행시킬수 있도록 할것이다
	//이걸해야 포커스가 제대로 작동한다고 한다
	
	const title = $("#title").val().trim();
	const content = $("#content").val().trim();
	//제목이나 내용의 값을 공백을 제외해서 가져온다
	if(title == ''){
	alert("제목 입력 하라");
	e.preventDefault();
	//focus를 제대로 작동시키기 위해서 쓰는함수 + 알럿이 뜬 후에 화면의 이동을 금지시키는 방법
	//화면이동이 안되어야지 focus가 작동한다고 한다
	//기본 동작을 막다란 의미로 받아들이면 된다고 한다
	$("#title").focus();
	return;
	}
	if(content == ''){
	alert("내용 입력 하라");
	e.preventDefault();
	$("#content").focus();
	return;
	}
	//두개다 값들이 없으면 알럿을 띄우고 커서를 해당부분에 위치
	
	
	
	})
	
	
	
	
	
	
	
})
















