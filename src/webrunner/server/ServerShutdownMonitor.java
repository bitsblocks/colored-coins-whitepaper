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
	
	private ServerMan   _man;
	  
	public ServerShutdownMonitor( ServerMan man ) throws Exception  
	{
	   _man    = man;
	   _port   = _man._shutdownPort;
		  
	   setDaemon( true );
	   setName( "ServerShutdownMonitor/"+_man.getServerName() );
       _socket = new ServerSocket();
       _socket.setReuseAddress( true );
       _socket.bind( new InetSocketAddress( "127.0.0.1", _port ), 1 ); // set backlog (maximum queue length for incoming connection) to 1
	}
	
	@Override
	public void run() 
	{
	  try
	  {
	     __log.info( "running server (" + _man.getServerName() + ") shutdown service on port " + _port );
         Socket accept = _socket.accept();
	     BufferedReader reader = new BufferedReader(new InputStreamReader( accept.getInputStream() ));
	     reader.readLine();
	     __log.info( "stopping server (" + _man.getServerName() + ")" );
	  
	     _man.onExit();
	  } 
	  catch( Exception ex ) 
	  {
		throw new RuntimeException( ex );
	  }
    } // method run
	
} // class ServerShutdownMonitor