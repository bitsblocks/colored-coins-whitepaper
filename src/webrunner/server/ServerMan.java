package webrunner.server;


import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import webrunner.server.i.ServerCommand;
import webrunner.server.i.ServerCommands;
import webrunner.utils.StringUtils;



public abstract class ServerMan 
{	
   final static Logger __log = Logger.getLogger( ServerMan.class );	
	
   public String _title; 	
	
   public ServerMan( String title )
   {
	   _title = title;
   }	
	
	public int            _port;
	public String         _serverHost;     // e.g. https://127.0.0.1:4343 or http://127.0.0.1:4242 


	public ArgParser _argParser;

	
	abstract protected ServerCommands createServerCommands();
	
	abstract protected void onInit() throws Exception; 
	
	abstract public String getServerName();  // e.g. jetty / jetty v71 / tomcat / etc.

	
	protected void init() throws Exception
	{
		onInit();
		
		if( _argParser.getPort() != null ) 
		{
			String argPort = _argParser.getPort();
			_port = Integer.parseInt( argPort, 10 );		
		}
		
		__log.info( "port: " + _port + ", shutdownPort: " + (_port+1) );				
		
		_serverHost = "http://127.0.0.1:" + _port;
					
		__log.info( "serverHost: " + _serverHost );
	}
    

	public int run( String[] args ) 
	{
		_argParser = new ArgParser( args );

		int exitCode = 1;  
		
		try 
		{							
			init();
			
			__log.info( "after init - dispatch/find command using args" );

		    if( _argParser.isStart() || _argParser.isMenu() )  // NB: /menu gets handled like /start 
		    {
		    	ServerCommand cmd = createServerCommands().createStart( this );
		    	__log.info( "before server start - run" );
		    	exitCode = cmd.run();
		    }
		    else if( _argParser.isStop() )
		    {
		    	ServerCommand cmd = createServerCommands().createStop( this );
		    	__log.info( "before server stop - run" );
		    	exitCode = cmd.run();
		    }
		    else if( _argParser.isStatus() )
		    {
		    	ServerCommand cmd = createServerCommands().createStatus( this );
		    	__log.info( "before server status - run" );
		    	exitCode = cmd.run();
		    }
		    else  
		    {
		    	StringBuilder buf = new StringBuilder();
		    	for( String arg : args ) {
					buf.append( arg + " " );
				}
		    	
		    	showMessageBoxError(
			    		"Unbekannter Befehl: " + buf.toString() + "\n\n" +
			    		"Bekannte Befehle: start, stop, status, menu"  );
		    	
		    	// note: Windows (DOS) kennt keine System.exists kleiner 0; daher Fehler -> 1 statt etwa -1
		    	
		    	return 1; // NB: with exitCode - OK == 0, ERROR == 1
		    }	    

		    return exitCode; // NB: with exitCode - OK == 0, ERROR == 1
		} 		
		catch( Throwable anyError ) 
		{
			__log.fatal( "fatal error:", anyError );

		    showMessageBoxError( 
		    		"Schwerer Systemfehler.\n\n" +
		    		"Bitte verständigen Sie Ihren Systembetreuer.\n\n\n" +
		    		"-- [Details] ------------------------\n" +
		    		anyError.toString() + "\n" + 
		    		StringUtils.formatStackTrace( anyError ) +
    		        "-------------------------------------"
		        );			

	    	return 1; // NB: with exitCode - OK == 0, ERROR == 1
		}
	} // method run

	
/////////////////////////////////////////
// some helper methods
	
	public int showMessageBoxError( String msg )
	{
		Shell shell = new Shell( Display.getDefault() );
		
	    MessageBox msgBox = new MessageBox( shell, SWT.OK | SWT.ICON_ERROR );
	    msgBox.setText( _title );
	    msgBox.setMessage( msg );
	    return msgBox.open();
	}

	public void checkStopIfRunning() 
	{
	  try
	  {
		Socket s = new Socket( InetAddress.getByName("127.0.0.1"), _port+1 );
			  		 
		OutputStream out = s.getOutputStream();
			   
		__log.info( "sending stop request" );
			 
		out.write( "\r\n".getBytes() );
		out.flush();
			
		s.close();
	  }
	  catch( ConnectException ex )
	  {
		// silently log if can't connect to server 
		__log.info( "can't connect to shutdown service; can't shutdown server: " + ex.toString() );
	  }
	  catch( IOException ex2 )
	  {
	    // silently log if can't connect to server 
		__log.info( "can't shutdown server: " + ex2.toString() );
	  }		   		 
    } // method checkStopIfRunning()
	
	
} // class ServerMan (server manager)
