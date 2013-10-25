package webstart.utils;

public class StringUtils 
{
    public static String formatStackTrace( Throwable t )
    {
    	 StackTraceElement[] stack = t.getStackTrace();
    	 
    	 StringBuilder buf = new StringBuilder();
    	 
    	 for( StackTraceElement trace : stack )
    	 {
    	    buf.append(  "  " + trace.toString() + "\n" );
    	 }
    	 
    	 return buf.toString();
    }
        
  	public static String formatMillis( long time )
	{	
		long ms;
		long seconds;
		long minutes;
		long hours;
		int  days;
				
		seconds =  time / 1000;
		ms = time % 1000;

		minutes = seconds / 60;
		seconds = seconds % 60;
		
		hours = minutes / 60;
		minutes = minutes % 60;

		days = (int) hours / 24;
		hours = hours  % 24; 

/*		
		if( days > 0 )
		    return String.format( "%d " +pluralize( "day", days )+ "+%02d:%02d.%02d,%03d", days, hours, minutes, seconds, ms );
		else if( hours > 0 || minutes > 0 )	
			return String.format( "%02d:%02d.%02d,%03d", hours, minutes, seconds, ms );
	    else
	        return String.format( "%2d,%03d", seconds, ms );
*/	       		

		if( days > 0 ) {
			return String.format( "%d " +pluralize( "day", days )+ "+%02d:%02d.%02d,%03d", days, hours, minutes, seconds, ms );
		} else {
			return String.format( "%02d:%02d.%02d,%03d", hours, minutes, seconds, ms );
		}	
	}
  	
	public static String pluralize( String noun, int counter )
	{
		return counter == 1 ? noun : noun + "s"; 
	}  	
}
