package chatbot;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper //맵퍼인식하려면 @MappScan. -얜 아직. (NaverBootApplication여기가서해)//매퍼파일이다.
@Repository //@ComponentScan 필요. -얜 되어있는데,//객체로생성해준다.
public interface PizzaMapper {
		int insertPizza(PizzaDTO dto); 
		//인서트 이름과같음. 
		//해당 id pizza-mapping 의 sql실행해줌.//행갯수주니까 int하던, void안볼거면이거써도됨.
		//인터페이스 모든메서드 안붙여도 public
}

//이게 DAO다.
//db관련 일 함 -----
//서비스 써야함. 이젠. 피자서비스 만들어서, 피자맵퍼한테,주문내용시키게해야함->피자서비스만들어. 
//구현클래스 
//1.@mapper @Repository 붙일것.
//2-1. sql이랑이름같은거 주면 자동 메소드 실행. 
//2. mapping.xml파일이랑 파라메터타입따라매개변수결정, 리턴타입 결정.
//미리 알리아스 설정. (마이바티스-config.xml)