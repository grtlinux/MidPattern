package com.thomsonreuters.ema.examples.mrn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
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

class AppClient implements OmmConsumerClient {

    public static final int FRAGMENT =  32641;
    public static final int TOT_SIZE =  32480;
    public static final int GUID =      4271;

    // store fragments for reassmbly
    Hashtable<String,ArrayList<ByteBuffer>> fragBuilderHash = new Hashtable<String,ArrayList<ByteBuffer>>();
    // store total sizes
    Hashtable<String,Long> totalSizes = new Hashtable<String,Long>();
    private MRNFXMain _mn = null;

    public AppClient() {}

    public void setFXMain(MRNFXMain mn) {
        _mn = mn;
    }

    public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event) {
        System.out.println("onRefreshMsg");
        System.out.println("Item Name: " + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
        System.out.println("Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));

        System.out.println("Item State: " + refreshMsg.state());

        if (DataType.DataTypes.MAP == refreshMsg.payload().dataType()) {
            decode(refreshMsg.payload().map());
        }

        System.out.println();
    }

    public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) {
        System.out.println("onUpdateMsg");
        System.out.println("Item Name: " + (updateMsg.hasName() ? updateMsg.name() : "<not set>"));
        System.out.println("Service Name: " + (updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>"));
        System.out.println("Closure: "+event.closure());

        if (DataType.DataTypes.MAP == updateMsg.payload().dataType()) {
            decode(updateMsg.payload().map());
        } else if (DataType.DataTypes.FIELD_LIST == updateMsg.payload().dataType()) {
            decode(updateMsg.payload().fieldList(),event.closure());
        }

        System.out.println();
    }

    public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) {
        System.out.println("Item Name: " + (statusMsg.hasName() ? statusMsg.name() : "<not set>"));
        System.out.println("Service Name: " + (statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>"));

        if (statusMsg.hasState()) {
            System.out.println("Item State: " + statusMsg.state());
        }

        System.out.println();
    }

    public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent) {
    }

    public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent) {
    }

    public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent) {
    }

	
	


    void decode(Map map) {
        System.out.println("Decode map:");
        if (DataTypes.FIELD_LIST == map.summaryData().dataType()) {
            System.out.println("Map Summary data:");
            decode(map.summaryData().fieldList(),null);
            System.out.println();
        }

        Iterator<MapEntry> iter = map.iterator();
        MapEntry mapEntry;
        while (iter.hasNext()) {
            mapEntry = iter.next();

            if (DataTypes.BUFFER == mapEntry.key().dataType()) {
            	System.out.println("Action: " 
					+ mapEntry.mapActionAsString() 
					+ " key value: " 
					+ EmaUtility.asHexString(mapEntry.key().buffer().buffer()));
					//System.out.println("Action: " + mapEntry.mapActionAsString() + " key value: " + EmaUtility.asHexString(mapEntry.key().buffer().byteBuffer()));
            }

            if (DataTypes.FIELD_LIST == mapEntry.loadType()) {
                System.out.println("Entry data:");
                decode(mapEntry.fieldList(), null);
                System.out.println();
            }
        }
    }

    void decode(FieldList fieldList, Object closure) {
        @SuppressWarnings("unused")
		boolean retVal;
        System.out.println("Decode fieldList:");
        Iterator<FieldEntry> iter = fieldList.iterator();
        FieldEntry fieldEntry;
        long totalSize = 0;
        String guid = "";
        while (iter.hasNext()) {
            fieldEntry = iter.next();
            System.out.println("FIELD_ENTRY: " + fieldEntry.fieldId() + "/" + fieldEntry.name() + ": " + fieldEntry.load() + " loadType: " + fieldEntry.loadType());
            
			if (fieldEntry.loadType() == DataTypes.BUFFER) {
                if (fieldEntry.fieldId() == FRAGMENT) {
                	System.out.println("=>FRAGMENT JSON zipped SIZE: "+fieldEntry.buffer().buffer().limit());//System.out.println("=>FRAGMENT JSON zipped SIZE: "+fieldEntry.buffer().byteBuffer().array().length);

                    if(totalSize == 0) // not a first segment
                        totalSize = totalSizes.get(guid).longValue();
                    if (fieldEntry.buffer().buffer().limit() == totalSize) {//if (fieldEntry.buffer().byteBuffer().array().length == totalSize) {
                        // there is only one segment, we are ready
                        // unzip using gzip
                    	String strFlatFrag = unzipPayload(Arrays.copyOf(fieldEntry.buffer().buffer().array(), fieldEntry.buffer().buffer().limit()));//String strFlatFrag = unzipPayload(fieldEntry.buffer().byteBuffer().array());
                        System.out.println("=>FRAGMENT JSON STRING: " + strFlatFrag);
                        try {
                            JSONObject jsonResponse = new JSONObject(strFlatFrag);
                            // pretty-print json response
                            int spacesToIndentEachLevel = 2;
                            System.out.println("FRAGMENT JSON PRETTY:\n" + jsonResponse.toString(spacesToIndentEachLevel));
                             if(null != _mn) {
                                int which = 0;
                                if(null != closure)
                                    which = ((Integer)closure).intValue();
                                _mn.updateTxtArea(which, jsonResponse.toString(spacesToIndentEachLevel));
                             }
                        }
                        catch (Exception e) {
                            System.err.println("Exception parsing json: " + e);
                            e.printStackTrace(System.err);
                        }
                    } else {
                        ArrayList<ByteBuffer> alFrags;
                        if(!fragBuilderHash.containsKey(guid)) {
                            alFrags = new ArrayList<ByteBuffer>();
                            alFrags.add(fieldEntry.buffer().buffer());//alFrags.add(fieldEntry.buffer().byteBuffer());
                            fragBuilderHash.put(guid, alFrags);
                        } else {
                            alFrags = fragBuilderHash.get(guid);
                            alFrags.add(fieldEntry.buffer().buffer());//alFrags.add(fieldEntry.buffer().byteBuffer());
                            int sizeNow = 0;
                            for(int i = 0; i < alFrags.size(); i++) {
                            	sizeNow+=alFrags.get(i).limit();//sizeNow+=alFrags.get(i).array().length;
                            }
                            if(sizeNow < totalSize) {
                                fragBuilderHash.put(guid, alFrags);
                            } else {
                                // we are ready
                                String strFlatFrag = "";
                                ByteArrayOutputStream fragOutputStream = new ByteArrayOutputStream( );

                                for(int i = 0; i < alFrags.size(); i++) {
                                    try {
                                    	fragOutputStream.write( Arrays.copyOf(alFrags.get(i).array(), alFrags.get(i).limit()) );//fragOutputStream.write( alFrags.get(i).array() );
                                    } catch(Exception e) {}
                                }
                                strFlatFrag+= unzipPayload(fragOutputStream.toByteArray());
                                System.out.println("=>FRAGMENT JSON STRING: " + strFlatFrag);
								try {
									JSONObject jsonResponse = new JSONObject(strFlatFrag);
									// pretty-print json response
									int spacesToIndentEachLevel = 2;
									System.out.println("FRAGMENT JSON PRETTY:\n" + jsonResponse.toString(spacesToIndentEachLevel));
									/*                      if(mn.isInitCompleted()) {
									 mn.updateTxtArea(0, jsonResponse.toString(spacesToIndentEachLevel));
									 } else {
									 //                         mn.updateTxtArea(0,"Update");
									 }
									 */                        }
								catch (Exception e) {
									System.err.println("Exception parsing json: " + e);
									e.printStackTrace(System.err);
								}
                            }

                        }
                    }
                }
            } else if(fieldEntry.fieldId() == TOT_SIZE) {
                totalSize = fieldEntry.uintValue();
            } else if(fieldEntry.fieldId() == GUID) {
                guid = fieldEntry.rmtes().toString();
                if(totalSize!=0)
                    totalSizes.put(guid, (new Long(totalSize)));
            }
        }
    }

    public String unzipPayload(byte[] bytes) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream gis = new GZIPInputStream(bis);
            byte[] buffer = new byte[bytes.length+1/*1024*/];
            int len = -1;
            while ((len = gis.read(buffer, 0, /*1024*/bytes.length+1)) != -1) {
                bout.write(buffer, 0, len);
            }
        } catch (Exception e) {
            System.out.println("Parse News error:" + e.getMessage());
        }
        return bout.toString();
    }
}

