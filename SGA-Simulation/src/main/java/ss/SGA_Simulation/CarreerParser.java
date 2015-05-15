package ss.SGA_Simulation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CarreerParser {

	public static Carreer parse(String xml_path) {
		try {

			File fXmlFile = new File(xml_path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList years = doc.getElementsByTagName("year");

			Map<Integer, Map<Integer, Course>> plan = new HashMap<Integer, Map<Integer, Course>>();

			for (int y = 0; y < years.getLength(); y++) {
				Node year = years.item(y);
				NodeList courses = year.getChildNodes();
				Map<Integer, Course> year_courses = new HashMap<Integer, Course>();
				Element year_element = (Element) year;
				int carreer_year = Integer.parseInt(year_element
						.getAttribute("id"));

				for (int c = 0; c < courses.getLength(); c++) {
					Node course = courses.item(c);

					if (course.getNodeType() == Node.ELEMENT_NODE) {
						Element course_element = (Element) course;
						int carreer_course_code = Integer
								.parseInt(course_element.getAttribute("id"));
						String carreer_course_name = course_element
								.getElementsByTagName("name").item(0)
								.getTextContent();
						NodeList correlatives = ((Element) course_element
								.getElementsByTagName("correlatives").item(0))
								.getElementsByTagName("correlative");

						List<Integer> carreer_course_correlatives = new ArrayList<Integer>();
						for (int cr = 0; cr < correlatives.getLength(); cr++) {
							Node correlative = correlatives.item(cr);

							if (course.getNodeType() == Node.ELEMENT_NODE) {
								Element correlative_element = (Element) correlative;
								carreer_course_correlatives.add(Integer
										.parseInt(correlative_element
												.getAttribute("id")));
							}
						}
						Course carreer_course = new Course(carreer_course_name,
								0, carreer_year, carreer_course_code, 0.0f,
								carreer_course_correlatives);
						year_courses.put(carreer_course_code, carreer_course);
					}
				}
				plan.put(carreer_year, year_courses);
			}
			return new Carreer("SoftwareEngineering", plan);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
