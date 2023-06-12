package stt_csr;

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
public class STTController {
	@Autowired //서비스 자동주입해줘. 빈 있으면 가져와.
	@Qualifier("sttservice") //어떤건지 알려줘.
	NaverService service;//stt서비스해주는 bean불러옴. // service.메소드(NaverService오버라이딩메소드호출)
	
	//1.바탕화면 ai_images 파일리스트->보여주는 뷰->사용자에게 보여준다.(파일선택해서, 선택된 파일 )
	@RequestMapping("/sttinput")
	public ModelAndView sttinput() {
		
		File f =new File(MyNaverInform.path); //파일과 디렉토리 정보 제공 //java.io 파일->파일시스템 작업 //디렉토리정보.
		String[] filelist = f.list(); //#전체 내용 다 들어가있음. 
		
		
		String file_ext[] = {"mp3", "m4a", "wav"};
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
		mv.setViewName("sttinput");//.jsp
		return mv;
	}
	
	@RequestMapping("/sttresult")
	public ModelAndView sttresult(String mp3file, String lang) throws IOException{//sttresult?mp3file=a.mp3 //image=a.mp3&lang=kor변수이름은 이걸로가는거.
		String sttresult = null;
		if(lang == null) {
			//이미지파일이름전달.-리턴결과 스트링
			sttresult = service.test(mp3file);//기본언어 한국어.
		}else { 
			//언어+파일전달. 
			sttresult = ((STTServiceImpl)service).test(mp3file, lang); //타입변환.
		}
		
		ModelAndView mv= new ModelAndView();
		mv.addObject("sttresult", sttresult);//네이버 응답결과. 텍스트 뷰 출력"{text:텍스트변환결과}" 텍스트변환결과만가져오려고 아래에제이슨오브젝트.
		mv.setViewName("sttresult");
		
		
		//추가 (영구보관)MyNaverInform.path 경로 내부에 text형태로 저장.     2023.06.09112022.txt파일로 저장. 오늘날짜,시간
		//경로 : MyNaverInform.path 
		//파일이름 : image(mp3전송받은파일이름)_현재날짜시분초.txt
		//FileWriter  저장. 저장경로 
		

		//오늘 날짜+시간 추가하기위해.
		//DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		//Date currentDate = new Date();
		//String datetime = dateFormat.format(currentDate);
		
		//저장파일 시점 시각까지가져오도록.
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String now_string = sdf.format(now);

		//[파일이름형식지정]파일 이름 만들기 
		//String filename = mp3file + "_" + datetime + ".txt";
		//mp3file확장자포함, 뺴고싶으면 substring// mp3file.substring(0, mp3file.lastIndexOf("."))
		String filename = mp3file.substring(0, mp3file.lastIndexOf(".")) + "_" + now_string + ".txt";

		//[선택]파일을 만들건데 - 경로에, 이름(내가원하는형식으로지정한)것으로 파일을 만든다.  
		//여기에서 (파일을 저장하려는 원하는 경로) 및 문자열을 File사용하여 개체가 생성됩니다 . 
		//경로와 파일 이름을 결합하여 전체 파일 경로를나타냅니다.
		//MyNaverInform.pathfilename
		//File file = new File(MyNaverInform.path + filename);//경로파일명. false(새내용업뎃), true(기존내용유지, 추가)

		
		//[실제저장]파일을 쓸(저장할)건데, 위에 file에 경로와, 이름으로 된 걸로 내가 선택한 파일을 써서 저장한다. 
		/*이 줄에서 FileWriter호출된 개체가 fw초기화됩니다. 
		 * 객체 file(파일 경로와 이름을 나타냄)와 true. 
		 * 이 true매개변수는 파일을 추가 모드로 열어야 함을 나타냅니다. 
		 * 즉, 내용을 덮어쓰지 않고 기존 파일의 끝에 새 데이터가 추가됩니다.
 		(최종)원하는 형식으로 파일 이름을 만들고 개체를 사용하여 파일 경로와 이름을 지정 
 		File하고 FileWriter개체를 초기화하여 데이터를 파일에 기록함으로써 새 데이터가 기존 파일에 추가되도록 합니다.
		 * */
		//FileWriter fw = new FileWriter(file, true);//안덮어쓰기위해 : 기존파일끝에 데이터추가시 두번째매개값에true
		FileWriter fw = new FileWriter(MyNaverInform.path + filename, true);
		
		
		JSONObject jsontext = new JSONObject(sttresult);
		String text = (String)jsontext.get("text");//변수명 text읽어오면 오른쪽 텍스트변환결과값 STring가져온다.
		
		fw.write(text); //값만 저장한다.
		fw.close();//writing끝나다고 알려줘야한다.

		return mv; //이제 뷰만들러가면됨.sttresult
		
		
		//[추가구현]
		//기존 파일에 mp3file.substring(0, mp3file.lastIndexOf(".")) 이거 있냐(원본파일이름) 없으면 저장으로 하면 더좋다. 
		
	}

}

//파일라이터 throws IOException-부트때 걍 이걸로 다 넘겨버려 걍 다 떠넘기기 -코드집중