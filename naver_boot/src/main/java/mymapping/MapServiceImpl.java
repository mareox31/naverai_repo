package mymapping;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.example.ai.NaverService;

//생성자 내부에 질문-답 생성해놔야함. -생성자내용까지 맵서비스에 들어간다.
//MapServiceImpl mapservice = new MapServiceImpl();



@Service("mapservice")
public class MapServiceImpl implements NaverService{

	HashMap<String , String> map = new HashMap();
	//생성자- 초기 값 저장.
	public MapServiceImpl() {
		//최초 키-값 초기데이터 저장. =>테스트메서드가 가져다 쓴다.
		//질문-답 저장.
		//답변 2천자까지가능. 음성은 1분까지.
		map.put("이름이 뭐니?", "클로버야");
		map.put("무슨 일을 하니?", "ai서비스관련일을 해");
		map.put("멋진 일을 하는구나","고마워");
		map.put("난 훌륭한 개발자가 될거야","넌 할 수 있어");
		map.put("잘 자","내꿈꿔");
		
	}
	
	
	
	@Override //필수 -네이버서비스 오버라이딩
	public String test(String request) { //질문전달받음.
		//조회
		String response = map.get(request); //키에 대한 값, 조회.
		if(response ==null) {
			response ="아직은 저도 몰라요";
		}
		return response;
	}
	
	
	
}
