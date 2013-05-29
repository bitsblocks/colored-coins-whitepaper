package webstart.utils;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;


public class ClassPathUtils 
{
	final static Logger __log = Logger.getLogger( ClassPathUtils.class );			


    static public String buildClassPath( String root )
    {
    	String includes[] = { ".jar" };
    	String excludes[] = { };
    	return buildClassPath( root, includes, excludes );
    }
    
    static public String buildClassPath( String root, String excludes[] )
    {
    	String includes[] = { ".jar" };
    	return buildClassPath( root, includes, excludes );
    }
    
	static public String buildClassPath( String root, String includes[], final String excludes[] )
	{
			__log.info( "  buildClassPath | root=" + root );
			
			File rootFile = new File( root );
			
			FilenameFilter jarFilter = new ClassPathFilter( includes, excludes );
			File jarFiles[] = rootFile.listFiles( jarFilter );

			StringBuilder classPath = new StringBuilder(); 

			if( jarFiles == null )
			{
				__log.warn( "  *** buildClassPath.listFiles() returns null; does the folder exist?" );
			}
			else
			{				
			    for( File jar : jarFiles )
			    {	
			       try
			       {
				     String jarPath         = jar.getCanonicalPath();			  
				     __log.info( "  buildClassPath | add to classPath: " + jarPath );			  			  
				  
				     if( classPath.length() > 0 ) 
					   classPath.append( ";" );
						 
				     classPath.append( jarPath );
			       } 
			       catch( IOException ioex )
			       {
			    	   __log.error( " buildClassPath | getCanonicalPath failed", ioex );
			    	   // rethrow IOException from File#getCanonicalPath as wrapped runtime exception
			    	   throw new RuntimeException( ioex );
			       }
			    }
			}
			
			__log.info( "  buildClassPath | classPath=" + classPath.toString() );
		 		 
           return classPath.toString();		 
	 }
	
    static class ClassPathFilter implements FilenameFilter 
    {
		List<String> _includeExtensions = new ArrayList<String>();
		List<String> _includeNames      = new ArrayList<String>();
		List<String> _excludeExtensions = new ArrayList<String>();
		List<String> _excludeNames      = new ArrayList<String>();

    	public ClassPathFilter( String includes[], String excludes[] )
    	{
			for( String include : includes ) {
			   if( include.startsWith( "." ) )  // assume extension if it starts w/ .
				   _includeExtensions.add( include );
			   else
				   _includeNames.add( include );
			}
			
			for( String exclude : excludes ) {
			   if( exclude.startsWith( "." ) )  // assume extension if it starts w/ .
				   _excludeExtensions.add( exclude );
			   else
				   _excludeNames.add( exclude );
			}
    	}
    	
		public boolean accept( File dir, String name ) 
		{
		   for( String include : _includeExtensions )  {
			   if( name.toLowerCase().endsWith( include ) )
			   {
				   __log.debug( "  buildClassPath | include match extension " + include + ": " + name );
				   return true;
			   }
		   }

		   for( String include : _includeNames )  {
			   if( name.equalsIgnoreCase( include ) )
			   {
				   __log.debug( "  buildClassPath | include match name " + include + ": " + name );
				   return true;
			   }
		   }
		   
		   for( String exclude : _excludeExtensions )  {
			   if( name.toLowerCase().endsWith( exclude ) )
				   return false;
		   }

		   for( String exclude : _excludeNames )  {
			   if( name.equalsIgnoreCase( exclude ) )
			      return false;
		   }

		   __log.warn( "   *** buildClassPath | skipping file: " + name );
		   return false;		   
		}
    }  //  class ClassPathFilter
	
} // class ClassPathUtils
