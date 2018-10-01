package hanwha.neo.branch.common.transferfile.client;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

public class SftpTestMain {

	private static boolean flag;

	static {
		flag = true;

		if (flag) System.out.println(">>>>> " + getClassInfo());
	}

	//////////////////////////////////////////////////////////////

	private static String getClassInfo() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();

		if (flag) sb.append(e.getClassName()).append('.').append(e.getMethodName()).append("()");
		if (flag) sb.append(" - ");
		if (flag) sb.append(e.getFileName()).append('[').append(e.getLineNumber()).append(']');

		return sb.toString();
	}

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + getClassInfo());

		if (flag) test01(args);

		System.exit(0);
	}

	private static void test01(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + getClassInfo());

        // String host = "10.1.21.11"; 		 /* 운영서버 */
        String host = "10.3.41.116"; 		/*개발서버*/
        int port = 22;
        String username = "hrsftp";
        String password = "dlfgkwk1!";

        JSch jsch = null;
        Session session = null;
        Properties config = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        try {
			jsch = new JSch();

			session = jsch.getSession(username, host, port);
			session.setPassword(password);

			config = new Properties();
			config.put("StrictHostKeyChecking", "no"); // 인증서 검사를 하지 않음
			session.setConfig(config);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;

			String currentDiretory = null;

			currentDiretory = channelSftp.pwd();
			System.out.println(">>>>> " + currentDiretory);

			String path00 = "/appdata/hwgiapp/servlet_home/webapps/hrs/hrfile/HNLI/circle/";
			channelSftp.cd(path00);

			currentDiretory = channelSftp.pwd();
			System.out.println(">>>>> " + currentDiretory);

			SftpATTRS attrs = channelSftp.stat(path00);
			System.out.println(">>>>> " + attrs);

			//channelSftp.mkdir("/DATA1/onldata/hrs/HNLI/circle/hello");

			currentDiretory = channelSftp.pwd();
			System.out.println(">>>>> " + currentDiretory);

			channelSftp.mkdir("/appdata/hwgiapp/servlet_home/webapps/hrs/hrfile/HNLI/circle/hello");

			currentDiretory = channelSftp.pwd();
			System.out.println(">>>>> " + currentDiretory);

			channelSftp.quit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("##### program exit.... #####");
		}
	}
}
