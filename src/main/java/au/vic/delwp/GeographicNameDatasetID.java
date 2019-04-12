package au.gov.vic.delwp;

public class GeographicNameDatasetID implements java.io.Serializable {

    public short DatasetID;
    public short GeNameID;

	public GeographicNameDatasetID( ){ }
	
	public boolean equals( Object RHS ){
         if( (this == RHS ) ) return true;
		 if( (RHS == null ) ) return false;
		 if( !(RHS instanceof GeographicNameDatasetID) ) return false;
		 GeographicNameDatasetID cast = ( GeographicNameDatasetID ) RHS; 
         
		 return ( GeNameID == cast.GeNameID )
				&& ( DatasetID == cast.DatasetID );
		}
   
   public int hashCode( ){
         int result = 17;
         
         result = 37 * result + GeNameID;
         result = 37 * result + DatasetID;
         return result;
		}   
	}


