package webrunner.server.jetty;

import org.apache.log4j.BasicConfigurator;

import webrunner.server.ServerStatus;
import webrunner.server.ServerStop;
import webrunner.server.ServerCommand;
import webrunner.server.ServerCommands;
import webrunner.server.ServerShell;


public class JettyServerShell extends ServerShell 
{
	public String getServerName() { return "Jetty"; }
	
	public JettyServerShell( String title )
	{
		super( title );
	}
	
	
	@Override
	protected ServerCommands createServerCommands()
	{
        return new ServerCommands() {
        	public ServerCommand createStart( ServerShell sh )
        	{ 
        	  return new JettyStart( sh );
        	}

       	    public ServerCommand createStatus( ServerShell sh )  
       	    { 
       		  return new ServerStatus( sh, "/test/status" ); 
       	    }
       	 
       	    public ServerCommand createStop( ServerShell sh )    
       	    { 
       	      return new ServerStop( sh ); 
       	    }
        };		
	}
	
	@Override
	protected void onInit() throws Exception 
	{
		initLog4J();
	}
	
	protected void initLog4J()
	{
		 BasicConfigurator.configure();
		 
		 logRuntimeArgs();
	}

	
	public static void main(String[] args) 
	{
		JettyServerShell sh  = new JettyServerShell( "Jetty Server Title" );
		sh.run( args );
	}		
}
