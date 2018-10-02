import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CallHttpMain {

	public static void main(String[] args) {

		//args = new String[] { "http://hanwha.eagleoffice.co.kr/neo/ws/common/transferFile?wsdl", "utf-8" };

		if (args.length != 2) {
			System.out.println("USAGE: java CallHttpMain [url] [charSet]");
			System.exit(-1);
		}

		String urlPath = args[0];
		String charSet = args[1];

		try {
			URL url = new URL(urlPath);
			URLConnection con = (URLConnection) url.openConnection();
			InputStreamReader isr = new InputStreamReader(con.getInputStream(), charSet);
			BufferedReader reader = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\r\n");
			}

			reader.close();

			System.out.println(sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
