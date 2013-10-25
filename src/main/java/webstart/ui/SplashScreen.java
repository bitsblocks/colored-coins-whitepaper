package webstart.ui;

import org.apache.log4j.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;



public class SplashScreen 
{	
	final static Logger __log = Logger.getLogger( SplashScreen.class );
			
	Display   _display;
	Image     _image;
	
	public Shell _shell = null;

	public SplashScreen( Display display, Image image ) 
	{
		_display       = display;
		_image         = image; 
	}
		
	public void open() 
	{
		__log.info( "open splash shell" );
		
		_shell = new Shell( _display, SWT.None );

		// todo/fix: add msg to image
		// Image image = _settings.createSplashImage( _display, _msg );
		
		// The image determines the splash screen size 
		Rectangle rect = _image.getBounds();
		_shell.setSize( rect.width, rect.height );
		_shell.setBackgroundImage( _image );

		// place the splash screen centrally 
		// - use getPrimaryMonitor to support multi-monitor displays
		
		Rectangle displayRect = _display.getPrimaryMonitor().getClientArea();
		Point location = new Point((displayRect.width-rect.width)/2, (displayRect.height-rect.height)/2);
		_shell.setLocation(location);
		__log.info( "splash shell: (x,y,w,h)=(" + location.x + "," + location.y + "," + rect.width + "," + rect.height +").");

		_shell.setVisible( true );
	}

	public void dispose() 
	{
		if( _shell != null &&  !_shell.isDisposed() )
		{
		  __log.info( "before dispose splash shell" );
		  _shell.dispose();
		  __log.info( "after dispose splash shell" );		   
		}
	}
	
	public boolean isDisposed()
	{
		if( _shell != null ) 
		  return _shell.isDisposed();
		else 
		  return true;
	}		
} // class SplashScreen