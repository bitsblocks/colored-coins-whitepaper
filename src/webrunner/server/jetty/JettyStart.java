package webrunner.server.jetty;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.HashSessionIdManager;

import webrunner.server.ServerCommand;
import webrunner.server.ServerShutdownMonitor;
import webrunner.server.ServerTrayMan;
import webrunner.ui.SplashScreen;
import webrunner.utils.SocketUtils;


public abstract class JettyStart implements ServerCommand 
{
	final static Logger __log = Logger.getLogger( JettyStart.class );	

	public JettyMan _man;	
	public Server   _server;

	// use protected instead of public - who cares?
	public long _beginServer;   // timestamp for tracking startup time 
	public long _endServer;     // timestamp for tracking startup time      		 

	
	
	// keep track of webapps for live update
	//  - used by undeploy/deploy servlet (start/stop)
	
	public Map<String, ContextHandler> _webapps = new HashMap<String,ContextHandler>(); 
	
	public void addWebapp( ContextHandler webapp ) {
		_webapps.put( webapp.getContextPath(), webapp );
	}
	
	public ContextHandler getWebappByContext( String context ) {
		return _webapps.get(context);
	}
	
	
	public JettyStart( JettyMan man )
	{
       __log.debug( "ctor" );

       _man = man;
	}
	

	protected ContextHandler createStartContextHandler( String context )
	{
	   ContextHandler cfg = new ContextHandler();
	   cfg.setContextPath( context );   // e.g. /deploy or /start
	   cfg.setHandler( new StartHandler( this ) );
	   return cfg;
	}
	
	protected ContextHandler createStopContextHandler( String context )
	{
		ContextHandler cfg = new ContextHandler();
		cfg.setContextPath( context );  // e.g.  /undeploy / or /stop  
		cfg.setHandler( new StopHandler( this ) );
        return cfg;
	}

	
	public abstract void openStartPage();	
	public abstract Image createSplashImage();
	public abstract Image[] createTrayImages();
	public abstract String createTrayTitle();

	
	public abstract void onServerCreateBefore();
	public abstract void onServerCreateAfter();
	public abstract void onServerStartBefore();
	public abstract void onServerStartAfter();
	
	
	// todo/fix: move to server Man ?? is generic - why / why not?? 

	protected void createServerShutdownMonitor() throws Exception
	{
        __log.info( "start shutdown monitor" );

		Thread monitor = new ServerShutdownMonitor( _man );
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
	
	public int runWorker()
	{
		try
		{
			_beginServer = System.currentTimeMillis();

			if( _man._argParser.openStartPage() )
			{
	            // /start command w/ open flag; don't restart server if already running;
				//   just popup browser page
				
				// check if server is running; check if port is available
	        
	            boolean isJettyDown = SocketUtils.isPortAvailable( _man._port );
	            if( !isJettyDown )
	            {
	               __log.info( "assume jetty is running; bind on port failed" );

	               openStartPage();
	              
	               __log.info( "bye" );
	               return 0;  // NB: with exitCode - OK == 0, ERROR == 1
	            }
			}
			
			 		
			_man._display = new Display();
			
			if( _man._argParser.showSplash() )
			{
			  __log.info( "begin create splash" );

			  Image splashImage = createSplashImage(); 
			  
			  _man._splash = new SplashScreen( _man._display, splashImage );
			  _man._splash.open();
			  __log.info( "after create splash" );
			}		

			_man.checkStopIfRunning(); 


			onServerCreateBefore();       
			
			  // todo: check - do we need to create a shell - yes -> required for menu add to tray
			  //   required for event loop too
			 _man._shell   = new Shell( _man._display );
			
			 String trayTitle   = createTrayTitle();
			 Image[] trayImages = createTrayImages();
			 
	         _man._tray = new ServerTrayMan( _man, _man._display, _man._shell, trayTitle, trayImages );
			
	        
	        createServerShutdownMonitor();        
			
	        createServer();
	        
			onServerCreateAfter();       

			onServerStartBefore();
			
			_server.start();
			  		
			 __log.info( "end start server" );		  

			 _endServer = System.currentTimeMillis(); 		 
	       
			 onServerStartAfter();
			  
			 _man._tray.running();
			  
			  __log.info( _man._title + " Betriebsbereit" );
			
		// fix:
		//   add menu structure
		//
		//	  _tray.buildMenu( _man._serverHost, _settings.getRootDir(), _paket.isTest() );
			
			  _man._tray.buildMenu( _man._serverHost, null );
			  
			  
			if( _man._argParser.showSplash() )
			{
			   // close splash screen
			   if( _man._splash != null && !_man._splash.isDisposed()) 
				 _man._splash.dispose();
			}
			
			if( _man._argParser.openStartPage() )
				openStartPage();

			
			  __log.debug( "starting swt event dispatch loop" );   		
				
			  
				/* as long as at least one of the shells is not disposed, run the dispatcher */
				while(!_man._shell.isDisposed()) 
				{
					if(!_man._display.readAndDispatch()) {
						_man._display.sleep();
					}				
				}
				
				__log.info( "swt event dispatcher stopped; disposing of display" );
				
				// todo: do we need to dispose default display??
			    _man._display.dispose();
			  		  
			  
	          _server.join();
	          
	          // todo: fix: add event dispatch loop for swt??
	          
	          __log.info( "bye" ); 
	          
	          return 0;  // NB: with exitCode - OK == 0, ERROR == 1
		}
		catch( Exception ex )
		{
			__log.error( "run server failed: " + ex.toString() );
			return 1; // NB: with exitCode - OK == 0, ERROR == 1
		}
	} // method runWorker()

	
	public int run()
	{
       __log.debug( "enter run" );

       int exitCode = runWorker();
       
       __log.debug( "leave run" );
       
	  return exitCode; // NB: with exitCode - OK == 0, ERROR == 1

	} // method run()
	
	
} // class JettyStart