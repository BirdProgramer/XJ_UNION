
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static final int port=30000;
	public static void main(String[] args){
		// TODO Auto-generated method stub
		ServerSocket ss;
		try {
			ss = new ServerSocket(20000);
			while(true) {
				Socket s=ss.accept();
				System.out.println("Starting new thread");
				new ServerThread(s).start(); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}