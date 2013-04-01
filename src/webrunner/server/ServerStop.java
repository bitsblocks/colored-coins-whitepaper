package webrunner.server;


import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;


//fix: rename to ServerStatusCommand or similar -- not jetty specific!!!!
//reuse code!!


public class ServerStop implements ServerCommand 
{
	final static Logger __log = Logger.getLogger( ServerStop.class );		

	ServerShell _sh;
	
    public ServerStop( ServerShell sh  )
    {
    	_sh = sh;
    }
	
 
	public void run() throws Exception
    {
		try
		{
		  Socket s = new Socket(InetAddress.getByName("127.0.0.1"), _sh._port+1 );
		  		 
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
	   	
		__log.info( "bye" );
    }
}
