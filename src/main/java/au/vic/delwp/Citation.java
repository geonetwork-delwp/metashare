package au.gov.vic.delwp;

public class Citation {

	public int ID;
	public String Author;
	public String Title;
	public String SeriesTitle;
	public String Pages;
	public String CorporateAuthor;
	public String PublisherName;
	public String YearPublished;
	public String Isbn;
	public String AdditionalDetail;
	
	public String getCorporateAuthor(){
		return CorporateAuthor;
		}

	public String getOrganisationName(){
		String organisationName = CorporateAuthor;

		if (PublisherName != null && !PublisherName.equals(CorporateAuthor) && PublisherName.trim().length()!=0){
			organisationName += " - " + PublisherName;
			}

		return organisationName;
		}

	public String getAuthor(){
		return Author;
  }

	public String getOtherCitationDetails(){
			return AdditionalDetail;
		}

	public boolean isAuthorNotNull( ){
		return Author != null;
		}

	public boolean isTitleNotNull( ){
		return Title != null;
		}

	public boolean isSeriesTitleNotNull( ){
		return SeriesTitle != null;
		}

	public boolean isPagesNotNull( ){
		return Pages != null;
		}

	public boolean isCorporateAuthorNull( ){
		return !isCorporateAuthorNotNull();
		}

	public boolean isCorporateAuthorNotNull( ){
		return CorporateAuthor != null && CorporateAuthor.trim().length()!=0;
		}

	public boolean isPublisherNameNotNull( ){
		return PublisherName != null;
		}

	public boolean isYearPublishedNotNull( ){
		return YearPublished != null;
		}

	/*public boolean isIsbnNotNull( ){
		return Isbn != null;
		}*/
	// Method modified to handle spaces dated 18-08-08
	public boolean isIsbnNotNull( ){
		if (Isbn == null || Isbn == "null" || Isbn.trim().length()==0 ){
			return false;
		}
		else {
			return true;
		}
	}

	public boolean isAdditionalDetailNotNull( ){
		return AdditionalDetail != null;
		}

	}
