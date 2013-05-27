package webrunner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import webrunner.server.jetty.JettyMan;

public class TestJettyMan 
{
  final static Logger __log = Logger.getLogger( TestJettyMan.class );	  
	
  public static void main( String args[] )
  {
	BasicConfigurator.configure();
	__log.debug( "log4j configured; lets go" );	  
	  
	JettyMan man  = new JettyMan( "Jetty Man" );
	int exitCode = man.run( args );

	__log.info( "bye (" + exitCode + ")" );
  }		
}
