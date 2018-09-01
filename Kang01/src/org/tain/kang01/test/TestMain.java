package org.tain.kang01.test;

public class TestMain {

	private static boolean flag;

	static {
		flag = true;
	}

	/////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + Utils.getClassInfo());
	}
}