public class MRNConsumer implements Runnable{

    protected static String _ip;
    protected static String _port;
    protected static String _serviceName;
    protected static String _userName;
    public static String _ricsMRN[] = {"MRN_HDLN", "MRN_STORY", "MRN_TRSI", "MRN_TRNA"};
    AppClient _appClient = null;

    //public MRNConsumer() {}
    //public void mrnInit(String[] args) {
    public MRNConsumer(String[] args) {
        //  adslon01 14002 ERT_EDGE rmds
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
            _appClient = new AppClient();
            OmmConsumerConfig config = EmaFactory.createOmmConsumerConfig();
            OmmConsumer consumer = EmaFactory.createOmmConsumer(config.host(_ip + ":" + _port).username(_userName));
            ReqMsg reqMsg = EmaFactory.createReqMsg();
            for(int i = 0; i<_ricsMRN.length; i++) {
                consumer.registerClient(reqMsg
                		.domainType(EmaRdm.MMT_NEWS_TEXT_ANALYTICS)
                		.serviceName(_serviceName)
                		.name(_ricsMRN[i])
                		, _appClient
                		, (new Integer(i)));
            }
    //        Thread.sleep(60000);			// API calls onRefreshMsg(), onUpdateMsg() and onStatusMsg()		} catch ( OmmException excp ) {
        } catch (OmmException excp) {
            System.out.println(excp.getMessage());
        }
    }

    public void setFXMain(MRNFXMain mn){
        _appClient.setFXMain(mn);
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
            } catch(InterruptedException ie){}
        }
    }

    //////////////////////////////
    public static void main(String[] args) {

    	args = new String[] { "10.117.216.106", "14002", "ELEKTRON_DD", "testuser" };
    	args = new String[] { "239.234.234.200", "51031", "ELEKTRON_DD", "testuser" };
    	args = new String[] { "159.220.246.3",   "8101", "hEDD", "S639103-001" }; // S639103-002
    	args = new String[] { "159.220.246.3",  "14002", "hEDD", "S639103-001" }; // S639103-002
    	args = new String[] { "159.220.246.19",  "8101", "hEDD", "S639103-001" }; // S639103-002
    	args = new String[] { "159.220.246.19", "14002", "hEDD", "S639103-001" }; // S639103-002
    	args = new String[] { "159.220.246.3",   "8101", "hEDD", "S639103-002" }; // S639103-002
    	//args = new String[] { "159.220.246.3",   "8101", "hEDD", "testuser" }; // S639103-002

    	new MRNConsumer(args);
	}
}
