package edu.suffolk.litlab.efspserver.codes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DocumentTypeTableRow {
  public String code;
  public String name;
  public String filingcodeid;
  public boolean iscourtuseonly;
  public boolean isdefault;
  public String efspcode;
  public String location;
  
  public DocumentTypeTableRow(ResultSet rs) throws SQLException {
    this(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
  }
  
  public DocumentTypeTableRow(String code, String name, String filingcodeid, String iscourtuseonly, String isdefault, String efspcode, String location) {
    this.code = code;
    this.name = name;
    this.filingcodeid = filingcodeid;
    this.iscourtuseonly = Boolean.parseBoolean(iscourtuseonly);
    this.isdefault = Boolean.parseBoolean(isdefault);
    this.efspcode = efspcode;
    this.location = location;
  }

  public static String getDocumentTypeWithFilingCode() {
    return """
        SELECT code, name, filingcodeid, iscourtuseonly, isdefault, efspcode, location 
        FROM documenttype 
        WHERE location=? AND iscourtuseonly='False' AND filingcodeid=?""";
  }

  public static String getDocumentTypeNoFiling() {
    return """
        SELECT code, name, filingcodeid, iscourtuseonly, isdefault, efspcode, location 
        FROM documenttype 
        WHERE location=? AND iscourtuseonly='False' AND filingcodeid=''""";
  }
  
  
}