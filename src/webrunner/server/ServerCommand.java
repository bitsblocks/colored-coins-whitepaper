package webrunner.server;

/////////
//
// nb: unfortunately cannot reuse Runnable 
// sorry, we need to return exitCode (not possible with void)
//  0 == OK, 1 == ERROR

public interface ServerCommand {
   public int run(); 
}
