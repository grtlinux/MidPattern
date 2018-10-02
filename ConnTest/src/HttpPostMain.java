import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public class HttpPostMain {

	public static void main(String[] args) {

		try {
			String param = "name=" + URLEncoder.encode("미니", "UTF-8");

			URL url = new URL("http://www.word.pe.kr/url.php");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			DataOutputStream out = null;

			try {
				out = new DataOutputStream(conn.getOutputStream());
				out.writeBytes(param);
				out.flush();
			} finally {
				if (out != null) out.close();
			}

			Scanner scan = new Scanner(conn.getInputStream());

			int line = 1;
			while (scan.hasNext()) {
				String str = scan.nextLine();
				System.out.println((line++) + ":" + str);
			}
			scan.close();

		} catch (MalformedURLException e) {
			System.out.println("The URL address is incorrect.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("It can't connect to the web page.");
			e.printStackTrace();
		}
	}
}
