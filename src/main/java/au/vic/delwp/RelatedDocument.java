package au.gov.vic.delwp;

public class RelatedDocument {

	public int ID;
	public String Path;
	public String RelDocCurrent;
	public String Description;
	public String UnixPrefix;
	public String PcPrefix;
	
	public boolean isPathNotNull( ){
		return Path != null;
		}

	public boolean isRelDocCurrentNotNull( ){
		return RelDocCurrent != null;
		}

	public boolean isDescriptionNotNull( ){
		return Description != null;
		}

	public boolean isUnixPrefixNotNull( ){
		return UnixPrefix != null;
		}

	public boolean isPcPrefixNotNull( ){
		return PcPrefix != null;
		}

	}
