package webrunner.browser;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class MiniBrowserShellEx extends MiniBrowserShell 
{
	final static Logger __log = Logger.getLogger( MiniBrowserShellEx.class );
	
    //////////
	// standard ctors and createSubShell machinery 

	String _refreshForPath[];
	
	public MiniBrowserShellEx( Display display, String refreshForPath[] )
	{
	   this( display, UNTITLED, null, refreshForPath );
	  
	}
	
    public MiniBrowserShellEx( Display display, String title, Image images[], String refreshForPath[] )
	{
	   super( display, title, images );
	   
	   _refreshForPath = refreshForPath;
	}

	MiniBrowserShellEx( MiniBrowserShellEx parent ) 
	{
	   super( parent );
	   
	   _refreshForPath = parent._refreshForPath;
	}

	@Override
	public MiniBrowserShell createSubShell()
	{
	  __log.debug( "#"+_id+"| createSubShell called" );
	  return new MiniBrowserShellEx( this );
	}

	
	///////////////////////////////////////////
	// add extra variables and code 

	static int     __openWindowCounter = 0; // track open pop-ups (NB: will NOT include top level window)	
    static boolean __canClose = false;

    boolean _firstPage = true;   // HACK: refresh first page
    
    
	static class OpenWindowCountFun extends BrowserFunction {
	    private MiniBrowserShellEx _shell;
		OpenWindowCountFun( MiniBrowserShellEx shell, String name ) {
			super( shell._browser, name );
			_shell = shell;
		}
			
		public Object function(Object[] arguments) {
            __log.debug( "OpenWindowCountFun.function called" );
			return new Short( (short) _shell.__openWindowCounter );
		}
	}

	static class SetCanCloseFlagFun extends BrowserFunction {
		private MiniBrowserShellEx _shell;
		SetCanCloseFlagFun( MiniBrowserShellEx shell, String name ) {
			super( shell._browser, name );
			_shell = shell;
		}
			
		public Object function(Object[] arguments) {
            __log.debug( "SetCanCloseFlagFun.function called" );
			_shell.__canClose = true;
			return new Boolean( _shell.__canClose );
		}
	}
	  
	static class HelloFun extends BrowserFunction {
		HelloFun( MiniBrowserShellEx shell, String name ) {
			super( shell._browser, name );
		}
		
		public Object function (Object[] arguments) {
            __log.debug( "HelloFun.function called" );
			return new String( "Hello from Webrunner!" );
		}
	}


	@Override
	public void onCreateTopShell()
	{
	  __log.debug( "#"+_id+"| create top browser shell" );
	   
	  __log.debug( "#"+_id+"| adding browser functions" );
		
	    final BrowserFunction fun1 = new HelloFun( this, "webrunnerHello" );
		final BrowserFunction fun2 = new OpenWindowCountFun( this, "webrunnerOpenWindowCount" );
		final BrowserFunction fun3 = new SetCanCloseFlagFun( this, "webrunnerSetCanCloseFlag" );
		
		_shell.addListener( SWT.Close, new Listener() {
			    public void handleEvent( Event ev ) {			        
			      __log.debug( "#"+_id+"| shell-close: openWindowCounter: " + __openWindowCounter + ", canClose: " + __canClose );
			    	  
			      if( __openWindowCounter > 0 && __canClose == false )
			      {
			    	ev.doit = false;

					String js = "if( typeof webrunner !== 'undefined' && typeof webrunner.closeWindow === 'function') { webrunner.closeWindow(); } else { webrunnerSetCanCloseFlag(); window.close(); }";
						 
					__log.debug( "#"+_id+"| browser-execute: >" +js + "<" );
					_browser.execute( js );
			      }
			    } // method handleEvent	
			});
	}

	@Override
	public void onCreateSubShell()
	{
		__log.debug( "#"+_id+"| create sub browser shell" );

		_shell.addListener( SWT.Close, new Listener() {
		      public void handleEvent( Event event ) {
		    	  __openWindowCounter--;
		    	  __log.debug( "#"+_id+"| shell-close: openWindowCounter--: " + __openWindowCounter );
		    	  
		    	  __log.debug( "#"+_id+"| shell-close: clearSessions" );
		    	  Browser.clearSessions();
		      }	
		});
		
		_browser.addProgressListener( new ProgressListener() {
		      public void changed( ProgressEvent ev ) {
		          if(ev.total == 0) 
		        	  return;                            
		          int ratio = ev.current * 100 / ev.total;
		  		  __log.debug( "#"+_id+"| browser-progress-changed - current: " + ev.current + ", total: " + ev.total );
		      }
		      public void completed( ProgressEvent ev ) {
		  		  __log.debug( "#"+_id+"| browser-progress-completed - firstPage: " + _firstPage + ", url: " + _browser.getUrl() );

		  		  if( _firstPage == true )
		  		  {
		  			  for( String path : _refreshForPath )
		  			  {
		  				if( _browser.getUrl().contains( path ))			  			
					  	{
						  __log.debug( "#"+_id+"| refresh first page" );
					  	  _browser.refresh();
					  	  break;
					     }  
		  			  }
			  		  
		  		     _firstPage = false;
		  		  }
		      }
		    });

		// track open popups count
		__openWindowCounter++;
		__log.debug( "#"+_id+"| shell-open: openWindowCounter++: " + __openWindowCounter );
	}
	
} // class MiniBrowserShellEx
