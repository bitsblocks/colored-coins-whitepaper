package webrunner.server;

import org.apache.log4j.Logger;

import webrunner.utils.StringUtils;

public class ArgParser 
{
	final static Logger __log = Logger.getLogger( ArgParser.class );	

	private String  _port          = null;    // /PORT:<NUMBER>
       
    // commands: start, stop, status
    private boolean  _start         = false;
    private boolean  _stop          = false;
    private boolean  _status        = false;
    
    
    // flags: debug, splash, etc.
    private boolean _debug   = false;
    private boolean _open    = false;   // openStartPage
    private boolean _splash  = false;   // showSplashScreen
    
		
	private String[] _args = null;
	
	
	public ArgParser( String[] args ) 
	{
		_args = args;
		parse();
		
		// if no command is set; assume/set it to start
		if( _start == false && _stop == false && _status == false ) {
			_start = true;
		}
	}

	public String  getPort()  { return _port; }

	// commands
	
	public boolean isStart()  { return _start; }
	public boolean isStop()   { return _stop; }
	public boolean isStatus() { return _status; }
	
	// flags
	
    public boolean isDebug()     { return _debug; }

    public boolean showSplash()    { return _splash; }
    public boolean openStartPage() { return _open; }
	
	
	private void parse() 
	{
		for( String arg : _args ) 
		{
			int colonNdx = arg.indexOf(':');
			// NB: allow values separated w/ : e.g. /p:4343

			String parameterName;
			String parameterValue;								

			if( colonNdx > 0) 
			{
				parameterName = arg.substring(0, colonNdx);
				parameterValue = arg.substring(colonNdx + 1);

				if(    "PORT".equalsIgnoreCase( parameterName ) 
					|| "/PORT".equalsIgnoreCase( parameterName )
					|| "P".equalsIgnoreCase( parameterName )
					|| "/P".equalsIgnoreCase( parameterName )) 
				{
				  _port = parameterValue;
				}
				// todo: add warning about unknown params?

			}
			else // allow params without values (e.g. /START)
			{
			    parameterName = arg;

				if(    "STOP".equalsIgnoreCase( parameterName ) 
					|| "/STOP".equalsIgnoreCase( parameterName ))
				{
				  _stop = true;	
				}
				else if(   "STATUS".equalsIgnoreCase( parameterName ) 
						|| "/STATUS".equalsIgnoreCase( parameterName ))
				{
				  _status = true;
				}				
				else if(    "START".equalsIgnoreCase( parameterName ) 
						 || "/START".equalsIgnoreCase( parameterName ))
				{
				  _start = true;	
				}
				else if(    "MENU".equalsIgnoreCase( parameterName ) 
						 || "/MENU".equalsIgnoreCase( parameterName ))
				{
				  // convenience shortcut for
			      //   /start w/ flags /splash /open
				  _start  = true;
				  _splash = true;
				  _open   = true;
				}
				else if(    "DEBUG".equalsIgnoreCase( parameterName ) 
						 || "/DEBUG".equalsIgnoreCase( parameterName )
						 || "D".equalsIgnoreCase( parameterName )
						 || "/D".equalsIgnoreCase( parameterName ))
				{
				  _debug = true;	
				}
				else if(    "OPEN".equalsIgnoreCase( parameterName ) 
						 || "/OPEN".equalsIgnoreCase( parameterName ))
				{
				  // NB: check - only available for start command (not global)
				  _open = true;	
				}
				else if(    "SPLASH".equalsIgnoreCase( parameterName ) 
						 || "/SPLASH".equalsIgnoreCase( parameterName ))
				{
				  // NB: check - only available for start command (not global)
				  _splash = true;	
				}
				// todo: add warning about unknown command or flag?
			}				
		}
	} // method parse

	
	public void dump() 
	{
		if( _args.length == 0 )
		{
		  	__log.info( "no startup (command line) args" );
		}
		else
		{		
		  __log.info( _args.length + " startup (command line) " + StringUtils.pluralize( "arg", _args.length ) + ": " );
		
		  for( int i = 0; i < _args.length; i++ )
		  {
			__log.info( "  arg[" + i + "] = >>" + _args[i] + "<<" );
		  }
		}
	}
	
} // class ArgParser