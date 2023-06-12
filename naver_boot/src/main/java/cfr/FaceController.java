package cfr;

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
public class FaceController {
	@Autowired //서비스 자동주입해줘. 빈 있으면 가져와.
	@Qualifier("faceservice1") //어떤건지 알려줘.
	NaverService service;//닮은 연예인 서비스.
	
	@Autowired //만들어진 객체 자동주입
	@Qualifier("faceservice2")//서비스 구분.
	NaverService service2; //안면,나이,성별,감정, 눈코입 위치 서비스.

	//동작 2개 
	
	//1.바탕화면 ai_images 파일리스트->보여주는 뷰->사용자에게 보여준다.(파일선택해서, 선택된 파일 )
	@RequestMapping("/faceinput")
	public ModelAndView faceinput() {
		//파일 중 하나 선택해서 이미지 분석해줘. 
		File f =new File(MyNaverInform.path); //파일과 디렉토리 정보 제공 //java.io 파일->파일시스템 작업 //디렉토리정보.
		String[] filelist = f.list(); //#전체 내용 다 들어가있음. 
		//MyNaverInform안의 파일리스트/ 특정 디렉토리 리스트 보여줌. =>리턴 스트링배열
		//ai_images 파일목록들 보여줄 것.
		
		
		String file_ext[] = {"jpg", "gif", "png", "jfif"};//확장자제한. -확장자추가되면 더 써주면됨.
		//file_ext 확장자만, 파일리스트에 집어넣는다. 모델에포함.
	
		
		ArrayList<String> newfilelist = new ArrayList(); //#스트링만저장해도됨.파일이름이니까.
		for(String onefile : filelist) { //#파일하나가져와.
			//bangta.jpg - .으로 분리시 뒷부분. 근데 bangta.1.jpg.. 이럴수도 있음. 
			//마지막 . 이후의것만 가져와 : onefile.lastIndexOf('.');
			String myext = onefile.substring(onefile.lastIndexOf(".")+1);//.찾아서 그 뒤에부터.
			//jpg가져왔음. //.빼야함. 그거뒤에란소리.
			//마지막 점 찾아서 끝까지.
			//onefile분리해 쪼개는데 마지막 점 발견된데서 나머지. //jpg가져옴.
			
			for(String imgext : file_ext) { //내파일확장자, imgext랑 같은거있나.같냐물어봐.
				if(myext.equals(imgext)) {
					newfilelist.add(onefile); //onefile-전체이름, myext는 확장자만 뽑아냈음.
					break;//다찾으면 빠져나와.
				}//if end
			}//안for end
		}//겉 for//end
			
			
		//String newfilelist[] = new String[?]; 
		//#이미지 확장자인것만 쓰겠다. ->배열. ->문제:몇개짜리로 만드는가?
		//#배열 동적관리 = >ArrayList 전달된 뷰서 어떻게 사용할지 결정. - >갯수조절안해도 동적관리가능.
		
		ModelAndView mv= new ModelAndView();
		mv.addObject("filelist", newfilelist);//파일리스트 전달. 
		mv.setViewName("faceinput");//faceinput.jsp
		return mv;
	}
	
	@RequestMapping("/faceresult")
	public ModelAndView faceresult(String image){
		//서비스클래스 요청-https://naveropenapi.apigw.ntruss.com/vision/v1/celebrity -json
		String faceresult = service.test(image);//이미지파일이름전달.-리턴결과 스트링
		ModelAndView mv= new ModelAndView();
		mv.addObject("faceresult", faceresult);//네이버 응답결과. "{a:100}"
		mv.setViewName("faceresult");
		return mv; //이제 뷰만들러가면됨.faceresult
		
		
	}
	
	
	
	
	//눈코입 위치 등
	@RequestMapping("/faceinput2")
	public ModelAndView faceinput2() {
		//파일 중 하나 선택해서 이미지 분석해줘. 
		File f =new File(MyNaverInform.path); //파일과 디렉토리 정보 제공 //java.io 파일->파일시스템 작업 //디렉토리정보.
		String[] filelist = f.list(); //#전체 내용 다 들어가있음. 

		String file_ext[] = {"jpg", "gif", "png", "jfif"};//확장자제한. -확장자추가되면 더 써주면됨.

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
		mv.setViewName("faceinput2");//뷰
		return mv;
	}
	
	//안면인식서비스
	@RequestMapping("/faceresult2")
	public ModelAndView faceresult2(String image){ //jsp에서 쓰고싶어 ${param.image} -사용자가 선택한이미지파일가져올수있음.
		//서비스클래스 요청-https://naveropenapi.apigw.ntruss.com/vision/v1/face -json
		String faceresult2 = service2.test(image);//스트링이지만 제이슨형태임! //이미지파일이름전달.-리턴결과 스트링
		ModelAndView mv= new ModelAndView();
		mv.addObject("faceresult2", faceresult2);//네이버 응답결과. "{a:100}"
		//mv.setViewName("faceresult2");//텍스트로 성별,나이,감정,얼굴방향,눈코입위치,
		mv.setViewName("faceresult3"); //canvas(이미지, 도형)얼굴 사각형 표시
		return mv; //이제 뷰만들러가면됨.faceresult
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

//서비스마다 얼굴/소리 등 패키지 따로하는데 공통적인건 com.example.ai.
//인증정보, mywebconfig처럼 맵핑한거. 얼굴인식서비스서만쓰는게아니니까.
//com.example.ai - 모든ai서비스 
//cft-얼굴인식만관련해서 넣음.