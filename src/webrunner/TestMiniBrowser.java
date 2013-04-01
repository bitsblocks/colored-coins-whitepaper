package webrunner;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.swt.widgets.Display;

import webrunner.browser.MiniBrowserWindow;
import webrunner.browser.MiniBrowserWindowEx;


public class TestMiniBrowser 
{
	public static void main( String args[] )
	{
		BasicConfigurator.configure();
		
		String url = (args.length == 0) ?  "http://google.com" : args[0];
				
		Display display = new Display();
		
		String refreshPath[] = {};

		MiniBrowserWindow win = new MiniBrowserWindowEx( display, refreshPath );
		// MiniBrowserWindow win = new MiniBrowserWindow( display );
		
		win.getBrowser().setUrl( url );
		win.getShell().open();
		
		while( !win.getShell().isDisposed() ) 
		{
		   if( !display.readAndDispatch() )
				display.sleep();
		}
		display.dispose();
		
		System.out.println( "bye" );
	}	
}  // class TestMiniBrowser
