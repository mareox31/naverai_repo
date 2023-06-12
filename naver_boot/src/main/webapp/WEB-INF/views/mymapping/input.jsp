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
	$("#ajaxbtn").on("click",function(){
				
		$.ajax({
			url : "/myoutput",
			data : {'request':$("#request").val()}, 
			type : "get",
			dataType : "json",
			success : function(server){
				$("#response").html(server.response);
				$("#mp3").attr("src","/naverimages/"+server.mp3);
			},
			error :function(e){
				alert(e);
			}
			
		});//ajax end
	});//on end
});//ready end

</script>

</head>
<body>
<!-- 여기는 이동 -->
<!-- 
<form action="/myoutput">
질문 : <input type=text id="request" name="request">
<input type=submit value="대화" >
</form>
 -->

<!-- 여긴 눌러도 이동안해, 폼 없어서, 그냥 머물러있음. 서브밋 의미가 없어.  -->
질문 : <input type=text id="request" name="request">
<input id="ajaxbtn" type=button  value="대화" >


<!-- 1.자리만들어둔다. div로  -->
<h3>답변(텍스트)<div id="response"></div></h3>
<audio id="mp3" src="" controls ></audio>


</body>
</html>

<!-- jquery 후속처리, CSS (Id) // name속성 : 값넘길때 request변수에담아 전달.  -->
<!-- 
1.인풋jsp , 답변받아 올 것, 현재 뷰에서 다 보여준다. 
2.에이젝스 제이쿼리 이용.

$(document).ready(function(){
	//구현
	#("#ajaxbtn").on("click",function(){
				
		$.ajax({
			url : "/myoutput",
			data : {'request' : $("#request").val()}, 
						//질문내용전달.
						//데이터전송방식, '컨트롤러가 전달받아야하는 매개변수랑 똑같이 생긴 변수 ' : 질문내용(아래임력된것.)
			type : "get",
			dataType : "json",
			success : function(server){
				//변수 - 서버로부터 전달받은 결과임.
				//server={"response" : 텍스트답변, "mp3" : 변환된mp3파일이름 }//컨트롤러는 json줌 =>컨트롤러 이렇게 리턴되게바꿔======
				//텍스트 답변 꺼내오려면 server.response  쓰고, div에넣는다. 
				$("#response").html(server.response);
				$("#mp3").attr("src","/naverimages/"+server.mp3);
				//제이쿼리 쓰고있음. attr 속성 .attr("src","속성넣을값")
				//오디오태그 - src속성이있고. 거기에 파일이름적어.url경로포함.
			},//error, success는 함수
			error :function(e){
				alert(e);
			}
			
		});//ajax end
	});//on end
});//ready end

 -->
