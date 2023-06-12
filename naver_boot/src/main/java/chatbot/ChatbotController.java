package chatbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.ai.NaverService;

@Controller
public class ChatbotController {

	@Autowired
	@Qualifier("chatbotservice")
	ChatbotServiceImpl service;
	//NaverService service;
	//첨부터 챗봇서비스유형이어도됨.
	
	
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
	
	
	//ajax 대화내용리셋, 그래서 파일이든뭐든 저장해놓고다시 불러와야 옛날기록 됨.
	
}
