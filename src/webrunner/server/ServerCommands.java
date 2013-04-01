package webrunner.server;

public interface ServerCommands 
{
    public ServerCommand createStart( ServerShell sh );
    public ServerCommand createStatus( ServerShell sh );
    public ServerCommand createStop( ServerShell sh );
}
