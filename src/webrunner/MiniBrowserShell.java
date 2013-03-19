package webrunner;

import org.apache.log4j.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class MiniBrowserShell 
{
	final static Logger __log = Logger.getLogger( MiniBrowserShell.class );

	static int __counter = 0; 
	static int __openWindowCounter = 0; // track open popus (NB: will NOT include top level window)
	
	Shell     _shell;
	Browser   _browser;
	Display   _display;
	
	boolean  _isTop;

	int _id;
	
//////////////
//  
	String _title;
	Image  _images[];
	
	
/////////////////
// use Factory to create browser shell
//  e.g.  MiniBrowserShell.create(...)

	public static MiniBrowserShell create( Display display )
	{
		return create( display, "Untitled Mini Browser", null );
	}
	
	public static MiniBrowserShell create( Display display, String title, Image images[] )
	{
		return new MiniBrowserShell( display, title, images );
	}
	

	private void init( Display display, MiniBrowserShell parent, String title, Image images[] )
	{
		__counter++;
		_id = __counter;
		
		_isTop       = parent == null;

		_display     = display;		
		_title       = title;
		_images      = images;
		
		_shell = new Shell( _display );
		_shell.setLayout( new FillLayout() );
		
		_shell.setText( _title );
		if( _images != null )
			_shell.setImages( _images );		

		createBrowser();
	}
	
	private MiniBrowserShell( Display display, String title, Image images[] )
	{
		init( display, null, title, images );
	}
		
	
	private MiniBrowserShell( MiniBrowserShell parent ) 
	{
	   // NB: for sub shells inherit (pass along)  display, title, images, etc.
	   init( parent._display, parent, parent._title, parent._images );
	}
	
	private void createBrowser()
	{
		__log.debug( "#"+_id+"| before create browser widget" );
		
		_browser = new Browser( _shell, SWT.NONE );
		
		__log.debug( "#"+_id+"| after create browser widget" );

		
		// title handler gets title from web page (lets us update shell/window title using web page title)
		_browser.addTitleListener( new TitleListener() 
		{
			public void changed( TitleEvent ev )
			{
				__log.debug( "#"+_id+"| browser-title-changed: title=" +ev.title );
				
				if( ev.title.startsWith( "http://" ) || ev.title.contains( "application/pdf" ))
				   return;
				
				_shell.setText( ev.title );
			}
		});

		
	 _browser.addOpenWindowListener( new OpenWindowListener() {
		public void open( WindowEvent event ) {
			__log.debug( "#"+_id+"| browser-window-open: required=" + event.required );

			// if (!event.required) return;	/* only do it if necessary */

			final MiniBrowserShell shell = new MiniBrowserShell( MiniBrowserShell.this );
			
			shell._shell.addListener( SWT.Close, new Listener() {
			      public void handleEvent( Event event ) {
			    	  __openWindowCounter--;
			    	  __log.debug( "#"+shell._id+"| shell-close: openWindowCounter--: " + __openWindowCounter );
			      }	
			});
			
			// track open popups count
			__openWindowCounter++;
			__log.debug( "#"+shell._id+"| shell-open: openWindowCounter++: " + __openWindowCounter );

			shell._shell.open();
		    
			event.browser = shell._browser;		
		}
	});

	 _browser.addVisibilityWindowListener( new VisibilityWindowListener() {
		public void hide( WindowEvent event ) {
			__log.debug( "#"+_id+"| browser-window-hide" );
			Browser browser = (Browser) event.widget;
			Shell shell = browser.getShell();
			shell.setVisible( false );
		}
		
		public void show( WindowEvent ev ) {
			__log.debug( "#"+_id+"| browser-window-show" );			
			Browser browser = (Browser) ev.widget;
			Shell shell = browser.getShell();
			
			if( ev.location != null ) {
				__log.debug( "#"+_id+"| location.x=" + ev.location.x + ", y=" + ev.location.y );
				shell.setLocation( ev.location );
			}

			if( ev.size != null ) {
				__log.debug( "#"+_id+"| size.x=" + ev.size.x + ", y=" + ev.size.y );
				Point size = ev.size;
				shell.setSize( shell.computeSize( size.x, size.y ) );
			}
			shell.open();
		}
	});
	 
	_browser.addCloseWindowListener( new CloseWindowListener() {
		// NB: only fired/called if user clicks exit symbol in web page 
		public void close( WindowEvent event ) {
			__log.debug( "#"+_id+"| browser-window-close" );

			Browser browser = (Browser) event.widget;
			Shell shell = browser.getShell();
			shell.close();
		}
	});
	} // method createBrowser
	
} // class class MiniBrowserShell 
