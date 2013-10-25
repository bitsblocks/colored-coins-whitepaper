package webstart.utils;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;

import webstart.os.win32.WinUtils;


public class LogUtils 
{
  final static Logger __log = Logger.getLogger( LogUtils.class );

  static public void dumpOsProps()
  {
	__log.info( "os.name=" + System.getProperty( "os.name" ) +
			    ", os.version=" + System.getProperty( "os.version" ) +
			    ", os.arch=" + System.getProperty( "os.arch" ) );
  }

  static public void dumpSwtProps()
  {
    __log.info( "swt.platform=" + SWT.getPlatform() +
	  	        ", swt.version=" + SWT.getVersion() );
  }
  
  static public void dumpMemProps()
  {
    __log.info( "maxHeapMemory=" + String.format( "%,d k", (Runtime.getRuntime().maxMemory()/1024)) );                
      
    if( __log.isInfoEnabled() ) 
	  __log.info( WinUtils.getMemoryStatusLogMsg() );
  }
 
  static public void dumpJavaProps()
  {
	 __log.info( "java.version=" + System.getProperty( "java.version" ) +
				   ", java.vendor=" + System.getProperty( "java.vendor" ) + 
				   ", java.home=" + System.getProperty( "java.home" ) );
			
     __log.info( "java.vm.version=" + System.getProperty( "java.vm.version" ) +
     		   ", java.vm.vendor=" + System.getProperty( "java.vm.vendor" ) + 
     		   ", java.vm.name=" + System.getProperty( "java.vm.name" ) +
     		   ", java.vm.info=" + System.getProperty( "java.vm.info", "(n/a)" ) );
 
	__log.debug( "java.class.path: "   + System.getProperty("java.class.path" ));
	__log.debug( "java.ext.dirs: "     + System.getProperty( "java.ext.dirs" ));
	__log.debug( "java.library.path: " + System.getProperty( "java.library.path" ));
	__log.debug( "java.io.tmpdir: "    + System.getProperty( "java.io.tmpdir", "(n/a)" ) );
  }

  static public void dumpUserProps()
  {
	__log.info( "user.dir: " + System.getProperty( "user.dir" ));
  }
} // class LogUtils