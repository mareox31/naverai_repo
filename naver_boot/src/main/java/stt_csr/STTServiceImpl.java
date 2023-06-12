package stt_csr;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.example.ai.MyNaverInform;
import com.example.ai.NaverService;

@Service("sttservice")
public class STTServiceImpl implements NaverService {

	//기본 mp3전해주면 kor만.
	public String test(String mp3file) { //일단 추가. 필수오버라이딩. //매개변수다른 메소드오버로딩.
		return test(mp3file, "Kor");  //자기자신메서드 2개스트링 메서드 아래쪽 호출함. //파라메터전달받은거있다면 없으면 기본값 kor
	}
	
	
	//언어도 같이 전달하면 언어선택가능.
    public String test(String mp3file, String lang){//이미지로줘도되는데 의미적으로이거로씀 //매개변수형태랑 일치해야함, 오버라이딩.
    	StringBuffer response = null;
        String clientId = MyNaverInform.clientID;//넣기
        String clientSecret = MyNaverInform.secret;//넣기

        try {
            String imgFile =  MyNaverInform.path +mp3file;//넣기
            File voiceFile = new File(imgFile);//?

            String language = lang;        // 언어 코드 ( Kor, Jpn, Eng, Chn ) //-필수지정 //사용자에게유도.
            String apiURL = "https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=" + language;
            URL url = new URL(apiURL);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(voiceFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();
            BufferedReader br = null;
            int responseCode = conn.getResponseCode();
            if(responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {  // 오류 발생
                System.out.println("error!!!!!!! responseCode= " + responseCode);
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String inputLine;

            if(br != null) {
                 response = new StringBuffer();
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
       return response.toString();
    }
}
