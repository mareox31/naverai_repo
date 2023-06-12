<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 스크립트내부서 jsp코드 넣어도됨. -->
<script>
window.onload = function(){
	let mycanvas = document.getElementById("facecanvas");
	let mycontext= mycanvas.getContext("2d");//붓
	//drawImage등등 쓰면됨.
	//이미지 위에 사각형그려.
	
	let faceimage = new Image(); //빈 이미지.
	//컨트롤러가 전달받은 이미지, 모두 바탕화면 이미지즈에있음. 맵핑할 수 있또록 설정한 파일 mywebconfig. /naverimages/설정해놨음
	//faceresult3에서 http://localhost:8064/naverimages/${param.image} 보여줘야할 이미지임.
	//url맵핑이 naverimages //포트번호까지생략하려면 /부터시작.
	//컨트롤러 매개변수 jsp가져올떄 ${param.image}
	faceimage.src="/naverimages/${param.image}";//url통신 이미지 다운로드 시간 대기 // 이 부분 없으면 그릴 이미지가없음. 
	//이미지에src속성넣어서, 어떤이미지 그릴거야 말하면된다. 
	//이미지도 실제 이미지 url해당 url과 통신해서, 그 이미지를 다운로드 하는시간동안 대기해야한다. 그래야 이미지 보여줄 수 있다.
	faceimage.onload = function(){
		mycontext.drawImage(faceimage, 0,0, faceimage.width, faceimage.height ); //0,0 캔버스 시작부분부터, 원 이미지 크기대로가져오자. 
		//그릴대상, 크기나+xy좌표지정해야함.
		//여기부터 얼굴좌표필요함. 
		//여기다 jsp넣으면됨--------------------
		<%
		String faceresult2 = (String)request.getAttribute("faceresult2");
		JSONObject total = new JSONObject(faceresult2);

		//얼굴크기정보만 필요. faces[i].roi.x   /  faces[i].roi.y  /   faces[i].roi.width    / faces[i].roi.height
		JSONArray faces = (JSONArray)total.get("faces");
		//성별, 나이, 감정, 얼굴방향, 눈코입위치(ladnmark= null일 경우) //faceCount == faces.length(); //faces.get(i);1사람정보. 
		//젠더는 제이슨오브젝트임.
		for(int i = 0; i<faces.length(); i++){
			JSONObject oneperson = (JSONObject)faces.get(i);
			JSONObject roi = (JSONObject)oneperson.get("roi");//x,y,width,height
			int x = (Integer)roi.get("x");//얼굴시작x좌표
			int y = (Integer)roi.get("y");
			int width = (Integer)roi.get("width");
			int height = (Integer)roi.get("height");
		%>
		//-----------------------여기까지.↑저 변수는 jsp변수임.
		mycontext.lineWidth = "3";
		mycontext.strokeStyle = "pink";
		mycontext.strokeRect(<%=x%>,<%=y%>,<%=width%>,<%=height%>); //얼굴위치인식
		
		
		//일부 이미지만 이동.
		//getimage복사본가져오기, 붙여넣기 put
		var copyimage = mycontext.getImageData(<%=x%>,<%=y%>,<%=width%>,<%=height%>);//얼굴영역 일부이미지 잘라내고.카피이미지.
		mycontext.putImageData(copyimage,<%=x%>, <%=y+300%> ) //붙여넣기위치 정하기. -어떤이미지를?, x좌표, y좌표
		
		
		
		// 채우면 fill, 겉테두리만은 stroke (x,y,가로,세로) 
		mycontext.fillStyle="orange";
		mycontext.fillRect(<%=x%>,<%=y%>,<%=width%>,<%=height%>);//모자이크처리
		
		
		
		
		<%
		}//jsp for end
		%>
	}//faceimage.onlad end
}//onload end

</script>

</head>
<body>


<canvas id="facecanvas" width="800" height="500" style="border:2px solid pink"></canvas>


</body>
</html>
<!-- 그림그리는 작업은 자바스크립트로. -->