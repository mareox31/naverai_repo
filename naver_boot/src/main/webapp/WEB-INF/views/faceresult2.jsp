<%@page import="java.math.BigDecimal"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>${faceresult2 }</h3>

<!-- 겟어트리뷰트 - 오브젝트임. 우린 스트링으로전달받음, 스트링으로줬으니께 -->
<!-- w제이슨오브젝트.get은 오브젝트임.
인포가무슨타입인지 알려줘야함.   info{} 2key 이게 두개 객체
 페이스카운트 값 줘. faceCount 7 (정수임)인티저형변환.
 여러명 반복문. 
 페이시즈 - 배열. 
 -->
 <!-- jsp -->
 <!-- faceresult2변수이름이라 ""필요없어. -->
<%
String faceresult2 = (String)request.getAttribute("faceresult2");
JSONObject total = new JSONObject(faceresult2);
JSONObject info = (JSONObject)total.get("info");
int faceCount= (Integer)info.get("faceCount");


JSONArray faces = (JSONArray)total.get("faces");
//성별, 나이, 감정, 얼굴방향, 눈코입위치(ladnmark= null일 경우) //faceCount == faces.length(); //faces.get(i);1사람정보. 
//젠더는 제이슨오브젝트임.
for(int i = 0; i<faces.length(); i++){
	JSONObject oneperson = (JSONObject)faces.get(i);
	JSONObject gender = (JSONObject)oneperson.get("gender");
	String gender_value = (String)gender.get("value");
	//나머지도 아래 conf 이걸로 활용하면됨.
	BigDecimal gender_conf = (BigDecimal)gender.get("confidence");
	Double gender_conf_double = gender_conf.doubleValue();
	
	//위에꺼활용
	JSONObject age = (JSONObject)oneperson.get("age");
	String age_value = (String)age.get("value");
	
	JSONObject emotion = (JSONObject)oneperson.get("emotion");
	String emotion_value = (String)emotion.get("value");
	
	JSONObject pose = (JSONObject)oneperson.get("pose");
	String pose_value = (String)pose.get("value");
	
	out.println("<h3>" + (i+1) + " 번째 얼굴의 성별 = " + gender_value + " , 정확도 = " + gender_conf_double + "</h3>");
	out.println("<h3>" + " 나이 = " + age_value + "</h3>");
	out.println("<h3>" + " 감정 = " + emotion_value + "</h3>");
	out.println("<h3>" + " 얼굴방향 = " + pose_value + "</h3>");
	
	//스트링 널로 처리 "null"이라고 생각함. 그래서 널 비교할때. !=null이아니라, 값 자체가 null인것으로 비교.
	//널이아니면 찾아라
	if(!oneperson.get("landmark").equals(null)){
		JSONObject landmark = (JSONObject)oneperson.get("landmark");
		JSONObject nose = (JSONObject)landmark.get("nose"); //x,y좌표가짐
		out.println("<h3>" + " 코위치 x = "+(Integer)nose.get("x")+ " , y = "+(Integer)nose.get("y")+ "</h3>");
		
		
	}
	else{
		out.println("<h3> 눈코입을 파악할 수 없습니다. </h3>");
	}
		
	
}

%>

<h3>총 <%=faceCount %> 명의 얼굴을 찾았습니다. </h3>

</body>
</html>

<!-- 제이슨 결과 , 서비스가 스트링이라는걸로 주니까. 자바는 스트링, 실제 내부형식은 제이슨형태. -->

<!-- api값 가져올 때 jull값 처리시 주의  -->