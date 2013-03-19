package webrunner;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.swt.widgets.Display;

public class TestMini 
{
	public static void main( String args[] )
	{
		BasicConfigurator.configure();
		
		Display display = new Display();
		
		MiniBrowserShell shell = MiniBrowserShell.create( display );
		
		shell._browser.setUrl( "http://google.com" );		
		
		shell._shell.open();
		
		while( !shell._shell.isDisposed() ) {
		   if(!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		
		System.out.println( "bye" );
	}	
}  // class TestMini
