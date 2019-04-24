package au.gov.vic.delwp;

public class AssociatedResource {

	public String anzlicId;
	public String hostNameForLinks;

  public String getMetadataRecordUrl() {
    return hostNameForLinks + "catalog.search?anzlicid=" + anzlicId;
  }

}
