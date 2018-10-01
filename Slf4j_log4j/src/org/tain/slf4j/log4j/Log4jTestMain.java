package org.tain.slf4j.log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTestMain {

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(Log4jTestMain.class);

		//PropertyConfigurator.configure("./lib/log4j.propertise");
		//BasicConfigurator.configure();//log4j.xml을 안쓸 경우

		logger.info("info1 {}", "INFO");
		logger.info("info0");

		logger.debug("dbg0 {}", "DEBUG");
		logger.debug("dbg1");

		logger.error("err0 {}", "ERROR");
		logger.error("err1");
	}
}
