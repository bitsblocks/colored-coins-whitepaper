package webrunner.server.jetty;

import org.apache.log4j.Logger;

import webrunner.server.ServerMan;
import webrunner.server.i.ServerCommand;

public class JettyStart implements ServerCommand 
{
	final static Logger __log = Logger.getLogger( JettyStart.class );	

	ServerMan _sh;
	
	public JettyStart( ServerMan sh )
	{
       __log.debug( "ctor" );

       _sh = sh;
	}
	
	public void run() throws Exception
	{
       __log.debug( "enter run" );

       // to be done
       
       __log.debug( "leave run" );
	}
	
} // class JettyStart
