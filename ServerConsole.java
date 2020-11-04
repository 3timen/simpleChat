import java.util.Scanner;
import common.ChatIF;

public class ServerConsole implements ChatIF {
	
	final public static int DEFAULT_PORT =5555;
	private static EchoServer server;
	private Scanner console;
	
	public ServerConsole(int port) {
		this.server = new EchoServer(port, this);
		this.console = new Scanner(System.in);
	}
	
	public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = console.nextLine();
	        analyse(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	
	private void analyse(String message) {
		if (message.charAt(0) != '#') {
			 server.sendToAllClients("SERVER MSG > "+message);
			 display(message);
			 return;
		 }
		 Scanner scanner = new Scanner(message);
		 String token = scanner.next();
		 if (token.equals("#close")) {
			 try {
				 server.close();
			 } catch (Exception e) {
				 System.out.println("Error: could not close the server correctly");
			 }
			 return;
		 }
		 if (token.equals("#stop")) {
			 server.stopListening();
			 return;
		 }
		 if (token.equals("#quit")) {
			 System.exit(0);
			 return;
			 
		 }
		 if (token.equals("#start")) {
			 try {
				 server.listen();
			 } catch (Exception e) {
				 System.out.println("Error: could not start the server");
			 }
			 return;
		 }
		 if (token.equals("#getport")) {
			 System.out.println(server.getPort());
			 return;
		 }
		if (token.substring(0, 8).equals("#setport")) {
			 int port = Integer.parseInt(token.substring(token.indexOf('<')
						, token.indexOf('>')));
			 server.setPort(port);
			 return;			
		 }
	}
	
	public void display(String message) {
		System.out.println("SERVER MSG > "+ message);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port;
		try
	    {
	      port = Integer.parseInt(args[0]);
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      port = DEFAULT_PORT;
	    }
	    ServerConsole chat= new ServerConsole(port);
	    try 
	    {
	      server.listen(); //Start listening for connections
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	    chat.accept();  //Wait for console data
	}

}
