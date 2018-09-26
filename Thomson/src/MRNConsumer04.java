import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

@SuppressWarnings("unused")
class AppClient04 implements OmmConsumerClient {

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
	private static Hashtable<String, List<ByteBuffer>> fragBuilderHash = new Hashtable<>();
	// store total sizes
	private static Hashtable<String, Long> totalSizes = new Hashtable<>();

	/////////////////////////////////////////////////////////////////////

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
			decode(refreshMsg.payload().map());
		}

		if (flag) System.out.println();
	}

	@Override
	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onUpdateMsg ##### Message");

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
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onGenericMsg #####");
	}

	@Override
	public void onAllMsg(Msg msg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onAllMsg #####");
	}

	@Override
	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent event) {
		if (flag) System.out.println("##### onGenericMsg #####");
	}

	/////////////////////////////////////////////////////////////////////

	private void decode(Map map) {
		if (flag) System.out.println(">>>>> Decode map: cnt = " + map.size());

		if (map.summaryData().dataType() == DataTypes.FIELD_LIST) {
			if (flag) System.out.println("Map Summary data: ");
			decode(map.summaryData().fieldList(), null);

			if (flag) System.out.println();
			//return;
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


	private void decode(FieldList fieldList, Object closuer) {
		if (flag) System.out.println(">>>>> Decode fieldList: cnt = " + fieldList.size());

		Iterator<FieldEntry> iter = fieldList.iterator();
		FieldEntry fieldEntry;
		String guid = null;
		long totalSize = 0;

		while (iter.hasNext()) {
			fieldEntry = iter.next();
			if (flag) System.out.printf("  FIELD_ENTRY: %d/%s/loadType(%d) %s%n"
					, fieldEntry.fieldId()
					, fieldEntry.name()
					, fieldEntry.loadType()
					, fieldEntry.load());

			if (fieldEntry.loadType() == DataTypes.BUFFER) {
				if (fieldEntry.fieldId() == FRAGMENT) {
					if (flag) System.out.println("  => FRAGMENT JSON zipped SIZE: " + fieldEntry.buffer().buffer().limit());

					if (totalSize == 0) {
						// not a first segment
						totalSize = totalSizes.get(guid).longValue();
					}

					if (fieldEntry.buffer().buffer().limit() == totalSize) {
						// there is only one segment, we are ready unzip using gzip
						String strFlatFrag = unzipPayload(Arrays.copyOf(fieldEntry.buffer().buffer().array(), fieldEntry.buffer().buffer().limit()));
						if (flag) System.out.println("  => FRAGMENT JSON STRING: " + strFlatFrag);
						try {
							// pretty-print json response
							JSONObject jsonResponse = new JSONObject(strFlatFrag);
							int spacesToIndentEachLevel = 2;
							if (flag) System.out.println("  => FRAGMENT JSON PRETTY:\n" + jsonResponse.toString(spacesToIndentEachLevel));
						} catch (Exception e) {
							System.err.println("Exception parsing json: " + e);
							e.printStackTrace(System.err);
						}
					} else {
						List<ByteBuffer> alFrags = null;
						if (!fragBuilderHash.containsKey(guid)) {
							// no hash key
							alFrags = new ArrayList<>();
							alFrags.add(fieldEntry.buffer().buffer());
							fragBuilderHash.put(guid, alFrags);
						} else {
							// exist hash key
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
								ByteArrayOutputStream bos = null;
								String strFlagFrag = null;

								try {
									bos = new ByteArrayOutputStream();
									for (int i=0; i < alFrags.size(); i++) {
										bos.write(Arrays.copyOf(alFrags.get(i).array(), alFrags.get(i).limit()));
									}
									strFlagFrag = unzipPayload(bos.toByteArray());
									if (flag) System.out.println("  => FRAGMENT JSON STRING: " + strFlagFrag);

									// pretty-print json response
									JSONObject jsonResponse = new JSONObject(strFlagFrag);
									int spacesToIndentEachLevel = 2;
									if (flag) System.out.println("  => FRAGMENT JSON PRETTY:\n" + jsonResponse.toString(spacesToIndentEachLevel));
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									if (bos != null) try { bos.close(); } catch (Exception e) {}
								}
							}
						}
					}
				}
			} else if (fieldEntry.fieldId() == TOT_SIZE) {
				totalSize = fieldEntry.uintValue();
			} else if (fieldEntry.fieldId() == GUID) {
				guid = fieldEntry.rmtes().toString();
				if (totalSize != 0)
					totalSizes.put(guid, new Long(totalSize));
			}
		}
	}

	private String unzipPayload(byte[] bytes) {
		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;

		try {
			bis = new ByteArrayInputStream(bytes);
			bos = new ByteArrayOutputStream();

			GZIPInputStream gis = new GZIPInputStream(bis);
			byte[] buffer = new byte[bytes.length + 1];
			int len = -1;
			while ((len = gis.read(buffer, 0, bytes.length + 1)) != -1) {
				bos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) try { bis.close(); } catch (IOException e) {}
			if (bos != null) try { bos.close(); } catch (IOException e) {}
		}

		return bos.toString();
	}
}

public class MRNConsumer04 {

	private static final boolean flag = true;

	/////////////////////////////////////////////////////////////////////

	private static String _ip;
	private static String _port;
	private static String _serviceName;
	private static String _userName;

	private static String[] _ricsMRN = { "MRN_HDLN", "MRN_STORY", "MRN_TRSI", "MRN_TRNA" };

	private static AppClient04 _appClient;

	public MRNConsumer04(String[] args) throws Exception {

		if (args == null || args.length != 4) {
			System.out.println("Insufficient arguments - requires:");
			System.out.println("serverip port servicename username");
			System.out.println("e.g. 10.117.216.106 14002 ELEKTRON_DD testuser");
			System.out.println("e.g. 239.234.234.200 51031 ELEKTRON_DD testuser");
			System.exit(-1);;
		}

		_ip = args[0];
		_port = args[1];
		_serviceName = args[2];
		_userName = args[3];

		try {
			_appClient = new AppClient04();

			OmmConsumerConfig config = EmaFactory.createOmmConsumerConfig();
			OmmConsumer consumer = EmaFactory.createOmmConsumer(config.host(_ip + ":" + _port).username(_userName));
			ReqMsg reqMsg = EmaFactory.createReqMsg();

			for (int i=0; i < _ricsMRN.length; i++) {
				consumer.registerClient(reqMsg
						.domainType(EmaRdm.MMT_NEWS_TEXT_ANALYTICS)
						.serviceName(_serviceName)
						.name(_ricsMRN[i])
						, _appClient
						, new Integer(i));
			}
		} catch (OmmException e) {
			throw e;
		}
	}

	/////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + ClassUtils.getClassInfo());

		if (flag) {
	    	//args = new String[] { "159.220.246.3",   "8101", "hEDD", "TY6_03_RHB_KR02895" };
	    	args = new String[] { "159.220.246.3",  "14002", "hEDD", "TY6_03_RHB_KR02895" };
	    	//args = new String[] { "159.220.246.19",  "8101", "hEDD", "TY6_03_RHB_KR02895" };
	    	//args = new String[] { "159.220.246.19", "14002", "hEDD", "TY6_03_RHB_KR02895" };
		}

    	new MRNConsumer04(args);
	}
}
