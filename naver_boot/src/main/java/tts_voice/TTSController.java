package tts_voice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.ai.MyNaverInform;
import com.example.ai.NaverService;

@Controller
public class TTSController {
	@Autowired //서비스 자동주입해줘. 빈 있으면 가져와.
	@Qualifier("ttsservice") //어떤건지 알려줘.
	NaverService service;//stt서비스해주는 bean불러옴. // service.메소드(NaverService오버라이딩메소드호출)
	
	//1.바탕화면 ai_images 파일리스트->보여주는 뷰->사용자에게 보여준다.(파일선택해서, 선택된 파일 )
	@RequestMapping("/ttsinput")
	public ModelAndView ttsinput() {
		
		File f =new File(MyNaverInform.path); //파일과 디렉토리 정보 제공 //java.io 파일->파일시스템 작업 //디렉토리정보.
		String[] filelist = f.list(); //#전체 내용 다 들어가있음. 
		
		
		String file_ext[] = {"txt"};
		//file_ext 배열 존재하는 확장자만 모델 포함.
		
		ArrayList<String> newfilelist = new ArrayList(); //#스트링만저장해도됨.파일이름이니까.
		for(String onefile : filelist) { //#파일하나가져와.
			String myext = onefile.substring(onefile.lastIndexOf(".")+1);//.찾아서 그 뒤에부터.
			
			for(String imgext : file_ext) { //내파일확장자, imgext랑 같은거있나.같냐물어봐.
				if(myext.equals(imgext)) {
					newfilelist.add(onefile); //onefile-전체이름, myext는 확장자만 뽑아냈음.
					break;//다찾으면 빠져나와.
				}//if end
			}//안for end
		}//겉 for//end
			
		
		ModelAndView mv= new ModelAndView();
		mv.addObject("filelist", newfilelist);//파일리스트 전달. 
		mv.setViewName("ttsinput");//.jsp
		return mv;
	}
	
	@RequestMapping("/ttsresult")
	public ModelAndView sttresult(String text, String speaker) throws IOException{//sttresult?mp3file=a.mp3 //image=a.mp3&lang=kor변수이름은 이걸로가는거.
		String ttsresult = null;
		if(speaker == null) {
			//이미지파일이름전달.-리턴결과 스트링
			ttsresult = service.test(text);//기본음색 jinho
		}else { 
			//언어+파일전달. 
			ttsresult = ((TTSServiceImpl)service).test(text, speaker); //타입변환.
		}
		
		ModelAndView mv= new ModelAndView();
		mv.addObject("ttsresult", ttsresult);//ttsresult →mp3파일 이름 리턴.
		mv.setViewName("ttsresult");
		
		
		return mv; //이제 뷰만들러가면됨.sttresult
		
	
	}

}
