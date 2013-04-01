package webrunner.server;


import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import webrunner.server.i.ServerCommand;



public class ServerStop implements ServerCommand 
{
	final static Logger __log = Logger.getLogger( ServerStop.class );		

	ServerMan _man;
	
    public ServerStop( ServerMan man )
    {
    	_man = man;
    }

    
    /// fix: to do NOT throw exception!!!
    // catch exception
	public void run() throws Exception
    {
		try
		{
		  Socket s = new Socket(InetAddress.getByName("127.0.0.1"), _man._port+1 );
		  		 
          OutputStream out = s.getOutputStream();
		   
		  __log.info( "sending server stop request" );
		 
		  out.write( "\r\n".getBytes() );
		  out.flush();
		
		  s.close();
		 }
		 catch( ConnectException ex )
		 {
			// silently log if can't connect to server 
			__log.info( "can't connect to server shutdown service; can't shutdown server: " + ex.toString() );
		 }
		 catch( IOException ex2 )
		 {
		    // silently log if can't connect to server 
			__log.info( "can't shutdown server: " + ex2.toString() );
		 }		 
		 
// use rethrow as runtime ex ??
//		 catch( Exception ex ) 
//		  {
//			throw new RuntimeException(ex);
//		  }
	   	
		__log.info( "bye" );
		
		// fix: return return code 0 success 1 error ???
    }
}
