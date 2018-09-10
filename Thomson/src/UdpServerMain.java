import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class UdpServerMain {

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

	private static class LunchMenu {
		private static final String[] LUNCH = {
				"김치찌개",
				"짜장명",
				"냉면",
				"돈까스",
				"된장찌개",
				"감자탕",
				"순두부찌개",
				"짬뽕",
				"볶음밥",
				"불고기",
		};

		public static String selectMenu() {
			return LUNCH[new Random().nextInt(LUNCH.length)];
		}
	}

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
		if (flag) System.out.println(">>>>> " + LunchMenu.selectMenu());

		DatagramSocket socket = new DatagramSocket(UDP_PORT);
		DatagramPacket packet = null;
		byte[] buffer = null; // new byte[SIZ_PACKET];
		boolean listen = true;

		while (listen) {
			try {
				// RECEIVE
				buffer = new byte[SIZ_PACKET];
				packet = new DatagramPacket(buffer, buffer.length);
				if (!flag) System.out.printf(">>>>> ready (%d)%n", buffer.length);
				socket.receive(packet);
				String req = new String(packet.getData(), 0, packet.getLength());
				if (flag) System.out.printf(">>>>> receive [%s](%d)%n", req, packet.getLength());

				// SEND
				String menu = "Today's menu is " + LunchMenu.selectMenu();
				buffer = menu.getBytes();

				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(buffer, buffer.length, address, port);
				socket.send(packet);
				if (flag) System.out.printf(">>>>> send [%s](%d)%n", menu, buffer.length);

			} catch (Exception e) {
				e.printStackTrace();
				listen = false;
			}
		}

		socket.close();
	}
}
