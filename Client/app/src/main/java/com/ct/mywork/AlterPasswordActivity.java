package com.ct.mywork;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class AlterPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText_oldPassword;
    private EditText editText_newPassword;
    private EditText editText_againPassword;
    private Button button_cancell;
    private Button button_OK;
    public Handler uiHandler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:                                 //sucesss
                    Toast.makeText(AlterPasswordActivity.this,"Alter Success",Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent=new Intent(AlterPasswordActivity.this,FirstActivity.class);
                    Toast.makeText(AlterPasswordActivity.this,"Please Login Again",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    break;
                case 2:
                    Toast.makeText(AlterPasswordActivity.this,"Alter Fail",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_password);
        SocketThread.setAlterPasswordActivity(this);
        editText_oldPassword=findViewById(R.id.editText_oldPassword);
        editText_newPassword=findViewById(R.id.editText_newPassword);
        editText_againPassword=findViewById(R.id.editText_againPassword);
        button_cancell=findViewById(R.id.button_cancellAlter);
        button_cancell.setOnClickListener(this);
        button_OK=findViewById(R.id.button_confirmAlter);
        button_OK.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_cancellAlter:
                finish();
                break;
            case R.id.button_confirmAlter:
                String oldPassword=editText_oldPassword.getText().toString();
                String newPassword=editText_newPassword.getText().toString();
                String agaginPassword=editText_againPassword.getText().toString();
                if(!oldPassword.equals(ServerData.password)){
                    Toast.makeText(AlterPasswordActivity.this,"Old Password is Invalid",Toast.LENGTH_SHORT).show();
                    break;
                }else if(newPassword.equals("")){
                    Toast.makeText(AlterPasswordActivity.this,"New Password is Invalid",Toast.LENGTH_SHORT).show();
                    break;
                }else if(!newPassword.equals(agaginPassword)){
                    Toast.makeText(AlterPasswordActivity.this,"Please Check New Password Again",Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    upload(newPassword);
                    break;
                }
            default:
                break;
        }
    }
    private void upload(String newPassword){
        Message message=new Message();
        message.what=3;
        Bundle bundle=new Bundle();
        bundle.putString("password",newPassword);
        message.setData(bundle);
        SocketThread.socketHandler.sendMessage(message);
    }
}
