package mymapping;

import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.ai.MyNaverInform;
import com.example.ai.NaverService;

@Controller
public class MapController {
	//MapServiceImpl service; 
	//이거도되는데 공통적으로 네이버서비스임-네이버서비스 상속받은애들 다 쓸 수 있음.=서비스종류 어떤객체든올 수 있음.(호환,변경,서비스범위넓혀줌)
	//NaverService service = new FaceServiceImpl();
	
	@Autowired//질문답변받는text
	@Qualifier("mapservice")
	NaverService service;
	
	@Autowired//음성변환
	@Qualifier("ttsservice")
	NaverService service2;
	
	
	@GetMapping("/myinput")
	public String input() {
		return "mymapping/input"; 
	}
	
	
/*	//ajax사용안하고, 요청받아 처리부분. -뷰 2개 아웃풋, 인풋.
 * 	//질문내용 request, 답변 mp3 - 클라이언트 질문 전달받음.
	@RequestMapping("/myoutput")
	public ModelAndView output(String request) throws IOException {
		String response = service.test(request);//질문 답 찾아옴.
		//테스트메서드에 request넘김.=>결과하나 되돌아옴. (질문에대한 답)String
		//ttsserviceImpl에게 스트링줘야해. 음색지정, 
		//구현할떄 텍스트 파일을 넘겨주기로했었음. 그 파일내용을 찾아내서 보내는거까지 서비스가해주고있음. 
		//tts에게 리스폰스 그냥 보내면 안됨. 
		//답변받은text 가지고, tts서비스임플에게 보낸 뒤->mp3로 바꿔라 하는게 맞는데,
		//ttsserviceimpl이 텍스트 파일 이름으로 받고 있다. 
		//우리가 또 수정안하려면 파일 형태로 줘야한다. 
		//아님ㄴ 텍스트로 받는걸 메서드를 또 만들던가, 기존형태대로 우선간다. 
		
		//답변텍스트를 txt파일로 생성한다.
		//문자 들어가고 파일저장 = 파일라이터 //문자아닌건 아웃풋스트링(이진데이터)
		FileWriter fw=  new FileWriter(MyNaverInform.path + "response.txt");//답변파일, 위치까지.
		fw.write(response);//전달받은거 써. 
		fw.close();  //답변 받은 내용을 txt로 만들어주는 3문장 =========================
		
		
		String mp3 = service2.test("response.txt");//찾은답변 mp3변환
		//파일을 그대로 보낸다.  , 음색도 보낼거면 ""넣고, 아니면 진호음색.
		//mp3로 바뀐거야 .(서비스2가 ttsserviceimpl. responsetxt파일이름 받아서 - mp3하나 리턴함)
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("response", response);//모델 2개넘김, response변수 (답변인데 텍스트형태
		mv.addObject("mp3", mp3);//답변인데 음성으로 바뀐거.
		mv.setViewName("mymapping/output");
		
		return mv;
	}
*/	
	
	
	//[변경]ajax = >json데이터 주도록 바꿔. 
	@RequestMapping("/myoutput")
	@ResponseBody //ajax쓰려면 (sts3-pom.xml라이브러리 추가 필요/ sts4부트-라이브러리추가필요x 자동추가)
	public String output(String request) throws IOException {
		String response = service.test(request);//질문 답 찾아옴.
		FileWriter fw=  new FileWriter(MyNaverInform.path + "response.txt");//답변파일, 위치까지.
		fw.write(response);//전달받은거 써. 
		fw.close();  //답변 받은 내용을 txt로 만들어주는 3문장 =========================
		String mp3 = service2.test("response.txt");//찾은답변 mp3변환
		//---------동일 구현
		
		return  "{\"response\" : \""  +  response + "\" , \"mp3\":\"" + mp3 + "\" }";
		//json형태 주는게 ajax의 가장 큰 특징.
				//변수이름 , 스트링값도 이중따옴표!!!!!!!!!!!!!!!!!!=========================&&&&&&&&&&&&
		//{"response" : 텍스트답변, "mp3" : 변환된mp3파일이름 };
		//전체가 스트링이어야한다. 
	}
	
}

//ajax로 주고받음- 컨트롤러에 responsebody
//같은화면 - ajax로 구현한다. 
//output안쓰고, 저 답변과 그걸 input서 다 할것이다.  ajax요청할 수 있도록 한다. - 파일 구조 뜯어고치기. 1개 뷰 추가 변경내용만 업데이트 반영.
//구조 자체를 고친다. 
//1.ouput.jsp답변텍스트-답변내용. 부분, 화면 부분을 input으로 변경.
