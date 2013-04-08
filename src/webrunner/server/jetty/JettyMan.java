package webrunner.server.jetty;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import webrunner.server.ServerCommand;
import webrunner.server.ServerStatus;
import webrunner.server.ServerStop;
import webrunner.server.ServerMan;
import webrunner.server.ServerTrayMan;
import webrunner.ui.SplashScreen;


public class JettyMan extends ServerMan 
{
	final static Logger __log = Logger.getLogger( JettyMan.class );	

	
	public Display _display;
	public Shell   _shell;
	
	public ServerTrayMan   _tray;
	public SplashScreen    _splash; 	

	
	public JettyMan( String title )
	{
		super( title );
	}

	@Override
	public String getServerName() { return "Jetty"; }
	
	@Override
	protected ServerCommand createStart() { return new JettyStartEx( this ); }

	@Override
	protected ServerCommand createStatus() { return new ServerStatus( this, "/test/status" ); }
  
	@Override
    protected ServerCommand createStop()  { return new ServerStop( this ); }
       		
	
	@Override
	protected void onInit() throws Exception 
	{
		 _argParser.dump();  // dump args to log
	}	
	
	@Override
	protected void onExit() 
	{
		 __log.debug( "onExit called" );
		 
	    // NB: run on gui/swt thread
	     
		_display.syncExec( new Runnable() {
		   public void run()
		   {
	    	_tray.cleanUpAndSayGoodBye();   // rename to onExit() ??

	    	System.exit(0);
		   }
		});
	     

	     // _start._server.stop();
         //
	     // accept.close();
	     // _socket.close();
	     //
	     // exit and "abort" possible running initialization
	     // System.exit( 0 );
	}	
	
} // class JettyMan