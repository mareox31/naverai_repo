package chatbot;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.ai.MyNaverInform;


@Controller
public class ChatbotController {

	@Autowired
	@Qualifier("chatbotservice")
	ChatbotServiceImpl service;
	//NaverService service;
	//첨부터 챗봇서비스유형이어도됨.
	
	@Autowired
	@Qualifier("chatbotttsservice")
	ChatbotTTSServiceImpl ttsservice;
	
	@Autowired
	@Qualifier("chatbotsttservice")
	ChatbotSTTServiceImpl sttservice;
	
	@Autowired
	@Qualifier("pizzaservice")
	PizzaServiceImpl pizzaservice;
	
	
	
	
	@RequestMapping("/chatbotrequst")
	//뷰만 보여주면됨. 질문받아오기
	public String chatbotrequest() {
		return "chatbotrequest";
	}
	
	@RequestMapping("/chatbotresponse")
	public ModelAndView chatbotresponse(String request, String event) {
		String response = "";//두개의 테스트메서드결과.
		
		if(event.equals("웰컴메세지")) {
			response = service.test(request, "open");//원 타입 챗봇서비스임플로형변환.-2번 test메서드가주로임.
		//((ChatbotServiceImpl)service)네이버였으면 이렇게했어야함.
		//이벤트 따라 분리-서비스호출.
		}else {
			response = service.test(request, "send");
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("response",response );//json
		mv.setViewName("chatbotresponse");
		return mv;
	}
	
	
	//기본 답변만 분석한 뷰------ ajax이용(챗봇이랑 주고받기)-기본답변만 가능한 예.
	@RequestMapping("/chatbotajaxstart")
	public String chatbotajaxstart() {
		return "chatbotajaxstart";
	}
	
	//기본+이미지+멀티링크답변 ========모두 분석가능한 뷰
	@RequestMapping("/chatbotajax")
	public String chatbotajax() {
		return "chatbotajax";
	}
	
	
	//ajax이용
	@RequestMapping("/chatbotajaxprocess")
	//@ResponseBody
	//리턴타입지정하는 ↓여기나 위에 이자리 도 어디도 붙이면됨.
	public @ResponseBody String chatbotajaxprocess(String request, String event) {
		String response = "";//두개의 테스트메서드결과.
		
		if(event.equals("웰컴메세지")) {
			response = service.test(request, "open");//원 타입 챗봇서비스임플로형변환.-2번 test메서드가주로임.
		}else {
			response = service.test(request, "send");
		}

		return response;//json형태받아온그대로 리턴.
	}
	
	//-------------------ajax요청시, 답변내용 mp3로 변환 ----------------------------------
	//ajax 대화내용리셋, 그래서 파일이든뭐든 저장해놓고다시 불러와야 옛날기록 됨.
	@RequestMapping("/chatbottts")
	@ResponseBody //mp3파일은 리턴해주는 데이터의 타입 스트링으로주면, 현재바디에 ajax있는데 보내줄건데 mp3야. 
	public String chatbottts(String text) {//챗봇에 전달받은 답변
		String mp3 = ttsservice.test(text);//답변 텍스트를 -- 해당경로 -- mp3파일 이름 리턴.
		//return mp3;//리턴이 스트링이면 뷰여야하는데, 실제 mp3파일 이름이 되어야한다. //ajax내부 음성으로 바뀐걸 주려고. 
		return "{\"mp3\":\"" +  mp3 + "\"}";//이게 json형태, 제이슨이해하는 자바스크립이아니라 자바니까. 스트링형태, 이렇게써야 json데이터.
				//20230613시분초.mp3
	}
	
	//음성질문 서버 업로드
	@PostMapping("/mp3upload")//http://localhost:8046/ai/mp3upload        /mp3upload랑 챗봇에이젝서jsp에 /ai/mp3upload ajaxulr같음.
	//컨텍스트이름까지 포함.
	@ResponseBody
	public String mp3upload(MultipartFile file1) throws IOException{ //폼데이터 이름 file1해놔서.
		String uploadFile = file1.getOriginalFilename();//a.mp3//사용자가업로드한이름뽑아와 //사용자가전송한이름그대로. uuid오늘안씀.
		//c:/kdt/upload/a.mp3 //uuid썼던거 a54566776.mp3
		//c:file1.ai_images/dt/upload..
		String uploadPath = MyNaverInform.path; //업로드경로.
		File saveFile = new File(uploadPath+uploadFile);//빈파일생성? 지정이름으로 //복사할 파일.
		file1.transferTo(saveFile);//서버내부로저장됨. //서버내부 특정파일로 복사해
		return "{\"result\" : \"잘 받았습니다\"}"; //뷰안에 데이터 json주는것임.
	}
	
	//업로드한 음성 질문 mp3파일을 텍스트로 변환---------
	@RequestMapping("/chatbotstt")
	@ResponseBody
	public String chatbotstt(String mp3file) { //string을 json형태로 바꿔줄것임.//ajax부분에 똑같은 변수주면됨. 컨트롤러서정하고가자.
		//sttservice.test(mp3file,"kor");//기본 선택 kor //mp3파일만넘겨주면, 1번테스트메서드호출되서 kor로됨.
		String text = sttservice.test(mp3file);//결과 mp3파일을 텍스트로바꾼것.
		//return  "{\"text\" : \"" + text + "\"}";      //json형태로 리턴.
		return text;//얘가 json형태임
	}
	
	//피자db에 저장.
	@RequestMapping("/pizzaorder")
	@ResponseBody
	public String pizzaorder(PizzaDTO dto) {
		int insertrow = pizzaservice.insertPizza(dto);
		//{"insertrow" : 1} //전달시 json형태 -전체적 스트링형태.
		return "{\"insertrow\" :" + insertrow + "}";//뷰로 전달하는 jsondata임.
	}
	
	
}
