package webrunner.server.jetty;

import webrunner.server.ServerCommand;
import webrunner.server.ServerShell;

public class JettyStart implements ServerCommand {
	
	ServerShell _sh;
	
	public JettyStart( ServerShell sh )
	{
		_sh = sh;
	}
	
	public void run() throws Exception
	{
		// to be done
	}
	
}
