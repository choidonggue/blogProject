
/**
 * 
  로그인 관련 자바스크립트 내용(login.jsp)
  회원가입 자바스크립트와 내용이 비슷하다고 한다
 */
 
 $(function () {
    $('#loginForm').on('submit', function (e) {
        if ($('#id').val().trim() === '') {
            alert('아이디를 입력하세요.');
            e.preventDefault();
            return;
        }
        if ($('#pwd').val().trim() === '') {
            alert('비밀번호를 입력하세요.');
            e.preventDefault();
            return;
        }
        //데이터 유효성 검사 과정
        // 여기까지 통과하면 실제 폼이 /member/login 으로 제출됨
    });
});
 
