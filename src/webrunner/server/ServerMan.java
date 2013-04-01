package webrunner.server;


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


	public ArgumentParser _argParser;

	
	public int showMessageBoxError( String msg )
	{
		Shell shell = new Shell( Display.getDefault() );
		
	    MessageBox msgBox = new MessageBox( shell, SWT.OK | SWT.ICON_ERROR );
	    msgBox.setText( _title );
	    msgBox.setMessage( msg );
	    return msgBox.open();
	}

	public void logRuntimeArgs() 
	{
		String args[] = _argParser.getArgs();

		__log.info( args.length + " startup " + StringUtils.pluralize( "argument", args.length ) + ": " );
		
		for( int i = 0; i < args.length; i++ )
		{
			__log.info( "  argument " + i + "=" + args[i] );
		}
	}

	// todo/fix: find a better name  - builder? factory?
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
    

	public void run( String[] args ) 
	{
		_argParser = new ArgumentParser( args );

		try 
		{							
			init();
			
			__log.info( "after init - dispatch/find command using args" );

		    if( _argParser.isStart() || _argParser.isMenu() )  // NB: /menu gets handled like /start 
		    {
		    	ServerCommand cmd = createServerCommands().createStart( this );
		    	__log.info( "before server start - run" );
		    	cmd.run();
		    }
		    else if( _argParser.isStop() )
		    {
		    	ServerCommand cmd = createServerCommands().createStop( this );
		    	__log.info( "before server stop - run" );
		    	cmd.run();
		    }
		    else if( _argParser.isStatus() )
		    {
		    	ServerCommand cmd = createServerCommands().createStatus( this );
		    	__log.info( "before server status - run" );
		    	cmd.run();
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
		    	
		    	// fix: return 1
		    	System.exit( 1 );
		    }	    
			
			
		    // fix return 0
			System.exit( 0 );
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
			
		    // fix return 1 ; change void run to int run
			System.exit( 1 );
		}
	}
		
} // class ServerMan (server manager)
