
/**
 * 마이페이지 라는 장소의 
 자바스크립트
 */
 
 
 
 //탭 기능을 가지게하는 자바스크립트
 $(function () {
    $('.mypage-tab-btn').on('click', function () {
//클릭 이벤트가 발생했을때 함수 실행
        const target = $(this).data('tab'); // data-tab 속성값 (myPosts, myReplies, myLikes)
//this란건 방금 사용자가 클릭한 버튼을 의미한다.
//target이란 변수에 사용자가 선택한 버튼과 그 내용(data)를 저장한다



       // 버튼 active 상태 전환(탭의 제목을 옮긴다고 생각하자)

//불을 껐다가 킨다고 생각하면 좋다고 한다 
//끄면 데이터가 사라지고 키면 새로운 데이터가 대입된다
//이렇게 안하고 그냥 block:none; 식으로 진행하면 코드가 너무 길어진다고 한다
        $('.mypage-tab-btn').removeClass('active');
//기존에 선택되어 있떤 버튼을 지운다(흐리게 한다)
        $(this).addClass('active');
//내가 선택한 버튼을 밝게 킨다

        // 패널 전환(내용 전환)
        $('.mypage-panel').removeClass('active');
//기존의 내용을 끈다
        $('#' + target).addClass('active');
//위에서 저장했던 target(탭의 제목과 데이터)의 앞에 #을 붙여서 불을 켜준다.
    });
});

