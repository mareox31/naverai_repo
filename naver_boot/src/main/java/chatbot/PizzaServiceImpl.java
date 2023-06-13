package chatbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("pizzaservice")
public class PizzaServiceImpl {
	@Autowired
	PizzaMapper mapper;
	
	public int insertPizza(PizzaDTO dto) {
		return mapper.insertPizza(dto);
	}
	
	
	
}

//피자주문내용 -db에저장- 네이버서비스상속X(구조다름)




//다른서비스는 네이버서비스 상속받았음. 
//얘도 그럴거면 String test(String )이렇게 받아와야한다. 
//저렇게 받아와서 , 
//String test(String x) {
//	PizzaMapper.insert(PizzaDTO);//주문정보넣어줘 할건데,피자디티오필요. 네이버상속받은구조로하면, 스트링으로받아야함, 기존네이버서비스와다름. 
//}