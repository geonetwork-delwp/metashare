package au.gov.vic.delwp;

import com.opencsv.bean.CsvBindByPosition;

public class DelwpColumn {

  @CsvBindByPosition(position = 0)
  public String anzlicId;
  @CsvBindByPosition(position = 1)
	public String name;
  @CsvBindByPosition(position = 2)
	public String obligation;
  @CsvBindByPosition(position = 3)
	public String unique;
  @CsvBindByPosition(position = 4)
	public String dataType;
  @CsvBindByPosition(position = 5)
	public String dataLength;
  @CsvBindByPosition(position = 6)
	public String dataPrecision;
  @CsvBindByPosition(position = 7)
	public String dataScale;
  @CsvBindByPosition(position = 8)
	public String refTabOwnerName;
  @CsvBindByPosition(position = 9)
	public String refTabTableName;
  @CsvBindByPosition(position = 10)
	public String refTabCodeColumnName;
  @CsvBindByPosition(position = 11)
	public String shortColumnName;
  @CsvBindByPosition(position = 12)
	public String comments;

  boolean isNameNotNull() {
   return name != null;
  }

  boolean isObligationNotNull() {
   return obligation != null;
  }

  boolean isUniqueNotNull() {
   return unique != null;
  }

  boolean isDataTypeNotNull() {
   return dataType != null;
  }

  boolean isDataLengthNotNull() {
   return dataLength != null;
  }

  boolean isDataPrecisionNotNull() {
   return dataPrecision != null;
  }

  boolean isDataScaleNotNull() {
   return dataScale != null;
  }

  boolean isRefTabOwnerNameNotNull() {
   return refTabOwnerName != null;
  }

  boolean isRefTabTableNameNotNull() {
   return refTabTableName != null;
  }

  boolean isRefTabCodeColumnNameNotNull() {
   return refTabCodeColumnName != null;
  }

  boolean isShortColumnNameNotNull() {
   return shortColumnName != null;
  }

  boolean isCommentsNotNull() {
   return comments != null;
  }


}
