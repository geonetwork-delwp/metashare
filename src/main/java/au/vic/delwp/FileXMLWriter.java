package au.gov.vic.delwp;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import java.io.StringWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;

import org.jibx.runtime.JiBXException;

public class FileXMLWriter {
    public static void main( String args[] ){

		IMarshallingContext mctx = getMarshallingContext( );
		
		Session src = new Configuration( ).configure("SourceDB.cfg.xml").buildSessionFactory( ).openSession( );
/*
		Session dest = new Configuration( ).configure("TargetDB.cfg.xml").buildSessionFactory( ).openSession( );
*/

		/* Process output directory if specifed, otherwise use current directory as default */
		String path = "." + File.separator;
		if( args.length > 0 && args[0].matches(".*\\S+.*") ){
			path = args[0];
			/* Append final separator character if not already suppled */
			if( ! path.endsWith( File.separator ) ) path += File.separator;
			}
		
		/* Fetch list of (or iterator over?) datasets from Oracle DB */
		String HQL = "FROM Dataset WHERE ( ANZLIC_ID IS NOT NULL AND name IS NOT NULL AND title IS NOT NULL AND NAME NOT LIKE 'CIP%' )"; // Build a HQL query string from command line arguments plus some default
		if( args.length > 1 && args[1].matches(".*\\S+.*") ) HQL += " AND " + args[1];
		ArrayList datasets = (ArrayList) src.createQuery( HQL ).list( );
		
		try {
            for( int i = 0; i < datasets.size(); ++i ){
                Dataset d = (Dataset) datasets.get( i ); // Fetch dataset object from the list of datasets
				System.out.println("Processing Dataset '" + d.Name + "' " + d.ANZLIC_ID);
				
				/* See if matching metadata_profile record already exists (query on DatasetID property) */
                ANZMetadataProfile m;
/*
				Query q = dest.createQuery( "FROM ANZMetadataProfile WHERE DatasetID = ?" );
				q = q.setShort( 0, d.ID );
				m = (ANZMetadataProfile) q.uniqueResult( );
				if( m == null ){ // No pre-existing record
*/
					m = new ANZMetadataProfile( );
					d.UUID = Dataset.generateUUID( ); // generate new UUID for dataset
					/* set metadata attributes */
					m.UUID = d.UUID; // also apply to new metadata record
					m.DatasetID = d.ID;
					m.ANZLIC_ID = d.ANZLIC_ID;
					m.Datestamp = d.LastUpdated; // use dataset update date for date stamp
					m.LastUpdated = d.LastUpdated; // Added on 28th Aug 08
/*
					}
				else { // Pre-existing record - use existing UUID and date
				    m.LastUpdated = d.LastUpdated; // Added on 28th Aug 08
					d.UUID = m.UUID;
					//d.LastUpdated = m.Datestamp;  -- Commented on 28th Aug 08
					}
*/
				
				/* Transform Dataset instance to XML */
				try {
					StringWriter sw = new StringWriter( );
	                mctx.marshalDocument( d, "utf-8", Boolean.FALSE, sw );
					
	                /* Update other metadata profile attributes which may change and persist */
					m.Name = d.Name;
					m.Title = d.Title;
					//m.LastUpdated = d.LastUpdated; -- Commented on 28th Aug 08
					m.XML = sw.toString( );
					System.out.println("Validating '" + d.Name + "' against http://www.isotc211.org/2005/gmd/gmd.xsd :" );
					m.XMLIsValid = ISO19139Validator.isValid( m.XML );
					System.out.println( );
					System.out.println("Validating '" + d.Name + "' against ANZLIC Metadata Schematron:");
					m.ContentIsValid = ANZLICSchematronValidator.isValid( m.XML );

					/* Write XML out to file */
					FileWriter fw = new FileWriter( path + d.UUID + ".xml" );
					fw.write( m.XML );
					fw.close( );
					}
				catch( JiBXException e ){
					/* This usually due to data problems such as unexpected nulls or
					 * referential integrity failures. Once a JiBXException occurs, JiBX's
					 * state is corrupted, hence it must be reinitialised */
					logThrowableMsgStack( e.getRootCause( ), d.Name );
					mctx = getMarshallingContext( );					
					}
				catch( Exception e ){
					/* Write exception info to console, then continue processing next dataset record */
					logThrowableMsgStack( e, d.Name );
					}
				finally {
					// Have finished with these elements, so purge them from the session
					//dest.evict( m );
					src.evict( d );
					}
				} // for each dataset				
			}
		catch( org.hibernate.HibernateException e ){
			System.out.println( "Hibernate exception occurred" );
			logThrowableMsgStack( e, "N/A" );
			System.exit( 1 );
			}
		finally {
			src.close( );
			//dest.close( );
			}
		}
	
	
	private static void logThrowableMsgStack( Throwable t, String objName ){

		java.io.PrintStream ps = System.out;
		
		ps.println( "Problem whilst processing dataset '" + objName + "'" );
		t.printStackTrace(ps);
		do ps.println( "Cause: " + t.getMessage( ) + " - " + t.getClass( ).getName( ) );
		while( ( t = t.getCause( ) ) != null );
		ps.println( "Processing of '" + objName + "' aborted" );
		}

		
	private static IMarshallingContext getMarshallingContext( ){
		//Following lines of code reads Jibx binding information and creates context from file binding.xml
		IMarshallingContext mctx;
		try {
			IBindingFactory bfact = BindingDirectory.getFactory( Dataset.class );
			try {
				mctx = bfact.createMarshallingContext( );
				}
			catch( JiBXException e ){
				throw new RuntimeException("Problem instantiating JiBX Marshalling Context", e );
				}
			}
		catch( JiBXException e ){
			throw new RuntimeException("Problem instantiating JiBX Binding Factory", e );
			}
		mctx.setIndent( 1 );
		return mctx;
		}
	}
