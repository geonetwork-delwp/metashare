package au.gov.vic.delwp;

import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.FileNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContactRole {

  private static final Logger logger = LogManager.getLogger("contact");

	static protected HashMap RoleTranslations = new HashMap( );

	public int ID;
	protected String Description;	
	// public java.util.Set Contacts = new java.util.HashSet( );
	
	// Initialise the contents of the RoleTranslations (ANZLIC Codes -> ISO CI_RoleCodes) Map
	static {
		MapUtils.Populate( "reftab__ms2ap_role.txt", RoleTranslations, "\\d+\\|.+" );
		}

	public String getRole( ){
    logger.debug("ROLE: "+ID+" looked up "+(String) RoleTranslations.get( ID+"" ));
		return (String) RoleTranslations.get( ID+"" );
		}
	}
