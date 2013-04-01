package webrunner.server;

public class ArgumentParser 
{
    private String  _port          = null;    // /PORT:<NUMBER>
       
    // commands: start, stop, status, menu
    private boolean  _start         = false;
    private boolean  _stop          = false;
    private boolean  _status        = false;
    private boolean  _menu          = false;
    
		
	private String[] _args = null;
	public String[] getArgs() { return _args; }
	
	
	public ArgumentParser( String[] args ) 
	{
		_args = args;
		parseRuntimeArguments(args);
		
		// if no command is set; assume/set it to start
		if( _start == false && _stop == false && _status == false && _menu == false ) {
			_start = true;
		}
	}

	public String getPort()           { return _port; }

	public boolean isStart()  { return _start; }
	public boolean isStop()   { return _stop; }
	public boolean isStatus() { return _status; }
	public boolean isMenu()   { return _menu; }
	
		
	private void parseRuntimeArguments(String[] args) 
	{
		for( String arg : args ) 
		{
			int colonNdx = arg.indexOf(':');
			/* most likely a VIP standard parameter */

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
				// todo: add warning about unknown param?

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
				  _menu = true;	
				}				
				// todo: add warning about unknown command?
			}				
		}
	}
}