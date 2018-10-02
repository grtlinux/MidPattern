import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public class HttpGetMain {

	public static void main(String[] args) {

		//args = new String[] { "http://google.com" };

		if (args.length != 1) {
			System.out.println("USAGE: java HttpUrlMain [url]");
			System.exit(-1);
		}

		String urlPath = args[0];

		try {
			String param = "name=" + URLEncoder.encode("강석", "utf-8");

			URL url = new URL(urlPath + "?" + param);
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);

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
