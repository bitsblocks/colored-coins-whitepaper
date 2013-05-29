package webstart.os.win32;


import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface Kernel32 extends StdCallLibrary 
{   
  public class MEMORYSTATUS extends Structure 
  {
	public int dwLength;
	public int dwMemoryLoad;
	public int dwTotalPhys;
	public int dwAvailPhys;
	public int dwTotalPageFile;
	public int dwAvailPageFile;
	public int dwTotalVirtual;
	public int dwAvailVirtual;
  }

  Kernel32 INSTANCE = (Kernel32) Native.loadLibrary( "kernel32", Kernel32.class );
	
  void GlobalMemoryStatus( MEMORYSTATUS result );
  
  int GetTickCount(); // windows uptime in millis
}