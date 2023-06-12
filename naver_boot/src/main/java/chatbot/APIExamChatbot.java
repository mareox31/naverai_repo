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

import com.example.ai.MyNaverInform;

	public class APIExamChatbot {

		//우린 main호출->나머지는 연달아서 자기들이 불러옴.
	public static void main(String args[]) {
		//답변 = main("질문", apiurl, secret); //아래 메인 호출. //암호화,제이슨형태로 아래꺼불러와서 알아서 함.
		//String result = main("", MyNaverInform.chatbot_apiurl, MyNaverInform.chatbot_Secret);//웰컴 메세지 확인
		String result = main("날씨 알려주세요", MyNaverInform.chatbot_apiurl, MyNaverInform.chatbot_Secret);
		System.out.println("=== 챗봇결과 ===");
		System.out.println(result);
		
		//웰컴메세지 받기.-질문무시, 웰컴대사뱉음.
		
		
	}
		
		
		//이름만main apichat봇이가진 걍 메서드임.
		//질문을 챗봇에게 전달 - 답변받아오는 역힐(필요한거3개, 질문/커스텀연동에apiurl, 시크릿키 연동정보.
	  public static String main(String voiceMessage, String apiURL, String secretKey) {

	        String chatbotMessage = "";

	        try {
	            //String apiURL = "https://ex9av8bv0e.apigw.ntruss.com/custom_chatbot/prod/";

	            URL url = new URL(apiURL);

	            String message = getReqMessage(voiceMessage);
	            System.out.println("##" + message);

	            String encodeBase64String = makeSignature(message, secretKey);

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

	        return chatbotMessage;
	    }

	  
	  //json형태로 요청값 전달. (json형태로 바꿔서 요청형태를 전달)
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

	    
	 //질문던지면 시크릿키-암호화
	    public static String getReqMessage(String voiceMessage) {

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
	            obj.put("event", "send"); //send, open중 하나 선택(send:질문에대한 답, open:웰컴메세지)

	            requestBody = obj.toString();

	        } catch (Exception e){
	            System.out.println("## Exception : " + e);
	        } 

	        return requestBody;

	    }
	}	