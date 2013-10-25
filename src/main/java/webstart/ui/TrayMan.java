package webstart.ui;

import org.apache.log4j.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;



public class TrayMan 
{
  final static Logger __log = Logger.getLogger( TrayMan.class );

  protected Display   _display;
  protected Shell     _shell;
  protected Tray      _tray;
  protected TrayItem  _trayItem;
  protected String    _title;
  
  public TrayMan( Display display, Shell shell, String title )
  {
	  _display = display;
	  _shell   = shell;
	  _title   = title;

	  _tray    = _display.getSystemTray();

	  _trayItem = new TrayItem( _tray, SWT.NONE );
  }
	
} // class TrayMan
