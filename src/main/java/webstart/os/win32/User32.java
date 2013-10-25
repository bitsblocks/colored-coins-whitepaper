package webstart.os.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;


/**
 * The OpenIcon function restores a minimized (iconic) window to 
 * its previous size and position; it then activates the window. 
 * Syntax:
 *   BOOL OpenIcon(HWND hWnd);
 * Parameters:
 *   hWnd [in] Handle to the window to be restored and activated. 
 * Return Value:
 *   If the function succeeds, the return value is nonzero.
 *   If the function fails, the return value is zero. To get 
 *   extended error information, call GetLastError.
 * 
 * @see http://msdn2.microsoft.com/en-us/library/ms633535.aspx 
 */

public interface User32 extends StdCallLibrary 
{   
	interface WndEnumProc extends StdCallCallback
	{
        /** Return whether to continue enumeration. */
		 boolean callback( int hWnd, int lParam );
	}	
	
	
   User32 INSTANCE = (User32) Native.loadLibrary( "user32", User32.class );

   int OpenIcon( int hwnd ); 
   boolean EnumWindows( WndEnumProc wndenumproc, int lParam );
}
