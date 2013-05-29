package webstart.os.win32;

import java.util.Date;

import webstart.utils.StringUtils;


public class WinUtils 
{
	
	static Kernel32 __kernel32 = Kernel32.INSTANCE;
		
	public static String fmtMemBytes( int bytes )
	{
		if( bytes == -1 ) {
			return "(n/a)";
		} else if( bytes == 0 ) {
			return "0";
		} else {
			return String.format( "%,d k", bytes/1024 );
		}	
	}
	
	public static String fmtMemStatus( Kernel32.MEMORYSTATUS mem )
	{
		String msg = "memoryLoad: " + mem.dwMemoryLoad + "%, " +
	     "totalPhys: " + fmtMemBytes( mem.dwTotalPhys ) + ", " +
        "availPhys: " + fmtMemBytes( mem.dwAvailPhys ) + ", " + 
        "totalPage: " + fmtMemBytes( mem.dwTotalPageFile ) + ", " + 
        "availPage: " + fmtMemBytes( mem.dwAvailPageFile ) + ", " + 		             
        "totalVirtual: " + fmtMemBytes( mem.dwTotalVirtual ) + ", " +
        "availVirtual: " + fmtMemBytes( mem.dwAvailVirtual );
		
		return msg;
	}
		
	public static Kernel32.MEMORYSTATUS getMemoryStatus()
	{
		Kernel32.MEMORYSTATUS mem = new Kernel32.MEMORYSTATUS();
		
		__kernel32.GlobalMemoryStatus(mem);
		
		return mem;
	}
	
	public static String getMemoryStatusLogMsg()
	{
		// see http://msdn.microsoft.com/en-us/library/aa366772%28VS.85%29.aspx for MEMORYSTATUS struct
		
		Kernel32.MEMORYSTATUS mem = new Kernel32.MEMORYSTATUS();
		
		__kernel32.GlobalMemoryStatus(mem);		
		
		return fmtMemStatus( mem );
	}
	
	public static int getMemoryLoad()
	{
		Kernel32.MEMORYSTATUS mem = new Kernel32.MEMORYSTATUS();
		
		__kernel32.GlobalMemoryStatus(mem);
		
		return mem.dwMemoryLoad;		
	}
        
    public static String getUptimeLogMsg()
	{              
        int uptime =__kernel32.GetTickCount(); // uptime in Millis
        long now = System.currentTimeMillis();
          
  		Date boottime = new Date( now - uptime );          
          
        return StringUtils.formatMillis( uptime ) + " since " + boottime.toString();
	}
}
