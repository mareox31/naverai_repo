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
<ul>
	<li><a href="/faceinput"> 유명인 닮은꼴 서비스 </a></li>
	<li><a href="/faceinput2"> 얼굴분석(표정,나이 등) 서비스 </a></li>
	<li><a href="/objectinput"> 객체탐지 서비스 </a></li>
	<li><a href="/poseinput"> 자세분석 서비스 </a></li>
	<li><a href="/sttinput"> STT 서비스(mp3를 text로 변환) </a></li>
	<li><a href="/ttsinput"> TTS 서비스(text를 mp3로 변환) </a></li>
	<li><a href="/ocrinput"> OCR 서비스(글자분석서비스) </a></li>
	<!-- <li><a href="/chatbotrequst"> 챗봇 서비스</a></li> -->
	<!-- <li><a href="/chatbotajaxstart"> 챗봇 서비스(ajax)</a></li> -->
	<li><a href="/chatbotajax"> 챗봇 서비스(ajax)</a></li>
</ul>

</body>
</html>