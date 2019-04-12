package au.gov.vic.delwp;

public class ScopeCodePair {

    private final String seriesID;
    private final String dataTypeID;

    public ScopeCodePair( String series, String dataType ){
		seriesID = series;
		dataTypeID = dataType;
		}

    public String getSeriesID( ){ return seriesID; }
    public String getDataTypeID( ){ return dataTypeID; }

    public String toString( ){
        return "(" + seriesID + ", " + dataTypeID + ")";
		}

    private static boolean equals( Object lhs, Object rhs ){
		return (lhs == null && rhs == null) || (lhs != null && lhs.equals(rhs));
		}

    public boolean equals( Object o ){
		
		if( o instanceof ScopeCodePair ){
			ScopeCodePair other;
			other = (ScopeCodePair) o;
			return ( seriesID != null ? seriesID.equals( other.seriesID ) : other.seriesID == null )
			     && ( dataTypeID != null ? dataTypeID.equals( other.dataTypeID ) : other.dataTypeID == null );
			}
		else return false;
		}

    public int hashCode( ){
		if( seriesID == null ) return (dataTypeID == null) ? 0 : dataTypeID.hashCode( ) + 1;
		else if( dataTypeID == null ) return seriesID.hashCode( ) + 2;
		else return seriesID.hashCode( ) * 17 + dataTypeID.hashCode( );
		}
	}
