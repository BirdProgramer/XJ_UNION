package com.ct.mywork;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FirstActivity extends AppCompatActivity implements View.OnClickListener{
    private static String TAG="FirstActivity";
    private EditText editText_account;
    private EditText editText_password;
    private Button button_login;
    private Button button_register;
    public static Handler loginHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        SocketThread.setFirstActivity(this);
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
                        Bundle bundle=msg.getData();
                        ServerData.name=bundle.getString("name");
                        ServerData.email=bundle.getString("email");
                        ServerData.telNumber=bundle.getString("telNumber");
                        ServerData.credit=bundle.getString("credit");
                        ServerData.password=editText_password.getText().toString();
                        finish();
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(FirstActivity.this, "Name or password is invalid", Toast.LENGTH_SHORT).show();break;
                    case 3:
                        Toast.makeText(FirstActivity.this, "Connection timeout", Toast.LENGTH_SHORT).show();break;
                    default:
                        Toast.makeText(FirstActivity.this, "ERROR", Toast.LENGTH_SHORT).show();break;
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
                Bundle bundle=new Bundle();
                bundle.putString("account",account);
                bundle.putString("password",password);
                Message message=new Message();
                message.what=1;
                message.setData(bundle);
                SocketThread.socketHandler.sendMessage(message);
                Log.e(TAG,"SendMessage");
                break;
            case R.id.button_register:
                Intent intent=new Intent(FirstActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
