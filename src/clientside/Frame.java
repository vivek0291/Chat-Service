package clientside;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Frame extends JFrame {
	private JTextArea ChatBox=new JTextArea(10,45);
    private JScrollPane myChatHistory=new JScrollPane(ChatBox,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private JTextArea UserText = new JTextArea(5,40);
    private JScrollPane myUserHistory=new JScrollPane(UserText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private JButton Send = new JButton("Send");
    private JButton Start = new JButton("Connect");
    private Client ChatClient;
    private ReadThread myRead=new ReadThread();
    private JTextField Server=new JTextField(20);
    private JLabel myLabel=new JLabel("Server Name :");
    private JTextField User=new JTextField(20);
    private String ServerName;
    private String UserName;
     
     
    public Frame() {
        setResizable(false);
        setTitle("Client");
        setSize(560,400);
        Container cp=getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(new JLabel("Chat History"));
        cp.add(myChatHistory);
        cp.add(new JLabel("Chat Box : "));
        cp.add(myUserHistory);
        cp.add(Send);
        cp.add(Start);
        cp.add(myLabel);
        cp.add(Server);
        cp.add(User);
        Send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(ChatClient!=null) {
                     
                    System.out.println(UserText.getText());
                    ChatClient.SendMassage(UserText.getText());
                }
            }
        });
        Start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChatClient=new Client();
                ChatClient.start();
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
         
         
    }
    
    public String getUser(){
    	return UserName;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        new Frame();
    }

	public String getServer() {
		// TODO Auto-generated method stub
		return ServerName;
	}

	public JTextArea getChatBox() {
		// TODO Auto-generated method stub
		return ChatBox;
	}

	public Client getChatClient() {
		// TODO Auto-generated method stub
		return ChatClient;
	}

	public ReadThread getReadThread() {
		// TODO Auto-generated method stub
		return myRead;
	}
}
