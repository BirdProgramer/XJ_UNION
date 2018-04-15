package com.ct.mywork;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class AlterInformattionActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText_name;
    private EditText editText_email;
    private EditText editText_telNumber;
    private Button button_cancell;
    private Button button_OK;
    public Handler uiHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:                                 //sucesss
                    Toast.makeText(AlterInformattionActivity.this,"Alter Success",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(AlterInformattionActivity.this,"Alter Fail",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocketThread.setAlterInformattionActivity(this);
        setContentView(R.layout.activity_alter_informattion);
        button_OK=findViewById(R.id.button_confirmAlter);
        button_OK.setOnClickListener(this);
        button_cancell=findViewById(R.id.button_cancellAlter);
        button_cancell.setOnClickListener(this);
        editText_email=findViewById(R.id.editText_email);
        editText_email.setText(ServerData.email);
        editText_name=findViewById(R.id.editText_name);
        editText_name.setText(ServerData.name);
        editText_telNumber=findViewById(R.id.editText_phoneNumber);
        editText_telNumber.setText(ServerData.telNumber);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_cancellAlter:
                finish();
                break;
            case R.id.button_confirmAlter:
                //upload information
                uploadInfo();
                break;
        }
    }
    private void uploadInfo(){
        String name=editText_name.getText().toString();
        String email=editText_email.getText().toString();
        String telNumber=editText_telNumber.getText().toString();
        Bundle bundle=new Bundle();
        bundle.putString("name",name);
        bundle.putString("email",email);
        bundle.putString("telNumber",telNumber);
        Message message=new Message();
        message.setData(bundle);
        message.what=2;
        SocketThread.socketHandler.sendMessage(message);
    }
}
