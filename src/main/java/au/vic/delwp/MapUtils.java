package au.gov.vic.delwp;

import java.util.Arrays;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.FileNotFoundException;

public class MapUtils {

	private static final String dataPrefix = "data" + File.separator;
	
	
	protected static void Populate( String filename, Map m ){
	
		Populate( filename, m, ".+\\|.+" );
		}	
	
	protected static void Populate( String filename, Map m, String pattern ){

		BufferedReader br;
		try {
			br = new BufferedReader( new FileReader( dataPrefix + filename ) );
			}
		catch( FileNotFoundException e ){
			throw new RuntimeException( "Error initialising application - translation file '" + filename + "' not found", e );
			}
			
		try {
			String line = br.readLine( );
			while( line != null ){
				// This could be optimised for performance by compiling the pattern strings into a RegEx
				if( line.matches( pattern ) ){ // if line is determined have some appropriate content...					
					String[] fields;
					fields = line.split( "\\|" );
          if (fields.length == 2) {
					  m.put( fields[0], fields[1] );
          } else {
            m.put( fields[0], Arrays.copyOfRange(fields, 1, fields.length) );
          }
				}
				line = br.readLine( );
			}
			br.close( );
		} catch( IOException e ) {
			throw new RuntimeException( "Error initialising application - error reading translation file", e );
		}
	}

		
	protected static void PopulateScopeCodeMap( String filename, Map m ){

		BufferedReader br;
		try {
			br = new BufferedReader( new FileReader( dataPrefix + filename ) );
			}
		catch( FileNotFoundException e ){
			throw new RuntimeException( "Error initialising application - translation file '" + filename + "' not found", e );
			}
			
		try {
			String line = br.readLine( );
			while( line != null ){
				if( line.matches(".*\\|.*\\|.+") ){ // if line appears to have some useful content...
					String[] fields;
					fields = line.split( "\\|" );
					// Fold "" into null as this is what the DB will be providing
					if( fields[0].length( ) == 0 ) fields[0] = null;
					if( fields[1].length( ) == 0 ) fields[1] = null;
					m.put( new ScopeCodePair( fields[0], fields[1] ), fields[2] ); 
					}
				line = br.readLine( );
				}
			br.close( );
			}
		catch( IOException e ){
			throw new RuntimeException( "Error initialising application - error reading translation file", e );
			}
		}

		
	protected static void PopulateMulti( String filename, Map m ){

		BufferedReader br;
		try {
			br = new BufferedReader( new FileReader( dataPrefix + filename ) );
			}
		catch( FileNotFoundException e ){
			throw new RuntimeException( "Error initialising application - translation file '" + filename + "' not found", e );
			}
			
		try {
			String line = br.readLine( );
			while( line != null ){
				if( line.matches(".*\\S+\\|.+") ){ // if line appears to have some useful content...
					List l;
					String[] fields;
					fields = line.split( "\\|" );
					if( ! m.containsKey( fields[0] ) ){
						l = new LinkedList( );
						m.put( fields[0], l );					
						}
					else l = (List) m.get( fields[0] );					
					l.add( fields[1] );
					}
				line = br.readLine( );
				}
			br.close( );
			}
		catch( IOException e ){
			throw new RuntimeException( "Error initialising application - error reading translation file", e );
			}
		}
	
	
	}
