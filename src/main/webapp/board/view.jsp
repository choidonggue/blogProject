<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="/include/header.jsp" %>
   
   
   <main class="view-wrap">
   <!-- 아래 파트들을 전부 $를 붙인 지정자를 사용하여서
   dto에 저장된 값들을 출력하도록 할것이다 -->
      <div class="view-header">
         <h2 class="view-title">${view.title}</h2>
         <p class="view-meta">작성자 ${view.writerNickname} | ${view.regDate} | 조회 ${view.viewCnt}</p>
      </div>
      <div class="view-image">
         <img src="/imgs/${view.imgPath}" alt="" class="view-thum">
      </div>
      <div vlass="view-content">
         ${view.content}
      </div>
      
      <div class="view-actions">
         <button type="button" id="likeBtn" class="btn btn-outline like-btn ${liked ? 'liked' :''}">
         <!-- 3항 연산자로 liked가 없으면 liked클래스를 추가하고
          liked가 있다면 liked 클래스를 지워라 -->
            좋은가요 <span id="likeCount">${view.likeCnt}</span>
         </button> 
         <div class="view-actions-right">
            <a href="${pageContext.request.contextPath}/blog/update?bno=${view.bno}" class="btn btn-outline">수정</a>
            <a href="${pageContext.request.contextPath}/blog/delete?bno=${view.bno}" class="btn btn-outline" onclick="return confirm('삭제 할래요?')">삭제</a>
            <!-- 삭제하기 버튼을 누르면 BoardDAO에서 delete메서드문을 실행되도록 한다 
            여기에서 delete? 라는 것은 ?뒤에 나오는 쿼리 스트링을 실행하라는 것인데
            내가 누른 view의 bno의 값을 html로 부터 추출해서
            request.getParameter 로써 그 bno를 dto로 보낼수 있도록 하는 과정이다
            -->
            
            <a href="${pageContext.request.contextPath}/blog/list" class="btn btn-primary">목록</a>
         </div>
      </div>
         <!-- 댓글 영역 -->
         <div class="reply-section">
            <h3 class="reply-title">댓글 <span id="replyCount">${fn:length(replyList)}</span></h3>
            <!-- 댓글의 길이를 보이는 방법(댓글의 갯수) -->
            
            
            
            <ul class="reply-list" id="replyList">
            
            <!-- 실제로 내가 쓴 댓글이 보이는 장소 -->
            
            <c:forEach var = "r" items = "${replyList}">
            <!-- replyList에 저장되어 있는 값들을 가져와서 하나씩 선택해서 반복적으로 전부 보여라
            db에 저장된 댓글들이 전부 보이게 된다 
            강화 for문처럼 쓴다-->
            
               <li class="reply-item" data-rno="${r.rno }">
                  <div class="reply-info">
                     <span class="reply-writer">${r.nickName }</span>
                     <span class="reply-date">${r.regDate }</span>
                  </div>
                  <p class="reply-content">${r.content}</p>
                  <div class="reply-btns">
                     <button type="button" class="reply-edit-btn">수정</button>
                     <button type="button" class="reply-delete-btn">삭제</button>
                  </div>
               </li>
               
               </c:forEach>
              
            </ul>
            
            <div class="reply-form">
               <input type="text" id="replyContent" placeholder="댓글을 입력하세요" class="reply-input">
               <button type="button" id="replySubmitBtn" class="btn btn-primary">등록</button>
            </div>
            
         </div>
      
   </main>
   
   <!-- Modal (뭔가 이상하게 생긴 알럿을 띄우는 css다) -->
<div class="modal fade" id="replyModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
    <!-- 평소에는 화면에 숨겨져있다가 호출하면 부드럽게 튀어나오는 역할 -->
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">댓글 수정</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        <!-- 상자에 댓글 수정이라는 이름과 x버튼이 있는 틀을 만든다 -->
      </div>
      <div class="modal-body">
        <input type = "hidden" id = "modalRno">
        <textarea id = "modalContent" class = "form-control" row = "4"></textarea>
        <!-- 눈에 보이지 않는 비밀 주머니이다. 자바스크립트가 몇번째 댓글을 수정할건지
         몇 줄을 입력할것인지를 정한다. 여기선 4줄까지 가능하게 했다
         -->
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" id="modalSaveBtn">Save changes</button>
        <!-- 취소와 저장 버튼 부분을 생성 -->
      </div>
    </div>
  </div>
