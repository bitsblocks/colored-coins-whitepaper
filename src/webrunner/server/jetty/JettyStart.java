package webrunner.server.jetty;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.HashSessionIdManager;

import webrunner.server.ServerMan;
import webrunner.server.ServerShutdownMonitor;
import webrunner.server.i.ServerCommand;

public class JettyStart implements ServerCommand 
{
	final static Logger __log = Logger.getLogger( JettyStart.class );	

	public ServerMan _man;	
	public Server   _server;

	// keep track of webapps for live update
	//  - used by undeploy/deploy servlet (start/stop)
	
	public Map<String, ContextHandler> _webapps = new HashMap<String,ContextHandler>(); 
	
	public void addWebapp( ContextHandler webapp ) {
		_webapps.put( webapp.getContextPath(), webapp );
	}
	
	public ContextHandler getWebappByContext( String context ) {
		return _webapps.get(context);
	}
	
	
	public  Display _display;
	private Shell   _shell;
	
	
	public JettyStart( ServerMan man )
	{
       __log.debug( "ctor" );

       _man = man;
	}
	
	// fix/todo: add createStartHandler (deploy)
	// fix/todo: add createStopContextHandler ?? (undeploy) etc.
	
	// todo: move start/stop handler to webrunner.server.jetty

	protected void createServerShutdownMonitor() throws Exception
	{
        __log.info( "begin start shutdown monitor" );

        // fix: use _man.shutdownPort;
        
		Thread monitor = new ServerShutdownMonitor( _man, _man._port+1 );
		monitor.start();		  
	}
	
	protected void createServer()
	{
		  __log.info( "begin create server" );
		  
		  _server = new Server();
		 		  

	      /* old plain http connector */ 
	      SelectChannelConnector selConnector=new SelectChannelConnector();
	      selConnector.setReuseAddress( true );
	      selConnector.setPort( _man._port );
	      selConnector.setHost( "127.0.0.1" );
	
/*	      
	      // disable https/ssl support for now; not used
	      
	      // todo: check how to include/get SslSelectChanncelConnector (using non-blocking i/o - nio)
	      SslSocketConnector sslConnector=new SslSocketConnector();	  
	      sslConnector.setMaxIdleTime( 30000 );   // todo: check what does it all mean?? 
	      sslConnector.setKeystore( "../jetty/etc/keystore" );
	      sslConnector.setPassword( "pwtest1" );
	      sslConnector.setKeyPassword( "pwtest1" );
	      sslConnector.setReuseAddress( true );
	      sslConnector.setPort( _port+2 );
	      sslConnector.setHost( "127.0.0.1" );

		  _server.setConnectors(new Connector[]{ sslConnector, selConnector });
*/
	      
		  _server.setConnectors( new Connector[]{ selConnector });
		  
	      // fix slow startup bug
	      //   see http://docs.codehaus.org/display/JETTY/Connectors+slow+to+startup  
	      _server.setSessionIdManager(new HashSessionIdManager(new java.util.Random()));		  
	}
	
	public int runInner()
	{
		return 0;
	}
	
	public int run()
	{
       __log.debug( "enter run" );

       
       int exitCode = runInner();
       
       
       __log.debug( "leave run" );
       
	  return 0; // NB: with exitCode - OK == 0, ERROR == 1
	} // method run()
	
} // class JettyStart
