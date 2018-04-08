package com.ct.mywork;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText_name;
    private EditText editText_number;
    private EditText editText_email;
    private EditText editText_phoneNumber;
    private EditText editText_password;
    private EditText editText_confirmpassword;
    private Button button_cancellRegister;
    private Button button_confirmReguster;
    private Handler registerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText_name=findViewById(R.id.editText_name);
        editText_number=findViewById(R.id.editText_number);
        editText_phoneNumber=findViewById(R.id.editText_phoneNumber);
        editText_email=findViewById(R.id.editText_email);
        editText_password=findViewById(R.id.editText_password);
        editText_confirmpassword=findViewById(R.id.editText_confirmPassword);
        button_confirmReguster=findViewById(R.id.button_confirmRegister);
        button_cancellRegister=findViewById(R.id.button_cancellRegister);
        button_cancellRegister.setOnClickListener(this);
        button_confirmReguster.setOnClickListener(this);
        registerHandler=new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 1:
                        Toast.makeText(RegisterActivity.this, "Register succeed", Toast.LENGTH_SHORT).show();finish();break;
                    case 2:
                        Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();break;
                    case 3:
                        Toast.makeText(RegisterActivity.this, "Connection error", Toast.LENGTH_SHORT).show();break;
                    default:

                        Toast.makeText(RegisterActivity.this, "Fuck", Toast.LENGTH_SHORT).show();break;
                }
            }
        };
    }
    public void onClick(View v){                    //点击事件
        switch(v.getId()){
            case R.id.button_cancellRegister:
                finish();
                break;
            case R.id.button_confirmRegister:
                confirmRegister();
                break;
            default:
                break;
        }
    }
    private void confirmRegister(){                                     //处理注册信息
        String name=editText_name.getText().toString();
        String number=editText_number.getText().toString();
        String telNumber=editText_phoneNumber.getText().toString();
        String email=editText_email.getText().toString();
        String password=editText_password.getText().toString();
        String confirmPassword=editText_confirmpassword.getText().toString();
        if(!checkName(name)){
            Toast.makeText(RegisterActivity.this,"Name is invalid",Toast.LENGTH_SHORT).show();
        }else if(!checkNumber(number)){
            Toast.makeText(RegisterActivity.this,"Number is invalid",Toast.LENGTH_SHORT).show();
        }else if(!checkPhoneNumber(telNumber)){
            Toast.makeText(RegisterActivity.this,"PhoneNumber is invalid",Toast.LENGTH_SHORT).show();
        }else if(!checkEmail(email)){
            Toast.makeText(RegisterActivity.this,"Email is invalid",Toast.LENGTH_SHORT).show();
        }else if(!checkPassword(password)){
            Toast.makeText(RegisterActivity.this,"Password is invalid",Toast.LENGTH_SHORT).show();
        }else if(!checkConfirmPassword(password,confirmPassword)){
            Toast.makeText(RegisterActivity.this,"Please confirm password",Toast.LENGTH_SHORT).show();
        }else{
            RegisterThread registerThread=new RegisterThread(name,password,number,telNumber,email);
            registerThread.start();
            Toast.makeText(RegisterActivity.this, "Registering", Toast.LENGTH_SHORT).show();
        }
    }
    //检查各输入信息
    private boolean checkName(String  s){
        if(s.equals("")){
            return false;
        }else{
            return true;
        }
    }
    private boolean checkNumber(String s){
        if(s.equals("")){
            return false;
        }else{
            return true;
        }

    }
    private boolean checkPhoneNumber(String s){
        if(s.equals("")){
            return false;
        }else{
            return true;
        }
    }
    private boolean checkEmail(String s){
        if(s.equals("")|| !s.contains("@")){
            return false;
        }else{
            return true;
        }
    }
    private boolean checkPassword(String s){
        if(s.equals("")){
            return false;
        }else{
            return true;
        }
    }
    private boolean checkConfirmPassword(String p,String cp){
        if(cp.equals("")|| !cp.equals(p)){
            return false;
        }else{
            return true;
        }
    }

    class RegisterThread extends Thread {
        private String name,password,number,telnumber,email;
        public RegisterThread(String name,String password,String number,String telnumber,String email){
            this.name=name;
            this.password=password;
            this.number=number;
            this.telnumber=telnumber;
            this.email=email;
        }
        public void run(){
            Message message=new Message();
            try{
                Socket socket = new Socket(ServerData.IP, ServerData.PORT);
                InputStreamReader in=new InputStreamReader(socket.getInputStream());
                OutputStreamWriter outputStreamWriter=new OutputStreamWriter(socket.getOutputStream(),"utf-8");
                outputStreamWriter.write("1");
                outputStreamWriter.flush();
                Log.e("outputStream",name);
                JsonWriter jsonWriter = new JsonWriter(outputStreamWriter);
                jsonWriter.beginObject();
                jsonWriter.name("name").value(name);
                jsonWriter.name("password").value(password);
                jsonWriter.name("number").value(number);
                jsonWriter.name("telnumber").value(telnumber);
                jsonWriter.name("email").value(email);
                jsonWriter.endObject();
                outputStreamWriter.flush();
                //接收消息
                char c=(char)(in.read());
                switch(c){
                    case 's':message.what=1;break;
                    case 'f':message.what=2;break;
                    default:break;
                }
                jsonWriter.close();
                outputStreamWriter.close();
                in.close();
                socket.close();
            }catch (IOException e) {
                e.printStackTrace();
                Log.e("注册失败",number+"注册失败");
                message.what=3;
            }
            registerHandler.sendMessage(message);
        }
    }
}
