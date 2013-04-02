package webrunner.server.i;

/////////
//
// nb: unfortunately canNOT reuse Runnable 
//   we need to include throws Excepption for convenience (sorry)
//
// check back in the future (maybe we can use just Runnable ? )

public interface ServerCommand {
   public int run(); 
}
