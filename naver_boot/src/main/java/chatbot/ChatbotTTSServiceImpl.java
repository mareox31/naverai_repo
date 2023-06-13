package chatbot;

//네이버 음성합성 Open API 예제
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.example.ai.MyNaverInform;
import com.example.ai.NaverService;

@Service("chatbotttsservice")
public class ChatbotTTSServiceImpl implements NaverService{

 @Override
	public String test(String text) {
	 	return test(text, "jinho"); //매개변수2개있는데로 보냄, 화자 기본값 설정.
	}

 	//텍스트파일 입력받도록 바꿔야함, 
public String test(String text, String speaker)  {//챗봇이 답변받아올떄 그냥 텍스트파일로 받아옴.
	 String tempname = null;
     String clientId = MyNaverInform.voice_clientID;//네이버tts용      1.아이디/시크릿 적음.
     String clientSecret = MyNaverInform.voice_secret;//네이버tts용
     
     try {//답변내용 텍스트라서 ------utf-8로인코딩만하면됨.
         //String text = URLEncoder.encode("지금은 네이버 플랫폼을 활용한 ai 서비스 진행중입니다.", "UTF-8"); // 13자  //2000자 까지 가능. 한글이라 utf-8변환 2.음성으로바꿀것.(2000자제한)
    	 //텍스트파일 읽어오는거 파일리더
    	 //String text="";
    	// FileReader fr = new FileReader(MyNaverInform.path + textfile); //저경로의 해당파일 읽어라. 
    	 //fr.read();//한글자씩 읽음
    	 //여러개위해선 스캐너
    	 //Scanner sc= new Scanner(System.in); //입력한거 한줄씩읽어옴.
    	 //파일리더는 한줄씩 읽는거없어서 scanner씀
    	 //전달파일내용 읽는거로바뀜. - 파일내용읽어서 대체하면되는거. 
    	// Scanner sc= new Scanner(fr);
    	// while(sc.hasNextLine()){//읽을 다음줄있냐?
    	//	 text += sc.nextLine();//읽고 다음줄 가.//마지막줄 더 읽을거 없을떄 while빠져나올것.
    		 //읽을 줄 있는 만큼, text변수에 저장해놓는다. 
    	// }
    	 
    	 text = URLEncoder.encode(text, "UTF-8");//읽어온거 이제 utf-8인코딩결과로 바꿔줘.
    	 
    	 //우리가 파일 읽어서 그걸 전달해서 utf-8로 인코딩.
    	 
    	 
    	 String apiURL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts";//음성변환 
         URL url = new URL(apiURL);
         HttpURLConnection con = (HttpURLConnection)url.openConnection();
         con.setRequestMethod("POST");
         con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
         con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
         // post request          //스피커, 텍스트 필수. 나머지는 기본값.
         String postParams = "speaker="+speaker+"&volume=0&speed=0&pitch=0&format=mp3&text=" + text; //[변경].화자.
         con.setDoOutput(true);
         DataOutputStream wr = new DataOutputStream(con.getOutputStream());
         wr.writeBytes(postParams);
         wr.flush();
         wr.close();
         int responseCode = con.getResponseCode();
         BufferedReader br;
         if(responseCode==200) { // 정상 호출
             InputStream is = con.getInputStream();
             int read = 0;
             byte[] bytes = new byte[1024];
             // 랜덤한 이름으로 mp3 파일 생성 - 결과는 mp3파일로 만들어진다. //스트링리턴안함, 
             //파일이름생성------
             tempname = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ; //현재날짜에포맷팅주고 연도~초까지 시간.
             
             
             //mp3로 바뀐거 저장-------
 //          String tempname = Long.valueOf(new Date().getTime()).toString(); //오늘날자로 파일이름만들어. (시간1/1000초단위임)
             File f = new File(MyNaverInform.path + tempname + ".mp3"); 
             //확장자mp3 =>이 이름 파일이 실행한 결과다. 20230609112223.mp3 //4. 경로만들어줘야함.
             f.createNewFile(); //파일에 기록하게해준다.
             OutputStream outputStream = new FileOutputStream(f); //아까 텍스트는 writer고, 이번엔 텍스트들어가지않아, 음성만들어가. (음성-내부적2진데이터.)
             while ((read =is.read(bytes)) != -1) { //음성파일을 파일에 기록해주는거. 
                 outputStream.write(bytes, 0, read);
             }
             is.close();//기록다되면 클로즈.
             System.out.println(tempname + " 파일은 해당 경로에서 확인하세요." +MyNaverInform.path);
         } else {  // 오류 발생
             br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
             String inputLine;
             StringBuffer response = new StringBuffer();
             while ((inputLine = br.readLine()) != null) {
                 response.append(inputLine);
             }
             br.close();
             System.out.println(response.toString());
         }
     } catch (Exception e) {
         System.out.println(e);
     }
     
     return tempname + ".mp3"; //파일이름 줘. //mp3파일하나 리턴함. 
 }
}