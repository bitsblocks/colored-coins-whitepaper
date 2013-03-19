package webrunner;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class MiniBrowserShellEx extends MiniBrowserShell 
{
	final static Logger __log = Logger.getLogger( MiniBrowserShellEx.class );

	static int     __openWindowCounter = 0; // track open pop-ups (NB: will NOT include top level window)	
    static boolean __canClose = false;

    
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

	
    //////////
	// public top-level ctors

	public MiniBrowserShellEx( Display display )
	{
	   this( display, "Untitled Mini Browser", null );
	}
	
	public MiniBrowserShellEx( Display display, String title, Image images[] )
	{
	   super( display, title, images );

	   __log.debug( "ctor" );
	   
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

	/////////////////////////
	// protected sub shell ctors (called for popups)
	
	MiniBrowserShellEx( MiniBrowserShell parent ) 
	{
	   super( parent );
	}
	

	/// mark as overriden
	public MiniBrowserShell createSubBrowserShell()
	{
		__log.debug( "#"+_id+"| create sub browser shell" );
		final MiniBrowserShellEx shell = new MiniBrowserShellEx( this );

		shell._shell.addListener( SWT.Close, new Listener() {
		      public void handleEvent( Event event ) {
		    	  __openWindowCounter--;
		    	  __log.debug( "#"+shell._id+"| shell-close: openWindowCounter--: " + __openWindowCounter );
		      }	
		});
		
		// track open popups count
		__openWindowCounter++;
		__log.debug( "#"+shell._id+"| shell-open: openWindowCounter++: " + __openWindowCounter );

		
		return shell;
	}
	
} // class MiniBrowserShellEx
