import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClientMain {

	private static boolean flag;
	private static int UDP_PORT;
	private static int SIZ_PACKET;

	static {
		flag = true;
		UDP_PORT = 1234;
		SIZ_PACKET = 1024;
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

		DatagramSocket socket = new DatagramSocket();
		DatagramPacket packet = null;
		byte[] buffer = null; //new byte[SIZ_PACKET];

		// SEND
		String str = "Hello, world!!!";
		buffer = str.getBytes();
		if (flag) System.out.printf(">>>>> send [%s](%d)%n", str, buffer.length);

		InetAddress address = InetAddress.getByName("127.0.0.1");
		packet = new DatagramPacket(buffer, buffer.length, address, UDP_PORT);
		socket.send(packet);

		// RECEIVE
		buffer = new byte[SIZ_PACKET];
		packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);

		String lunchMenu = new String(packet.getData(), 0, packet.getLength());
		if (flag) System.out.printf(">>>>> lunchMenu is '%s'(%d)%n", lunchMenu, packet.getLength());

		socket.close();
	}
}
