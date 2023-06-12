<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="js/jquery-3.6.4.min.js"></script>
<script>
$(document).ready(function(){
	//구현
});
</script>
</head>
<body>
<%
String[] languages = {"Kor", "Eng","Jpn","Chn"};
String[] languages_names = {"한국어", "영어","일본어","중국어"};




%>

<!-- 컨트롤러서받는거 요청파라메터 네임속성으로 같이감 lang 네임값=요청파라메터이름 value=진짜 전달되는값 Kor //유저한텐 한국어로보임.-->
<!-- sttresult?lang=Kor&mp3file=a.mp3  // lang으로 전달, mp3file 변수에담아 전달. -->
<form action="sttresult" method=get>
	언어 선택 : <br>
	<%for(int i= 0;  i< languages.length; i++){%>
		<input type=radio name="lang" value=<%=languages[i]%>> <%=languages_names[i]%><br>
	<%}%>
<!-- 파일선택하도록. 양이 많으니 셀렉트옵션태그. option value 전송값.  -->

	mp3 파일 선택 : <br>
	<select name="mp3file"> <!-- 컨트롤러/sttresult 여기다 지정해둔이름. -->
		<c:forEach items="${filelist }" var="onefile">
			<option value="${onefile }">${onefile } </option>
		</c:forEach>
	</select>
	<input type=submit value="텍스트로 변환 요청">
</form>




</body>
</html>

<!-- 1.폼통해 언어입력받음. 라디오/셀렉트옵션 
<form>
<input type=radio name="lang" value="Kor"> 한국어 

<a href="sttresult?image=${xx}&lang=Kor"
radio 하나만선택해.
-->
