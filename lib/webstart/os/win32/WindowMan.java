package webstart.os.win32;

import org.apache.log4j.Logger;
import org.eclipse.swt.internal.win32.OS;


public class WindowMan 
{	
	final static Logger __log = Logger.getLogger( WindowMan.class );

	@SuppressWarnings("serial")
	public static class ShutdownException extends Exception 
	{
	}
	
	private boolean _windowExists = false;
	private int     _windowHandle = 0;  // HWND_TOP ?

	
	final User32 _user32 = User32.INSTANCE;
		
    User32.WndEnumProc _wndenumproc = new User32.WndEnumProc()
    {
		/*
		 * BOOL CALLBACK EnumWindowProc(HWND hwnd, LPARAM lParam) hwnd is the
		 * handle to a top-level window lParam is the application-defined value
		 * given in EnumWindows returns TRUE (=1), to continue operation, FALSE
		 * (=0) stop stop enumeration.
		 * 
		 * In our case, the second argument is the target hash code. If a window
		 * with a matching hash code in the GWL_USERDATA is detected, the window
		 * handle is stored.
		 */
    	
    	public boolean callback( int hWnd, int lParam )
        {
			// application defined (hash Code) 
			int ownHash = lParam;
			
			
			// use trace?? level
			// __log.debug( "checking hwnd " + hWnd + ", ownHash " + ownHash);

			int hash = OS.GetWindowLong( hWnd, OS.GWL_USERDATA );

			if( hash == ownHash ) 
			{
				__log.info( "bingo - discovered matching window hash" );
				__log.info( "  window data: "	+ collectWindowData( hWnd ) );
				
				_windowHandle = hWnd;
				_windowExists = true;
				
				if( !__log.isDebugEnabled() )
					return false; // we don't search further, especially when in production
			} 
			else 
			{
				if( __log.isDebugEnabled()) 
				{
					// use trace?? level
					// __log.debug(  "no match "
					//	 	+ collectWindowData(hWnd) + ", foreign hash = "
					//		+ foreignHash );
				}
			}
			return true;
        }
     };
	
		private String collectWindowData(int hwnd) 
		{
			int windowTextLength = 1 + OS.GetWindowTextLengthA(hwnd);
			byte[] windowText = new byte[windowTextLength];
			OS.GetWindowTextA( hwnd, windowText, windowTextLength );
			StringBuffer answer = new StringBuffer();
			answer.append(hwnd);
			answer.append(' ');
			answer.append(new String(windowText));
			return answer.toString();
		}


	/*
	 * Different OS versions behave differently here: either the OS call or the
	 * User32 call get the hwnd...
	 */
	public void registerWindow( int hwnd, int hash ) throws ShutdownException 
	{
		__log.debug( "registerWindow hwnd: " + hwnd + ", hash: " + hash );

		if( findWindow( hash )  ) 
		{
			/*
			 * RACE CONDITION: a parallel swt client with the same hash
			 *  registered itself while checking
			 * for parallel instances. In this case we assume, that the faster
			 * one, i.e. the other one is the swt client "in charge" and we
			 * terminate ourselves.
			 */
			
			bringWindowToTop();

			// fix: use mutex for registration a la launch4j (see launch4j.sourceforge.net)
			
			__log.warn( "registerWindow: another window has same USERDATA, we assume two started swt instances and commmit suicide." );
			throw new ShutdownException();			
		}


		int userData = OS.GetWindowLong(hwnd, OS.GWL_USERDATA);
		
		if( userData != 0 )
		{
			// todo: does this ever happen???
			__log.warn( "registerWindow: the window found already has USERDATA, don't overwrite." );
		}
		else
		{
		   OS.SetWindowLong(hwnd, OS.GWL_USERDATA, hash);
		}
	}

	// return true if window exists, otherwise false
	public boolean findWindow( int hash ) 
	{
		__log.info( "begin findWindow hash: " + hash );
		
		_user32.EnumWindows( _wndenumproc, hash ); 
			
		/*
		 * if the callback discovered a match, both the windowHandle is
		 * stored and windowExists flag is true.
		 */
		
		__log.info( "windowExists? " + _windowExists );
		__log.debug( "windowHandle: " + _windowHandle  );

		__log.info( "end findWindow" );
		
		return _windowExists;
	}


	public boolean bringWindowToTop() 
	{
		boolean activated = false;

		__log.info( "begin bringWindowToTop" );

		if( _windowExists )
		{
			try {
				// TODO: find out which OS API activates the window.
				// any of those four should do its purpose
				
				OS.SetFocus( _windowHandle );
				OS.SetForegroundWindow( _windowHandle );
				OS.SetActiveWindow( _windowHandle );
				OS.BringWindowToTop( _windowHandle );

				// client is iconified...
				if( OS.IsIconic( _windowHandle ) ) 
				{
					__log.info(	"bringWindowToTop: isn't it iconic, don't you think?" );
					try 
					{
						_user32.OpenIcon( _windowHandle );
						__log.info( "bringWindowToTop: window should be deiconified" );
					} catch( Exception ex ) {
						__log.error( "deiconify failed. ", ex );
					}
				}
				__log.info( "window activated" );
				activated = true;
			} catch( Exception ex ) {
				__log.error( "bringWindowToTop  failed", ex );
			}
		} else {
			__log.warn( "no window handle exists" );
		}

		__log.info( "end bringWindowToTop" );
		return activated;
	} // method bringWindowToTop
	
} // class WindowMan
