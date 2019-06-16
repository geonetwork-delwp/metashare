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
import java.io.FileOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import au.csiro.utils.Xml;
import au.csiro.utils.XmlResolver;

import org.jibx.runtime.JiBXException;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import jeeves.JeevesJCS;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileXMLWriter {

 private static final Logger logger = LogManager.getLogger("main");

 public static void main( String args[] ){

    String oasisCatalogFile = "schemas/iso19115-3/src/main/plugin/iso19115-3/oasis-catalog.xml";

    try {
      JeevesJCS.setConfigFilename("src/main/config/cache.ccf");
      JeevesJCS.getInstance(XmlResolver.XMLRESOLVER_JCS);
    } catch (Exception ce) {
      logger.error("Failed to create cache for schema files");
    }

    String hostNameForLinks = "https://dev-metashare.maps.vic.gov.au/geonetwork/srv/eng/";

		IMarshallingContext mctx = getMarshallingContext( );
		
		Session src = new Configuration( ).configure("SourceDB.cfg.xml").buildSessionFactory( ).openSession( );

    Options options = new Options();
    options.addOption("h", true, "Optional: Specify host name for linkages to metadata records. If not specified then https://dev-metashare.delwp.vic.gov.au/geonetwork/srv/eng/ will be used.");
    options.addOption("q", true, "Optional: Specify a query condition eg. ANZLIC_ID = 'ANZVI0803002511' for debugging purposes.");
    options.addOption("s", false, "Optional: Skip validation. Useful for debugging because reading the schemas takes some time.");
    options.addRequiredOption("d", "directory", true, "Specify the directory name for output xml files.");
    options.addRequiredOption("c", "contact_directory", true, "Specify the directory name for output contact xml fragment files.");

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = null;

    String header = "Convert metashare metadata from database to XML\n\n";
 
    HelpFormatter formatter = new HelpFormatter();

    try {
      cmd = parser.parse(options, args);
    } catch (Exception e) {
      formatter.printHelp("metartool", header, options, null, true);
      System.exit(1);
    }

    // Process the hostname if specified
    if (cmd.hasOption("h")) {
      hostNameForLinks = cmd.getOptionValue("h");
    }
    
		/* Process output directory */
		String path = cmd.getOptionValue("d");
		/* Append final separator character if not already suppled */
		if(!path.endsWith(File.separator)) path += File.separator;
    File op = new File(path + "invalid");
    op.mkdirs(); // creates both output directory and invalid directory if they don't exist

		/* Process output directory for contact fragments */
		String contactpath = cmd.getOptionValue("c");
    File ocp = new File(contactpath);
    ocp.mkdirs();

    System.setProperty("XML_CATALOG_FILES", oasisCatalogFile);
    Xml.resetResolver();

	  HashSet uuidSet = new HashSet();
	
		/* Fetch list of (or iterator over?) datasets from Oracle DB */
		String HQL = "FROM Dataset WHERE ( ANZLIC_ID IS NOT NULL AND name IS NOT NULL AND title IS NOT NULL AND NAME NOT LIKE 'CIP%' )"; // Build a HQL query string from command line arguments plus some default
		if( cmd.hasOption("q")) {
      String query = cmd.getOptionValue("q");
      if (query.matches(".*\\S+.*")) HQL += " AND " + query;
    }
    logger.info("Requesting metashare records using:\n" + HQL);

		ArrayList datasets = (ArrayList) src.createQuery( HQL ).list( );
		
		try {
      for( int i = 0; i < datasets.size(); ++i ){
        Dataset d = (Dataset) datasets.get( i ); 
        d.hostNameForLinks = hostNameForLinks;
				
				d.UUID = d.generateUUID( ); // generate new UUID for dataset
				logger.info("Processing Dataset\n"+
           "        '" + d.Name + "'\n"+
           "        ANZLIC_ID " + d.ANZLIC_ID + "\n" +
           "        UUID: " + d.UUID);
        if (uuidSet.contains(d.UUID)) {
          logger.error("Sha1 CLASH: "+d.ANZLIC_ID);
          System.exit(1);
        } else {
          uuidSet.add(d.UUID);
        } 
			
        boolean xmlIsValid, contentIsValid = true;	
				/* Transform Dataset instance to XML */
				try {
					StringWriter sw = new StringWriter( );
	        mctx.marshalDocument( d, "utf-8", Boolean.FALSE, sw );
					
          // Now run the created XML through a postprocessing XSLT which does
          // things like add the GML polygons (if anzlic_id matches)
          Element mdXml = Xml.loadString(sw.toString(), false);
          Map<String,String> xsltparams = new HashMap<String,String>();
          xsltparams.put("anzlicid", d.ANZLIC_ID);
			    logger.debug("Transforming "+d.UUID );
          Element result = Xml.transform(mdXml, "data" + File.separator + "insert-gml.xsl",  xsltparams);

          //logger.debug("Result was \n"+Xml.getString(result));

          // Now validate the transformed result
          xmlIsValid = true;
          if (cmd.hasOption("s")) {
					  logger.error("Validation is skipped.");
          } else {
            try {
					    Xml.validate(result);
            } catch (Exception e) {
					    logger.error("Validation of '" + d.Name + "' ("+d.UUID+") with ANZLIC_ID "+d.ANZLIC_ID+", against http://schemas.isotc211.org/19115/-3/mdb/2.0 FAILED:" );
              logger.error("\n"+e.getMessage());
              xmlIsValid = false;
            }
          }

					/* Write XML out to file */
          String outputFile = path;
          if (!xmlIsValid) {
            outputFile += "invalid" + File.separator;
          } 
          outputFile += d.UUID + ".xml";
           
          
          XMLOutputter out = new XMLOutputter();
          Format f = Format.getPrettyFormat();  
          f.setEncoding("UTF-8");
          out.setFormat(f);  
          FileOutputStream fo = new FileOutputStream(outputFile);
          out.output(result, fo);
          fo.close();

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
		} catch( org.hibernate.HibernateException e ) {
			logger.error( "Hibernate exception occurred" );
			System.exit( 1 );
		}

		/* Fetch list of contacts from Oracle DB */
		HQL = "FROM Contact"; 
		ArrayList contacts = (ArrayList) src.createQuery( HQL ).list( );

    contacts.add(DatasetContact.getDefault().contact);
		
		try {
      for( int i = 0; i < contacts.size(); ++i ){
        Contact c = (Contact)contacts.get(i);
        logger.debug("Processing contact "+c.getContactID());
				try {
					StringWriter sw = new StringWriter( );
	        mctx.marshalDocument( c, "utf-8", Boolean.FALSE, sw );
          Element cXml = Xml.loadString(sw.toString(), false);
         
          String outputFile = contactpath + File.separator + c.getContactID() + ".xml";

          XMLOutputter out = new XMLOutputter();
          Format f = Format.getPrettyFormat();  
          f.setEncoding("UTF-8");
          out.setFormat(f);  
          FileOutputStream fo = new FileOutputStream(outputFile);
          out.output(cXml, fo);
          fo.close();

				} catch( JiBXException e ){
					/* This usually due to data problems such as unexpected nulls or
					 * referential integrity failures. Once a JiBXException occurs, JiBX's
					 * state is corrupted, hence it must be reinitialised */
					logThrowableMsgStack( e.getRootCause( ), c.getContactID() );
					mctx = getMarshallingContext( );					
				} catch( Exception e ) {
					/* Write exception info to console, then continue processing next dataset record */
					logThrowableMsgStack( e, c.getContactID() );
				} finally {
					// Have finished with these elements, so purge them from the session
					//dest.evict( m );
					src.evict( c );
				}
			} // for each contact				
		} catch( org.hibernate.HibernateException e ) {
			logger.error( "Hibernate exception occurred" );
			System.exit( 1 );
		} finally {
			src.close( );
		}

	}
	
	
	private static void logThrowableMsgStack( Throwable t, String objName ){

		StringBuffer ps = new StringBuffer();
		
		ps.append( "Problem whilst processing dataset '" + objName + "'" );
		do ps.append( "Cause: " + t.getMessage( ) + " - " + t.getClass( ).getName( ) );
		while( ( t = t.getCause( ) ) != null );
		ps.append( "Processing of '" + objName + "' aborted" );

    logger.error(ps.toString());
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
