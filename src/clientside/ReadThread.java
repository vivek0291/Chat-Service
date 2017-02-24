package clientside;

public class ReadThread extends Thread{
	
	public void run() {
        ChatClient.ReadMassage();
    }
}
