package webrunner.server.jetty;

import org.apache.log4j.Logger;

import webrunner.server.ServerStatus;
import webrunner.server.ServerStop;
import webrunner.server.ServerMan;
import webrunner.server.i.ServerCommand;
import webrunner.server.i.ServerCommands;


public class JettyMan extends ServerMan 
{
	final static Logger __log = Logger.getLogger( JettyMan.class );	
		
	public JettyMan( String title )
	{
		super( title );
	}

	@Override
	public String getServerName() { return "Jetty"; }
	
	@Override
	protected ServerCommands createServerCommands()
	{
		__log.debug( "createServerCommands called" );

		return new ServerCommands() {
        	public ServerCommand createStart( ServerMan man )
        	{ 
        	  return new JettyStart( man );
        	}

       	    public ServerCommand createStatus( ServerMan man )  
       	    { 
       		  return new ServerStatus( man, "/test/status" ); 
       	    }
       	 
       	    public ServerCommand createStop( ServerMan man )    
       	    { 
       	      return new ServerStop( man ); 
       	    }
        };		
	}
	
	@Override
	protected void onInit() throws Exception 
	{
		 _argParser.dump();  // dump args to log
	}	
}
