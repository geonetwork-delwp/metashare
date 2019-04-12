package au.gov.vic.delwp;

public class SearchWordFullID implements java.io.Serializable {

    public short DatasetID;
    public short SearchWordID;

	public SearchWordFullID( ){ }
	
	public boolean equals( Object RHS ){
         if( (this == RHS ) ) return true;
		 if( (RHS == null ) ) return false;
		 if( !(RHS instanceof SearchWordFullID) ) return false;
		 SearchWordFullID cast = ( SearchWordFullID ) RHS; 
         
		 return ( SearchWordID == cast.SearchWordID )
				&& ( DatasetID == cast.DatasetID );
		}
   
   public int hashCode( ){
         int result = 17;
         
         result = 37 * result + SearchWordID;
         result = 37 * result + DatasetID;
         return result;
		}   
	}


