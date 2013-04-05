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

import webrunner.ui.TrayMan;

public class ServerTrayMan extends TrayMan {

	final static Logger __log = Logger.getLogger( ServerTrayMan.class );
	
	ServerMan _man;
		
	Image     _iconStarting;
	Image     _iconRunning;
	Image     _iconStopping;
	
	public ServerTrayMan( ServerMan man, Display display, Shell shell, String title, Image[] icons )
	{
		 super( display, shell, title );
		 
		 _man     = man;  

		 _iconStarting = icons[0];
		 _iconRunning  = icons[1];
		 _iconStopping = icons[2];
		  
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
			  _man.onExit();			  
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
	}
	
} // class ServerTray