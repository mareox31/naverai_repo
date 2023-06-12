package chatbot;
	import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.ai.MyNaverInform;
import com.example.ai.NaverService;



@Service("chatbotservice")
public class ChatbotServiceImpl implements NaverService {
		public String test(String request) { //네이버서비스가 가진형태.
			return test(request,"send"); //질문있는경우 
		}
		
		
	//(질문, 답변)send ("", 웰컴메시지)open -구분 // 이벤트동작지정,
	//질문을 챗봇에게 전달 - json답변
	public String test(String request, String event) {//질문넣자.//이벤트동작지정, open/send
		//String result = main(request, MyNaverInform.chatbot_apiurl, MyNaverInform.chatbot_Secret);
		//System.out.println("=== 챗봇결과 ===");
		//System.out.println(result);

		//메서드 하나로 합친다. ---------------------------
		//이름만main apichat봇이가진 걍 메서드임.
		//질문을 챗봇에게 전달 - 답변받아오는 역힐(필요한거3개, 질문/커스텀연동에apiurl, 시크릿키 연동정보.

	        String chatbotMessage = "";

	        try {
	        	String apiURL = MyNaverInform.chatbot_apiurl;//전송해야할url
	        	String secretKey = MyNaverInform.chatbot_Secret;
	        	
	            URL url = new URL(apiURL);//서버연결에씀.
	            String message = getReqMessage(request,event);//json형태로 변경 , 질문(send,open)
	            //send-open나누는 evnet전달//json형태로 질문 변경.//질문은 request니까.-매개변수
	            System.out.println("##" + message);

	            String encodeBase64String = makeSignature(message, secretKey);//암호화

	            HttpURLConnection con = (HttpURLConnection)url.openConnection();
	            con.setRequestMethod("POST");
	            con.setRequestProperty("Content-Type", "application/json;UTF-8");
	            con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

	            // post request
	            con.setDoOutput(true);
	            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	            wr.write(message.getBytes("UTF-8"));
	            wr.flush();
	            wr.close();
	            int responseCode = con.getResponseCode();

	            BufferedReader br;

	            if(responseCode==200) { // Normal call
	                System.out.println(con.getResponseMessage());

	                BufferedReader in = new BufferedReader(
	                        new InputStreamReader(
	                                con.getInputStream()));
	                String decodedString;
	                while ((decodedString = in.readLine()) != null) {
	                    chatbotMessage = decodedString;
	                }
	                //chatbotMessage = decodedString;
	                in.close();

	            } else {  // Error occurred
	                chatbotMessage = con.getResponseMessage();
	            }
	        } catch (Exception e) {
	            System.out.println(e);
	        }

	        return chatbotMessage;//답변-json형태.
	    }

	  
	///질문던지면 시크릿키-암호화
	    public static String makeSignature(String message, String secretKey) {

	        String encodeBase64String = "";

	        try {
	            byte[] secrete_key_bytes = secretKey.getBytes("UTF-8");

	            SecretKeySpec signingKey = new SecretKeySpec(secrete_key_bytes, "HmacSHA256");
	            Mac mac = Mac.getInstance("HmacSHA256");
	            mac.init(signingKey);

	            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
	            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);//안드로이드 -> java.util 자바 api암호화.
	            //안드로이드 암호화 로직 객체 우린 안드로이드 api없어.

	            return encodeBase64String;

	        } catch (Exception e){
	            System.out.println(e);
	        }

	        return encodeBase64String;

	    }

	    
	 
	 //json형태로 요청값 전달. (json형태로 바꿔서 요청형태를 전달)
	    public static String getReqMessage(String voiceMessage, String event) {//우리가 send/open인지 전달할 수 있또록 변경.

	        String requestBody = "";

	        try {

	            JSONObject obj = new JSONObject();

	            long timestamp = new Date().getTime();

	            System.out.println("##"+timestamp);

	            obj.put("version", "v2");
	            obj.put("userId", "U47b00b58c90f8e47428af8b7bddc1231heo2");
	//=> userId is a unique code for each chat user, not a fixed value, recommend use UUID. use different id for each user could help you to split chat history for users.

	            obj.put("timestamp", timestamp);

	            JSONObject bubbles_obj = new JSONObject();

	            bubbles_obj.put("type", "text");

	            JSONObject data_obj = new JSONObject();
	            data_obj.put("description", voiceMessage);

	            bubbles_obj.put("type", "text");
	            bubbles_obj.put("data", data_obj);

	            JSONArray bubbles_array = new JSONArray();
	            bubbles_array.put(bubbles_obj);

	            obj.put("bubbles", bubbles_array);
	            obj.put("event", event); //여기다 send open나눔.//

	            requestBody = obj.toString();

	        } catch (Exception e){
	            System.out.println("## Exception : " + e);
	        }

	        return requestBody;

	    }
	}	