package webrunner.server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;




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
	
	public int run()
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
		  
		  // assume server is not running
		  return 1; // NB: with exitCode - OK == 0, ERROR == 1
	    }
			  
		__log.info( "done fetching." );
		
		// assume server is running
		return 0; // NB: with exitCode - OK == 0, ERROR == 1
    } // method run()
} // class ServerStatus 
