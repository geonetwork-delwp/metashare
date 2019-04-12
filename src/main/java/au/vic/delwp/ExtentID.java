package au.gov.vic.delwp;

public class ExtentID implements java.io.Serializable {

    public int OtherID;
    public int DatasetID;

	public ExtentID( ){ }
	
	public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof ExtentID) ) return false;
		 ExtentID castOther = ( ExtentID ) other; 
         
		 return ( this.OtherID == castOther.OtherID )
				&& ( this.DatasetID == castOther.DatasetID );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.OtherID;
         result = 37 * result + this.DatasetID;
         return result;
   }   


}


