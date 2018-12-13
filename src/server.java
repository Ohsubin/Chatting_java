import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class server {

	ServerSocket ssc;
	Socket sk;
	Vector v = new Vector();
	Vector nameV = new Vector();
	StringBuffer sb = new StringBuffer();

	public void serverStart() { //서버 시작
		try {
			ssc = new ServerSocket(8000);
			System.out.println("Server START!");
			while (true) {
				sk = ssc.accept();
			  System.out.println(sk.getInetAddress()+"님이 들옴");
				PlayThread pt = new PlayThread(this,sk);
				addThread(pt);
				pt.start();
			}
		} catch (Exception ex) {System.out.println("서버연결종료");}
	}

	public void addThread(PlayThread pt){
		v.addElement(pt);
	}
	
	public void removeThread(PlayThread pt){
		v.removeElement(pt);  
	}
	
	//--------이미 접속해 있는 client에게 이름 보내기--------
	public void broadCasting(String st){ 
		for(int i=0;i<v.size();i++){
			PlayThread pt = (PlayThread)v.elementAt(i);
			pt.sendMsg(st);
		}
	}
	
 public void broadCastingName(PlayThread pt){
	 
	 for(int i=0;i<nameV.size();i++){
		 sb.append("/f");
		 sb.append(nameV.get(i));
		 pt.sendMsg(sb.toString());
		 sb.setLength(0);
	 }
 }
 public void addNameVector(String st){
	 nameV.add(st);
 }
 
 public void removeCastingName(PlayThread pt){
	 for(int i=0;i<nameV.size();i++){
		 sb.append("/e");
		 sb.append(nameV.get(i));
		 pt.sendMsg(sb.toString());
		 sb.setLength(0);
	 }
 }
 public void removeNameVector(String st){
	 nameV.remove(st);
 }
//----------------------------------------------
 
	public static void main(String[] args) {
		server ser = new server();
		ser.serverStart();
	}
	
	public class PlayThread extends Thread {

		 Socket sk;
		 BufferedReader in;
		 OutputStream out;
		 server s3;
		 String str;
		 String name;

		public PlayThread(server s3,Socket s) {			
			this.s3 = s3;
			this.sk = s;			
		}

		public void run() {

			try {
				InputStreamReader isr = new InputStreamReader(sk.getInputStream());
				in = new BufferedReader(isr);
				out = sk.getOutputStream();
				PrintWriter pw = new PrintWriter(sk.getOutputStream(),true);
				name = in.readLine();
				
				s3.broadCasting("/f"+name);
				s3.broadCastingName(this);
				s3.addNameVector(name);
				
				s3.broadCasting("["+name+"]"+"님이 접속하셨습니다.");

				while(true){
					str = in.readLine();
					System.out.println(str);
					if(str==null) return;
					if(str.charAt(1)=='s'){
						String rname=str.substring(2,str.indexOf('-')).trim();
						for(int i=0; i<v.size(); i++){
							PlayThread pt = (PlayThread)v.elementAt(i);
							if(rname.equals(pt.name)){
								pt.sendMsg(name+"님의 귓속말 ▶▶ "+str.substring(str.indexOf('-')+1));
								break;
							}
						}
					}else{
					s3.broadCasting(""+name+"님의 말: "+str);
					}
				}
			}catch(Exception ex){
				s3.broadCasting("/e"+name);
				s3.removeCastingName(this);
				s3.removeNameVector(name);
				s3.broadCasting("["+name+"]"+"님이 퇴장하셨습니다.");
				s3.removeThread(this);
				System.out.println(sk.getInetAddress()+"님 퇴장");
			}
		} 
		
		 public void sendMsg(String st){
			 try{
			 PrintWriter pw = new PrintWriter(sk.getOutputStream(),true);
			 pw.println(st);
			 }catch(Exception ex){}
		 }
		
	}


}