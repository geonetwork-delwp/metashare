package au.gov.vic.delwp;

public class DatasetContactID implements java.io.Serializable {

    public int contactID;
    public int roleID;
    public int datasetID;

	public DatasetContactID( ){ }
	
	public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof DatasetContactID) ) return false;
		 DatasetContactID castOther = ( DatasetContactID ) other; 
         
		 return ( this.contactID == castOther.contactID )
				&& ( this.roleID == castOther.roleID )
				&& ( this.datasetID == castOther.datasetID );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.contactID;
         result = 37 * result + this.roleID;
         result = 37 * result + this.datasetID;
         return result;
   }   


}


