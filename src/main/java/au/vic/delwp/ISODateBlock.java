package au.gov.vic.delwp;

public class ISODateBlock {

	public String date;
	public String dateType;

  public boolean isDate() {
    return !(date.contains("T"));
  }

  public boolean isDateTime() {
    return (date.contains("T"));
  }

}
