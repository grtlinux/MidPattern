import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSendMain {

	private static boolean flag;
	private static String UDP_HOST;
	private static int UDP_PORT;
	//private static int SIZ_PACKET;

	static {
		flag = true;
		UDP_HOST = "127.0.0.1";
		UDP_PORT = 1234;
		//SIZ_PACKET = 100 * 1024;
	}

	///////////////////////////////////////////////////////////////////////////

	public static String getClassInfo() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();

		if (flag) sb.append(e.getClassName()).append('.').append(e.getMethodName()).append("()");
		if (flag) sb.append(" - ");
		if (flag) sb.append(e.getFileName()).append('(').append(e.getLineNumber()).append(')');

		return sb.toString();
	}

	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + getClassInfo());

		if (flag) test01(args);
	}

	private static void test01(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + getClassInfo());

		for (int i=0; i < 10; i++) {
			// SEND
			String str = String.format("Hello, world!!! - %05d", i);
			byte[] buffer = str.getBytes();

			DatagramSocket socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(UDP_HOST), UDP_PORT);
			socket.send(packet);
			socket.close();
			if (flag) System.out.printf(">>>>> send [%s](%d)%n", str, buffer.length);

			try { Thread.sleep(2000); } catch (InterruptedException e) {}
		}
	}
}
