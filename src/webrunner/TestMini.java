package webrunner;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.swt.widgets.Display;

public class TestMini 
{
	public static void main( String args[] )
	{
		BasicConfigurator.configure();
		
		String url = (args.length == 0) ?  "http://google.com" : args[0];
				
		Display display = new Display();
		
		// MiniBrowserShell shell = new MiniBrowserShell( display );
		MiniBrowserShell shell = new MiniBrowserShellEx( display );
		
		shell._browser.setUrl( url );
		shell._shell.open();
		
		while( !shell._shell.isDisposed() ) {
		   if(!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		
		System.out.println( "bye" );
	}	
}  // class TestMini
