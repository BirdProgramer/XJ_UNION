package com.ct.mywork;


import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2018/4/12.
 */

class SocketThread extends Thread {
    private static String TAG="SocketThread";
    private static Socket socket;
    private static InputStreamReader inputStreamReader;
    private static OutputStreamWriter outputStreamWriter;
    private static JsonWriter jsonWriter;
    private static JsonReader jsonReader;
    private static String account;
    private static String password;
    private static AlterInformattionActivity alterInformattionActivity;
    private static AlterPasswordActivity alterPasswordActivity;
    private static FirstActivity firstActivity;
    private static MainActivity mainActivity;
    public static Timer timer;

    public static Handler socketHandler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:                                                         //login
                    account=msg.getData().getString("account");
                    password=msg.getData().getString("password");
                    login();
                    break;
                case 2:                                                         //set information
                    Bundle bundle=msg.getData();
                    setInfo(bundle);
                    break;
                case 3:                                                         //set password
                    String newPassword=msg.getData().getString("password");
                    setPassword(newPassword);
                    break;
                default:
                    break;
            }
        }
    };
    public static void setMainActivity(MainActivity m){
        SocketThread.mainActivity=m;
    }
    public static void setAlterInformattionActivity(AlterInformattionActivity alt){
        alterInformattionActivity=alt;
    }
    public static void setAlterPasswordActivity(AlterPasswordActivity alt){
        alterPasswordActivity=alt;
    }
    public static void setFirstActivity(FirstActivity fir){
        firstActivity=fir;
    }
    private static void login(){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Message message=new Message();
        try{
            socket = new Socket();
            socket.connect(new InetSocketAddress(ServerData.IP, ServerData.PORT),ServerData.timeout);
            inputStreamReader=new InputStreamReader(socket.getInputStream());
            outputStreamWriter=new OutputStreamWriter(socket.getOutputStream());
            jsonWriter = new JsonWriter(outputStreamWriter);
            jsonWriter.beginObject();
            jsonWriter.name("intent").value("login");
            jsonWriter.name("account").value(account);
            jsonWriter.name("password").value(password);
            jsonWriter.endObject();
            jsonWriter.flush();
            //接收消息
            Bundle bundle=new Bundle();
            jsonReader=new JsonReader(inputStreamReader);
            jsonReader.beginObject();
            while(jsonReader.hasNext()){
                bundle.putString(jsonReader.nextName(),jsonReader.nextString());
            }
            jsonReader.endObject();
            switch(bundle.getString("result")){
                case "success":
                    message.what=1;
                    message.setData(bundle);
                    timer=new Timer();
                    TimerTask timerTask=new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                jsonWriter = new JsonWriter(outputStreamWriter);
                                jsonWriter.beginObject();
                                jsonWriter.name("intent").value("check");
                                jsonWriter.endObject();
                                jsonWriter.flush();
                                //接收消息
                                Log.e(TAG,"sent");
                            }catch(IOException e){
                                Log.e(TAG,"not connected");
                                Message message=new Message();
                                message.what=100;
                                mainActivity.uiHandler.sendMessage(message);
                                timer.cancel();
                            }
                        }
                    };
                    timer.scheduleAtFixedRate(timerTask,ServerData.interval,ServerData.interval);
                    break;
                case "failure":message.what=2;break;
                default:break;
            }
            Log.e(TAG,bundle.getString("result"));
        }catch (SocketTimeoutException e) {
            Log.e(TAG,"Connection timeout");
            message.what=3;
        }catch (IOException e) {
            Log.e(TAG,"Connection fail");
            message.what=3;
        }
        FirstActivity.loginHandler.sendMessage(message);
    }
    private static void setInfo(Bundle bundle){
        Message message=new Message();
        try{
            jsonWriter = new JsonWriter(outputStreamWriter);
            jsonWriter.beginObject();
            jsonWriter.name("intent").value("editInfo");
            jsonWriter.name("name").value(bundle.getString("name"));
            jsonWriter.name("telNumber").value(bundle.getString("telNumber"));
            jsonWriter.name("email").value(bundle.getString("email"));
            jsonWriter.endObject();
            jsonWriter.flush();
            //接收消息
            Bundle bt=new Bundle();
            jsonReader=new JsonReader(inputStreamReader);
            jsonReader.beginObject();
            while(jsonReader.hasNext()){
                bt.putString(jsonReader.nextName(),jsonReader.nextString());
            }
            jsonReader.endObject();
            switch(bt.getString("result")){
                case "success":message.what=1;break;
                case "failure":message.what=2;break;
                default:break;
            }
        }catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"alter failed");
            message.what=3;
        }
        if(message.what==1){
            ServerData.email=bundle.getString("email");
            ServerData.name=bundle.getString("name");
            ServerData.telNumber=bundle.getString("telNumber");
            Message msg1=new Message();
            Message msg2=new Message();
            msg1.what=1;
            msg2.what=1;
            alterInformattionActivity.uiHandler.sendMessage(msg1);       //tell sucess
            mainActivity.uiHandler.sendMessage(msg2);                    //reset nav_header
        }else if(message.what==2){
            Message msg=new Message();
            msg.what=2;
            alterInformattionActivity.uiHandler.sendMessage(msg);       //tell failure
        }
    }
    private static void setPassword(String newPassword){
        Message message=new Message();
        try{
            jsonWriter = new JsonWriter(outputStreamWriter);
            jsonWriter.beginObject();
            jsonWriter.name("intent").value("editPassword");
            jsonWriter.name("password").value(newPassword);
            jsonWriter.endObject();
            jsonWriter.flush();
            //接收消息
            Bundle bt=new Bundle();
            jsonReader=new JsonReader(inputStreamReader);
            jsonReader.beginObject();
            while(jsonReader.hasNext()){
                bt.putString(jsonReader.nextName(),jsonReader.nextString());
            }
            jsonReader.endObject();
            switch(bt.getString("result")){
                case "success":message.what=1;break;
                case "failure":message.what=2;break;
                default:break;
            }
        }catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"alter failed");
            message.what=3;
        }
        if(message.what==1){
            ServerData.password=newPassword;
            Message msg=new Message();
            msg.what=1;
            alterPasswordActivity.uiHandler.sendMessage(msg);       //tell success
        }else if(message.what==2){
            Message msg=new Message();
            msg.what=2;
            alterPasswordActivity.uiHandler.sendMessage(msg);       //tell failure
        }
    }
}
