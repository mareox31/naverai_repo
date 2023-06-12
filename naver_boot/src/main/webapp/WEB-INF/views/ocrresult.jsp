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
	var json = JSON.parse('${ocrresult }');//string-->JSON 객체 변환(JSON.parse).json.변수명 //객체로바뀐거니까.
	//문자열String데이터 string(json형식갖춤.)-제이슨을 스트링형태로가지고있음, 
	//스트링형태를 JSON객체 형태로 변환해준다.
	$("#output").html(JSON.stringify(json) );//json-->string 변환(JSON.stringify) //이결과만출력하면 {'변수명':'값'...}
	//제이슨 다시 문자열형태로 펼쳐둠	
	//제이슨객체가진걸 문자열로 바꾸고싶다.
	
	
	//json.predictions[0][0~17인덱스있음].score 정확도, x 좌표, y 좌표 //스코어 ,x, y하나씩있음.
	//json.predictions[0][0].y //코의 y좌표
	//원본 이미지위에다 해야하므로, 우선 포즈캔버스에 이미지넣어야함.
	//제이쿼린데.
	//$("#posecanvas");//제이쿼리객체.(배열)- 내부적 배열 //-.getcontext가 없어.//모든제이쿼리객체 .get(0);제이쿼리객체 0번인덱스 줘. 
	//$("#posecanvas").get(0);//제이쿼리객체를 자바 스크립트 객체로 바꿈(리턴이 자바스크립트. )-여기까지해야 자바스크립트객체임.
	
	//캔버스 하나 읽자.
 	let mycanvas = document.getElementById("ocrcanvas");
	let mycontext = mycanvas.getContext("2d");
	let myimage = new Image(); //우선 빈 이미지.
	myimage.src = "/naverimages/${param.image}"; //경로가 없어서, 경로 맵핑을 써줘야함.
	//모든 이미지 로딩대기시간.
	myimage.onload = function(){ //콜백함수 자동실행
		
		//캔버스크기조정.
		if(myimage.width> mycanvas.width){
			mycanvas.width = myimage.width;
				if(myimage.height> mycanvas.height){
					mycanvas.height = myimage.height;
				}
		}
		else{
			if(myimage.height> mycanvas.height){
				mycanvas.height = myimage.height;
			}
		}//캔버스크기조정end
		
		
		mycontext.drawImage(myimage, 0,0,myimage.width, myimage.height); //매개변수5개," 뭘?, x좌표,y좌표, 가로크기, 세로크기 "
		//이미지뜨고 후속작업가능
		//이미지 글씨 박스화
		//JSON(데이타모두- 이미지즈의 0번인덱스, fields까지찾아야 글자 정보나옴.)
		let fieldslist = json.images[0].fields;//배열 단어 갯수만큼의 분석된 배열임.
		for(let i in fieldslist){
			if(fieldslist[i].lineBreak == true){
			$("#output2").append(fieldslist[i].inferText+ "<br>");//1단어 정보.[i]까지.//html은 앞내용지우고 새내용바꿈.
			}
			else{
				$("#output2").append(fieldslist[i].inferText+ "&nbsp;");
				}
			
			var x =fieldslist[i].boundingPoly.vertices[0].x;//단어시작x좌표. 
			var y =fieldslist[i].boundingPoly.vertices[0].y;
			//글씨는 사각형이니까. x,y좌표.
			var width = fieldslist[i].boundingPoly.vertices[1].x -x; //단어 가로크기
			var height = fieldslist[i].boundingPoly.vertices[2].y - y; //단어 세로크기.
			
			mycontext.strokeStyle="blue";
			mycontext.lineWidth=2;
			mycontext.strokeRect(x,y,width,height);//사각형그릴때 - 요소4가지 시작x,y좌표, 가로크기, 세로크기. - 글씨발견 가로세로크기.
			//103-63 가로길이 1번인덱스-0번인덱스.
			}//for end
		
	}//myimage.onload
	 
});
</script>
</head>
<body>
<div id="output" style="border:2px solid orange"></div> <!-- 전체제이슨결과모두 -->
<div id="output2" style="border:2px solid green"></div> <!-- 글씨만  -->
<canvas id="ocrcanvas" style="border:2px solid purple" width="500" height="500"> 
</body>
</html>

<!-- ex=사무실 이란(원하는) 글씨 찾아서 박스처리  // 글시는다 체크-->