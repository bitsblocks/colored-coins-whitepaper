package webrunner;

import webrunner.server.jetty.JettyMan;

public class TestJettyMan 
{
  public static void main( String args[] )
  {
	JettyMan man  = new JettyMan( "Jetty Man" );
	man.run( args );
	System.out.println( "bye" );
  }		
}
