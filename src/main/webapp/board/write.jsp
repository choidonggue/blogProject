
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- 글쓰기 입력 파트 -->

<%@ include file="/include/header.jsp"%>
<!-- 인크루드 파일은 ${pageContext.request.contextPath}를 못쓴다 -->

<main class="write-wrap">

	<h2 class="write-page-title">글쓰기</h2>
	<form id="writeForm" class="write-form" method="post"
		enctype="multipart/form-data"
		
		
		action="${pageContext.request.contextPath}/blog/write">
		<!--글쓰기를 수행 시 컨트롤러에서 blog에 요청되는 모든 처리를 하도록 하기때문에 여기 경로를 바꿔둔다 -->
		
		<input type="hidden" name="writer" value = "${sessionScope.loginMember.id}">
		<!-- writer라는 세션 값을 숨긴 상태로 url로 넘겨라
		이러면 글쓴이를 사용할수 있게된다 -->
		
		<div class="form-row">
			<label for="writer">작성자</label>
			<p class = "write-author">
			<input type="text" id="writer"
						class="form-input" value = "${sessionScope.loginMember.nickname}" readonly>
						<!-- 작성자 부분(nickname)의 값을 받아오기위한 방법. 수정되면 안되기에 readonly속성을 넣는다 -->
				</p>
		</div>
		
		<div class="form-row">
			<label for="title">제목</label> <input type="text" id="title"
				name="title" placeholder="제목을 입력하라" class="form-input">
		</div>
		<!-- 첨부파일이 있을 시는 enctype속성을 추가해줘라 
		encoding type의 약자다
		multipart : 여러개의 조각으로 나누어서 보내라
		form-data : 사용자가 입력해서 보내는 데이터를
		> 합치면 사용자가 보낸 값은 글자뿐만 아니라 file(그림)도 있을수 있으니
		여러개로 쪼개서 보내라
		-->
		
		<!-- label태그는 글자와 링크를 합쳐주는 태그다
		여기서는 제목이란 글자를 누르면 focus()기능같이 인풋 박스에 커서가 위치하게 된다 -->

		<div class="form-row">

			<label>대표이미지<span class="form-option">(선택)</span></label>
			<div class="image-upload-box">

				<img id="imgPreview"
					src="${pageContext.request.contextPath}/images/default.jpg"
					alt="이미지 미리보기" class="img-preview">
				<div class="image-upload-btns">
				
					<label for="imgFile" class="btn btn-outline file-label">이미지선택 </label> 
					<!-- 이미지선택이라는 글자를 누르면(라벨) 아래의 인풋타입 file이라는 버튼이 눌리도록 연결한다 -->
						<input type="file" id="imgFile" name="imgFile"
						accept="image/*" class="file-input">
						<!--accpept: 받아들일 첨부파일 종류는 이미지로만 받겠다 -->
						
					<button type="button" id="imgRemoveBtn" class="btn btn-outline">이미지
						제거</button>
					<!-- 첨부파일 처리할때는 인풋 타입 file을 사용
    					accept는 이미지만 첨부파일로 받겠다는 의미다
    					이것을 하면 사진을 불러와서 입힐 수 있는 기능이 나온다 -->
				</div>
			</div>

		</div>

		<div class="form-row">

			<label for="content">내용</label>
			<textarea rows="12" id="content" name="content"
				placeholder="내용을 입력하라" class="form-textarea"></textarea>
				<!-- textarea는 본문 내용을 적는 장소이다
				textarea태그는 반드시 /textarea태그를 딱 붙여서 해줘야한다
				안그러면 글을 적는 곳에서 공백이 적용되어서 커서의 시작점이 매겨진다 -->
		</div>

		<div class = "write-btns">
		
			<a href="${pageContext.request.contextPath}/board/list"
			
			class = "btn btn-outline">취소</a>
			<button type = "submit" class = "btn btn-primary">등록</button>
		</div>
		<!-- context:내가 필요한 정보를 담고있는 보관함 -->


	</form>
	
	<!-- 제목이라는 글자를 누르면 커서가 input 박스에 위치하는데
    	이게 label태그다 참고로 for은 반복문이 아니다-->

</main>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src = "${pageContext.request.contextPath}/js/write.js"></script>
<!-- 위에 두 문장의 순서를 지켜야 한다. 먼저 jquery의 라이브러리를 불러오고
나의 javascript경로를 지정해줘야 된다 -->



<%@ include file="/include/footer.jsp"%>
