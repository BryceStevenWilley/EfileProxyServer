package edu.suffolk.litlab.efspserver.codes;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PartyType {
  public String code;
  public String name;
  public boolean isAvailableForNewParties;
  public String casetypeid;
  public boolean isrequired;
  public BigDecimal fee;
  public String numberofpartiestoignore;
  public String sendforredaction;
  public String dateofdeath;
  public int displayorder;
  public String efspcode;
  public String location;
  

  public PartyType(String code, String name, String isAvailable, String casetypeid, 
      String isrequired, String fee, String numberofpartiestoignore, String sendforredaction,
      String dateofdeath, String displayorder, String efspcode, String location) {
    this.code = code;
    this.name = name;
    this.isAvailableForNewParties = Boolean.parseBoolean(isAvailable);
    this.casetypeid = casetypeid;
    this.isrequired = Boolean.parseBoolean(isrequired);
    this.fee = new BigDecimal(Double.parseDouble(fee));
    this.numberofpartiestoignore = numberofpartiestoignore;
    this.sendforredaction = sendforredaction;
    this.dateofdeath = dateofdeath;
    this.displayorder = Integer.parseInt(displayorder);
    this.efspcode = efspcode;
    this.location = location;
  }
  
  public PartyType(ResultSet rs) throws SQLException {
    this(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), 
        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
        rs.getString(11), rs.getString(12));
  }

  public static String getPartyTypeFromCaseType() {
    return """
        SELECT p.code, p.name, p.isavailablefornewparties, p.casetypeid, p.isrequired, p.amount, 
               p.numberofpartiestoignore, p.sendforredaction, p.dateofdeath, p.displayorder, 
               p.efspcode, p.location 
        FROM partytype AS p 
        WHERE p.location=? AND p.casetypeid=?""";
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(code).append(", ").append(name).append(", ").append(isrequired).append(fee);
    return sb.toString();
  }
  

}
