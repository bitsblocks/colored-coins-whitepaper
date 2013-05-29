package webstart.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import org.apache.log4j.Logger;

public class SocketUtils 
{
	final static Logger __log = Logger.getLogger( SocketUtils.class );		
	
	public static boolean isPortAvailable( int port )
	{
		ServerSocket s = null;		
		
	   try 
	   {
			s = new ServerSocket();
			// s.setReuseAddress(true);  -- NB: do NOT set - otherwise will "hijack"/ reuse socket already in use
			s.setSoTimeout( 1 ); // 1ms block on accept call 
		    s.bind( new InetSocketAddress( "127.0.0.1", port ) );  		    
		    s.close();
		 
		    __log.debug( "port " + port + " isBound? " + s.isBound() );
		    
		    __log.info( "free port @ " + port );
		    
		    return true;
		}
		catch( IOException ex ) 
		{           
			 __log.info( "non-free port @ " + port + "; reason: " + ex.toString() );
		     return false;
		}
		finally
		{
			try
			{ 
				if( s != null ) {
					s.close();
				}
			}
			catch( Exception ex ){ }
				
			s = null;
		}		
	}
}
