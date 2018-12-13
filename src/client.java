import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class client {

	Socket sc= null;
	PrintWriter pw=null;
	
	static client c;
	
	//�α���, ä�� ������ ����
	JFrame frame_login = new JFrame("Login");
	JFrame frame_chatting = new JFrame("Chatting");
	
	//�α��� ȭ�� ��� ����
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
	
	//ä�� ȭ�� ��� ����
	String ip, port, name, birthday;
	JLabel label_information = new JLabel("�� ����",JLabel.CENTER);
	JLabel label_inname = new JLabel();
	JLabel label_inbirthday = new JLabel();
	List list = new List();
	CheckboxGroup ch_group = new CheckboxGroup();
	Checkbox ch_all = new Checkbox("��ü��", ch_group,true);
	Checkbox ch_whisper = new Checkbox("�ӼӸ�", ch_group,false);
	JTextField textfield_text = new JTextField("�ؽ�Ʈ�� �Է��ϼ���.");
	JButton button_send = new JButton("����");
	JScrollPane scrollPane_chat = new JScrollPane();
	JTextArea textArea_chat = new JTextArea();
	JPanel p = new JPanel();
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JPanel p3 = new JPanel();
	JPanel p4 = new JPanel();
	JPanel p5 = new JPanel();
	
	//������ �� 
	int count =0;
	
	//ȭ�� 
	class Handler implements ActionListener,FocusListener{

		//ä�� â �޴��� ����
		MenuBar mb=new MenuBar();
		Menu mfile=new Menu("����");
		MenuItem mfile_save =new MenuItem("����");
		MenuItem mfile_exit =new MenuItem("����");
		Menu mhelp=new Menu("����");
		MenuItem mhelp_de =new MenuItem("������ ����");
		
		//�α��� â �����ϱ�
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
			
			//������ �ޱ�
			button_login.addActionListener(this);
			button_cancle.addActionListener(this);
			mfile_save.addActionListener(this);
			mfile_exit.addActionListener(this);
			mhelp_de.addActionListener(this);
			
			frame_login.setBounds(700,300,340,285);
			frame_login.setResizable(false);
			frame_login.setVisible(true);
		}
		
		//�α��� â grid ���̾ƿ����� ��ġ ���� �Լ�
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
		
		//ä�� â �����ϱ�
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
			
			list.add("*** ������ ��� ***");
			
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
			
			//�޴��� ����
			mfile.add(mfile_save); 
			mfile.addSeparator(); mfile.add(mfile_exit);
			mhelp.add(mhelp_de);
			mb.add(mfile); mb.add(mhelp); 
			frame_chatting.setMenuBar(mb);
			
			//������ �ޱ�
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
				label_inname.setText(" �г��� : " + name);
				label_inbirthday.setText(" ������� : " + birthday);
				connect(ip,port,name);
			}
			else if(e.getSource()==button_send||e.getSource()==textfield_text){
				sendprocess();
				textfield_text.setText("");
				textfield_text.requestFocus();
			}
			else if(e.getSource() == mfile_save) {
				filesave();
				JOptionPane.showMessageDialog(null, "����Ǿ����ϴ�.\n ������(chatting.txt)");
			}
			else if(e.getSource()==mfile_exit) {
				int result = JOptionPane.showConfirmDialog(null, "���� �����ðڽ��ϱ�?",
						"Confirm",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					removeName(name);
					System.exit(1);
				}
			}
			else if(e.getSource() == button_cancle) {
				int result = JOptionPane.showConfirmDialog(null, "���� �����ðڽ��ϱ�?",
						"Confirm",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					System.exit(1);
				}
			}
			else if(e.getSource() == mhelp_de) {
				JOptionPane.showMessageDialog(null, "������ : ������", "Developer",
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
				System.out.println("ä�ù濡 �����Ͽ����ϴ�.");

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
				System.out.println(name+"���� ����  : " + str2);
				}	
			else{
				try{
					String name=list.getSelectedItem();
					pw.println(("/s"+name+"-"+str2));
					System.out.println("����  : /s"+name+"-"+str2);
					textArea_chat.append(name+"�Բ� ������ �Ӹ� ���� "+str2+"\n");
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
		System.out.println(name+"���� �����ϼ̽��ϴ�.");
		list.add(name);
	}
	public void removeName(String name){
		System.out.println(name+"���� �����ϼ̽��ϴ�.");
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
		}catch(Exception ex){System.out.println("����");}
	}
	
}