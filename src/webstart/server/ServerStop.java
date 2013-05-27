package webrunner.server;


import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;




public class ServerStop implements ServerCommand 
{
	final static Logger __log = Logger.getLogger( ServerStop.class );		

	ServerMan _man;
	
    public ServerStop( ServerMan man )
    {
    	_man = man;
    }

    
	public int run()
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
			return 1; // NB: with exitCode - OK == 0, ERROR == 1
		 }
		 catch( IOException ex2 )
		 {
		    // silently log if can't connect to server 
			__log.info( "can't shutdown server: " + ex2.toString() );
		  return 1; // NB: with exitCode - OK == 0, ERROR == 1
		 }		 
		 
// use rethrow as runtime ex ??
//		 catch( Exception ex ) 
//		  {
//			throw new RuntimeException(ex);
//		  }
	   	
		__log.info( "bye" );
		return 0; // NB: with exitCode - OK == 0, ERROR == 1
    } // method run() 
	
} // class ServerStop 
