package org.tain.slf4j.log4j2;

import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2TestMain {

	public Log4j2TestMain() {
		Logger logger = LoggerFactory.getLogger(this.getClass());

		logger.info("info1: {}", "INFO");
		logger.info("info0");

		logger.debug("dbg0: {}", "DEBUG");
		logger.debug("dbg1");

		logger.error("err0: {}", "ERROR");
		logger.error("err1");

		logger.error("MESSAGE: {}", "Hello, world!!!");
	}

	public static void main(String[] args) {
		System.setProperty(XmlConfigurationFactory.CONFIGURATION_FILE_PROPERTY,"log4j.xml");
		new Log4j2TestMain();
	}
}
