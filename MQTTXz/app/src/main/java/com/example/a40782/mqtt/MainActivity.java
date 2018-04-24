package com.example.a40782.mqtt;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.warkiz.widget.IndicatorSeekBar;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private ImageView djzt_t;
    private TextView sbsj_t;
    private TextView djzs_t;
    private TextView djfx_t;
    private Switch djqt_sw;
    private Toolbar mToolbar;
    private Toast mToast;
    private PopupWindow mPopupWindow;
    private Button ConnectButton;
    private String host = "tcp://wjserver01.mqtt.iot.gz.baidubce.com:1883";
    private String userName = "wjserver01/device01";
    private String passWord = "ude+5Fo/HtdsR/+Ez+Pp8hnXDCSb9NPd+Rg92HK1Gmg=";
    private Button PubButton;
    private Handler handler;
    private IndicatorSeekBar djzs_bar;
    private MqttClient client;
    private String sbsj = "0";
    private String djzs = "0";
    private String djzt = "Off";
    private String djfx = "CW";
    private String djkz = "0";
    private String myTopic = "WJ/V1/GAT/TCP/R/J/CT/TID01I0FA";
    private Vibrator mVibrator;
    private MqttConnectOptions options;

    private ScheduledExecutorService scheduler;

    private static final String TAG = "TestTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();

        init();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    JSONObject message = JSON.parseObject((String) msg.obj);
                    try {
                        if (message != null) {
                            sbsj = message.getString("sbsj");
                            //System.out.println(sbsj);
                            if (sbsj != null) {
                                sbsj = sbsj.substring(0, 4) + "年" + sbsj.substring(4, 6) + "月" + sbsj.substring(6, 8) + "日"
                                        + sbsj.substring(8, 10) + "时" + sbsj.substring(10, 12) + "分" + sbsj.substring(12, 14) + "秒";
                                sbsj_t.setText(sbsj);}
                            }

                    }
                    catch(JSONException e)
                    {e.printStackTrace();
                    }
//                    String message = (String) msg.obj;
//                    MqttPacketModel xxs = MqttMsgHeader.MqttMsgHeaderPasre(myTopic, message.getBytes());
//                    String receive = new String(xxs.mPayload);
//                    if (xxs != null) {
//                        msg.obj = new String(xxs.mPayload);
//                        System.out.println(msg.obj + "2-----");}

                        Toast.makeText(MainActivity.this, (String) msg.obj,
                                Toast.LENGTH_SHORT).show();

                    //JSONObject message = new JSONObject(new String(msg.obj));
                    //int num = message.size();
                    //String receives[] = msg.obj.toString().split(",");

                    // System.out.println("----"+msg.obj);
                    //Toast.makeText(MainActivity.this,new String(yy.mPayload),
                    //Toast.LENGTH_SHORT).show();
                    System.out.println("-----------------------------");
                } else if (msg.what == 2) {
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    try {
                        client.subscribe(myTopic, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == 3) {
                    Toast.makeText(MainActivity.this, "连接失败，系统正在重连", Toast.LENGTH_SHORT).show();
                }
            }
        };

        PubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publish("djzt","1");
            }
        });

        djzs_bar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {

            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {
                //only callback on discrete series SeekBar type.
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                Toast.makeText(MainActivity.this, String.valueOf(djzs_bar.getProgress()),
                        Toast.LENGTH_SHORT).show();

            }

              });
                startReconnect();


    }

    private void initview() {
        mToolbar = findViewById(R.id.toolbar);
        ConnectButton = findViewById(R.id.connect);
        PubButton = findViewById(R.id.button_publish);
        djzs_bar = findViewById(R.id.seekBar);
        djqt_sw = findViewById(R.id.djqt_sw);
        djzt_t = findViewById(R.id.round_imageView);
        djfx_t = findViewById(R.id.djfx_value);
        djzs_t = findViewById(R.id.djzs_value);
        sbsj_t = findViewById(R.id.sbsj_value);
        djfx_t.setText(djfx);
        djzt_t.setColorFilter(android.graphics.Color.argb(255,24,241,0));
        sbsj_t.setText(sbsj);
        djfx_t.setText(djfx);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    private void init() {
        try {
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(host, "test",
                    new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            //设置回调
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    System.out.println("connectionLost----------");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                }

                @Override
                public void messageArrived(String topicName, MqttMessage message)
                        throws Exception {
                    //subscribe后得到的消息会执行到这里面
                    System.out.println("messageArrived----------");
                    Message msg = new Message();
                    msg.what = 1;
                    //msg.obj = topicName + "---" + message.toString();
                    String rec = message.toString();
                    msg.obj = rec.substring(rec.indexOf("{\""));
                    handler.sendMessage(msg);
                    //String receive = new String(xxs.mPayload);
                }
            });
            //connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void publish(String key,String value) {
        final String pkey = key;
        final String pval = value;
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (client.isConnected()) {
                    JSONObject js1 = new JSONObject(new LinkedHashMap());
                    js1.put("SBBH","TID01I0FA");
                    js1.put(pkey,pval);
                    //js1.put("SBAL", "1");
                    js1.put("sbsj","20180412092559");
                    js1.put("djzs","300");
                    //js1.put("djfx","NSW");
                    //js1.put("djzt","1");
                    System.out.println(new String(js1.toJSONString()));
                    MqttPacketModel testpacket = MqttMsgHeader.MqttMsgHeaderMake("WJ/V1/GAT/TCP/R/J/CT/TID01I0FA", js1.toJSONString().getBytes(), false);
                    //System.out.println(new String(testpacket.mPayload));
                    Log.i(TAG, "testlog" + new String(testpacket.mPayload));
                    try {
                        client.publish("WJ/V1/GAT/TCP/R/J/CT/TID01I0FA", testpacket.mPayload, 1, false);
                    } catch (Exception e) {
                        Log.i(TAG, "publish error");
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    private void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    client.connect(options);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (client != null && keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                client.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            scheduler.shutdown();
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}