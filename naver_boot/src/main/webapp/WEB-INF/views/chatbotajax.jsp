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
					//1.기본(텍스트/ 텍스트+url) ->텍스트 음성으로 변경.
					if(bubbles[b].type=='text'){
						$("#response").append("기본답변 : " + bubbles[b].data.description + "<br>");
						if(bubbles[b].data.url != null){ //url 있다면
							$("#response").append
							('<a href=' + bubbles[b].data.url + '>' + bubbles[b].data.description + '</a><br>');
							// bubbles[b].data.description 이게 텍스트답변인데 이걸 음성으로 바꿔 줄 것. 
						}
						//텍스트라면.-mp3로 변형.				
						$.ajax({
							url : '/chatbottts',
							data : {'text' : bubbles[b].data.description},
							type : 'get',
							dataType : 'json',
							success : function(server){
								//alert(server.mp3); 확인용.
								//실제론 오디오태그에 보여줘야한다. --------
								//id tts audio 태그 재생.
								//1.id tts인 태그 읽어라. dom으로.
								let audio = document.getElementById("tts");
								
								//2.1번 src속성 재생 mp3 지정
								audio.src = "/naverimages/"+server.mp3;
								
								//3. 1번 play()
								audio.play();//자동재생. 없으면, 시작버튼 눌러야함.
								
								
								
							},
							error : function(e){ //필수아님.
								alert(e);
							}
							
						});//ajax end
					
						//////////////////////////////////////////////
						/// 피자주문						
						//////////////////////////////////////////////
						var order_reply = bubbles[b].data.description; // 주문에대한 답변/ 여러번쓸거같아서 변수선언.
						
						if(order_reply.indexOf("주문하셨습니다.") >=0){   
							// >=0, 주문포함하고있다 란 소리.
						//챗봇답변이 일정형식있음, 맨앞, 피자종류 공백 사이즈 공백 주문하셧습니다. 전화번호. 이런순서. 
						//답변 전체를 공백으로 나누어 스플릿.
						//0~3번 인덱스까지 필요한 정보임. 파인애플피자/소/주문하셨습니다/핸드폰번호
							var split_result = order_reply.split(" "); //공백으로 분리.-배열임
							var kind = split_result[0];//피자종류
							var size = split_result[1];//사이즈
							var phone = split_result[3];//폰번호
							var kinds = ["콤비네이션피자","소세지크림치즈피자","파인애플피자"];
							var prices = [10000,15000,12000];
							var sizes = ["소","중","대","특대"];
							//소 : 기본가격, 중 : 기본가격+2000, 대: 기본가격+5000, 특대 : 기본가격+10000
							var add_prices = [0, 2000, 5000, 10000];
							
							var price = 0;
							var addPirce = 0;
							var totalPrice = 0;
							
							
							for(var i=0; i<kinds.length; i++){
								if(kind == kinds[i]){
									price = prices[i];
									break;
								}
									
							}
							for(var i =0; i<sizes.length; i++){
								if(size == sizes[i]){
									addPirce = add_prices[i];
									break;
								}
							} 
							
							totalPrice = price + addPirce ;
							 $("#response").append("총 지불가격 : " + totalPrice);
							 
							 
							//피자-db저장ajax ==============================테스트중.
							$.ajax(
								{
									url: "/pizzaorder", 
									data: {
										"kind":kind, 
										"size":size, 
										"price":totalPrice,
										"phone":phone}, //data end -게터세터있어야만 가능.
									type:"get",
									dataType: 'json',
									success: function(server){
										//인서트행갯수 전달해보도록하자. 
										alert(server.insertrow);
									} //오류발생은 제외함.
								});//ajax end
							 
							
						}//if end
						
						
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
<button id= "record" > 음성질문녹음시작 </button>
<button id= "stop" > 음성질문녹음종료 </button>
<div id="sound"></div>

<br>
대화내용 : <div id="response" style="border: 2px solid pink"></div>
음성답변 : <audio id="tts" controls="controls"></audio>

<script>
//스크립트 태그 한 파일안에 여러군데있어도됨.
let record = document.getElementById("record");//레코드버튼 클릭이벤트처리위해
let stop = document.getElementById("stop");
let sound = document.getElementById("sound");

//브라우저 녹음기나 카메라 사용 지원여부.
if(navigator.mediaDevices){ //사용할수이니? 
		console.log("지원가능");//개발자도구서 보이는곳.
		var constraint = {"audio":true}; //녹음기능 활성화 . //카메라쓸거면 video쓰겠다하면됨.
}

//녹음 진행 동안 -blob객체 - 녹음 종료- mp3파일 생성 저장 //이진수 기록객체. blob 녹음내용가지고있는 객체를 ->mp3로 만들어줘.그걸 div태그 audio에들려줘.
let chunks = []; //여기다 녹음내용 모아둘것, 배열에다. 모았다 blob으로 만들어서 mp3객체로만들어줄것.
//catch 에러발생시.에러처리
navigator.mediaDevices.getUserMedia(constraint).then( function(stream){//녹음기사용가능하면, 스트림이 녹음중데이터를말함, 그걸 미디오레코더 녹음기로쓰겠다?
	var mediaRecorder = new MediaRecorder(stream);//녹음기 준비.(녹음할준비o)
	record.onclick = function(){
		mediaRecorder.start();//녹음시작
		record.style.color = "red";//녹음중일떈 빨간색보여짐.
		record.style.backgroundColor="blue";
	}
	
	stop.onclick= function(){
		mediaRecorder.stop();//녹음중단
		record.style.color = "";
		record.style.backgroundColor="";
	}
	
	//녹음시작상태이면,(시작버튼누르면) chunks에 녹음 데이터 저장하라. 
	mediaRecorder.ondataavailable = function(d){ //데이터모아.
		chunks.push(d.data);//녹음데이터 모아.
		
	}

	//녹음 정지상태이면 chunks=>blob - >mp3
	mediaRecorder.onstop = function(){
		//audio태그 생성부분-- audio태그 추가하라. 
		var audio = document.createElement("audio");//돔구조로 오디오 태그만들어. 
		audio.setAttribute("controls","");
		audio.controls = true;//<컨트롤즈 속성 하나만든것. <audio controls></audio>
		sound.replaceChildren(audio); //이전꺼 다 없애고 추가하라고, 앞에 써줌.
		sound.appendChild(audio);//[계속늘어남]오디오 태그 추가하라. <div><audio controls></audio></div>
		
		
		//녹음 데이터 가져와서 audio 태그 재생
		var blob = new Blob(chunks, {"type":"audio/mp3"});//녹음데이터 chunk를 가져와서 blob으로만들고, 그걸 아래서 mp3로만든것. 
		var mp3url = URL.createObjectURL(blob);
		audio.src= mp3url;
			
		//다음 녹음을 위해 chunks 초기화  : 녹음시작누르면 새롭게 써야하니 초기화.
		chunks = [];
		
		//바로업로드할거라 이제 이거 a태그필요없어짐----------------
		//var a = document.createElement("a");
		//sound.appendChild(a); //<div><audio controls></audio> <a href=mp3파일명>파일로저장</a> </div>
		//a.href=mp3url;
		//a.innerHTML = "파일로 저장";
		//a.download = "a.mp3";
		//클라이언트 컴퓨터에 브라우저가 지정한 경로에 \a.mp3로 저장
		
		
		//스프링부트 서버 요청. a.mp3 upload + ajax로 업로드. 
		//new FormData() 파일하나 담아서 ajax형태로 요청??할때 , 필요함. -소슨 다 있는데, .. ....ㅇ..ㅎ...firstboot -다운/업로드
		//스프링부트 서버로 upload 구현
		var formData = new FormData();
		formData.append("file1", blob, "a.mp3"); //변수명 file1으로 정함.= a.mp3파일줄것.(서버로 보낼때 이 이름으로 보낼것임)
		//보낼거 더 있으면 append계속더해.
		//파일1이란이름으로 blob데이터 , 보내는데 server.mp3파일이름으로 보낼것임. 혼란최소화위해 걍 a.mp3로 클라이언트폴더에있는이름대로보낸다.
		//blob데이터.오디오 정보 객체 var blob = new Blob(chunks, {"type":"audio/mp3"});
		//전송해야할폼이라 생각하면, <ioput type=file name="file1"> 파일열기창, 파일1변수에저장되서 서버에전송.이걸흉내 폼없어서.
		
		$.ajax({
			url : "/mp3upload", //슬래스쓰면 localhost:8084 포트번호까지생략. 뒤에 /ai/mp3upload컨텍스트있으면 써줘야함.
			data :formData,//여기안에 파일하나있어//파일전송시.----------------1.파일전송시 꼭써야함.
			type : "post",			//파일전송시.----------------2.파일전송시 꼭써야함.
			processData : false, //파일전송시.----------------3.파일전송시 꼭써야함.
			contentType : false, //지금까지보내던타입이녀.--------4.파일전송시 꼭써야함.
			success : function(server){
				//alert(server);
				//a.mp3파일 서버 녹음 파일 저장 -->stt서비스로변환.
				//서버에업로드된 mp3파일을 텍스트로 변환.
				$.ajax({
					url:"/chatbotstt",
					data:{"mp3file":"a.mp3"}, //파일이름만전송, 경로어케알아"? 마이네이버인폼패스로 경로써서 통일된거다. 컨트롤러든 jsp던. 아니면경로까지 매개변수로전달해야함.
							//a.mp3로만 만들어놔가지고 이 이름으로하면됨.
					type : "get", 
					dataType : 'json',
					success : function(server){
					//변수text에 담겨왔음. -음성질문녹음한걸 text로 , 이걸 질문으로 넣자. -div 질문 id=request에 셋팅.보여지게
					//리퀘스트 인풋태그. 값 가져올때 val(매개변수 )//인풋태그 값을 매개변수로바꿔줘.
						$("#request").val(server.text);
					//질문입력란에 셋팅->그후 답변눌러. 
					}
					
				});//내부ajax end
			},
			error : function(){
				
				
			}
			
			
		});//ajax end
		
		
	}
	
	
})//then end- then안에 함수들어가있듯, catch 에러처리도 안에 들어감. //녹음 내가 쓰겠다.//콜백함수
.catch(function(err){console.log("오류발생" + err )});//catch end







</script>





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