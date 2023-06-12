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


<h3>${sttresult}</h3>
<audio id="mp3" src="/naverimages/${param.mp3file}" >
</audio>
<!-- 오디오 - 컨트롤즈 없으면, 재생바가 없어. 아니면 아래 스크립트에 시킨다. <audio id="mp3" src="/naverimages/${param.mp3file}" controls>
컨트롤즈 없으면 : play();자동시작. 
-->

<!-- 얘가 위쪽이면 윈도우.onload해야함. -->
<script>
document.getElementById("mp3").play();
</script>

</body>
</html>
<!-- param.뭐 =>요청받아 처리하던 stt컨트롤러 sttresult 전달받은 매개변수 
JSP : @RequestMapping("/sttresult")
	public ModelAndView sttresult(String mp3file, String lang) 여기꺼 쓴거.
 -->