package com.example.ai;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StartController {

	//htto://localhost:8064/
	//서버루트 서버시작지점.
	@GetMapping("/") //겟맵핑이 시작화면이라 더 좋다. //url /a는
	//@ResponseBody //뷰이름이아님. 뷰안에전달하는 json데이터란 뜻. 
	public String start(){
		//뷰이름.
		//메뉴 모델 전달 
		//뷰가결정하게해도되고.
		//메뉴 전달 = 뷰 모델전달방법  ? 모델앤뷰
		return "start"; //.jsp
	}
	
	
}
