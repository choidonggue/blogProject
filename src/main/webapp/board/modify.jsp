
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- 내가 쓴 글을 수정하는 파트
내가 보기엔 예전의 내용을 불러와서 다시 글쓰기 파트를 진행시키는 
과정같다 보인다
현재 내가 썼던글을 불러오는 파트는 누락된것 같다-->

<%@ include file="/include/header.jsp"%>
<!-- 인크루드 파일은 ${pageContext.request.contextPath}를 못쓴다 -->

<main class="write-wrap">

	<h2 class="write-page-title">글 수정</h2>
	<form id="modifyForm" class="write-form" method="post"
		enctype="multipart/form-data"
		
		
		
		
		action="${pageContext.request.contextPath}/blog/update">
		<!-- 아래 폼 과정을 다 거치면 write.jsp로 진행시켜서 글을 올리도록 해라 -->
		<!-- 컨트롤러에 지정되어있는 주소로 변경 
		이렇게 하는 이유는 보안의 문제도 있지만, jsp로 직접 연결해버리면 단순히 화면을 구경하겠다.라는 의미밖에 안된다 
		그래서 컨트롤러를 통해 DB도 같이 연동하겠다면
		jsp를 떼고 컨트롤러에 지정된 주소인 modify를 사용하는 것이다-->
		
		<input type = "hidden" name = "bno" value = "${board.bno}">
		<!-- 내가 수정하는 글번호(bno)가 몇번인지 db에게 알려주기 위한 방법이다.
		이 값은 수정되는 값은 아니고 단순히 어떤게 수정된다고 서버에게 보내주는 방법이다.
		다소 보안이 요구되는 기본키의 값이기에 히든으로 보낸다 -->
		
		<input type="hidden" id="removeImg" name="removeImg" value="false">
				
		<div class="form-row">
			<label for="title">작성자</label>
			<p class = "write-author">${board.writerNickname }</p>
			<!-- 작성자는 사용자가 고칠수 없어야 하기에 서버에서 전달되는 닉네임으로 고정하기 위해서 이런 짓을 한다 -->
		</div>
		
		
		<div class="form-row">
			<label for="title">제목</label> <input type="text" id="title"
				name="title" class="form-input" value = "${board.title }">
				<!-- 여기서는 서버에서 가져온 title의 값을 고정되는 부분으로 보여주는게 아니고
				value로써 넣어뒀는데, 사용자가 지금까지 썻던값들을 우선 보여줘야 하기에
				이런식으로 했다. 이것을 지우고 다시 쓰던 아니면 그대로 쓰던 하면 된다 -->
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
					src="${not empty board.imgPath ? '/imgs/'.concat(board.imgPath) :
					 pageContext.request.contextPath.concat('/images/default.jpg')}"
					<%-- 3항 연산자로 이미지가 첨부되어 있다면(경로가 있으면) 
					true : 서버에 imgs라는 폴더에 있는 이미지 경로를 연결해서(concat) 이미지를 가져와라
					false : images폴더내에 있는 기본 이미지로 대체해서 보여줘라 --%>
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
				placeholder="내용을 입력하라" class="form-textarea">${board.content }</textarea>
				<!-- textarea는 본문 내용을 적는 장소이다
				textarea태그는 반드시 /textarea태그를 딱 붙여서 해줘야한다
				안그러면 글을 적는 곳에서 공백이 적용되어서 커서의 시작점이 매겨진다 
				
				board.content를 써서 기존의 내용을 출력하도록 하였다-->
		</div>

		<div class = "write-btns">
		
			<a href="${pageContext.request.contextPath}/blog/view ? bno = ${board.bno}"
			<%-- 만약 수정하려다가 그냥 취소 누르면 수정하려던 게시글이 보이도록 한다. --%>
			
			class = "btn btn-outline">취소</a>
			<button type = "submit" class = "btn btn-primary">수정완료</button>
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








