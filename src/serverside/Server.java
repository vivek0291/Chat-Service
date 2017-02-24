package serverside;
import java.util.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import javax.swing.JTextArea;

public class Server extends Thread{
	private Frame serverFrame = new Frame();
	private static final int PORT=9999;
    private LinkedList Clients;
    private ByteBuffer ReadBuffer;
    private ByteBuffer WriteBuffer;
    public  ServerSocketChannel SSChan;
    private Selector ReaderSelector;
    private CharsetDecoder asciiDecoder;
    private InetAddress ServerAddress = serverFrame.getServerAddress();
    private JTextArea ChatBox = serverFrame.getChatBox();
    private Server ChatServer = serverFrame.getChatServer();
     
    public Server() {
        Clients=new LinkedList();
        ReadBuffer=ByteBuffer.allocateDirect(300);
        WriteBuffer=ByteBuffer.allocateDirect(300);
        asciiDecoder = Charset.forName( "US-ASCII").newDecoder();
    }
     
    public void InitServer() {
        try {
            SSChan=ServerSocketChannel.open();
            SSChan.configureBlocking(false);
            ServerAddress=InetAddress.getLocalHost();
            System.out.println(ServerAddress.toString());
             
            SSChan.socket().bind(new InetSocketAddress(ServerAddress,PORT));
             
            ReaderSelector=Selector.open();
            ChatBox.setText(ServerAddress.getHostName()+"<Server> Started. \n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void run() {
        InitServer();
         
        while(true) {
            acceptNewConnection();
             
            ReadMassage();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
             
             
        }
    }
     
    public void acceptNewConnection() {
        SocketChannel newClient;
        try {
             
            while ((newClient = SSChan.accept()) != null) {
                ChatServer.addClient(newClient);
                 
                sendBroadcastMessage(newClient,"Login from: " +newClient.socket().getInetAddress());
                 
                SendMassage(newClient,ServerAddress.getHostName()+"<server> welcome you !\n Note :To exit" +
                        " from server write 'quit' .\n");
            }
             
        } catch (IOException e) {
            e.printStackTrace();
        }
         
    }
     
    public void addClient(SocketChannel newClient) {
        Clients.add(newClient);
        try {
            newClient.configureBlocking(false);
            newClient.register(ReaderSelector,SelectionKey.OP_READ,new StringBuffer());
             
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
     
    public void SendMassage(SocketChannel client ,String messg) {
        prepareBuffer(messg);
        channelWrite(client);
    }
    public void SendMassage(String massg) {
        if(Clients.size()>0) {
            for(int i=0;i<Clients.size();i++) {
                SocketChannel client=(SocketChannel)Clients.get(i);
                SendMassage(client,massg);
            }
        }
    }
     
     
    public void prepareBuffer(String massg) {
        WriteBuffer.clear();
        WriteBuffer.put(massg.getBytes());
        WriteBuffer.putChar('\n');
        WriteBuffer.flip();
    }
     
    public void channelWrite(SocketChannel client) {
        long num=0;
        long len=WriteBuffer.remaining();
        while(num!=len) {
            try {
                num+=client.write(WriteBuffer);
                 
                Thread.sleep(5);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch(InterruptedException ex) {
                 
            }
        }
        WriteBuffer.rewind();
    }
     
    public void sendBroadcastMessage(SocketChannel client,String mesg) {
        prepareBuffer(mesg);
        Iterator i = Clients.iterator();
        while (i.hasNext()) {
            SocketChannel channel = (SocketChannel)i.next();
            if (channel != client) {
                channelWrite(channel);
            }
        }
    }
    public void ReadMassage() {
        try {
             
            ReaderSelector.selectNow();
            Set readkeys=ReaderSelector.selectedKeys();
            Iterator iter=readkeys.iterator();
            while(iter.hasNext()) {
                SelectionKey key=(SelectionKey) iter.next();
                iter.remove();
                 
                SocketChannel client=(SocketChannel)key.channel();
                ReadBuffer.clear();
                 
                long num=client.read(ReadBuffer);
                 
                if(num==-1) {
                    client.close();
                    Clients.remove(client);
                    sendBroadcastMessage(client,"logout: " +
                            client.socket().getInetAddress());
                     
                } else {
                     
                    StringBuffer str=(StringBuffer)key.attachment();
                    ReadBuffer.flip();
                    String data= asciiDecoder.decode(ReadBuffer).toString();
                    ReadBuffer.clear();
                     
                    str.append(data);
                     
                    String line = str.toString();
                    if ((line.indexOf("\n") != -1) || (line.indexOf("\r") != -1)) {
                        line = line.trim();
                        System.out.println(line);
                         
                        if (line.endsWith("quit")) {
                            client.close();
                             
                            Clients.remove(client);
                             
                            ChatBox.append("Logout: " + client.socket().getInetAddress());
                             
                            sendBroadcastMessage(client,"Logout: "
                                    + client.socket().getInetAddress());
                            ChatBox.append(""+'\n');
                        } else {
                            ChatBox.append(client.socket().getInetAddress() + ": " + line);
                             
                            sendBroadcastMessage(client,client.socket().getInetAddress()
                            + ": " + line);
                             
                            ChatBox.append(""+'\n');
                             
                            str.delete(0,str.length());
                        }
                    }
                }
            }
             
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}