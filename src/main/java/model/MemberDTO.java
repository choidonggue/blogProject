
package model;

public class MemberDTO {
	
	
   private String id;
   private String pwd;
   private String nickname;
   private String email;
   private String join_date;
   //join_date는 타입이 date타입이나 그러면 형변환이란 귀찮은 짓을 해야하니
   //String으로 받는다고 한다
   
   
   
   
   
   
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getPwd() {
	return pwd;
}
public void setPwd(String pwd) {
	this.pwd = pwd;
}

public String getNickname() {
	return nickname;
}
public void setNickname(String nickname) {
	this.nickname = nickname;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getJoin_date() {
	return join_date;
}
public void setJoin_date(String join_date) {
	this.join_date = join_date;
}

	

}
