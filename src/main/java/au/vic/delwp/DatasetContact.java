package au.gov.vic.delwp;

import java.io.InputStream;
import java.util.Properties;

public class DatasetContact {

	//private static final String propPrefix = "classes" + java.io.File.separator;
	public DatasetContactID ID;
	public Contact contact;
	public Dataset dataset;
	public ContactRole role;

  private static ClassLoader classLoader = DatasetContact.class.getClassLoader();

	protected static DatasetContact Default;
	
	static {
		Properties p = new Properties( );
		try{
			//p.load( new java.io.FileInputStream( propPrefix + "DefaultContact.properties" ) );
      InputStream inputStream = classLoader.getResourceAsStream("DefaultContact.properties");
			p.load( inputStream );
			}
		catch( java.io.FileNotFoundException e ){
			throw new RuntimeException( "Could not find default contact properties file", e );
			}
		catch( java.io.IOException e ){
			throw new RuntimeException( "Error reading default contact properties file", e );
			}
		catch( Exception e ){
			throw new RuntimeException( "Problem while attempting to load default contact details", e );
			}
		
		ContactRole r = new ContactRole( );
		r.ID = Short.parseShort( p.getProperty("Role") );

		Contact c = new Contact( );
		c.Position = p.getProperty("Position");

		c.Organisation = new IDnText( );
		c.Organisation.Text = p.getProperty("Organisation");
		c.Phone = p.getProperty("Phone");
		c.Fax = p.getProperty("Fax");
		c.Email = p.getProperty("Email");
		c.Mail1 = p.getProperty("Address");
		c.Locality = p.getProperty("City");
		c.State = p.getProperty("State");
		c.Postcode = p.getProperty("Postcode");
		c.Country = p.getProperty("Country");
		
		Default = new DatasetContact( );
		Default.role = r;
		Default.contact = c;
		Default.dataset = null;
		}
	
	public static DatasetContact getDefault( ){
		return Default;
		}

	}
