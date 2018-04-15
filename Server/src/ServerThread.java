import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import net.sf.json.*;

//name account telNumber email password credit receivedTaskId distributedTaskId
public class ServerThread extends Thread {
	private Socket socket;
	private InputStreamReader in;
	private OutputStreamWriter out;
	private JSONObject json;
	private String account;
	boolean flag;
	public ServerThread(Socket s) {
		this.socket=s;
		flag=true;
	}
	public void run() {
		char[] chars=new char[1024];
		String newstring,oldstring="";
		try {
			in=new InputStreamReader(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(flag){
			try {
				socket.setSoTimeout(12*1000);
				in.read(chars);
				newstring=String.valueOf(chars);
				if(newstring.equals(oldstring)) {
					continue;
				}else {
					json=JSONObject.fromObject(newstring);
					switch(json.getString("intent")) {
						case "register":register(json);break;			//代表注册
						case "login":login(json);break;				//代表登陆
						case "editInfo":editInfo(json);break;			//代表编辑个人信息
						case "editPassword":editPassword(json);break;	//代表修改密码
						case "sendTask":sendTask();break;			//代表发布任务
						case "acceptTask":acceptTask();break;		//代表接受任务
						case "check":System.out.println("check");newstring="";break;
						default:break;
					}
					oldstring=newstring;
				}
			} catch(SocketTimeoutException e1) {
				System.out.println("Time out");
				break;
			}catch (IOException e2) {
				// TODO Auto-generated catch block
				break;
			}
		}
		System.out.print(account+" log out");
	}
	private void register(JSONObject json) {
		String name=json.getString("name");
		String account=json.getString("account");
		String email=json.getString("email");
		String telNumber=json.getString("telNumber");
		String password=json.getString("password");
		String sql = "insert into "+MySQL.tname+" (name,account,telNumber,email,password) "
				+ "values(\""+name+"\",\""+account+"\",\""+telNumber+"\",\""+email+"\",\""+password+"\")";
		try{
			out=new OutputStreamWriter(socket.getOutputStream());
			if(MySQL.insert(sql)) {
				json=new JSONObject();
				json.put("result", "success");
				out.write(json.toString());
				out.flush();
				out.close();
				in.close();
				socket.close();
				flag=false;
			}else {
				json=new JSONObject();
				json.put("result", "failure");
				out.write(json.toString());
				out.flush();
				out.close();
				socket.close();
				flag=false;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		flag=false;		
	}
	private void login(JSONObject json) {
		try {
			String account=json.getString("account");
			String password=json.getString("password");
			String sql="select * from "+MySQL.tname+" where account=\""+account+"\"";
			String[] requests= {"password","name","email","credit","telNumber","credit"};
			String[] answers=new String[requests.length];
			out=new OutputStreamWriter(socket.getOutputStream());
			if(MySQL.query(sql,requests,answers) && answers[0].equals(password)) {
				this.account=account;
				json=new JSONObject();
				json.put("result", "success");
				json.put("name", answers[1]);
				json.put("email", answers[2]);
				json.put("credit", answers[3]);
				json.put("telNumber", answers[4]);
				out.write(json.toString());
				out.flush();
			}else {
				json=new JSONObject();
				json.put("result", "failure");
				out.write(json.toString());
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void editInfo(JSONObject json) {
		try {
			String name=json.getString("name");
			String email=json.getString("email");
			String telNumber=json.getString("telNumber");
			String sql="update "+MySQL.tname+" set name=\""+name+"\",email=\""+email+"\",telNumber=\""
					+telNumber+"\" where account=\""+account+"\"";
			//out=new OutputStreamWriter(socket.getOutputStream());
			System.out.println(sql);
			if(MySQL.update(sql)) {
				json=new JSONObject();
				json.put("result", "success");
				out.write(json.toString());
				out.flush();
			}else {
				json=new JSONObject();
				json.put("result", "failure");
				out.write(json.toString());
				out.flush();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void editPassword(JSONObject json) {
		try {
			String password=json.getString("password");
			String sql="update "+MySQL.tname+" set password=\""+password+"\" where account=\""+account+"\"";
			//out=new OutputStreamWriter(socket.getOutputStream());
			System.out.println(sql);
			if(MySQL.update(sql)) {
				json=new JSONObject();
				json.put("result", "success");
				out.write(json.toString());
				out.flush();
			}else {
				json=new JSONObject();
				json.put("result", "failure");
				out.write(json.toString());
				out.flush();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void sendTask() {
		
	}
	private void acceptTask() {
		
	}
}
