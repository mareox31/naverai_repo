package ocr;

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.ai.MyNaverInform;
import com.example.ai.NaverService;

@Controller
public class OCRController {
	@Autowired
	@Qualifier("ocrservice")
	NaverService service; //포즈서비스
	
	@RequestMapping("/ocrinput")//파일리스트 이미지만가져옴.
	public ModelAndView ocrinput() {
		File f =new File(MyNaverInform.path); //파일과 디렉토리 정보 제공 //java.io 파일->파일시스템 작업 //디렉토리정보.
		String[] filelist = f.list(); //#전체 내용 다 들어가있음. 
		
		String file_ext[] = {"jpg", "gif", "png", "jfif"};//확장자제한. -확장자추가되면 더 써주면됨.
		//file_ext 확장자만, 파일리스트에 집어넣는다. 모델에포함.
	
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
		mv.setViewName("ocrinput");//.jsp
		return mv;
	}
	
	
		
	
	@RequestMapping("/ocrresult")//json결과 jsp보내주기위해 모델앤뷰
	public ModelAndView objectresult(String image) { //파라메터이름다르면 @RequestParam("image")String i이렇게 하면됨.
		String ocrresult = service.test(image);//테스트메서드, 감지대상넣으면됨.//리턴결과 String
		ModelAndView mv = new ModelAndView();
		mv.addObject("ocrresult", ocrresult);
		mv.setViewName("ocrresult");
		
		return mv;
	}
	
}
