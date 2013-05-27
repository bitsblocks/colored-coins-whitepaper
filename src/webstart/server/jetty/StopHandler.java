package webrunner.server.jetty;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.ContextHandler;


public class StopHandler extends AbstractHandler 
{
	final static Logger __log = Logger.getLogger( StopHandler.class );	
	
	private JettyStart _jetty;
	
	public StopHandler( JettyStart jetty )
	{
		_jetty = jetty;
	}
	
    protected void doStart()
    {
   	   __log.info( "StopHandler.doStart called" );
    }
        
    protected void doStop()
    {
   	   __log.info( "StopHandler.doStop called" );        	   
    }
 	   
    public void handle(String target, HttpServletRequest req, HttpServletResponse res, int dispatch)
            throws IOException, ServletException
   {
		String context = req.getParameter( "context" );
		__log.info( "try stop (undeploy) app with context " + context );
		
		if( context == null ) 
		{
			__log.error( "no app (context) specified to stop" );
			res.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		}
		else 
		{
			ContextHandler webapp = _jetty.getWebappByContext( "/" + context );
			if( webapp == null )
			{
				__log.error( "app (context) >>" + context + "<< could not be found" );
				res.setStatus( HttpServletResponse.SC_CONFLICT );				
			}	
			else if( !webapp.isRunning() )
			{
				__log.error( "app (context) >>" + context + "<< is not running (hint: start/deploy first)" );
				res.setStatus( HttpServletResponse.SC_CONFLICT );								
			}
			else
			{
				try {
					webapp.stop();
					__log.info( "app successfully stopped" );
					res.setStatus( HttpServletResponse.SC_OK );
				} catch( Exception e ) {
					__log.error( "app (context) >>" + context + "<< could not be stopped: " + e.toString() );
					res.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
				}
			} 
		}
		
		// todo: why this code? what does it do - is it needed?
		OutputStreamWriter out = new OutputStreamWriter( res.getOutputStream() );
		out.flush();
		out.close();
		
		// todo/fix: check if we need to set setHandled!!!!! to true or just fall throug is good enough
		// ((Request)req).setHandled(true);				
	}
} // class StopHandler
