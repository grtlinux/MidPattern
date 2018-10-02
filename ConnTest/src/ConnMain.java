import java.net.Socket;

public class ConnMain {

	public static void main(String[] args) {

		//args = new String[] { "127.0.0.1", "2025" };

		if (args.length != 2) {
			System.out.println("USAGE: java ConnMain [host] [port]");
			System.exit(-1);
		}

		String host = args[0];
		String port = args[1];

		Socket socket = null;
		try {
			socket = new Socket(host, Integer.valueOf(port));
			System.out.printf("OK: connection is success!!! (%s:%s)%n", host, port);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.printf("FAIL: connection is failed!!! (%s:%s)%n", host, port);
		} finally {
			if (socket != null) try { socket.close(); } catch (Exception e) {}
		}
	}
}
