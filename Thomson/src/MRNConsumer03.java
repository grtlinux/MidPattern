import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import com.thomsonreuters.ema.access.AckMsg;
import com.thomsonreuters.ema.access.DataType;
import com.thomsonreuters.ema.access.DataType.DataTypes;
import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.EmaUtility;
import com.thomsonreuters.ema.access.FieldList;
import com.thomsonreuters.ema.access.GenericMsg;
import com.thomsonreuters.ema.access.Map;
import com.thomsonreuters.ema.access.MapEntry;
import com.thomsonreuters.ema.access.Msg;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmConsumerClient;
import com.thomsonreuters.ema.access.OmmConsumerConfig;
import com.thomsonreuters.ema.access.OmmConsumerEvent;
import com.thomsonreuters.ema.access.OmmException;
import com.thomsonreuters.ema.access.RefreshMsg;
import com.thomsonreuters.ema.access.ReqMsg;
import com.thomsonreuters.ema.access.StatusMsg;
import com.thomsonreuters.ema.access.UpdateMsg;
import com.thomsonreuters.ema.rdm.EmaRdm;

class Decode {

	private static final boolean flag = true;

	public static void decode(Map map) {
		if (flag) System.out.println(">>>>> Decode map:");

		if (map.summaryData().dataType() == DataTypes.FIELD_LIST) {
			if (flag) System.out.println("Map Summary data:");
			decode(map.summaryData().fieldList(), null);
			if (flag) System.out.println();
			return;
		}

		Iterator<MapEntry> iter = map.iterator();
		MapEntry mapEntry;
		while (iter.hasNext()) {
			mapEntry = iter.next();

			if (mapEntry.key().dataType() == DataTypes.BUFFER) {
				System.out.println("Action: "
						+ mapEntry.mapActionAsString()
						+ " key value: "
						+ EmaUtility.asHexString(mapEntry.key().buffer().buffer()));
			}

			if (mapEntry.loadType() == DataTypes.FIELD_LIST) {
				if (flag) System.out.println("Entry data:");
				decode(mapEntry.fieldList(), null);
				if (flag) System.out.println();
			}
		}
	}

	public static void decode(FieldList fieldList, Object closure) {

	}

	private static String unzipPayload(byte[] bytes) {
		ByteArrayOutputStream bos = null;
		ByteArrayInputStream bis = null;
		String ret = null;

		try {
			bos = new ByteArrayOutputStream();
			bis = new ByteArrayInputStream(bytes);

			GZIPInputStream gis = new GZIPInputStream(bis);
			byte[] buffer = new byte[bytes.length + 1];
			int len = -1;
			while ((len = gis.read(buffer, 0, bytes.length+1)) != -1) {
				bos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) try { bos.close(); } catch (Exception e) {}
			if (bis != null) try { bis.close(); } catch (Exception e) {}
		}

		return ret;
	}
}

class AppClient03 implements OmmConsumerClient {

	private static final boolean flag = true;

	@Override
	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onStatusMsg ##### Closed");
		if (flag) System.out.println(">>>>> Item Name: " + (statusMsg.hasName() ? statusMsg.name() : "<not set>"));
		if (flag) System.out.println(">>>>> Service Name: " + (statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>"));
		if (flag) System.out.println(">>>>> Item State: " + (statusMsg.hasState() ? statusMsg.state() : "<not set>"));

		if (flag) System.out.println();
	}

	@Override
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onRefreshMsg ##### Open");
		if (flag) System.out.println(">>>>> Item name:" + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
		if (flag) System.out.println(">>>>> Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));
		if (flag) System.out.println(">>>>> Item State: " + refreshMsg.state());

		if (refreshMsg.payload().dataType() == DataType.DataTypes.MAP) {

		}

		if (flag) System.out.println();
	}

	@Override
	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onUpdateMsg ##### Message");
		if (flag) System.out.println(">>>>> Item Name: " + (updateMsg.hasName() ? updateMsg.name() : "<not set>"));
		if (flag) System.out.println(">>>>> Service Name: " + (updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>"));
		if (flag) System.out.println(">>>>> Closure: " + event.closure());

		if (flag) System.out.println();
	}

	@Override
	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onGenericMsg #####");
	}

	@Override
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onAckMsg #####");
	}

	@Override
	public void onAllMsg(Msg msg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onAllMsg #####");
	}
}

public class MRNConsumer03 {

	private static final boolean flag = true;

	/////////////////////////////////////////////////////

	private static String _ip;
	private static String _port;
	private static String _serviceName;
	private static String _userName;

	private static AppClient03 _appClient;

	private static String _ricsMRN[] = { "MRN_HDLN", "MRN_STORY", "MRN_TRSI", "MRN_TRNA" };

	public MRNConsumer03(String[] args) throws Exception {

		if (args == null || args.length != 4) {
			System.out.println("Insufficient arguments - requires:");
			System.out.println("serverip port servicename username");
			System.out.println("e.g. 10.117.216.106 14002 ELEKTRON_DD testuser");
			System.out.println("e.g. 239.234.234.200 51031 ELEKTRON_DD testuser");
			return;
		}

		_ip = args[0];
		_port = args[1];
		_serviceName = args[2];
		_userName = args[3];

		try {
			_appClient = new AppClient03();
			OmmConsumerConfig config = EmaFactory.createOmmConsumerConfig();
			OmmConsumer consumer = EmaFactory.createOmmConsumer(config.host(_ip + ":" + _port).username(_userName));
			ReqMsg reqMsg = EmaFactory.createReqMsg();

			for (int i=0; i < _ricsMRN.length; i++) {
				consumer.registerClient(reqMsg
						.domainType(EmaRdm.MMT_NEWS_TEXT_ANALYTICS)
						.serviceName(_serviceName)
						.name(_ricsMRN[i])
						, _appClient
						, (new Integer(i)));
			}
		} catch (OmmException e) {
			throw e;
		}
	}

	/////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + ClassUtils.getClassInfo());

		if (flag) {
	    	//args = new String[] { "159.220.246.3",   "8101", "hEDD", "TY6_03_RHB_KR02895" };
	    	args = new String[] { "159.220.246.3",  "14002", "hEDD", "TY6_03_RHB_KR02895" };
	    	//args = new String[] { "159.220.246.19",  "8101", "hEDD", "TY6_03_RHB_KR02895" };
	    	//args = new String[] { "159.220.246.19", "14002", "hEDD", "TY6_03_RHB_KR02895" };
		}

    	new MRNConsumer03(args);
	}
}
