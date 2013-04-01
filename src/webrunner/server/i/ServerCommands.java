package webrunner.server.i;

import webrunner.server.ServerMan;

public interface ServerCommands 
{
    public ServerCommand createStart( ServerMan man );
    public ServerCommand createStatus( ServerMan man );
    public ServerCommand createStop( ServerMan man );
}
