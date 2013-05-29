package webstart.utils;

import org.apache.log4j.Logger;
import org.eclipse.swt.program.Program;

public class DesktopUtils 
{
  final static Logger __log = Logger.getLogger( DesktopUtils.class );

  
  static public void openDoc( String path )
  {
		__log.info( "openDoc - path=>>" + path + "<<" );
		Program.launch( path );
  }
  
  static public void openBrowser( String url )
  {
		__log.info( "openBrowser - url=>>" + url + "<<" );
		Program.launch( url );

		/* try {
			Runtime.getRuntime().exec("cmd /c start \"\" " + url);
		} catch (IOException e) {
			__log.debug("Runtime.getRuntime().exec()", e);
			Program.launch( url );
		}*/
  }
  
  static public void startProg( String path )
  {
		__log.info( "startProg - path=>>" + path + "<<" );
		Program.launch( path );
  }
  
}  // class DesktopUtils
