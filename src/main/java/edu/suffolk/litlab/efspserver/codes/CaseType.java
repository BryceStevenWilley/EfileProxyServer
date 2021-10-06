package edu.suffolk.litlab.efspserver.codes;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CaseType {
  public String code;
  public String name;
  public String casecategory;
  /** Whether the court accepts new cases of this type. */
  public boolean initial;
  public BigDecimal fee;
  public String willfileddate;
  public String efspcode;
  public String location;
  
  public CaseType(String code, String name, String casecategory, String initial,
      String fee, String willfileddate, String efspcode, String location) {
    this.code = code;
    this.name = name;
    this.casecategory = casecategory;
    this.initial = Boolean.parseBoolean(initial);
    this.fee = BigDecimal.valueOf(Double.parseDouble(fee));
    this.willfileddate = willfileddate;
    this.efspcode = efspcode;
    this.location = location;
  }
  
  /** A constructor that takes a result set (DOES NOT modify it), gotten from
   * executing the below {@link getCaseTypesFor} query.
   * @param rs
 * @throws SQLException 
   */
  public CaseType(ResultSet rs) throws SQLException {
      this(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
              rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
  }
  
  // HACK(brycew): here for when CaseType isn't present, but we have AllWrong collector
  public static CaseType DummyCase() {
    return new CaseType("Bad code", "Bad name", "Bad Case Categor", "true", "-1", 
        "Bad date", "Bade code", "Bad loc");
  }
  
  public static PreparedStatement prepQueryBroad(Connection conn, String courtLocationId,
      String caseCategoryCode) throws SQLException {
    PreparedStatement st = conn.prepareStatement(getCaseTypesForCategory());
    st.setString(1, courtLocationId);
    st.setString(2, caseCategoryCode);
    return st;
  }
  
  public static PreparedStatement prepQueryTiming(Connection conn, String courtLocationId,
      String caseCategoryCode, String isInitial) throws SQLException {
    PreparedStatement st = conn.prepareStatement(getCaseTypesForTiming());
    st.setString(1, courtLocationId);
    st.setString(2, caseCategoryCode);
    st.setString(3, isInitial);
    return st;
  }

  public static PreparedStatement prepQueryWithCode(Connection conn, String courtLocationId,
      String caseTypeCode) throws SQLException {
    PreparedStatement st = conn.prepareStatement(getCaseTypesWithCode());
    st.setString(1, courtLocationId);
    st.setString(2, caseTypeCode);
    return st;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(code);
    sb.append(", ");
    sb.append(name);
    sb.append(", ");
    sb.append(casecategory);
    sb.append(", ");
    sb.append(initial);
    sb.append(", ");
    sb.append(fee);
    sb.append(", ");
    sb.append(willfileddate);
    sb.append(", ");
    sb.append(efspcode);
    sb.append(", ");
    sb.append(location);
    return sb.toString();
  }

  private static String getCaseTypesForCategory() {
    return """
        SELECT code, name, casecategory, initial,
        fee, willfileddate, efspcode, location
        FROM casetype WHERE location=? AND casecategory=?""";
  }
  
  private static String getCaseTypesForTiming() {
    return getCaseTypesForCategory() + " AND initial ILIKE ?";
  }

  private static String getCaseTypesWithCode() {
    return """
        SELECT code, name, casecategory, initial,
          fee, willfileddate, efspcode, location
        FROM casetype WHERE location=? AND code=?
        """;
  }
}
