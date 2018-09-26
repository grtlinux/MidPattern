import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpRecvMain {

	private static boolean flag;
	private static int UDP_PORT;
	private static int SIZ_PACKET;

	static {
		flag = true;
		UDP_PORT = 1234;
		SIZ_PACKET = 1024 * 1024;
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

		DatagramSocket socket = new DatagramSocket(UDP_PORT);
		DatagramPacket packet = null;
		byte[] buffer = null; // new byte[SIZ_PACKET];
		boolean listen = true;

		while (listen) {
			try {
				// RECEIVE
				buffer = new byte[SIZ_PACKET];
				packet = new DatagramPacket(buffer, buffer.length);

				socket.receive(packet);
				String req = new String(packet.getData(), 0, packet.getLength());
				if (flag) System.out.printf(">>>>> receive (%d) [%s]%n", packet.getLength(), req);

			} catch (Exception e) {
				e.printStackTrace();
				listen = false;
			}
		}

		socket.close();
	}
}
