package webrunner.server.jetty;

import org.apache.log4j.Logger;

import webrunner.server.ServerCommand;
import webrunner.server.ServerStatus;
import webrunner.server.ServerStop;
import webrunner.server.ServerMan;


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
	protected ServerCommand createStart() { return new JettyStart( this ); }

	@Override
	protected ServerCommand createStatus() { return new ServerStatus( this, "/test/status" ); }
  
	@Override
    protected ServerCommand createStop()  { return new ServerStop( this ); }
       		
	
	@Override
	protected void onInit() throws Exception 
	{
		 _argParser.dump();  // dump args to log
	}	
}
