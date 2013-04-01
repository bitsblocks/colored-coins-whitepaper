package webrunner.server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;


// fix: rename to ServerStatusCommand or similar -- not jetty specific!!!!
// reuse code!!


public class ServerStatus implements ServerCommand
{
	final static Logger __log = Logger.getLogger( ServerStatus.class );	
		
	ServerShell _sh;
	String _status_service_path;  // e.g. /app/status
	
    public ServerStatus( ServerShell sh, String status_service_path )
    {
    	_sh = sh;
    	_status_service_path = _status_service_path;
    }
	
	public void run()
    {
		try
		{
		  URL url = new URL( _sh._server_host + _status_service_path ); 
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
		  System.exit(1);  // assume jetty is not running
	    }
			  
		__log.info( "done fetching." );
		System.exit(0);   // assume jetty is running     	    	
    }
}
