package au.gov.vic.delwp;

public class Extent{

	public ExtentID ID;
	public String WestLong;
	public String EastLong;
	public String SouthLat;
	public String NorthLat;
	public String Description;
	public IDnText Category;

	public String getWestLong( ){
		if (
		    WestLong != null && !WestLong.equals("") &&
		    EastLong != null && !EastLong.equals("") &&
		    SouthLat != null && !SouthLat.equals("") &&
		    NorthLat != null && !NorthLat.equals("")){
			return WestLong;
		    	}
		return "141.0";
		}
		
	public String getEastLong( ){
		if (WestLong != null && !WestLong.equals("") &&
		    EastLong != null && !EastLong.equals("") &&
		    SouthLat != null && !SouthLat.equals("") &&
		    NorthLat != null && !NorthLat.equals("")){
			return EastLong;
		    	}
		return "150.2";
		}
		
	public String getSouthLat( ){
		if (WestLong != null && !WestLong.equals("") &&
		    EastLong != null && !EastLong.equals("") &&
		    SouthLat != null && !SouthLat.equals("") &&
		    NorthLat != null && !NorthLat.equals("")){
			return SouthLat;
		    	}
		return "-39.2";
		}

	public String getNorthLat( ){
		if (WestLong != null && !WestLong.equals("") &&
		    EastLong != null && !EastLong.equals("") &&
		    SouthLat != null && !SouthLat.equals("") &&
		    NorthLat != null && !NorthLat.equals("")){
			return NorthLat;
		    	}
		return "-34.0";
		}

	public String getExtentDescription( ){
		if (WestLong != null && !WestLong.equals("") &&
		    EastLong != null && !EastLong.equals("") &&
		    SouthLat != null && !SouthLat.equals("") &&
		    NorthLat != null && !NorthLat.equals("")){
			if (Category != null && Category.Text != null && 
			    !Category.Text.equals("") && Description != null && !Description.equals("")){ 
				return Category.Text + " - " + Description;
				}
			else{
				return "";
				}
		    	}
		return "Victoria";
		}
			
	}
