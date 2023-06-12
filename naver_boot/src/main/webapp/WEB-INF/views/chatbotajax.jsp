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
			success: function(server){ //이곳만 변경 3가지 - 기본/이미지/멀티링크 =>가진변수다르니까.
				let bubbles = server.bubbles;
				for(let b in bubbles){
					//1.기본(텍스트/ 텍스트+url)
					if(bubbles[b].type=='text'){
						$("#response").append("기본답변 : " + bubbles[b].data.description + "<br>");
						if(bubbles[b].data.url != null){ //url 있다면
							$("#response").append
							('<a href=' + bubbles[b].data.url + '>' + bubbles[b].data.description + '</a><br>');
						}
					}
					//이미지이거나 멀티링크
					else if(bubbles[b].type=='template'){
						//2.이미지(이미지)
						if(bubbles[b].data.cover.type == 'image'){
							$("#response").append
							('<img src=' + bubbles[b].data.cover.data.imageUrl + ' width=200 height=200 ><br>');
						}
						//3.멀티링크(url) 
						else if(bubbles[b].data.cover.type == 'text'){
							$("#response").append("멀티링크답변 : " + bubbles[b].data.cover.data.description + "<br>");
						}
						//4.이미지+멀티링크 공통(url)
						for(let c in bubbles[b].data.contentTable){
							for(let d in bubbles[b].data.contentTable[c]){
								let link = bubbles[b].data.contentTable[c][d].data.title;
								let href = bubbles[b].data.contentTable[c][d].data.data.action.data.url;
								$("#response").append('<a href=' + href +'> ' + link +'</a><br>');
							}
						}
					
					}
				
				}//for(let b in bubbls) end
			},//success end //서버로부터받은 json결과 //결과중 답변.
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
 
 <!-- 
 for(let b in bubbles){
					//1.기본 -텍스트, 텍스트+url // **<주의 >** bubbles배열타입임!!! 여러개나올수도있음. 
					if(bubbles[b].type=='text'){
						$("#response").append("기본답변 : "+bubbles[b].data.description+ "<br>");
						if(bubbles[b].data.url !=null){ // 텍스트+url 있다면,
							$("#response").append('<a href='+ bubbles[b].data.url +'>'+ bubbles[b].data.description +'</a><br>');
							
						}
					}
					else if(server.bubbles[0].type=='template'){
							//2.이미지
						if(bubbles[b].data.cover.type=='image'){
							$("#response").append('<img src='+ bubbles[b].data.cover.data.imageUrl +' width=200 height=200><br>');
							//2-2.이미지 url
							if(bubbles[b].data.cover.data.imageUrl !=null){
								$("#response").append('<a href='+bubbles[b].data.contentTable[0][0].data.data.action.data.url +'>'
										+bubbles[b].data.contentTable[0][0].data.title+'</a><br>');
							}
						}
						else if(bubbles[b].data.cover.type=='text'){
						//3.멀티링크(url) + 이미지(url)
							$("#response").append("멀티링크답변 :" +bubbles[b].data.cover.data.description +"<br>"); //기본답변 위치랑 다름. 더깊음.
							//<a href = 'url'> 요기요</a>
							for( let c in bubbles[b].data.contentTable) //conten테이블 있는 만큼반복.
								for(let d in bubbles[b].data.contentTable[c]){
									//bubbles[b].data.contentTable[c][d] //보기1개.
									let link = bubbles[b].data.contentTable[c][d].data.title;//배민,요기요 등 나옴.
									let href = bubbles[b].data.contentTable[c][d].data.data.action.data.url;
									$("#response").append('<a href = '+href+'>'+ link+'</a><br>'); //보기들을 링크시킴.
							}
						}//elseif end
					}
				}//for(let b in bubbles) end
 
 
  -->