package cfr;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.example.ai.MyNaverInform;

// 네이버 얼굴인식 API 예제
public class APIExamFace2 {

    public static void main(String[] args) {
    	
/*{"info":{
 * 			"size":{"width":264,"height":200}, --얼굴전체 면적.
 * 			"faceCount":1}, --발견된얼굴갯수
 * 
 * 
 * --현재는 1사람. 여러명이면 늘어남.(faces여러개)
 * "faces":[ 
 * 		{
 * 			"roi":{"x":80,"y":45,"width":61,"height":61},   -- roi 핵심 눈코입 
 * 			"landmark":null,      --눈코입 정보 (네이버가 만들어만놓고안주는듯)-눈코입각각정보안줌.
 * 			"gender":{"value":"female","confidence":0.999912},       --성별, 99퍼 여자라고
 * 			"age":{"value":"24~28","confidence":0.856547},    --추정나이 , 85퍼정도 정확도
 * 			"emotion":{"value":"neutral","confidence":0.999967},       --표정 : 무표정, 확률
 * 			"pose":{"value":"right_face","confidence":0.61542}}]}        --포즈 --오른쪽얼굴보여준다.
 * 
 * 
 * 
 * */    	
    	
    	

        StringBuffer reqStr = new StringBuffer();
        String clientId = MyNaverInform.clientID;//넣기
        String clientSecret = MyNaverInform.secret;//넣기

        //400번 에러 파일크기 안맞음.
        try {
            String paramName = "image"; // 파라미터명은 image로 지정 
            String imgFile = MyNaverInform.path +"song.jpg";  //이름지정 //2mb이하크기(ncp사이트제한)400번 에러 파일크기 안맞음.
            File uploadFile = new File(imgFile);
            String apiURL = "https://naveropenapi.apigw.ntruss.com/vision/v1/face"; // 이사이트이용, 안면 관련 정보 제공,//얼굴 감지 
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            // multipart request
            String boundary = "---" + System.currentTimeMillis() + "---";
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            OutputStream outputStream = con.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);
            String LINE_FEED = "\r\n";
            // file 추가
            String fileName = uploadFile.getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
            writer.append("Content-Type: "  + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();
            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();
            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();
            BufferedReader br = null;
            int responseCode = con.getResponseCode();
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 오류 발생
                System.out.println("error!!!!!!! responseCode= " + responseCode);
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }
            String inputLine;
            if(br != null) {
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
            } else {
                System.out.println("error !!!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

//샘플코드 ->mvc로 변경하는법
//메서드이름/매개변수/리턴타입 적당히 바꿔서우리가 서비스로 가져오면된다.
//sql실행하는거 없어, 서비스만 시키면되는경우임.
//인터페이스 네이버서비스 상속받았기때문에 , 필수오버로딩.