</div>
   
   
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
   <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
   <!-- jquery부터 호출이 된 후에 부트스트랩이 실행되어야 한다고 한다 -->
   
   <script>
   //---좋아요 버튼 처리
   $("#likeBtn").on("click",function() {
	   
	   const bno = ${view.bno};
	   //view라는데 있는 bno를 자바변수에 저장시켜서 써먹는 방법
	   
	   
     $.ajax({
    	 url:"${pageContext.request.contextPath}/blog/like",
    	 //내가 해당 ajax값을 보낼 컨트롤러에 지정된 주소
    	 type:"post",
    	 data : {bno:bno},
    	 dataType:'json',
    	 success:function(result){
    		 if(!result.success){
    			 alert(result.message);
    			 //만약 전송에 실패했으면 해당 알럿을 띄운다
    			 
    			   location.href = "${pageContext.request.contextPath}/member/login";
    			
    			 return;
    		 }
    		 $("#likeCount").text(result.likedCount);
    		 //좋은가요 버튼을 누르면 그 값이 1 증가한다
    		 $("#likeBtn").toggleClass("liked",result.liked);
    		 //좋은가요 버튼 누르면 like <> liked로 토글된다. 이걸로 좋은가요 눌른걸 다시 취소할 수 도 있다
    	 },error:function(){
    		 alert("통신 에러");
    	 }
    	 
     })
   })
   
   //---댓글 등록---
   $("#replySubmitBtn").on("click", function() {
      const content = $("#replyContent").val().trim();
      if(content == '') {
         alert("댓글 내용을 입력하세요");
         return;
      }
      
      //비동기식으로 저장된 데이터를 서버로 보내는 문
      $.ajax({
    	
    	url : "${pageContext.request.contextPath}/reply/write",
    	//댓글이 쓰이는 장소
    	type : 'post',
    	//서버에 보내는 방식
    	data : {bno:${view.bno}, content:content},
    	//보내는 내용들(키와 값의 형태)
    	dataType:'json',
    	//보내는 데이터 타입(비동기식)
    	success:function(result){
    		//ReplyWrite클래스에 result의 처리식이 있다
    		if(!result.success){
    			//만약 보내기에 실패했으면 해당 알럿을 보여줌
    			alert(result.message || '댓글등록 실패')
    			
    			location.href = "${pageContext.request.contextPath}/member/login";
    			
    			return;
    		}
    		
    		
    		
    		 //'혼따옴표' 가 아니라 `벡틱이다`
    		   //여기에서는 위에서 받아온 값들을 대입해서 사용자에게 보여주는 과정이다
    		   //벡틱을 쓰는 이유는 \n같은 귀찮은 엔터같은것을 입력 안하고
    		   //엔터를 입력한 그대로 출력되기때문에 매우 가독성이 좋아진다고 한다
    		   //심지어 ""같은 것도 안적어도 된다고 한다
    		   
    		   //백틱안에서 문자를 직접적으로 보이는 방법은 $앞에 \를 붙여주는 것이다
    		   //백틱안에서는 주석문도 그대로 출력되어서 보이므로 주석 넣지말자.
    		   
    		   //보통 벡틱은 \를 붙이지 않으나, 여기서는 댓글에 result라는 상자의 내부값을
    		   //보여야 하는 특수한 상황이기에 json이 직접 result라는 글자를 해석 안하고
    		   //그대로 서버로 보내도록 하기위해서 \를 붙여서 예외처리를 한거다
    		   
    		   //result.rno만 ""로 묶은 이유는 html의 속성값으로 사용하기 위해서 어쩔수 없이 쓴거고
    		   //나머지는 직접 출력해서 보이는 요소이기 떄문에 ""를 붙이지 않는다
    		   
    		   //data-rno라는 것은 화면 내에 rno의 값을 몰래 숨겨뒀다가 나중에 삭제,수정등에 쓰기위해 만든 임시 변수다
    		   
    		   //날짜는 방금전으로 현재 고정중인데 이유는 나중에 다시 조회시에는 db에 저장된 댓글 전체 조회라는
    		   //다른 방법을 쓰기에 그떄 날짜를 불러올 수 있기 때문이다
    	      const newItem=`
    	            <li class="reply-item" data-rno="\${result.rno}">
    	            
    	               <div class="reply-info">
    	                  <span class="reply-writer">\${result.writeNickname}</span>
    	                  
    	                  <span class="reply-date">방금전</span>
    	               </div>
    	               <p class="reply-content">\${result.content}</p>
    	               
    	               <div class="reply-btns">
    	                  <button type="button" class="reply-edit-btn">수정</button>
    	                  <button type="button" class="reply-delete-btn">삭제</button>
    	               </div>
    	            </li>
    	      `;
    	      
    	      $("#replyList").append(newItem);
    	      $("#replyContent").val('');
    	   
    	},error:function(){
    		//통신 오류시 에러 출력
    		alert("댓글 등록중 오류 생김");
    	}
    	  
      })
      
      
        
      /* 실제 연동 시 비동기식으로 데이터 처리 할 거다
      $.ajax({
         
      })
      */
      
   });
   
   //---댓글 삭제 ---
   $("#replyList").on("click", '.reply-delete-btn', function() {
//댓글 리스트안에 있는 삭제 버튼을 클릭했을시
      if(confirm('댓글을 삭제?')) {
//컨펌은 알럿창이긴한데 확인과 취소 버튼이 있는 알럿이다
//확인 누르면 댓글을 제거한다

//현재 이 부분의 코드는 무조건 오류가 나온다 수정 필요
      
         //실제 연동시 rno값을 data-rno에서 꺼내서 비동기식으로 처리
       	const $item = $(this).closest('.reply-item');
         //클릭했던 댓글리스트를 가져오도록 한다 (클릭한 li요소)
         //closest는 현재 선택한 list의 가장 가까운 부모 요소를 가져오게 한다 그것을 item변수에 저장
         const rno = $item.data('rno');
         //선택한 부모에 숨겨져있던 rno를 가져오게 한다
         //위에서 화면에 숨기도록 선언했던 rno를 가져오게 한다 (li 태그의 data-rno 속성 값)
         //그리고 클릭한 댓글리스트를 묶어서 제거하도록 한다
         
         
         $.ajax({
			url:"${pageContext.request.contextPath}/reply/delete",
			//해당 속성을 보낼 주소
			type:'post',
			//보내는 방식
			data:{rno:rno},
			//서버로 보내는 자료
			dataType:"json",
			//서버에서 보내는 자료는 무조건 json
			success:function(result){
				if(result.success){
					//삭제 메서드를 잘 실행했으면 부모요소를 지워라
					$item.remove();
				}else{
					alert(result.message);
				}
				},
				error:function(){
					alert("통신 오류");
				   }
	        }); // $.ajax 호출을 닫아주는 정확한 괄호 쌍입니다.
	    }
	});
   
   //---댓글 수정 ---
   
   const replyModal = new bootstrap.Modal(document.getElementById('replyModal'));
   //id=replyModal인 요소를 자바스크립트가 제어 할 수 있도록 객체화 하는 과정
   
   document.getElementById('replyModal').addEventListener('show.bs.modal',function(){
	 
	   $('#modalContent').focus();
	   //화면에 모달창이 나오게되면 바로 자료를 입력 가능하게 커서를 띄우는 방법
   });
   
   $("#replyList").on("click", '.reply-edit-btn', function() {
      const $item = $(this).closest('.reply-item');
      //현재 클릭한 댓글의 부모 속성을 가져와서 item에 저장
      $('#modalRno').val($item.data('rno'));
      //현재 클릭한 댓글의 숨겨진 rno를 가져온다. 이걸로 수정된걸 db에 저장
      $('#modalContent').val($item.find('.reply-content').text());
      //수정이기 때문에 해당 댓글의 내용을 불러와서 띄운다
      replyModal.show();
      //준비가 다 되었으면 모달창을 보인다
   });
   
   $('#modalSaveBtn').on('click',function(){
	//댓글 내용을 입력후 저장버튼을 눌렀을 시
	   const rno = $('#modalRno').val();
	   const newContent = $('#modalContent').val().trim();
	   //입력받은 댓글 내용들을 newContent변수에 저장
	   if(newContent == ''){
		   //아무런 내용이 입력 안되었으면 입력하라는 메시지 띄움
		   alert('댓글 내용 입력');
		   return;
	   }
	   $.ajax({
		   //정상적으로 다 입력되었으면 ajax쪽으로 데이터 값을 전달
		   url:'${pageContext.request.contextPath}/reply/modify',
		   type:'post',
		   data:{rno:rno,content:newContent},
		   //새로운 댓글 내용을 전달하기위해 newcontent 사용
		   dataType:'json',
		   success:function(result){
			   
			   if(result.success){
				   $('#replyList .reply-item[data-rno="'+rno+'"]')
				   //해당 댓글의 고유 번호를 받는 과정
				   //벡틱을 안써서 되게 이상한 방식으로 쓰는데
				   //css선택자에서 속성값을 찾을떄 []를 써야하며
				   //rno에 나의 변수를 대입하기위해서 저런식으로 분리해서 적는다고 한다
				   //안그러면 json은 rno에 값을 대입 안하고 rno라는 문자로 처리해버린다고 한다
				   .find(".reply-content").text(result.content);
				   //입력받은 댓글의 내용을 덮어써서 보여주는 방법
				   replyModal.hide();
				   //사용한 모달은 다시 안보이게 화면 어딘가로 숨긴다
				   
			   }else{
				   alert(result.message);
			   }
		   },error:function(){
			   alert("통신 실패");
		   }
	   })
   })
   
   
   
   
   //자바스크립트
      /* document.getElementById("likeBtn").addEventListener("click",function() {
         let likeBtn = this;
         //좋아요 버튼
         likeBtn.classList.toggle("liked");
         //liked 클래스가 있으면 제거하고, 없으면 추가된다
         let count = parseInt(document.getElementById("likeCount").textContent);
         //
         if(likeBtn.classList.contains('liked')) {
            count = count + 1;
            document.getElementById('likeCount').textContent = count;
         }else{
            document.getElementById('likeCount').textContent = count - 1;
         }
         
      }) */
      
      
      
      
   </script>
   

<%@ include file="/include/footer.jsp" %>   
