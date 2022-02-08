package au.gov.vic.delwp;

public class AssociatedResource {

	public String anzlicId;
	public String UUID;
	public String title;
	public String hostNameForLinks;

  public String getMetadataRecordUrl() {
    return hostNameForLinks + "catalog.search#/metadata/" + UUID;
  }

}
