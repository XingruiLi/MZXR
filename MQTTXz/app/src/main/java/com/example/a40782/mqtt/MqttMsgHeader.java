package com.example.a40782.mqtt;

/**
 * Created by 28949 on 2017-04-21.
 */

public class MqttMsgHeader {

	 public static MqttPacketModel MqttMsgHeaderPasre(String topic, byte[] inputByte) {
	        if (inputByte.length < 10) {
	            return null;
	        }
	        MqttPacketModel model = new MqttPacketModel();
	        model.mCrcFlag = (byte) ((inputByte[1] >> 5) & 0x1);
	        if (model.mCrcFlag == 1) {
	            byte[] jsonByte = new byte[inputByte.length - 4];
	            System.arraycopy(inputByte, 4, jsonByte, 0, jsonByte.length);
	            byte calCrc[] = CrcHelper.getCRC(jsonByte);
	            if ((calCrc[0] == inputByte[2]) && (calCrc[1] == inputByte[3])) {
	                model.mPayload = jsonByte;
	                model.mTopic = topic;
	                model.mProtocolType = (byte) (inputByte[1] & 0xF);
	                model.mZipFlag = (byte) ((inputByte[1] >> 4) & 0x1);
	                model.mRetainFlag = (byte) (inputByte[0] & 0x1);
	                return model;
	            }
	        }
	        else if (model.mCrcFlag == 0) {
	            byte[] jsonByte = new byte[inputByte.length - 2];
	            System.arraycopy(inputByte, 2, jsonByte, 0, jsonByte.length);
	            model.mPayload = jsonByte;
	            model.mTopic = topic;
	            model.mProtocolType = (byte) (inputByte[1] & 0xF);
	            model.mZipFlag = (byte) ((inputByte[1] >> 4) & 0x1);
	            model.mRetainFlag = (byte) (inputByte[0] & 0x1);
	            return model;
	        }
	        return null;
	    }

	    public static MqttPacketModel MqttMsgHeaderMake(String topic, byte[] inputByte, boolean retain) {
	        byte[] sendByte = new byte[inputByte.length + 4];
	        System.arraycopy(inputByte, 0, sendByte, 4, inputByte.length);
	        byte[] crcByte = CrcHelper.getCRC(inputByte);
	        sendByte[2] = crcByte[0];
	        sendByte[3] = crcByte[1];

	        MqttPacketModel model = new MqttPacketModel();
	        model.mPayload = sendByte;
	        model.mTopic = topic;
	        sendByte[1] = 0;
	        sendByte[1] |= MqttPacketModel.PROTOCOL_JSON;
	        if (retain == true) {
	            model.mRetainFlag = 1;
	            sendByte[1] |= 1 << 4;
	        }
	        else {
	            model.mRetainFlag = 0;
	            sendByte[1] &= ~(1 << 4);
	        }
	        sendByte[1] |= 1 << 5;
	        sendByte[0] = 0;
	        return model;
	    }
	    
	   public static String getTopid(String DeviceNo){
		   if(DeviceNo!=null&&DeviceNo.length()>3)
		   {
		   DeviceNo=  DeviceNo.substring(3);
		   DeviceNo="TID"+DeviceNo;
		   }
		   return DeviceNo;
	   }
}
