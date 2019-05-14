package au.gov.vic.delwp;

import org.apache.commons.lang3.StringUtils;

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
		return !StringUtils.isBlank(Author);
		}

	public boolean isTitleNotNull( ){
		return !StringUtils.isBlank(Title);
		}

	public boolean isSeriesTitleNotNull( ){
		return !StringUtils.isBlank(SeriesTitle);
		}

	public boolean isPagesNotNull( ){
		return !StringUtils.isBlank(Pages);
		}

	public boolean isCorporateAuthorNull( ){
		return !isCorporateAuthorNotNull();
		}

	public boolean isCorporateAuthorNotNull( ){
		return !StringUtils.isBlank(CorporateAuthor);
		}

	public boolean isPublisherNameNotNull( ){
		return !StringUtils.isBlank(PublisherName);
		}

	public boolean isYearPublishedNotNull( ){
		return !StringUtils.isBlank(YearPublished);
		}

	/*public boolean isIsbnNotNull( ){
		return Isbn != null;
		}*/
	// Method modified to handle spaces dated 18-08-08
	public boolean isIsbnNotNull( ){
		if (StringUtils.isBlank(Isbn) || Isbn == "null"){
			return false;
		}
		else {
			return true;
		}
	}

  public boolean isSourceCitationNotNull() {
    System.out.println(this.toString());
    return isTitleNotNull() && isYearPublishedNotNull() && 
           isCorporateAuthorNotNull() && isAuthorNotNull() && 
           isAdditionalDetailNotNull() && isIsbnNotNull();
  }

	public boolean isAdditionalDetailNotNull( ){
		return !StringUtils.isBlank(AdditionalDetail);
		}

  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    output.append("Title: "+Title+"\n");
    output.append("YearPublished: "+YearPublished+"\n");
    output.append("Corporate Author: "+CorporateAuthor+"\n");
    output.append("Author: "+Author+"\n");
    output.append("AdditionalDetail: "+AdditionalDetail+"\n");
    output.append("Isbn: "+Isbn+"\n");
    return "Object citation: \n"+output.toString();
  }
}
