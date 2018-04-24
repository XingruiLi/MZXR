package com.example.a40782.mqtt;

/**
 * Created by 28949 on 2017-04-21.
 */

public class MqttPacketModel {
        public static final int PROTOCOL_JSON = 1;
        public static final int PROTOCOL_BIN = 2;
        public int mProtocolType;
        public int mZipFlag;
        public int mCrcFlag;
        public int mRetainFlag;
        public String mTopic;
        public byte[] mPayload;
}
