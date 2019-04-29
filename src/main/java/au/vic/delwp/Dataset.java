package au.gov.vic.delwp;

import java.util.ArrayList; 
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.HashMap; 
import java.util.List; 
import java.util.Map;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.regex.*;
import java.text.ParseException;
import java.util.StringTokenizer;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class Dataset {

	public int ID;
	public String Title;
	public String Name;
	public String Abstract;
	public String Purpose;
	public String Status;
	public String StoredDataFormat;
	public String AccessConstraint;
	public String UseConstraint;
	public String SpecialIpDetail;
	public String MaintenanceFrequency;
	public String AccessId;
	public String AccessAdvertScopeId;
	public String PositionalAccuracy;
	public String AttributeAccuracy;
	public String LogicalConsistency;
	public String Completeness;
	public String CompletenessCoverage;
	public String CompletenessVerification;
	public String CompletenessClassification;
	public String History;
	public String Credit;
	public String LayerRelationship;
	public String DesignIssuesCurrent;
	public String DesignIssuesFuture;
	public String AdditionalMetadata;
	public String AddMdUrl;
	// public Set ContactPoints = new HashSet( );
	// public Set ResponsibleParties = new HashSet( ); // But many responsible parties allowed
	public Date LastUpdated;
	public String LastUpdateUser;
	public String BeginningDate;
	public String EndingDate;
	public String ProcessingSteps;
	public String DataFormat;
	public String ANZLIC_ID;
	public String UUID;
	public IDnText DatasetOrganisation;
	public IDnText DatasetOriginality;
  public ISODateBlock PublicationDate;
	
	public String hostNameForLinks;

	protected String SourceDataScale;
	protected String SeriesID;
	protected IDnText Datum;
	protected IDnText Projection;
	protected String DataType;
	protected Set Contacts = new HashSet( );
	protected Set ANZLICSearchWords = new HashSet( );
	protected Set RelatedDocuments = new HashSet( );
	protected Set Citations = new HashSet( );
	protected Set GeographicNameDataset = new HashSet( );
	protected Set GeographicExtentPolygon = new HashSet( );
	private String CollectionMethod;
	private String SourceData;
	
	static protected HashMap MaintenanceFrequencies = new HashMap( );
	static protected HashMap ProgressCodes = new HashMap( );
	static protected HashMap EquivalentScales = new HashMap( );
	static protected HashMap IndeterminateDates = new HashMap( );
	static protected HashMap CRSCodes = new HashMap( );
	static protected HashMap DataTypes = new HashMap( );
	static protected HashMap ScopeCodes = new HashMap( );
	static protected HashMap Products = new HashMap( );
	static protected HashMap Objects = new HashMap( );
  static protected List<DelwpColumn> Columns = new ArrayList<DelwpColumn>();
	static protected SimpleDateFormat DBDateFormat = new SimpleDateFormat("ddMMMyyyy",Locale.ENGLISH );
	static protected SimpleDateFormat IS08601DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH );

	
	static private final String DEFAULT_DATA_FORMAT = "Most popular formats";
	
	static {
		MapUtils.Populate( "reftab__ms2ap_distance.txt", EquivalentScales, ".+\\|\\d+" );
		MapUtils.Populate( "reftab__ms2ap_progress.txt", ProgressCodes);
		MapUtils.Populate( "reftab__ms2ap_maintenance_frequency.txt", MaintenanceFrequencies);
		MapUtils.Populate( "reftab__ms2ap_temporal.txt", IndeterminateDates );
		MapUtils.Populate( "reftab__ms2ap_crscodes.txt", CRSCodes, ".+\\|\\d+" );
		MapUtils.Populate( "reftab__ms2ap_spatial_rep_type.txt", DataTypes, "\\d+\\|.+" );
		MapUtils.PopulateScopeCodeMap( "reftab__ms2ap_scope_with_series.txt", ScopeCodes );
    MapUtils.PopulateMulti( "product_object.csv", Products );
    MapUtils.Populate( "object.csv", Objects );
    Columns = MapUtils.PopulateUsingOpenCSV( "column.csv", DelwpColumn.class );
		}

	public boolean attributesNotNull() { 
    // this could get CPU intensive....
    for (DelwpColumn dc : Columns) {
      if (dc.anzlicId.equals(ANZLIC_ID)) return true;
    }
    return false;
  }
	
	public Set getAttributes() { 
    Set attributes = new HashSet();
    for (DelwpColumn dc : Columns) {
      if (dc.anzlicId.equals(ANZLIC_ID)) attributes.add(dc);
    }
    return attributes;
  }

	public String generateUUID( ){	
		//return java.util.UUID.randomUUID().toString();
    return DigestUtils.sha1Hex(ANZLIC_ID);
		}
	
	public boolean isAccessConstraintNotNull( ){
		return AccessConstraint != null;
		}
		
	public boolean isUseConstraintNotNull( ){
		return UseConstraint != null;
		}
		
	public boolean isSpecialIpDetailNotNull( ){
		return SpecialIpDetail != null;
		}
	// Modified on 26th Aug 08
	/*public boolean isCompletenessClassificationNotNull( ){
		return CompletenessClassification != null;
		}*/
	public boolean isCompletenessClassificationNotNull( ){
		if (CompletenessClassification == null || CompletenessClassification == "null" || CompletenessClassification.trim().length()==0 ){
			return false;
		}
		else {
			return true;
		}
	}

	public boolean isAttributeAccuracyNotNull( ){
		return AttributeAccuracy != null;
		}

	public boolean isPositionalAccuracyNotNull( ){
		return PositionalAccuracy != null;
		}

	public boolean isLogicalConsistencyNotNull( ){
		return LogicalConsistency != null;
		}

	public boolean isCompletenessOmissionNotNull( ){
		String completenessOmission = getCompletenessOmission();
		return CompletenessClassification != null && !completenessOmission.equals("");
		}

	public boolean hasVMDDSchema( ){
    String[] objectProps = (String[])Objects.get(ANZLIC_ID);
    return ((objectProps != null) && (objectProps.length >= 2) && !StringUtils.isBlank(objectProps[0]) && !StringUtils.isBlank(objectProps[1]));
  }

	public String getVMDDSchema( ){
    String[] objectProps = (String[])Objects.get(ANZLIC_ID);
    return objectProps[0]+":"+objectProps[1];
  }

	public boolean hasVMDDMasterPublicationDate( ){
    String[] objectProps = (String[])Objects.get(ANZLIC_ID);
    if ((objectProps != null) && (objectProps.length > 2) && !StringUtils.isBlank(objectProps[2])) {
      String date = objectProps[2];
      this.PublicationDate = new ISODateBlock();
      this.PublicationDate.date = date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8) + "T" + date.substring(8,10) + ":" + date.substring(10,12) + ":" + date.substring(12);
      this.PublicationDate.dateType = "publication";
      return true;
    } else {
      return false;
    }
  }

	public String getCompletenessOmission( ){
		String completenessOmission = "";
		if (Completeness != null && !Completeness.equals("")){
			completenessOmission += Completeness;
			}
		if (CompletenessCoverage != null && !CompletenessCoverage.equals("")){
			if (!completenessOmission.equals("")){
				completenessOmission += "\n\n" + CompletenessCoverage;
				}
			else{
				completenessOmission += CompletenessCoverage;
				}
			}
		if (CompletenessVerification != null && !CompletenessVerification.equals("")){
			if (!completenessOmission.equals("")){
				completenessOmission += "\n\nCompleteness Verification: " + CompletenessVerification;
				}
			else{
				completenessOmission += CompletenessVerification;
				}
			}

		return completenessOmission;
		}

	public String getResourceConstraint( ){
    System.out.println("AccessID: "+AccessId);
		String resourceConstraint = "Unknown";
		if (AccessId.toUpperCase().equals("R")){
			resourceConstraint = "restricted";
			}
		else if (AccessId.toUpperCase().equals("D")){
			resourceConstraint = "restricted";
			}
		else if (AccessId.toUpperCase().equals("G")){
			resourceConstraint = "unclassified";
			}
		return resourceConstraint;
		}
	
	public String getMetadataConstraint( ){
		String resourceConstraint = "Unknown";
		if (AccessAdvertScopeId.toUpperCase().equals("1")){
			resourceConstraint = "restricted";
			}
		else if (AccessAdvertScopeId.toUpperCase().equals("2")){
			resourceConstraint = "restricted";
			}
		else if (AccessAdvertScopeId.toUpperCase().equals("3")){
			resourceConstraint = "unclassified";
			}
		return resourceConstraint;
		}

	public String getMetadataAccessClassification( ){
		String resourceConstraint = "unknown";
		if (AccessAdvertScopeId.toUpperCase().equals("1")){
			resourceConstraint = "restricted";
			}
		else if (AccessAdvertScopeId.toUpperCase().equals("2")){
			resourceConstraint = "restricted";
			}
		else if (AccessAdvertScopeId.toUpperCase().equals("3")){
			resourceConstraint = "unclassified";
			}
		return resourceConstraint;
		}
	
	public String getResourceAccessClassification( ){
		String resourceConstraint = "unknown";
		if (AccessId.toUpperCase().equals("R")){
			resourceConstraint = "restricted";
			}
		else if (AccessId.toUpperCase().equals("D")){
			resourceConstraint = "restricted";
			}
		else if (AccessId.toUpperCase().equals("G")){
			resourceConstraint = "unclassified";
			}
		return resourceConstraint;
		}
	
	public String getMetadataAccessClassificationForDB( ){
		String resourceConstraint = "Unknown";
		if (AccessAdvertScopeId.toUpperCase().equals("1")){
			resourceConstraint = "Local";
			}
		else if (AccessAdvertScopeId.toUpperCase().equals("2")){
			resourceConstraint = "Department";
			}
		else if (AccessAdvertScopeId.toUpperCase().equals("3")){
			resourceConstraint = "World";
			}
		return resourceConstraint;
		}
	
	public String getResourceAccessClassificationForDB( ){
		String resourceConstraint = "Unknown";
		if (AccessId.toUpperCase().equals("R")){
			resourceConstraint = "Local";
			}
		else if (AccessId.toUpperCase().equals("D")){
			resourceConstraint = "Department";
			}
		else if (AccessId.toUpperCase().equals("G")){
			resourceConstraint = "World";
			}
		return resourceConstraint;
		}
	
	public String getUseLimitation( ){
		String useLimitation = "";
		if (isAccessConstraintNotNull( )){
			useLimitation = AccessConstraint;
			}
		if (isUseConstraintNotNull( )){
			if (useLimitation.equals("")){
				useLimitation = UseConstraint;
				}
			else{
				useLimitation += "\n\n" + UseConstraint;
				}
			}
		if (isSpecialIpDetailNotNull( )){
			if (useLimitation.equals("")){
				useLimitation = SpecialIpDetail;
				}
			else{
				useLimitation += "\n\n" + SpecialIpDetail;
				}
			}
		return useLimitation;
		}	

	public String getProgressCode( ){
		String progress = (String) ProgressCodes.get( Status );
    System.out.println("Status: "+Status+" Looked up: "+progress);
		if (progress.toLowerCase().equals("ongoing")){
			String maintenance = getMaintenanceFrequency( );
			if (maintenance.toLowerCase().equals("unknown") || maintenance.toLowerCase().equals("notplanned")){
				progress = "completed";
				}
			}
		return progress;
		}
		
	public boolean isProgressCodeNotNull( ){
		return Status != null && ProgressCodes.containsKey( Status );
		}
	
	public String getMaintenanceFrequency( ){
    System.out.println("MaintenanceFrequency: "+MaintenanceFrequency+" looked up: "+MaintenanceFrequencies.get( MaintenanceFrequency ));
		return (String) MaintenanceFrequencies.get( MaintenanceFrequency );
		}
		
	public boolean isMaintenanceFrequencyNotNull( ){
		return MaintenanceFrequency != null && MaintenanceFrequencies.containsKey( MaintenanceFrequency );
		}
	
	public String getRelatedDocuments( ) throws Exception {
		String relatedDocuments = "";

		if (RelatedDocuments != null){
			Iterator i = RelatedDocuments.iterator( );
			while (i.hasNext( )){
				RelatedDocument rd = (RelatedDocument) i.next();
 				if (relatedDocuments.equals("")){
					relatedDocuments += rd.Path + "." + ((rd.PcPrefix == null || rd.PcPrefix.toLowerCase().equals("null")) ? "" : rd.PcPrefix) + " - " + rd.Description;
					}
				else{
					relatedDocuments += "," + rd.Path + "." + ((rd.PcPrefix == null || rd.PcPrefix.toLowerCase().equals("null")) ? "" : rd.PcPrefix) + " - " + rd.Description;
					}
				}
			}

		if (relatedDocuments.equals("")){
			relatedDocuments = "None";
			}

		return relatedDocuments;

		}

  public boolean isObjectIDNotNull() {
    // TODO: Get from ANZLIC_ID map to object ID
    return true;
  }

  public String getObjectID() {
    // TODO: get object ID
    return "UNKNOWN";
  }

	public String getBrowseGraphicUrl( ){
    // Get bbox from geograhic extent
    Set extents = getExtents(); 
    Object[] exsa = extents.toArray();
    // has only one extent which is max of all boxes
    Extent ex = (Extent)exsa[0];
    

    return "http://pwms-ags-00.aaa.depi.vic.gov.au/arcgis/rest/services/BusinessApps/SDM_coverage_map/MapServer/export?dpi=96&transparent=false&format=png8&bbox="+ ex.WestLong + "," + ex.SouthLat + "," + ex.EastLong + "," + ex.NorthLat +"&bboxSR=4326&imageSR=4326&size=440,300&f=image&layerDefs=5:LCSMAP.VMDD_EXTENTS.OBJECT_ID=" + getObjectID() + "&layers=show:1,2,3,5";
  }

	public String getSupplementalInformation( ){
		String additionalMetadata = "";

		if (History != null && !History.equals("")){
			additionalMetadata = "History: " + History;
			}

		if (LayerRelationship != null && !LayerRelationship.equals("")){
			if (!additionalMetadata.equals("")){
				additionalMetadata += "\n\n";
				}
			additionalMetadata += "Relationship to other Datasets: " + LayerRelationship;
			}

		if (DesignIssuesCurrent != null && !DesignIssuesCurrent.equals("")){
			if (!additionalMetadata.equals("")){
				additionalMetadata += "\n\n";
				}
			additionalMetadata += "Current Design Issues: " + DesignIssuesCurrent;
			}

		if (DesignIssuesFuture != null && !DesignIssuesFuture.equals("")){
			if (!additionalMetadata.equals("")){
				additionalMetadata += "\n\n";
				}
			additionalMetadata += "Future Design Issues: " + DesignIssuesFuture;
			}

		if (!additionalMetadata.equals("")){
			additionalMetadata += "\n\n";
			}

		try{
			additionalMetadata += "Related Documents: " + getRelatedDocuments();
			}
		catch (Exception e){
			additionalMetadata += "Related Documents: None";
			}

		if (AdditionalMetadata != null && !AdditionalMetadata.equals("") && !AdditionalMetadata.toLowerCase().equals("not documented") && !AdditionalMetadata.toLowerCase().equals("None")){
			if (!additionalMetadata.equals("")){
				additionalMetadata += "\n\n";
				}
			additionalMetadata += AdditionalMetadata;
			}

		if (AddMdUrl != null && !AddMdUrl.equals("")){
			if (!additionalMetadata.equals("")){
				additionalMetadata += "\n\n";
				}
			additionalMetadata += AddMdUrl;
			}

		return additionalMetadata;

		}

	public Set getAssociatedResources( ) throws Exception {
    Set associatedResources = new HashSet();
   
    List resources = (List)Products.get( ANZLIC_ID ); 
    if (resources != null) {
      for (int i = 0; i < resources.size(); i++) {
         AssociatedResource assRes = new AssociatedResource();
         assRes.anzlicId = (String)resources.get(i);
         assRes.hostNameForLinks = hostNameForLinks;
         associatedResources.add(assRes);
      }
    }
    return associatedResources;
  }

	public Set getTopicCategories( ) throws Exception {
	
		Set topics = new HashSet( );
		
		Iterator i =  ANZLICSearchWords.iterator( );
		while( i.hasNext( ) ){
			SearchWordFull swf = (SearchWordFull) i.next( );
			ANZLICSearchWord sw = swf.SearchWord;
			try {
				topics.addAll( sw.getISOTopicCategories( ) );
				}
			catch( NullPointerException e ){
				throw new Exception( "Untranslated ANZLIC Search Word '" + sw.Text + "' encountered during processing", e );
				}	
			}
		return topics;
		}

	public Set getKeywords( ) throws Exception {
	
		Set keywords  = new HashSet( );
		
		Iterator i =  ANZLICSearchWords.iterator( );
		while( i.hasNext( ) ){
			SearchWordFull swf = (SearchWordFull) i.next( );
			ANZLICSearchWord sw = swf.SearchWord;

			String modifiedSw = "";
			boolean placedHyphen = false;
			StringTokenizer st = new StringTokenizer(sw.Text, " ");
			while (st.hasMoreTokens()){
				String token = st.nextToken();
				if (!placedHyphen){
					Pattern p = Pattern.compile("[A-Z]+");
					Matcher m = p.matcher(token);

					if (m.matches()){
						if (modifiedSw.equals("")){
							modifiedSw = token;
							}
						else{
							modifiedSw += " " + token; 	
							}
						}
					else{
						modifiedSw += "-" + token;
						placedHyphen = true;
						}		
					}
				else{
					modifiedSw += " " + token; 	
					}

				}
			keywords.add(modifiedSw);
			}
		return keywords;
		}

	public String getMaintenanceNote(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		return "Metadata Revision Date: " + sdf.format(LastUpdated) + " (" + LastUpdateUser + ")";
		}

	public Set getCitations( ) throws Exception {
	
		Set citations  = new HashSet( );
		
		Iterator i =  Citations.iterator( );
		while( i.hasNext( ) ){
			Citation c = (Citation) i.next( );
			citations.add(c);
			}
		return citations;
		}

	public boolean isExtentDescriptionNotNull( ) throws Exception {
		return !getExtentDescription().equals("");
		}

	public String getExtentDescription( ) throws Exception {

		Set extents = getExtents();
		String extentDescription = "";

		Iterator i = extents.iterator();
		while (i.hasNext()){
			Extent e = (Extent) i.next();
			if (e.getExtentDescription() != null && !e.getExtentDescription().equals("")){
				extentDescription += e.getExtentDescription() + " ";
				}
			}	

		extentDescription.trim();

		return extentDescription;

		}

	public Set getExtents( ) { //throws Exception {
	
		Set extents  = new HashSet( );
		
		Iterator i =  GeographicNameDataset.iterator( );
		while( i.hasNext( ) ){
			NameDataset nd = (NameDataset) i.next( );
			extents.add((Extent) nd.ExtentDetails);
			}

		String minLong = null;
		String maxLong = null;
		String minLat = null;
		String maxLat = null;
		Iterator j =  GeographicExtentPolygon.iterator( );
		while( j.hasNext( ) ){
			Extent e = (Extent) j.next( );

			// The extent objects generated from the geographic_extent_polygon table only have one
			// value for latitude and longitude so both WestLong and EastLong are the same and
			// Southlat and NorthLat are the same. Therefore we only need to worry about one of each.
			if (e.WestLong != null && e.SouthLat != null){

				if (minLong == null){
					minLong = e.WestLong;
					}
				else if (Double.parseDouble(e.WestLong) < Double.parseDouble(minLong)){
					// If no max has been set yet, the old min is now the max
					if (maxLong == null){
						maxLong = minLong;
						}
					minLong = e.WestLong;
					}
				else if (maxLong == null){
					maxLong = e.WestLong;
					}
				else if (Double.parseDouble(e.WestLong) > Double.parseDouble(maxLong)){
					maxLong = e.WestLong;
					}

				if (minLat == null){
					minLat = e.SouthLat;
					}
				else if (Double.parseDouble(e.SouthLat) < Double.parseDouble(minLat)){
					// If no max has been set yet, the old min is now the max
					if (maxLat == null){
						maxLat = minLat;
						}
					minLat = e.SouthLat;
					}
				else if (maxLat == null){
					maxLat = e.SouthLat;
					}
				else if (Double.parseDouble(e.SouthLat) > Double.parseDouble(maxLat)){
					maxLat = e.SouthLat;
					}


				}
			}

		if (minLong != null && maxLong != null && minLat != null && maxLat != null){
			Extent extent_poly = new Extent();
			extent_poly.WestLong = minLong;
			extent_poly.EastLong = maxLong;
			extent_poly.NorthLat = minLat;
			extent_poly.SouthLat = maxLat;

			extents.add(extent_poly);
			}

		return extents;
		}

	/*public boolean isPurposeNotNull( ){
		return Purpose != null;
		}*/
	public boolean isPurposeNotNull( ){
		if ( Purpose == null || Purpose == "null" || Purpose.trim().length()==0 ){
			return false;
		}
		else {
			return true;
		}
	}
		
	public boolean isCreditNotNull( ){
		if ( Credit == null || Credit == "null" || Credit.trim().length()==0 ){
			return false;
		}
		else {
			return true;
		}
	}
		
	
	public String getCollectionMethod( ){
		return CollectionMethod != null ? "Collection Method: " + CollectionMethod : null;
		}

		
	public boolean isCollectionMethodNotNull( ){
		return CollectionMethod != null;
		}
		
	public boolean isCompletenessNotNull( ){
		return Completeness != null;
		}
		
	
	/*public boolean isProcessingStepsNotNull( ){
		return ProcessingSteps != null;
		}*/
	// Modified on 26th Aug 08
	public boolean isProcessingStepsNotNull( ){
		if (ProcessingSteps == null || ProcessingSteps == "null" || ProcessingSteps.trim().length()==0 ){
			return false;
		}
		else {
			return true;
		}
	}
	
	
	public String getSourceData( ){
		return SourceData != null ? "Dataset Source: " + SourceData + "\n\nDataset Originality: " + (DatasetOriginality != null ? DatasetOriginality.Text : "") : null;
		}

	
	public boolean isSourceDataNotNull( ){
		return SourceData != null;
		}
		
		
	public boolean isDataQualityInfoNotNull( ){
		return isProcessingStepsNotNull( ) 
			|| isCompletenessNotNull( ) 
			|| isCollectionMethodNotNull( );
		//	|| isSourceDataNotNull( );
		}
		
		
	public String getDataFormat( ){
		return DataFormat != null ? DataFormat : DEFAULT_DATA_FORMAT;
		}
		
		
	public String getTitle( ){
		return Title + " ( " + Name + " )";
		}

		
	public ISODateBlock getDateStamp( ) throws ParseException {	
    ISODateBlock db = new ISODateBlock();
    db.date = IS08601DateFormat.format( LastUpdated );
    db.dateType = "revision";
    return db;
		}

		
	public DatasetContact getResourcePOC( ){
		
		Iterator i = Contacts.iterator( );
		
		while( i.hasNext( ) ){
			DatasetContact dc = (DatasetContact) i.next( );
			String roletype = dc.role.Description;
			IDnText org = dc.contact.Organisation; // ISO19139 XSD states organiation is compulsorary
			
			if( org != null && roletype.equals("Dataset Contact") ) return dc;
			}
		return DatasetContact.getDefault( );
		}

	/* This method assumes that every Dataset at least has a 'Dataset Data Manager'. My 
	 * checks indicate this is not 100% true */
	public DatasetContact getMetadataContact( ){
		
		Iterator i = Contacts.iterator( );
		DatasetContact mgr = null;
		
		while( i.hasNext( ) ){
			DatasetContact dc = (DatasetContact) i.next( );
			String roletype = dc.role.Description;
			IDnText org = dc.contact.Organisation; // ISO19139 XSD states organiation is compulsorary
			
			if( org != null ){
				if( roletype.equals("Metadata Author") ) return dc; // Found 1st preference -> return it
				else if( roletype.equals("Dataset Data Manager") ) mgr = dc; // Found 2nd preference -> keep reference, but continue looking for 1st
				}
			}
		if( mgr == null ) throw new IllegalStateException( "No metadata contact found for dataset '" + Name + "'" );
		return mgr; // Reach here only if did not find 1st preference, so return 2nd
		}
		
	public boolean hasOtherResponsibleParties( ){
		Set parties = getOtherResponsibleParties();

		if (parties.size() > 0){
			return true;
			}

		return false;
		}	

	public Set getOtherResponsibleParties( ){
	
		Set orps = new HashSet( );
		Iterator i = Contacts.iterator( ) ;
		
		while( i.hasNext( ) ){
			DatasetContact dc = (DatasetContact) i.next( );
			String roletype = dc.role.Description;
			IDnText org = dc.contact.Organisation; // ISO19139 XSD states organiation is compulsorary
			
			if( org != null && !( roletype.equals("Dataset Contact") || roletype.equals("GI Manager") || roletype.startsWith("Warehouse") ) )
				orps.add( dc );
			}	
		return orps;
		}
	
	
	public boolean isScaleNotNull( ){
		return SourceDataScale != null && EquivalentScales.containsKey( SourceDataScale );
		}
	
	
	public String getEquivalentScale( ){
		return (String) EquivalentScales.get( SourceDataScale );
		}
	
	
	public String getEquivalentScaleUnits( ){
		return ( ( EquivalentScales.get( SourceDataScale ) != null ) ? "m" : null );
		}
	
	
	public String getBeginningDate( ) throws java.text.ParseException {	
		return getTemporalDate( BeginningDate );
		}
		
		
	public String getEndingDate( ) throws java.text.ParseException {
		return getTemporalDate( EndingDate );
		}
	
	
	// Returns null if key not found (or in unlikely case where key maps to null)
	public String getIndeterminateBeginning( ){
		return (String) IndeterminateDates.get( BeginningDate.toLowerCase( ) );
		}

		
	// Returns null if key not found (or in unlikely case where key maps to null)
	public String getIndeterminateEnding( ){
		return (String) IndeterminateDates.get( EndingDate.toLowerCase( ) );
		}
	
	public String getReferenceSystemID( ){
	
		String s = null;
		// Choose Projection over Datum if available
		if( Projection != null && CRSCodes.containsKey( Projection.Text ) )
			s = (String) CRSCodes.get( Projection.Text );
		else if( Datum != null )
			s = (String) CRSCodes.get( Datum.Text );
		return s;
		}

	public boolean isRefSysNotNull( ){
		return getReferenceSystemID( ) != null;
		}
		
	public String getHierarchyLevel( ){
    System.out.println("SeriesID: "+SeriesID+" DataType: "+ DataType);
    System.out.println("ScopeCodePair: "+new ScopeCodePair( SeriesID, DataType )+" "+ScopeCodes);
    String hierarchyLevel;
    if (Products.get(ANZLIC_ID) != null) {
       hierarchyLevel = "product";
    } else {
       hierarchyLevel = (String) ScopeCodes.get( new ScopeCodePair( SeriesID, DataType ) );
		  // return ( SeriesID == null || SeriesID.equals("0") ) ? "dataset" : "series";
		}
    System.out.println("HIELevel = "+hierarchyLevel);
    return hierarchyLevel;
  }
	
	
	public String getHierarchyLevelName( ){
		return getHierarchyLevel( ) + " of " + Title;
		}
	
	
	public String getDataType( ){
    System.out.println("DataType: "+ DataType+ " Looked up: "+DataTypes.get( DataType ));
		return (String) DataTypes.get( DataType );
		}
		
	// Returns null if 'dateString' is not in the expected date format (ie. is not a ddMMMyyyy)
	private String getTemporalDate( String dateString ){

		String s = null;
		// if( dateString.matches("^\d{2}[A-Z]{3}\d{4}$") ) // It *looks* like a date...
		if( dateString.matches("\\d{2}.*") ){ // If it starts with a number then it must be a date...
			try {
				s = IS08601DateFormat.format( DBDateFormat.parse( dateString ) );
				}
			catch( ParseException e ){ // silently eat the exception
				}
			}
		return s;
		}
	}
