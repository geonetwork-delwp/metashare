package au.gov.vic.delwp;

import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.FileNotFoundException;


public class ContactRole {

	static protected HashMap RoleTranslations = new HashMap( );

	public short ID;
	protected String Description;	
	// public java.util.Set Contacts = new java.util.HashSet( );
	
	// Initialise the contents of the RoleTranslations (ANZLIC Codes -> ISO CI_RoleCodes) Map
	static {
		MapUtils.Populate( "reftab__ms2ap_role.txt", RoleTranslations, "\\d+\\|.+" );
		}

	public String getRole( ){
		return (String) RoleTranslations.get( Short.toString( ID ) );
		}
	}
