package com.example.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import objectdetection.ObjectDetectionController;

@SpringBootApplication
@ComponentScan //현재패키지대상이란소리.
//@ComponentScan(basePackages = "cfr")//직접패키지이름쓰기 
@ComponentScan(basePackages = {"cfr","pose","stt_csr","tts_voice","mymapping", "ocr","chatbot"})//배열로 추가하면되고,{"a","b"} 배열로만들어.
@ComponentScan(basePackageClasses = ObjectDetectionController.class) //이곳을 컴포넌트 스캔의 대상으로쓸것.-저 클래스있는 패키지를 스캔대상으로함.
public class NaverBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaverBootApplication.class, args);
	}

}

