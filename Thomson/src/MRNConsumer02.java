import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.json.JSONObject;

import com.thomsonreuters.ema.access.AckMsg;
import com.thomsonreuters.ema.access.DataType;
import com.thomsonreuters.ema.access.DataType.DataTypes;
import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.EmaUtility;
import com.thomsonreuters.ema.access.FieldEntry;
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

class UpdateBean {

	private static final boolean flag = true;

	private String dateTime;
	private String msgType;  // UPDAGE/STATUS/REFRESH/GENERIC/ACK/ALL ..

	private String name;
	private String serviceName;
	private String state;

	private String dataType;  // DataType.DateTypes.*
	private String loadType;

	/*
	if (flag) System.out.printf("FIELD_ENTRY: %d/%s/loadType(%d) %s%n"
			, fieldEntry.fieldId()
			, fieldEntry.name()
			, fieldEntry.loadType()
			, fieldEntry.load());
	*/

	private String guid;
	private String activDate;
	private String timactMs;
	private String mrnSrc;
	private String mrnType;
	private String mrnVerMajor;
	private String mrnVerMinor;
	private String totSize;
	private String[] fragNum;
	private String[] size;
	private String[] fragment;

	private String jsonMsg;
	private String prettyJsonMsg;
}

class StatusBean {

}

class RefreshBean {

}


@SuppressWarnings("unused")
class AppClient02 implements OmmConsumerClient {

	private static final boolean flag = true;

	private static final int TIMACT_MS = 4148;
	private static final int ACTIV_DATE = 17;
	private static final int MRN_TYPE = 8593;
	private static final int MRN_V_MAJ = 8506;
	private static final int MRN_V_MIN = 11787;
	private static final int TOT_SIZE = 32480;
	private static final int FRAG_NUM = 32479;
	private static final int GUID = 4271;
	private static final int MRN_SRC = 12215;
	private static final int FRAGMENT = 32641;

	// store fragments for reassembly
	Hashtable<String, List<ByteBuffer>> fragBuilderHash = new Hashtable<>();
	// store total sizes
	Hashtable<String, Long> totalSizes = new Hashtable<>();

	public AppClient02() {}

	@Override
	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onStatusMsg");
		if (flag) System.out.println(">>>>> Item Name: " + (statusMsg.hasName() ? statusMsg.name() : "<not set>"));
		if (flag) System.out.println(">>>>> Service Name: " + (statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>"));
		if (flag) System.out.println(">>>>> Item State: " + (statusMsg.hasState() ? statusMsg.state() : "<not set>"));

		if (flag) System.out.println();
	}

	@Override
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onRefreshMsg");
		if (flag) System.out.println(">>>>> Item name:" + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
		if (flag) System.out.println(">>>>> Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));
		if (flag) System.out.println(">>>>> Item State: " + refreshMsg.state());

		if (DataType.DataTypes.MAP == refreshMsg.payload().dataType()) {
			decode(refreshMsg.payload().map());
		}

		if (flag) System.out.println();
	}

	@Override
	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onUpdateMsg");
		if (flag) System.out.println(">>>>> Item Name: " + (updateMsg.hasName() ? updateMsg.name() : "<not set>"));
		if (flag) System.out.println(">>>>> Service Name: " + (updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>"));
		if (flag) System.out.println(">>>>> Closure: " + event.closure());

		if (updateMsg.payload().dataType() == DataType.DataTypes.MAP) {
			decode(updateMsg.payload().map());
		} else if (updateMsg.payload().dataType() == DataType.DataTypes.FIELD_LIST) {
			decode(updateMsg.payload().fieldList(), event.closure());
		}

		if (flag) System.out.println();
	}

	@Override
	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent event) {
		if (!flag) System.out.println("##### onGenericMsg");
	}

	@Override
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent event) {
		if (!flag) System.out.println("##### onAckMsg");
	}

	@Override
	public void onAllMsg(Msg msg, OmmConsumerEvent event) {
		if (!flag) System.out.println("##### onAllMsg");
	}



	private void decode(Map map) {
		if (flag) System.out.println(">>>>> Decode map:");
		if (map.summaryData().dataType() == DataTypes.FIELD_LIST) {
			if (flag) System.out.println("Map Summary data:");
			decode(map.summaryData().fieldList(), null);
			if (flag) System.out.println();
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
				System.out.println("Entry data:");
				decode(mapEntry.fieldList(), null);
				System.out.println();
			}
		}
	}

