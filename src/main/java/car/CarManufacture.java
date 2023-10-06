package car;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CarManufacture {
	@Bean
	public Car newCar() {
		Doors door = new Doors();
		Car c = new Car(door);
		return c;
	}
}
