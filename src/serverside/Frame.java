package serverside;
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

public class Frame extends JFrame{
	
	private JTextArea ChatBox = new JTextArea(10,45);
    private JScrollPane myChatHistory = new JScrollPane(ChatBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private JTextArea UserText = new JTextArea(5,40);
    private JScrollPane myUserHistory=new JScrollPane(UserText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private JButton Send = new JButton("Send");
    private JButton Start = new JButton("Start Server!");
    private Server ChatServer;
    private InetAddress ServerAddress ;
     
    public Frame() {
        setTitle("Server");
        setSize(560,400);
        Container cp = getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(new JLabel("Server History"));
        cp.add(myChatHistory);
        cp.add(new JLabel("Chat Box : "));
        cp.add(myUserHistory);
        cp.add(Send);
        cp.add(Start);
         
         
         
        Start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 
                ChatServer=new Server();
                ChatServer.start();
                 
            }
        });
        Send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChatServer.SendMassage(ServerAddress.getHostName()+" < Server > "+UserText.getText());
            }
        });
         
         
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);   
    }
    
    public InetAddress getServerAddress() { 
    	return ServerAddress; 
    	}
    
    public JTextArea getChatBox(){
    	return ChatBox;
    }
    
    public Server getChatServer() {
    	return ChatServer;
    }
     
   
    public static void main(String[] args) {
        new Frame();
    }
}
