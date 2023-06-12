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
<!-- 나중에 맵(키,값)으로 구분해서가봐. -->
<%
String speakers[] = 
{"mijin", "jinho", "clara", "matt", "shinji", "meimei", "liangliang", "jose",
		"carmen", "nnaomi", "nhajun", "ndain"};

String[] speakerinforms = {
		"미진:한국어, 여성음색", "진호:함국어, 남성음색", 
		"클라라 : 영어, 여성 음색", "매트 : 영어, 남성 음색",
		"신지: 일본어, 남성 음색", "메이메이 : 중국어, 여성 음색",
		"량량 : 중국어, 남성 음색", "호세 : 스페인어, 남성 음색",
		"카르멘 : 스페인어, 여성 음색", 	"나오미 : 일본어, 여성 음색",
		"하준 : 한국어, 아동 음색 (남)", "다인 : 한국어, 아동음색 (여)"
		};





%>

<form action="ttsresult" method=get>
	음색 선택 : <br>
	<%for(int i= 0;  i< speakers.length; i++){%>
		<input type=radio name="speaker" value=<%=speakers[i]%>> <%=speakerinforms[i]%><br>
	<%}%>
<!-- 파일선택하도록. 양이 많으니 셀렉트옵션태그. option value 전송값.  -->

	text 파일 선택 : <br>
	<select name="text"> <!-- 컨트롤러/sttresult 여기다 지정해둔이름. -->
		<c:forEach items="${filelist }" var="onefile">
			<option value="${onefile }">${onefile } </option>
		</c:forEach>
	</select>
	<input type=submit value="mp3 변환 요청">
</form>




</body>
</html>

<!-- 
전달할거 2개 
음색, 
 -->


