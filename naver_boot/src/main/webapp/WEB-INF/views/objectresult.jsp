<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="/js/jquery-3.6.4.min.js"></script> <!-- 지금은 내 프로젝트꺼 -->
<!-- 만약 firstboot폴더안에(여기포트가 8063임)쓸때 http://localhost:8063 -->
<script>
//JSON.parse("{'a':100}") //스트링으로바꿔줌.
//objectresult 타입은 스트링인데, 데이터 형태 가지고있음.
//모든EL다른언어갑으로쓸 수 있음.
//var json = JSON.parse('${objectresult}');//저 결과는 json객체타입임. 
//전체 데이터 다가지고있음 json
//자바스크립트니까 .으로 불러올 수 있음.   //num_detections갯수
//돔구조이용. 카운트id넣어주기.
//json.predictions[0].num_detections


$(document).ready(function(){
	var json = JSON.parse('${objectresult}');
	$("#count").html("<h3>" + json.predictions[0].num_detections + " 개의 사물 탐지</h3>");
	
	for(var i =0; i < json.predictions[0].num_detections; i++){ //발견된갯수만큼 반복.
		$("#names").append(
		json.predictions[0].detection_names[i] + " - " + //여기까지 배열 //발견된사물이름.
		parseInt(parseFloat(json.predictions[0].detection_scores[i]) * 100) + " 퍼센트<br>"
		//0.4546실수화 ->*100 - >정수화
		//parseInt(json.predictions[0].detection_scores[i]) * 100 + " 퍼센트 <br> "  //배열 //발견사물 정확도
		);

		//좌표 , x는 왼쪽부터 얼만큼 - > y브라우저위부터 얼마떨어져있나.
		//x1,y1 왼쪽위, x2,y2 오른쪽아래 이렇게 네모만들어짐.
		//y2-y1 세로 -width, height는 이걸로 확인.
		//x1, y1 = 0.444563 , 0.135105x2, y2 = 0.760471 , 0.320986
		//전체 이미지사진에 0.44정도 차지하는위치 비율.
/*		var x1 = json.predictions[0].detection_boxes[i][0] //첫 x좌표 
		var y1 = json.predictions[0].detection_boxes[i][1]
		var x2 = json.predictions[0].detection_boxes[i][2]
		var y2 = json.predictions[0].detection_boxes[i][3]
		$("#boxes").append("x1, y1 = " + x1 + " , " + y1 + "x2, y2 = " + x2 + " , " + y2 +"<br>"); 
*/	
	
	
	}//for end
	
	
	//캔버스 보여주는동안은 반복문 안 일 필요가 없다. 
	//이미지 캔버스에 그리고 이미지위에 박스치자. 
	let mycanvas = document.getElementById("objectcanvas");
	let mycontext= mycanvas.getContext("2d");
	
	let faceimage = new Image();
	faceimage.src = "/naverimages/${param.image}";//url 통신 이미지 다운로드 시간 대기
	faceimage.onload = function(){
		//화면에 이미지 뜰 때까지 대기.그래서 온로드밑실행하고 여기로올수도있음.- 펑션내부, 펑션 밖 작성 차이주의
		mycontext.drawImage(faceimage, 0, 0, faceimage.width, faceimage.height);
	
		//여기다 사각형그리는 작업 한다.
		var boxes = json.predictions[0].detection_boxes;//발견된 갯수만큼 배열 - 사물갯수만큼 좌표값.
		//boxes[0] //이건 객체임. 1개 사물의 좌표값 4개를 변수로 한 객체.(사실은 배열, 4개 좌표값 가진 배열.)
		//boxes[0][0]//좌표값 하나하나. 
		
		for(var i =0; i< boxes.length; i++){ //사물갯수만큼 반복
			//좌표계산 4가지값의 실제크기 계산. 전체 이미지 크기만큼의 비율이므로, 곱해서 가져옴.
			//x * width, y* height
			//실제 이미지 비율대로 비율계산한 실제 좌표.
		/*	가이드랑 달리 순서가 바뀌어있음.
			var x1 = json.predictions[0].detection_boxes[i][0] * faceimage.width //이미지가로크기; -가로시작위치. //faceimage.width가로크기
			var y1 = json.predictions[0].detection_boxes[i][1] * faceimage.height
			var x2 = json.predictions[0].detection_boxes[i][2] * faceimage.width
			var y2 = json.predictions[0].detection_boxes[i][3] * faceimage.height
		*/	
			var y1 = json.predictions[0].detection_boxes[i][0] * faceimage.height
			var x1 = json.predictions[0].detection_boxes[i][1] * faceimage.width 
			var y2 = json.predictions[0].detection_boxes[i][2] * faceimage.height
			var x2 = json.predictions[0].detection_boxes[i][3] * faceimage.width 
			
			
			//항상 스타일 지정 먼저해줘.
			//색상도 다르게해보자. let colors=["red", "yellow", "orange"];
			//사각형
			mycontext.lineWidth = "3";
			mycontext.strokeStyle = "green";
			mycontext.strokeRect(x1,y1, (x2-x1),(y2-y1)); //앞두개 좌표, 가로세로크기. - 가로는 x2-x1
			
			
			
			//텍스트- 사물이름써주ㅏ.(퍼센트까지 위에서 가져와도됨)
			mycontext.fillStyle="purple";
			mycontext.font = "bold 12px batang";
			mycontext.fillText(json.predictions[0].detection_names[i],x1, y1-5 ); //요소 3. 글씨 쓸 내용, 글씨x좌표, 글씨y좌표.
			
			
			
			
			//글자 테두리에 네모칸.-테스트해봤음.
			var textWidth = mycontext.measureText(json.predictions[0].detection_names[i]).width;
			mycontext.lineWidth = "2";
			mycontext.strokeStyle = "red";
			mycontext.strokeRect(x1,y1-15, textWidth, 17);  
			
		}//for end
	
	}//faceimage onload end
	//온로드밑 
	
	
	
	
});
</script>
</head>
<body>
<h3>${objectresult }</h3>


<!-- 1.얼굴분석 서비스 - 모델을 json객체 변환 - 자바API썼음(pom.xml추가) -->
<!-- 2.사물인식 서비스 - 모델을 json객체 변환 - 자바스크립트사용.(별도라이브러리추가필요x) JSON.parse -->

<script>





</script>
<div id="count"></div>
<div id="names" style="border : 2px solid lime"></div>
<div id="boxes" style="border : 2px solid orange"></div>
<canvas id="objectcanvas" width="800" height="500" style="border:2px solid pink"></canvas>

</body>
</html>

<!-- 
JSONObject total = new JSONObject(request.getAttribute("objectresult")) 
읽어오고, 전부 읽어온게 토탈. 그거의 predictions의 , 
(JSONArray)total.get("predictions") //배열이었음 ->0번인덱스, 갯수. 
계속 제이슨 오브젝트, 어레이->변수읽고 이래야하는데,
:코드너무복잡해. 바꿔보자. 

-->