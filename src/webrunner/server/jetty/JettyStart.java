package webrunner.server.jetty;

import org.apache.log4j.Logger;

import webrunner.server.ServerMan;
import webrunner.server.i.ServerCommand;

public class JettyStart implements ServerCommand 
{
	final static Logger __log = Logger.getLogger( JettyStart.class );	

	ServerMan _man;
	
	public JettyStart( ServerMan man )
	{
       __log.debug( "ctor" );

       _man = man;
	}
	
	public int run()
	{
       __log.debug( "enter run" );

       // to be done
       
       __log.debug( "leave run" );
       
	  return 0; // NB: with exitCode - OK == 0, ERROR == 1
	} // method run()
	
} // class JettyStart
