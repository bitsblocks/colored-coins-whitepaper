package webrunner.server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;

import webrunner.server.i.ServerCommand;



public class ServerStatus implements ServerCommand
{
	final static Logger __log = Logger.getLogger( ServerStatus.class );	
		
	ServerMan _man;
	String _statusServicePath;  // e.g. /app/status
	
    public ServerStatus( ServerMan man, String statusServicePath )
    {
    	_man               = man;
    	_statusServicePath = statusServicePath;
    }
	
	public void run()
    {
		try
		{
		  URL url = new URL( _man._serverHost + _statusServicePath ); 
		  __log.info( "fetching " + url.toString() );

		  BufferedReader reader = new BufferedReader( new InputStreamReader( url.openStream() ) );
		  String line;
		  while( (line = reader.readLine()) != null ) {
			__log.info( "  >> " + line );
		}
		  reader.close();
		}
		catch( Exception ex )
		{
		  __log.error( "error fetching status page", ex );
		  
		  // fix: return 1
		  System.exit(1);  // assume server is not running
	    }
			  
		__log.info( "done fetching." );
		// fix: return 0
		System.exit(0);   // assume server is running     	    	
    }
}
