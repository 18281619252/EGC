package com.example.blue;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 客户端发消息
 */
public class ClentSendActivity extends BaseActivity {
    private final static String TAG = Electrocardiogram.class.getSimpleName();
    private View button1;
    public BluetoothDevice device;
    public BluetoothSocket bTSocket;
    TextView btNmae;
    public static String string;
    public static int num;
   //  TextView tv_recv;
    public static int i;
    public static int j=1;

    public static int data1;
    public static int data2;
    public static int data3;
    public static int data4;
    public static int data5;
    public static int data6;
    public static int data7;


private ImageView imageView;

    public static String s;

    public static final int DATA=1;
    public static final int MAX=1;
    public static final int MIN=1;


    public static final int Image_run=4;
    public static final int Image_walk=5;
    public static final int Image_sit=3;
    public static final int Image_founder=8;
    public static final int Image_stand=6;
    public static final int Image_climb=7;
    

    private TextView data;
    private TextView max_text;
    private TextView min_text;
    public int min=800;
    public int max=0;
    public String min_string;
    public String max_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_clent_send);
        setContentView(R.layout.activity_clent_send);

  //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        btNmae = findViewById(R.id.bluetooth_name);

        imageView=(ImageView)findViewById(R.id.image);

        data=findViewById(R.id.data);
        max_text=findViewById(R.id.max);
        min_text=findViewById(R.id.min);

        device = getIntent().getParcelableExtra("device");
        btNmae.setText(device.getName() + "  " + device.getAddress());
        connectDevice();

    }




    private Handler handler=new Handler() {
        public  void handleMessage(Message msg)
        {
//            switch (msg.what){
//                case DATA:
//                    data.setText(s);
//                    break;
//                case MAX:
//                    data.setText(max_string);
//                case MIN:
//                    data.setText(s);
//
//
//            }




           if(msg.what==DATA)
            {
                Log.e(TAG,  "DATADATADATADATADATA");
                data.setText(s);
            }
            if(msg.what==MAX)
            {
                Log.e(TAG,  "MAXMAXMAXMAX");
                max_text.setText(max_string);
            }
            if(msg.what==MIN)
            {
                Log.e(TAG,  "MINMINMINMIN");
                min_text.setText(min_string);
            }

            if(msg.what==4)
            {
                Log.e(TAG,  "Image_runImage_runImage_runImage_run");
                imageView.setImageResource(R.drawable.run);
            }
            if(msg.what==8)
            {
                Log.e(TAG,  "Image_founderImage_founderImage_founderImage_founder");
                imageView.setImageResource(R.drawable.founder);
            }
            if(msg.what==3)
            {
                Log.e(TAG,  "Image_sitImage_sitImage_sitImage_sit");
                imageView.setImageResource(R.drawable.sit);
            }
            if(msg.what==6)
            {
                Log.e(TAG,  "DATADATADATADATADATA");
                imageView.setImageResource(R.drawable.stand);
            }
            if(msg.what==8)
                {
                    Log.e(TAG,  "Image_climbImage_climbImage_climbImage_climb");
                imageView.setImageResource(R.drawable.climb);
            }
            if(msg.what==5)
            {
                Log.e(TAG,  "Image_walkImage_walkImage_walkImage_walk");
                imageView.setImageResource(R.drawable.walk);
            }
        }
    };


    /**
     * 连接蓝牙
     */
    public void connectDevice() {
        showProgressDialog("正在连接蓝牙");
        new Thread() {
            public void run() {
                super.run();
                try {
                    bTSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(MainActivity.MY_UUID)); //创建一个Socket连接：只需要服务器在注册时的UUID号
                    bTSocket.connect(); //连接
                    showToast("连接成功");
                    dismissProgressDialog();
                    Electrocardiogram electrocardiogram
                            = (Electrocardiogram) findViewById(R.id.electrocardiogram);

                    Canvas canvas=null;


//                    while(data_string)
//                    {
//                        String s = String.valueOf(num%10000/10);
//                        tv.setText(s);
//                        data_string=false;
//                    }

                    while (true) {
                        InputStream is = bTSocket.getInputStream();
                        byte[] buffer = new byte[1];
                        int count = is.read(buffer);
                        string = new String(buffer, 0, 1, "utf-8");
                        //  tv_recv.append(string);
                        num = Integer.valueOf(string).intValue();
                        Log.e(TAG, num + "????????????");
                        if (j == 1) {
                            data1 = (int) num * 1000000;
                            j++;
                        } else if (j == 2) {
                            data2 = (int) num * 100000;
                            j++;
                        } else if (j == 3) {
                            data3 = (int) num * 10000;
                            j++;
                        } else if (j == 4) {
                            data4 = (int) num * 1000;
                            j++;
                        } else if(j==5){
                            data5 = (int) num * 100;
                            j++;
                        } else if(j==6) {
                            data6 = (int) num * 10;
                            j++;
                        } else if (j == 7)
                        {
                            data7 = data1+data2 + data3 + +data4+data5 + data6 +  num;
                            j = 1;

                        //判断数据是否有效
                        if ((int) data7 / 1000000 == 1) {
                            Log.e(TAG, data7 %10 + "******************");
                            //判断数据是否画图
                            if ( data7 % 10 < 4) {
                                electrocardiogram.getnum((int) data7 % 1000000);
                                i++;
                                Message message_sit = new Message();
                                message_sit.what = Image_sit;
                                handler.sendMessage(message_sit);
                            }
                            //图片改变
                            else if ( data7 % 10 == 4) {           //图片
                                Message message_run = new Message();
                                message_run.what = Image_run;
                                handler.sendMessage(message_run);
                            }
                            else if ( data7 % 10 == 5) {           //图片
                                Message message_walk = new Message();
                                message_walk.what = Image_walk;
                                handler.sendMessage(message_walk);
                            }
                            else if ( data7 % 10 == 6) {           //图片

                                Message message_stand = new Message();
                                message_stand.what = Image_stand;
                                handler.sendMessage(message_stand);
                            }
                            else if ( data7 % 10 == 8) {           //图片
                                Message message_founder = new Message();
                                message_founder.what = Image_founder;
                                handler.sendMessage(message_founder);
                            }
                            else if (data7 % 10 == 7) {           //图片
                                Message message_climb = new Message();
                                message_climb.what = Image_climb;
                                handler.sendMessage(message_climb);
                            }
                            //判断最大最小值
                            s = String.valueOf((int) data7 % 10000 / 10);
                            Message message = new Message();
                            message.what = DATA;
                            handler.sendMessage(message);
                            if (data7 % 10000 / 10 > max) {
                                Message message_max = new Message();
                                max = (int) data7 % 10000 / 10;
                                max_string = String.valueOf(max);
                                message_max.what = MAX;
                                handler.sendMessage(message_max);
                            }
                            if (data7 % 10000 / 10 < min) {
                                Message message_min = new Message();
                                min = (int) data7 % 10000 / 10;
                                min_string = String.valueOf(min);
                                message_min.what = MIN;
                                handler.sendMessage(message_min);
                            }
                        }
                    }
                    }

                } catch (IOException e) {
                    showToast("连接异常，请退出本界面再次尝试！");
                    dismissProgressDialog();
                    e.printStackTrace();
                }

            }


        }.start();
    }


 /*   public void getdata() {
        try {
            while (true) {

                float f = string;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
*/
}
