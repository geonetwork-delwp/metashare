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
		if (CorporateAuthor == null || CorporateAuthor.trim().length()==0){
			return "Unknown";
			}
		return CorporateAuthor;
		}

	public String getOrganisationName(){
		String organisationName = CorporateAuthor;

		if (PublisherName != null && !PublisherName.equals(CorporateAuthor) && PublisherName.trim().length()!=0){
			organisationName += " - " + PublisherName;
			}

		return organisationName;
		}

	public String getOtherCitationDetails(){
		String otherCitationDetails = "Author(s): " + Author;

		if (isAdditionalDetailNotNull()){
			otherCitationDetails += "\n\nSupplemental Information: " + AdditionalDetail;
			}

		return otherCitationDetails;
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

	public boolean isCorporateAuthorNotNull( ){
		return CorporateAuthor != null;
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
