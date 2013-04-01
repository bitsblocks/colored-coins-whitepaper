package webrunner.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;



public class ServerShutdownMonitor extends Thread 
{
	final static Logger __log = Logger.getLogger( ServerShutdownMonitor.class );		

	private ServerSocket _socket;
	private int          _port;
	
	private ServerShell   _sh;
	  
	public ServerShutdownMonitor( ServerShell sh, int port ) throws Exception  
	{
	   _sh = sh;
	   _port   = port;
		  
	   setDaemon( true );
	   setName( "ServerShutdownMonitor/"+_sh.getServerName() );
       _socket = new ServerSocket();
       _socket.setReuseAddress( true );
       _socket.bind( new InetSocketAddress( "127.0.0.1", port ), 1 ); // set backlog (maximum queue length for incoming connection) to 1
	}
	
	@Override
	public void run() 
	{
	  try
	  {
	     __log.info( "running server/" + _sh.getServerName() + " shutdown service on port " + _port );
         Socket accept = _socket.accept();
	     BufferedReader reader = new BufferedReader(new InputStreamReader( accept.getInputStream() ));
	     reader.readLine();
	     __log.info( "stopping server/" + _sh.getServerName() );
	     
	    // run on swt/gui thread
	     
	     //
		// _sh._display.syncExec( new Runnable() {
		 //  public void run()
		 //  {
	    //		_parent.cleanUpAndSayGoodBye();
		//  }} );
	     
	     ////////////
	     // fix/fix
	     // fix/fix
	     //   use onClose() or onCleanup or onExit or onGoodBye()
	     // and move display.syncExec to serverShell or derived class

	     // System.exit( 0 );  // exit and "abort" possible running initialization
	     
	     // _start._server.stop();
         //
	     // accept.close();
	     // _socket.close();
	     //
	     // exit and "abort" possible running initialization
	     // System.exit( 0 );
	  } 
	  catch( Exception ex ) 
	  {
		throw new RuntimeException(ex);
	  }
    } 
}	