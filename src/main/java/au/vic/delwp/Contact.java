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

  public String getUUID() {
    return Utils.generateContactUUID(getContactID());
  }	
	
  public String getTitle() {
    if (StringUtils.isEmpty(getName())) {
      return Organisation.Text;
    } else {
      return getName() + " @ " + Organisation.Text;
    }
  }	

	public String getName( ){
		String s = "";
		
		if( !Utils.isBlank(LastName) ) s +=  LastName;
		if( !Utils.isBlank(FirstName) ) s += (s.length( ) == 0 ? "" : " ") + FirstName;
		if( !Utils.isBlank(Salutation) ) s += (s.length( ) == 0 ? "" : " ") + Salutation;
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

		if (phone_plus_area.equals(".") || Utils.isBlank(phone_plus_area)) {
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

		if (fax_plus_area.equals(".") || Utils.isBlank(fax_plus_area)) {
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
		return !Utils.isBlank(getName());
		}

	
	// TODO: check not null constraints on these fields
	// Added not null constraints on Mail1 and Mail2  -- Dated 7th Aug 08
	public String getPostalAddress( ){
		String s = "";
		if( !Utils.isBlank(Mail1) ){
			if( !Utils.isBlank(Mail2) ){
				s += Mail1.trim() + " " + Mail2.trim();
			} else {
				s = Mail1.trim();
			}
		} else {
			if( !Utils.isBlank(Mail2) ) {
				s += Mail2.trim();
			}
		}
		return s;
	}	
	
	public boolean isPostNotNull( ){
		return !Utils.isBlank(getPostalAddress());
		}

  public boolean isIndividualNotNull() {
    return isPositionNotNull() || isNameNotNull() || isEmailOrPhoneNotNull();
  }
	
	public boolean isPositionNotNull( ){
		return !Utils.isBlank(Position);
		}
	
	public String getPosition( ){
		return Position.trim();
		}
	
	public boolean isContactInfoNotNull( ){
		return isAddressNotNull();
		}
		
	public String getCountry( ){
		return Country.trim();
		}
	
	public String getState( ){
		return State.trim();
		}
	
	public String getCity( ){
		return Locality.trim();
		}
	
	public String getPostCode( ){
		return Postcode.trim();
		}
	
	public String getEmail( ){
		return Email.trim();
		}
	
	public boolean isAddressNotNull( ){
		return isCountryNotNull() || isStateNotNull() || isCityNotNull()
			|| isPostCodeNotNull() || isPostNotNull( );
		}
	
	public boolean isCountryNotNull( ){
		return !Utils.isBlank(Country);
		}	
		
	public boolean isStateNotNull( ){
		return !Utils.isBlank(State);
		}	
		
	public boolean isCityNotNull( ){
		return !Utils.isBlank(Locality);
		}	
		
	public boolean isPostCodeNotNull( ){
		return !Utils.isBlank(Postcode);
		}	
		
	public boolean isPhoneNotNull( ){
		return ( isVoiceNotNull() || isFaxNotNull() );
		}
		
	public boolean isEmailOrPhoneNotNull( ){
    return isEmailNotNull() || isPhoneNotNull();
  }
		
	public boolean isEmailNotNull( ){
		return !Utils.isBlank(Email);
		}
	
	public String getContactID( ){
		return ""+ID;
		}

	}
