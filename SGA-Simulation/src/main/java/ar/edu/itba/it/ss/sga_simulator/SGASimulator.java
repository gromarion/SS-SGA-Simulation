package ar.edu.itba.it.ss.sga_simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SGASimulator {
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SGASimulator.class, args);
		
	}

}
