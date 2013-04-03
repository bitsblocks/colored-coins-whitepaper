package webrunner.server;


import java.util.List;

import org.apache.log4j.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class ServerTray {

	final static Logger __log = Logger.getLogger( ServerTray.class );
	
	ServerMan _man;
	
	Display   _display;
	Shell     _shell;
	Tray      _tray;
	TrayItem  _trayItem;
	String    _title;
	
	Image     _iconStarting;
	Image     _iconRunning;
	Image     _iconStopping;
	
	public ServerTray( ServerMan man, Display display, Shell shell, String title, Image iconStarting, Image iconRunning, Image iconStopping )
	{
		  _man     = man;  
		
		  // fix/todo: get shell, dipslay, title from _man ???
		  _display = display;
		  _shell   = shell;
		  
		  _title   = title;

		  _iconStarting = iconStarting;
		  _iconRunning  = iconRunning;
		  _iconStopping = iconStopping;
		  
		  _tray    = _display.getSystemTray();

		  _trayItem = new TrayItem( _tray, SWT.NONE );
		  
		  starting();
	}
	
	public void starting()
	{
	  _trayItem.setImage( _iconStarting );
	  _trayItem.setToolTipText( _title + " startet..." );		
	}
	
	public void running()
	{
	  _trayItem.setImage( _iconRunning );
	  _trayItem.setToolTipText( _title + " Betriebsbereit" );		  		
	}
		
	
	public void buildMenu( final String serverHost, List items )  // rename to open or openMenu?
	{
	     final Menu menu = new Menu( _shell, SWT.POP_UP );

		 MenuItem menuItem = null;
		 
		 // todo/fix: 
		 //   build menuitems for (nested) list items
		 
		 // menuItem = new MenuItem( menu, SWT.SEPARATOR );
		 
		  menuItem = new MenuItem( menu, SWT.PUSH );
		  menuItem.setText( "Server beenden" );
		  menuItem.addListener( SWT.Selection, new Listener () {			
			public void handleEvent( Event ev ) {				
			  cleanUpAndSayGoodBye();			  
			}
		  });
		  		  
		  _trayItem.addListener( SWT.MenuDetect, new Listener () {
			public void handleEvent(Event ev) {
				menu.setVisible( true );
			}
		  });
	}

	public void cleanUpAndSayGoodBye()
	{
	   __log.info( "server says goodbye; und tschuess." );
  	   
	   _trayItem.setImage( _iconStopping );
	   _trayItem.setToolTipText( "Stopping... Bye" );
	   _trayItem.dispose(); // cleanup;
          
	    System.exit( 0 );							  			
	}
	
	
} // class ServerTray

