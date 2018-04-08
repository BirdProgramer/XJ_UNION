import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import net.sf.json.*;
public class ServerThread extends Thread {
	private Socket socket;
	private InputStreamReader in;
	private OutputStreamWriter out;
	boolean flag;
	public ServerThread(Socket s) {
		this.socket=s;
		flag=true;
	}
	public void run() {
		char type;
		try {
			in=new InputStreamReader(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(flag){
			try {
				type=(char) in.read();
				switch(type) {
				case '1':register();break;			//1 代表注册
				case '2':login();break;				//2 代表登陆
				case '3':editInfo();break;			//3 代表编辑个人信息
				case '4':sendTask();break;			//4 代表发布任务
				case '5':acceptTask();break;		//5 代表接受任务
				default:break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void register() {
		try {
			char[] info=new char[1024];
			in.read(info);
			String str=String.valueOf(info);
			JSONObject json=JSONObject.fromObject(str);
			String name=json.getString("name");
			String number=json.getString("number");
			String email=json.getString("email");
			String telnumber=json.getString("telnumber");
			String password=json.getString("password");
			String sql = "insert into "+MySQL.tname+" values(\""+number+"\",\""+name+"\",\""+telnumber+"\",\""+email+"\",\""+password+"\")";
			out=new OutputStreamWriter(socket.getOutputStream());
			System.out.print(sql);
			if(MySQL.insert(sql)) {
				out.write("s");
				out.flush();
				System.out.println("out closed");
				out.close();
				in.close();
				socket.close();
				flag=false;
			}else {
				out.write("f");	
				out.close();
				socket.close();
				flag=false;
			}
		}catch(IOException e) {
			flag=false;
			e.printStackTrace();
		}
		
	}
	private boolean login() {
		char[] info=new char[1024];
		try {
			in.read(info);
			String str=String.valueOf(info);
			JSONObject json=JSONObject.fromObject(str);
			String number=json.getString("number");
			String password=json.getString("password");
			String sql="select * from "+MySQL.tname+" where unumber=\""+number+"\"";
			String[] requests= {"upassword"};
			String[] answers=new String[1];
			out=new OutputStreamWriter(socket.getOutputStream());
			if(MySQL.query(sql,requests,answers)&&answers[0].equals(password)) {
				out.write('s');
				out.flush();
			}else {
				out.write('f');
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private boolean editInfo() {
		return false;
	}
	private boolean sendTask() {
		return false;
	}
	private boolean acceptTask() {
		return false;
	}
}
