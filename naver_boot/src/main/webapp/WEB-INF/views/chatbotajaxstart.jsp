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
	//1.type button 클릭하면, id(request)인 질문내용을 div response에 붙여넣는다. (div 대화내용쌓아두기때문)
	$("input:button").on("click",function(){
		$("#response").append("질문 : "+$("#request").val() + "<br>");
		$.ajax(
		{
			url: "/chatbotajaxprocess", //포트번호생략.
			data: {"request" : $("#request").val() , "event" : $(this).val()}, //현재클릭한자신this.의 값.
			type:'get',
			dataType: 'json',
			success: function(server){
				$("#response").append("답변 : " + server.bubbles[0].data.description + "<br>");
				},//서버로부터받은 json결과 //결과중 답변.
			error : function(e){alert(e);} //에러내용.
			
		}				
		);//ajax end
		
	});//on click end
	
	
	
});
</script>
</head>
<body>

질문 : <input type=text id="request">
<input type=button value="답변" id='event1'>
<input type=button value="웰컴메세지" id='event2'>

<br>
대화내용 : <div id="response" style="border: 2px solid pink"></div>



</body>
</html>
<!-- 1줄 text, 여러줄 textarea -->
<!-- 서브밋 폼안에 2개이상이어도 버튼만여러개지, 뭘 클릭해도 액션은 동일 
name=이벤트 따라 답변다르게나올것. 
 -->