package webrunner.server.jetty;

import org.eclipse.swt.graphics.Image;

import webrunner.utils.DesktopUtils;

public class JettyStartEx extends JettyStart 
{
	public JettyStartEx( JettyMan man )
	{
		super( man );
	}
	
	@Override
	public void openStartPage()
	{
        DesktopUtils.openBrowser( _man._serverHost + "/test" );		
	}

	@Override
	public Image createSplashImage()
	{
		// fix: return dummy image
		return null;
	}	

	@Override
	public Image[] createTrayImages()
	{
		// fix: return dummy images/icons
	    return null;	
	}

	@Override
	public String createTrayTitle()   // rename to getTrayTitle ??
	{
		return "Tray Title";
	}
	
	@Override
	public void onServerCreateBefore()  {}
	@Override
	public void onServerCreateAfter()   {}
	@Override
	public void onServerStartBefore()  {}
	@Override
	public void onServerStartAfter()   {}
	
	
} // class JettyStartEx
