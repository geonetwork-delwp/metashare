package au.gov.vic.delwp;

import java.util.Date;

public class ANZMetadataProfile {

    public int ID;
    public int DatasetID;
    public String Name;
    public String Title;
    public String ANZLIC_ID;
    public String UUID;
    public Date Datestamp;
    public Date LastUpdated;
    public String XML; // XML String
    public boolean XMLIsValid = false;
    public boolean ContentIsValid = false;
    public String ResourceAccessClassification;
    public String MetadataAccessClassification;

	}