	private void decode(FieldList fieldList, Object closure) {
		if (flag) System.out.println("Decode fieldList: cnt = " + fieldList.size());

		Iterator<FieldEntry> iter = fieldList.iterator();
		FieldEntry fieldEntry;
		long totalSize = 0;
		long size = 0;
		String guid = "";

		while (iter.hasNext()) {
			fieldEntry = iter.next();

			if (flag) System.out.printf("FIELD_ENTRY: %d/%s/loadType(%d) %s%n"
					, fieldEntry.fieldId()
					, fieldEntry.name()
					, fieldEntry.loadType()
					, fieldEntry.load());

			if (fieldEntry.fieldId() == TOT_SIZE) {
				totalSize = fieldEntry.uintValue();
				size = 0;
			}

			if (fieldEntry.fieldId() == FRAGMENT) {
				System.out.println("=>FRAGMENT JSON zipped SIZE: " + fieldEntry.buffer().buffer().limit());
				size += (long) fieldEntry.buffer().buffer().limit();
			}

			if (size > 0 && size == totalSize) {
				size = 0;
				totalSize = 0;
				System.out.println("###############################################");
			}

			if (flag) continue;

			if (fieldEntry.fieldId() == TOT_SIZE) {
				totalSize = fieldEntry.uintValue();
			} else if (fieldEntry.fieldId() == GUID) {
				guid = fieldEntry.rmtes().toString();
				if (totalSize != 0) {
					totalSizes.put(guid, new Long(totalSize));
				}
			} else if (fieldEntry.loadType() == DataTypes.BUFFER) {
				if (fieldEntry.fieldId() == FRAGMENT) {
					System.out.println("=>FRAGMENT JSON zipped SIZE: " + fieldEntry.buffer().buffer().limit());

					if (totalSize == 0)  // not a first segment
						totalSize = totalSizes.get(guid).longValue();

					if (fieldEntry.buffer().buffer().limit() == totalSize) {
						// there is only one segment, we are ready
						// unzip using gzip
						String strFlatFlag = unzipPayload(Arrays.copyOf(fieldEntry.buffer().buffer().array(), fieldEntry.buffer().buffer().limit()));
						System.out.println("=>FRAGMENT JSON STRING: " + strFlatFlag);
						try {
							JSONObject jsonResponse = new JSONObject(strFlatFlag);
							// pretty-print json response
							int spacesToIndentEachLevel = 2;
							System.out.println("FRAGMENT JSON PRETTY:\n" + jsonResponse.toString(spacesToIndentEachLevel));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						List<ByteBuffer> alFrags;
						if (!fragBuilderHash.containsKey(guid)) {
							// other article
							alFrags = new ArrayList<>();
							alFrags.add(fieldEntry.buffer().buffer());
							fragBuilderHash.put(guid, alFrags);
						} else {
							// the same article
							alFrags = fragBuilderHash.get(guid);
							alFrags.add(fieldEntry.buffer().buffer());
							int sizeNow = 0;
							for (int i=0; i < alFrags.size(); i++) {
								sizeNow += alFrags.get(i).limit();
							}

							if (sizeNow < totalSize) {
								fragBuilderHash.put(guid, alFrags);
							} else {
								// we are ready
								ByteArrayOutputStream fragOutputStream = new ByteArrayOutputStream();
								String strFlatFrag = "";

								for (int i=0; i < alFrags.size(); i++) {
									try {
										fragOutputStream.write(Arrays.copyOf(alFrags.get(i).array(), alFrags.get(i).limit()));
									} catch (Exception e) {}
								}
								strFlatFrag += unzipPayload(fragOutputStream.toByteArray());

								if (flag) System.out.println("=>FRAGMENT JSON STRING: " + strFlatFrag);

								try {
									JSONObject jsonResponse = new JSONObject(strFlatFrag);
									// pretty-print json response
									int spacesToIndentEachLevel = 2;
									System.out.println("FRAGMENT JSON PRETTY:\n" + jsonResponse.toString(spacesToIndentEachLevel));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	private String unzipPayload(byte[] bytes) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

		try {
			GZIPInputStream gis = new GZIPInputStream(bis);
			byte[] buffer = new byte[bytes.length + 1];
			int len = -1;
			while ((len = gis.read(buffer, 0, bytes.length+1)) != -1) {
				bos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bos.toString();
	}
}

public class MRNConsumer02 {

	private static final boolean flag = true;

	private static String _ip;
	private static String _port;
	private static String _serviceName;
	private static String _userName;

	private static AppClient02 _appClient;

    private static String _ricsMRN[] = {"MRN_HDLN", "MRN_STORY", "MRN_TRSI", "MRN_TRNA"};

    public MRNConsumer02(String[] args) {

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
			_appClient = new AppClient02();
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

	///////////////////////////////////////////////////////////////////////////

	public static String getClassInfo() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();

		if (flag) sb.append(e.getClassName()).append('.').append(e.getMethodName()).append("()");
		if (flag) sb.append(" - ");
		if (flag) sb.append(e.getFileName()).append('(').append(e.getLineNumber()).append(')');

		return sb.toString();
	}

	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) {
		if (flag) System.out.println(">>>>> " + getClassInfo());

		if (flag) {
	    	//args = new String[] { "159.220.246.3",   "8101", "hEDD", "TY6_03_RHB_KR02895" };
	    	args = new String[] { "159.220.246.3",  "14002", "hEDD", "TY6_03_RHB_KR02895" };
	    	//args = new String[] { "159.220.246.19",  "8101", "hEDD", "TY6_03_RHB_KR02895" };
	    	//args = new String[] { "159.220.246.19", "14002", "hEDD", "TY6_03_RHB_KR02895" };
		}

    	new MRNConsumer02(args);
	}
}