package ss.SGA_Simulation;

import java.util.Map;


public class Simulator {
	public static void main(String[] args) {
		Carreer carreer = CarreerParser.parse("SoftwareEngineering.xml");
		System.out.println(carreer.name());
		for (int i = 1; i < carreer.years(); i++) {
			Map<Integer, Course> courses = carreer.courses(i);
			for (Integer code : courses.keySet()) {
				System.out.println(courses.get(code).toString());
			}
		}
	}
}
