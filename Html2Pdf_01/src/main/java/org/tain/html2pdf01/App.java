package org.tain.html2pdf01;

/**
 * Hello world!
 *
 */
public class App {

	private static boolean flag;

	static {
		flag = true;
	}

	public static String getClassInfo() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();

		if (flag) sb.append(e.getClassName()).append('.').append(e.getMethodName()).append("()");
		if (flag) sb.append(" - ");
		if (flag) sb.append(e.getFileName()).append('(').append(e.getLineNumber()).append(')');

		return sb.toString();
	}

	public static void main( String[] args ) {
		if (flag) System.out.println(">>>>> " + getClassInfo());

        System.out.println( "Hello World!" );
    }
}
