package hanwha.neo.commons;

import java.io.File;

public class Module {

	private static boolean flag;

	public static File DIR_BASE;
	public static File DIR_BASE_GROUP;

	static {
		flag = true;

		if (flag) {
			// Server test
			DIR_BASE = new File("/neo_data");
			DIR_BASE_GROUP = new File("/neo_data_eo");
		} else {
			// PC test
			DIR_BASE = new File("C:/hanwha/_CXF/base");
			DIR_BASE_GROUP = new File("C:/hanwha/_CXF/base_group");
		}
	}
}
