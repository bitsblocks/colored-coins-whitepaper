package webrunner;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.swt.widgets.Display;

import webrunner.browser.MiniBrowserShell;
import webrunner.browser.MiniBrowserShellEx;


public class TestMiniBrowser 
{
	public static void main( String args[] )
	{
		BasicConfigurator.configure();
		
		String url = (args.length == 0) ?  "http://google.com" : args[0];
				
		Display display = new Display();
		
		String refreshPath[] = {};

		MiniBrowserShell sh = new MiniBrowserShellEx( display, refreshPath );
		// MiniBrowserShell sh = new MiniBrowserShell( display );
		
		sh.getBrowser().setUrl( url );
		sh.getShell().open();
		
		while( !sh.getShell().isDisposed() ) 
		{
		   if( !display.readAndDispatch() )
				display.sleep();
		}
		display.dispose();
		
		System.out.println( "bye" );
	}	
}  // class TestMiniBrowser
