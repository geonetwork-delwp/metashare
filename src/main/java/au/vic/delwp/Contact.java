package au.gov.vic.delwp;

import org.apache.commons.lang3.StringUtils;

public class Contact {

	public int ID;
	public String Salutation;
	public String FirstName;
	public String LastName;
	public String Position;
	public IDnText Unit;
	public IDnText Organisation;
	public IDnText Division;
	public String Phone;
	public String Fax;
	public String Email;
	public String Mail1;
	public String Mail2;
	public String Locality;
	public String State;
	public String Postcode;
	public String Country;
		
	// public java.util.Set Datasets = new java.util.HashSet( );
	
	
	public String getName( ){
		String s = "";
		
		if( LastName != null ) s +=  LastName;
		if( FirstName != null ) s += (s.length( ) == 0 ? "" : " ") + FirstName;
		if( Salutation != null ) s += (s.length( ) == 0 ? "" : " ") + Salutation;
		return s.length( ) == 0 ? null : s;
		}

  public boolean isVoiceNotNull() {
    return !StringUtils.isEmpty(getPhone());
  }	

  public boolean isFaxNotNull() {
    return !StringUtils.isEmpty(getFax());
  }	

	public String getPhone(){
		String phone_plus_area = Phone;

		if (phone_plus_area == null){
			return "";
			}

		phone_plus_area = phone_plus_area.trim();

		if (phone_plus_area.equals("") || phone_plus_area.equals(".")){
			return "";
			}

		if (!phone_plus_area.startsWith("+61 3")){
			if (phone_plus_area.startsWith("(03)") || phone_plus_area.startsWith("03")){
				phone_plus_area = phone_plus_area.replaceAll("^\\s*\\(?03\\)?\\s*", "+61 3 ");
				}
			else{
				phone_plus_area = "+61 3 " + phone_plus_area;
				}
			}

		return phone_plus_area;
		}

	public String getFax(){
		String fax_plus_area = Fax;
	
		if (fax_plus_area == null){
			return "";
			}

		fax_plus_area = fax_plus_area.trim();

		if (fax_plus_area.equals("") || fax_plus_area.equals(".")){
			return "";
			}

		if (!fax_plus_area.startsWith("+61 3")){
			if (fax_plus_area.startsWith("(03)") || fax_plus_area.startsWith("03")){
				fax_plus_area = fax_plus_area.replaceAll("^\\s*\\(?03\\)?\\s*", "+61 3 ");
				}
			else{
				fax_plus_area = "+61 3 " + fax_plus_area;
				}
			}

		return fax_plus_area;
		}
	
	
	public boolean isNameNotNull( ){
		return getName( ) != null;
		}

	
	// TODO: check not null constraints on these fields
	// Added not null constraints on Mail1 and Mail2  -- Dated 7th Aug 08
	public String getPostalAddress( ){
		String s = "";
		if( Mail1 != null && Mail1 != "null" && Mail1 != ""  && Mail1 != " " && Mail1.length() != 0 ){
			if( Mail2 != null && Mail2 != "null" && Mail2 != ""  && Mail2 != " " && Mail2.length() != 0 ) {
				s += Mail1.trim() + " " + Mail2.trim();
			} else {
				s = Mail1.trim();
			}
		} else {
			if( Mail2 != null && Mail2 != "null" && Mail2 != ""  && Mail2 != " " && Mail2.length() != 0 ) {
				s += Mail2.trim();
			}
		}
		if ( (s.trim().length()==0) ) {
			s="Unknown";
		}
		return s;
	}	
	
	public boolean isPostNotNull( ){
		return getPostalAddress( ) != null;
		}

  public boolean isIndividualNotNull() {
    return isPositionNotNull() && isNameNotNull();
  }
	
	public boolean isPositionNotNull( ){
		return getPosition() != null && !getPosition().equals("") && !getPosition().equals("null");
		}
	
	public String getPosition( ){
		return ((Position != null) ? Position.trim() : null);
		}
	
	public boolean isContactInfoNotNull( ){
		return isAddressNotNull( ) || isPhoneNotNull( );
		}
		
	public String getCountry( ){
		return ((Country != null) ? Country.trim() : null);
		}
	
	public String getState( ){
		return ((State != null) ? State.trim() : null);
		}
	
	public String getCity( ){
		return ((Locality != null) ? Locality.trim() : null);
		}
	
	public String getPostCode( ){
		return ((Postcode != null) ? Postcode.trim() : null);
		}
	
	public String getEmail( ){
		return ((Email != null) ? Email.trim() : null);
		}
	
	public boolean isAddressNotNull( ){
		return isCountryNotNull() || isStateNotNull() || isCityNotNull()
			|| isPostCodeNotNull() || isPostNotNull( );
		}
	
	public boolean isCountryNotNull( ){
		return getCountry() != null && !getCountry().equals("") && !getCountry().equals("null");
		}	
		
	public boolean isStateNotNull( ){
		return getState() != null && !getState().equals("") && !getState().equals("null");
		}	
		
	public boolean isCityNotNull( ){
		return getCity() != null && !getCity().equals("") && !getCity().equals("null");
		}	
		
	public boolean isPostCodeNotNull( ){
		return getPostCode() != null && !getPostCode().equals("") && !getPostCode().equals("null");
		}	
		
	public boolean isPhoneNotNull( ){
		return ( isVoiceNotNull() || isFaxNotNull() );
		}
		
		
	public boolean isEmailNotNull( ){
		return getEmail() != null && !getEmail().equals("") && !getEmail().equals("null");
		}
	
	public String getContactID( ){
		return ""+ID;
		}

	}
