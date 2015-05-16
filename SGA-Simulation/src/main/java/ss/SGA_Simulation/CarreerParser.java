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

			Map<Integer, Year> plan = new HashMap<Integer, Year>();

			for (int y = 0; y < years.getLength(); y++) {
				Node year_node = years.item(y);
				Element year_element = (Element) year_node;
				NodeList quarters = year_element
						.getElementsByTagName("quarter");
				int carreer_year = Integer.parseInt(year_element
						.getAttribute("id"));
				Year year = new Year(carreer_year);

				for (int q = 0; q < quarters.getLength(); q++) {
					Node quarter = quarters.item(q);
					Element quarter_element = (Element) quarter;
					NodeList courses = quarter.getChildNodes();
					Map<Integer, Course> quarter_courses = new HashMap<Integer, Course>();
					int year_quarter = Integer.parseInt(quarter_element
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
							double repeating_course_probability = Double
									.parseDouble(course_element
											.getElementsByTagName(
													"repeating_course_probability")
											.item(0).getTextContent());
							int carreer_course_credits = Integer
									.parseInt(course_element
											.getElementsByTagName("credits")
											.item(0).getTextContent());
							NodeList correlatives = ((Element) course_element
									.getElementsByTagName("correlatives").item(
											0))
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
							Course carreer_course = new Course(
									carreer_course_name, 0,
									carreer_course_code,
									carreer_course_credits,
									repeating_course_probability,
									carreer_course_correlatives);
							quarter_courses.put(carreer_course_code,
									carreer_course);
						}
					}
					year.addQuarterPlan(year_quarter, quarter_courses);
					plan.put(carreer_year, year);
				}
			}
			return new Carreer("SoftwareEngineering", plan);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
