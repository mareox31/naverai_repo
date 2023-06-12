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
	var json = JSON.parse('${poseresult }');
	//json.predictions[0][0~17인덱스있음].score 정확도, x 좌표, y 좌표 //스코어 ,x, y하나씩있음.
	//json.predictions[0][0].y //코의 y좌표
	//원본 이미지위에다 해야하므로, 우선 포즈캔버스에 이미지넣어야함.
	//제이쿼린데.
	//$("#posecanvas");//제이쿼리객체.(배열)- 내부적 배열 //-.getcontext가 없어.//모든제이쿼리객체 .get(0);제이쿼리객체 0번인덱스 줘. 
	//$("#posecanvas").get(0);//제이쿼리객체를 자바 스크립트 객체로 바꿈(리턴이 자바스크립트. )-여기까지해야 자바스크립트객체임.
	
	//캔버스 하나 읽자.
	let mycanvas = document.getElementById("posecanvas");
	//자바 스크립트 객체형태이걸로 할 때 .getContext("2d");해야. 물감도나오고..뭐도그리고 됨.
	//document.getElementById("posecanvas").getContext("2d");
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
		
		//18개 포즈 배열 선언.
		//var body_inform=["코","목","오른쪽 어깨","오른쪽 팔굼치","오른쪽 손목","왼쪽 어깨","왼쪽 팔굼치","왼쪽 손목","오른쪽 엉덩이","오른쪽 무릎"
		//	,"오른쪽 발목","왼쪽 엉덩이","왼쪽 무릎","왼쪽 발목","오른쪽 눈","왼쪽 눈","오른쪽 귀","왼쪽 귀"];
		
		let bodyParts = ["코", "목", "오른쪽 어깨", "오른쪽 팔굼치", "오른쪽 손목", "왼쪽 어깨", "왼쪽 팔꿈치", "왼쪽 손목", "오른쪽 엉덩이", "오른쪽 무릎", "오른쪽 발목", "왼쪽 엉덩이", "왼쪽 무릎", "왼쪽 발목", "오른쪽 눈", "왼쪽 눈", "오른쪽 귀", "왼쪽 귀"];
		
		let colorinforms = ['red', 'orange', 'yellow', "green", "navy", "purple"];
		
		//2.여러명 신체 부위 분석 확대
		for(var j =0; j<json.predictions.length; j++){
			for(var i = 0; i < bodyParts.length; i++){
				if(json.predictions[j][i] !=null){
					var x = json.predictions[j][i].x * myimage.width; //.x 0.111비율 * 이미지 가로크기  //코정보. -.score정확도 
					var y = json.predictions[j][i].y * myimage.height;
					//1.colorinforms색상으로 글씨 변경
					
					mycontext.fillStyle=colorinforms[i % colorinforms.length];//0-6
					mycontext.font = "bold 12px batang";
					
					mycontext.fillText(bodyParts[i], x, y); //코란 텍스트를 x랑 y에 찍어라.(코좌표에)
					
					
				}//if end
			}//안for end-1사람당 포즈
		}//밖for end-사람여러명
	}//myimage.onload
	
});
</script>
</head>
<body>

<%--  <h3>${poseresult }</h3>  --%>

</body>
<div id="output" style="border:2px solid orange"></div>
<canvas id="posecanvas" style="border:2px solid purple" width="500" height="500"> 
</html>

<!-- 신체부위 알려주는 서비스. -->