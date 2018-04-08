package com.ct.mywork;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class FirstActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText_account;
    private EditText editText_password;
    private Button button_login;
    private Button button_register;
    public Handler loginHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        editText_account=findViewById(R.id.editText_account);
        editText_password=findViewById(R.id.editText_password);
        button_login=findViewById(R.id.button_login);
        button_register=findViewById(R.id.button_register);
        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);
        loginHandler=new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 1:
                        Toast.makeText(FirstActivity.this, "Login succeed", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(FirstActivity.this, "Name or password is invalid", Toast.LENGTH_SHORT).show();break;
                    case 3:
                        Toast.makeText(FirstActivity.this, "Connection error", Toast.LENGTH_SHORT).show();break;
                    default:

                        Toast.makeText(FirstActivity.this, "Fuck", Toast.LENGTH_SHORT).show();break;
                }
            }
        };
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button_login:
                String account=editText_account.getText().toString();
                String password=editText_password.getText().toString();
                Toast.makeText(FirstActivity.this,"Logining",Toast.LENGTH_SHORT).show();
                LoginThread loginThread=new LoginThread(account,password);
                loginThread.start();
                break;
            case R.id.button_register:
                Intent intent=new Intent(FirstActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    class LoginThread extends Thread {
        private String number,password;
        public LoginThread(String number,String password){
            this.number=number;
            this.password=password;
        }
        public void run(){
            Message message=new Message();
            try{
                Socket socket = new Socket(ServerData.IP, ServerData.PORT);
                InputStreamReader in=new InputStreamReader(socket.getInputStream());
                OutputStreamWriter outputStreamWriter=new OutputStreamWriter(socket.getOutputStream(),"utf-8");
                outputStreamWriter.write("2");
                outputStreamWriter.flush();
                JsonWriter jsonWriter = new JsonWriter(outputStreamWriter);
                jsonWriter.beginObject();
                jsonWriter.name("number").value(number);
                jsonWriter.name("password").value(password);
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
                Log.e("登陆失败","登陆失败");
                message.what=3;
            }
            loginHandler.sendMessage(message);
        }
    }
}
