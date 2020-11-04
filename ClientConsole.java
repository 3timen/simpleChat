// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  static Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String id, String host, int port) 
  {
    try 
    {
      client= new ChatClient(id, host, port, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
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
		 client.handleMessageFromClientUI(message);
		 return;
	 }
	 Scanner scanner = new Scanner(message);
	 String token = scanner.next();
	 if (token.equals("#quit")) {
		 client.quit();
		 return;
	 }
	 if (token.equals("#logoff")) {
		 try {
			 client.closeConnection();
		 } catch (Exception e) {
			 System.out.println("We are unable to log you off");
		 }
		 return;
	 }
	 if (token.equals("#login")) {
		 try {
			 client.openConnection();
		 } catch (Exception e) {
			 System.out.println("We are unable to log in");
		 }
		 return;
		 
	 }
	 if (token.equals("#gethost")) {
		 System.out.println("The host is: " + client.getHost());
		 return;
	 }
	 if (token.equals("#getport")) {
		 System.out.println("The port is: " + client.getPort());
		 return;
	 }
	 if (token.substring(0, 8).equals("#sethost")) {
		 String host = token.substring(token.indexOf('<')
				 					, token.indexOf('>'));
		 client.setHost(host);
		 return;
		 
	 }
	if (token.substring(0, 8).equals("#setport")) {
		 int port = Integer.parseInt(token.substring(token.indexOf('<')
					, token.indexOf('>')));
		 client.setPort(port);
		 return;			
	 }
	 
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    String id = "";
    int port;
    Scanner scanner = new Scanner(System.in);
    try {
    	id = args[0];
    } catch (ArrayIndexOutOfBoundsException e) {
    	System.out.println("Error - no login ID specified.");
    	return;
    }
    try
    {
      host = args[1];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    try
    {
      port = Integer.parseInt(args[2]);
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      port = DEFAULT_PORT;
    }


    ClientConsole chat= new ClientConsole(id, host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
