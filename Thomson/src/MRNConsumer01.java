import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;

import com.thomsonreuters.ema.access.AckMsg;
import com.thomsonreuters.ema.access.DataType;
import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.FieldList;
import com.thomsonreuters.ema.access.GenericMsg;
import com.thomsonreuters.ema.access.Map;
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
import com.thomsonreuters.ema.examples.mrn.MRNFXMain;
import com.thomsonreuters.ema.rdm.EmaRdm;

class AppClient01 implements OmmConsumerClient {

	public static final int FRAGMENT = 32641;
	public static final int TOT_SIZE = 32480;
	public static final int GUID = 4271;

	// store fragments for reassembly
	Hashtable<String, ArrayList<ByteBuffer>> fragBuilderHash = new Hashtable<String, ArrayList<ByteBuffer>>();
	// store total sizes
	Hashtable<String, Long> totalSizes = new Hashtable<String, Long>();
	private MRNFXMain _mn = null;

	public AppClient01() {}

	public void setFXMain(MRNFXMain mn) {
		_mn = mn;
	}

	@Override
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event) {
		// TODO Auto-generated method stub
		System.out.println("onRefreshMsg");
		System.out.println("Iten Name: " + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));

		System.out.println("Item State: " + refreshMsg.state());

		if (DataType.DataTypes.MAP == refreshMsg.payload().dataType()) {
			decode(refreshMsg.payload().map());
		}

		System.out.println();
	}

	@Override
	public void onUpdateMsg(UpdateMsg arg0, OmmConsumerEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusMsg(StatusMsg arg0, OmmConsumerEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGenericMsg(GenericMsg arg0, OmmConsumerEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAckMsg(AckMsg arg0, OmmConsumerEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAllMsg(Msg arg0, OmmConsumerEvent arg1) {
		// TODO Auto-generated method stub

	}

	public String unzipPayload(byte[] bytes) {

		return null;
	}

	void decode(FieldList fieldList, Object closure) {

	}

	void decode(Map map) {

	}
}

public class MRNConsumer01 implements Runnable {

	protected static String _ip;
	protected static String _port;
	protected static String _serviceName;
	protected static String _userName;
	public static String _ricsMRN[] = { "MRN_HDLN", "MRN_STORY", "MRN_TRSI", "MRN_TRNA" };
	AppClient01 _appClient = null;

	public MRNConsumer01(String[] args) {
		if (args == null || args.length != 4) {
			return;
		}

		_ip = args[0];
		_port = args[1];
		_serviceName = args[2];
		_userName = args[3];

		try {
			_appClient = new AppClient01();
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
			e.printStackTrace();
		}
	}

	public void setFXMain(MRNFXMain mn) {
		_appClient.setFXMain(mn);
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
	}
}
