import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class client {

	Socket sc= null;
	PrintWriter pw=null;
	
	static client c;
	
	//로그인, 채팅 프레임 생성
	JFrame frame_login = new JFrame("Login");
	JFrame frame_chatting = new JFrame("Chatting");
	
	//로그인 화면 요소 선언
	GridBagLayout gridlayout = new GridBagLayout();
	JLabel label_ip = new JLabel("IP : ", JLabel.CENTER);
	JLabel label_port = new JLabel("PORT : ", JLabel.CENTER);
	JLabel label_name = new JLabel("NICK NAME : ", JLabel.CENTER);
	JLabel label_birthday = new JLabel("BIRTHDAY : ", JLabel.CENTER);
	TextField textfield_ip = new TextField(20);
	TextField textfield_port = new TextField(10);
	TextField textfield_name = new TextField(10);
	TextField textfield_birthday = new TextField("0000.00.00");
	JButton button_login = new JButton("Login");
	JButton button_cancle = new JButton("Cancle");
	
	//채팅 화면 요소 선언
	String ip, port, name, birthday;
	JLabel label_information = new JLabel("내 정보",JLabel.CENTER);
	JLabel label_inname = new JLabel();
	JLabel label_inbirthday = new JLabel();
	List list = new List();
	CheckboxGroup ch_group = new CheckboxGroup();
	Checkbox ch_all = new Checkbox("전체말", ch_group,true);
	Checkbox ch_whisper = new Checkbox("귓속말", ch_group,false);
	JTextField textfield_text = new JTextField("텍스트를 입력하세요.");
	JButton button_send = new JButton("전송");
	JScrollPane scrollPane_chat = new JScrollPane();
	JTextArea textArea_chat = new JTextArea();
	JPanel p = new JPanel();
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JPanel p3 = new JPanel();
	JPanel p4 = new JPanel();
	JPanel p5 = new JPanel();
	
	//접속자 수 
	int count =0;
	
	//화면 
	class Handler implements ActionListener,FocusListener{

		//채팅 창 메뉴바 수정
		MenuBar mb=new MenuBar();
		Menu mfile=new Menu("파일");
		MenuItem mfile_save =new MenuItem("저장");
		MenuItem mfile_exit =new MenuItem("종료");
		Menu mhelp=new Menu("도움말");
		MenuItem mhelp_de =new MenuItem("개발자 정보");
		
		//로그인 창 구성하기
		public void Frame_Login() {
			frame_login.setTitle("Login");
			frame_login.setLayout(gridlayout);
			frame_login.setBackground(Color.CYAN);
			
			gbinsert(label_ip, 2,5,1,1); 
			gbinsert(textfield_ip,3,5,3,1); 
			gbinsert(label_port, 2,6,1,1); 
			gbinsert(textfield_port, 3,6,3,1); 
			gbinsert(label_name, 2,7,1,1); 
			gbinsert(textfield_name, 3,7,3,1); 
			gbinsert(label_birthday, 2,8,1,1); 
			gbinsert(textfield_birthday, 3,8,3,1); 
			gbinsert(button_login, 3,9,1,1);
			gbinsert(button_cancle, 4,9,1,1);
			
			//리스너 달기
			button_login.addActionListener(this);
			button_cancle.addActionListener(this);
			mfile_save.addActionListener(this);
			mfile_exit.addActionListener(this);
			mhelp_de.addActionListener(this);
			
			frame_login.setBounds(700,300,340,285);
			frame_login.setResizable(false);
			frame_login.setVisible(true);
		}
		
		//로그인 창 grid 레이아웃으로 위치 설정 함수
		public void gbinsert(Component c, int x, int y, int w, int h){
		       GridBagConstraints gbc = new GridBagConstraints();
		       gbc.fill= GridBagConstraints.BOTH;
		       gbc.gridx = x; 
		       gbc.gridy = y;
		       gbc.gridwidth = w; 
		       gbc.gridheight = h;
		       gbc.ipadx=10;
		       gbc.anchor = GridBagConstraints.SOUTH;
		       gbc.insets = new Insets(2, 2, 0, 4);
		       gridlayout.setConstraints(c,gbc); 
		       frame_login.add(c);
		       
		       frame_login.pack();
				
		       frame_login.addWindowListener(new WindowAdapter( ) {
					public void windowClosing(WindowEvent we){ System.exit(1) ; }
				}) ;
			
		       frame_login.setBounds(700,300,340,285);
		       frame_login.setResizable(false);
		       frame_login.setVisible(true);
			 }
		
		//채팅 창 구성하기
		public void Frame_Chatting() {
			frame_chatting.setTitle("Chatting");
			frame_chatting.setBackground(Color.CYAN);
			
			p2.setLayout(new GridLayout(3, 1));
			p2.add(label_information);
			p2.add(label_inname);
			p2.add(label_inbirthday);
			
			p3.setLayout(new GridLayout(1, 2));
			p3.add(ch_all);
			p3.add(ch_whisper);
			
			p5.setLayout(new GridLayout(1, 2));
			p5.add(textfield_text);
			p5.add(button_send);
			
			scrollPane_chat.setViewportView(textArea_chat);
			
			list.add("*** 접속자 목록 ***");
			
			p1.setLayout(new BorderLayout());
			p1.add(p2,BorderLayout.NORTH);
			p1.add(list, BorderLayout.CENTER);
			p1.add(p3, BorderLayout.SOUTH);
			
			p4.setLayout(new BorderLayout());
			p4.add(scrollPane_chat,BorderLayout.CENTER);
			p4.add(p5, BorderLayout.SOUTH);
			
			p.setLayout(new BorderLayout());
			p.add(p1,BorderLayout.WEST);
			p.add(p4, BorderLayout.CENTER);
			
			frame_chatting.add(p);
			
			//메뉴바 설정
			mfile.add(mfile_save); 
			mfile.addSeparator(); mfile.add(mfile_exit);
			mhelp.add(mhelp_de);
			mb.add(mfile); mb.add(mhelp); 
			frame_chatting.setMenuBar(mb);
			
			//리스너 달기
			textfield_text.addActionListener(this);
			textfield_text.addFocusListener(this);
			button_send.addActionListener(this);
		
			frame_chatting.setResizable(false);
			frame_chatting.setBounds(300,200, 650, 500) ;
			frame_chatting.setVisible(true) ;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == button_login) {
				frame_login.setVisible(false);
				frame_login.dispose();
				ip = textfield_ip.getText();
				port = textfield_port.getText();
				name = textfield_name.getText();
				birthday = textfield_birthday.getText();
				Frame_Chatting();
				label_inname.setText(" 닉네임 : " + name);
				label_inbirthday.setText(" 생년월일 : " + birthday);
				connect(ip,port,name);
			}
			else if(e.getSource()==button_send||e.getSource()==textfield_text){
				sendprocess();
				textfield_text.setText("");
				textfield_text.requestFocus();
			}
			else if(e.getSource() == mfile_save) {
				filesave();
				JOptionPane.showMessageDialog(null, "저장되었습니다.\n 저장경로(chatting.txt)");
			}
			else if(e.getSource()==mfile_exit) {
				int result = JOptionPane.showConfirmDialog(null, "정말 나가시겠습니까?",
						"Confirm",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					removeName(name);
					System.exit(1);
				}
			}
			else if(e.getSource() == button_cancle) {
				int result = JOptionPane.showConfirmDialog(null, "정말 나가시겠습니까?",
						"Confirm",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					System.exit(1);
				}
			}
			else if(e.getSource() == mhelp_de) {
				JOptionPane.showMessageDialog(null, "제작자 : 오수빈", "Developer",
						JOptionPane.INFORMATION_MESSAGE);
			}
	
		}
		
		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub
			textfield_text.setText("");
		}

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		public void connect(String ip, String port, String name){
			try{
				sc = new Socket(ip,Integer.parseInt(port));
				System.out.println("채팅방에 접속하였습니다.");

				ClientThread th = new ClientThread(sc,c);
				th.start();
				
				pw=new PrintWriter(new OutputStreamWriter(sc.getOutputStream()),true);
				pw.println(name);
			}catch(Exception ex){System.out.println(ex.getMessage());}
		}
		
		public void sendprocess(){

			try{
				String str2=textfield_text.getText();
			if(ch_all.getState()==true){
				pw.println(str2);
				System.out.println(name+"님이 보냄  : " + str2);
				}	
			else{
				try{
					String name=list.getSelectedItem();
					pw.println(("/s"+name+"-"+str2));
					System.out.println("보냄  : /s"+name+"-"+str2);
					textArea_chat.append(name+"님께 보내는 귓말 ▶▶ "+str2+"\n");
				}catch(Exception ex){textArea_chat.append(ex.getMessage());}
			}
		}catch(Exception ex){}
		}

		public void filesave(){
			try{
			FileWriter fw=new FileWriter("chatting.txt");
			BufferedWriter out = new BufferedWriter(fw);
			String data="";
			while(true){
				data=textArea_chat.getText();
				if(data==null) break;
				
				out.write(data); 
				out.close();
			}
			}catch(Exception ex){}
		}
	}
	

	public void addName(String name){
		System.out.println(name+"님이 입장하셨습니다.");
		list.add(name);
	}
	public void removeName(String name){
		System.out.println(name+"님이 퇴장하셨습니다.");
		list.remove(name);
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		c = new client();
		client.Handler hd=c.new Handler();
		hd.Frame_Login();
	}

}

class ClientThread extends Thread{
	Socket sc=null;
	BufferedReader br=null;
	String str;
	String name;
	client c;
	
	public ClientThread(Socket sc, client c) {
		this.sc=sc;
		this.c=c;
	}
	@Override
	public void run() {
		try{
			br= new BufferedReader(new InputStreamReader(sc.getInputStream()));
			while(true){
				str=br.readLine();
				if(str.indexOf("/f")==0){
					name=str.substring(2);
					c.count++;
					c.addName(name);	
				}
				else if(str.indexOf("/e")==0){
					name=str.substring(2);
					c.count--;
					c.removeName(name);
				}
				else{
					c.textArea_chat.append(str+"\n");
				}
			}
		}catch(Exception ex){System.out.println("에러");}
	}
	
}