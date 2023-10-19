package edu.suffolk.litlab.efspserver.tyler.codes;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class CaseType {
  private final String code;
  private final String name;
  /** Associates the code to a particular case category. */
  private final String casecategory;
  /** Whether the court accepts new cases of this type. */
  private final boolean initial;

  /** The court's fee for initiating a case of this case type. */
  private final BigDecimal fee;
  /**
   * Indicates the behavior of the date field for the filing of a will for this case type (Not
   * Available, Available, Required).
   */
  private final String willfileddate;

  private final String efspcode;
  private final String location;

  public CaseType(
      String code,
      String name,
      String casecategory,
      String initial,
      String fee,
      String willfileddate,
      String efspcode,
      String location) {
    this.code = code;
    this.name = name;
    this.casecategory = casecategory;
    this.initial = Boolean.parseBoolean(initial);
    this.fee = BigDecimal.valueOf(Double.parseDouble(fee));
    this.willfileddate = willfileddate;
    this.efspcode = efspcode;
    this.location = location;
  }

  /**
   * A constructor that takes a result set (DOES NOT modify it), gotten from executing the below
   * {@link prepQueryBroad} query.
   *
   * @param rs
   * @throws SQLException
   */
  public CaseType(ResultSet rs) throws SQLException {
    this(
        rs.getString(1),
        rs.getString(2),
        rs.getString(3),
        rs.getString(4),
        rs.getString(5),
        rs.getString(6),
        rs.getString(7),
        rs.getString(8));
  }

  public String getCode() {
    return code;
  }

  public Optional<String> getName() {
    return Optional.ofNullable(this.name);
  }

  public Optional<String> getCaseCategory() {
    return Optional.ofNullable(this.casecategory);
  }

  public boolean isInitial() {
    return initial;
  }

  /**
   * Since the fee is only on initial cases, and case types not in codes DB are always in older
   * cases, then there will always be no fee involved here.
   *
   * @return
   */
  public BigDecimal getFee() {
    return (fee != null) ? fee : new BigDecimal(0.0);
  }

  public Optional<String> getWillFiledDate() {
    return Optional.ofNullable(this.willfileddate);
  }

  public Optional<String> getEfspcode() {
    return Optional.ofNullable(this.efspcode);
  }

  /** Turns the object into a map; attributes can be combined with HATEOS endpoints */
  public Map<String, Object> toMap() {
    return Map.ofEntries(
        Map.entry("code", code),
        Map.entry("name", name),
        Map.entry("casecategory", casecategory),
        Map.entry("initial", initial),
        Map.entry("fee", fee),
        Map.entry("willfileddate", willfileddate),
        Map.entry("efspcode", efspcode),
        Map.entry("location", location));
  }

  // HACK(brycew): here for when CaseType isn't present, but we have AllWrong collector
  public static CaseType DummyCase() {
    return new CaseType(
        "Bad code",
        "Bad name",
        "Bad Case Categor",
        "true",
        "-1",
        "Bad date",
        "Bade code",
        "Bad loc");
  }

  public static PreparedStatement prepQueryBroad(
      Connection conn, String domain, String courtLocationId, String caseCategoryCode)
      throws SQLException {
    PreparedStatement st = conn.prepareStatement(getCaseTypesForCategory());
    st.setString(1, domain);
    st.setString(2, courtLocationId);
    st.setString(3, caseCategoryCode);
    return st;
  }

  public static PreparedStatement prepQueryTiming(
      Connection conn,
      String domain,
      String courtLocationId,
      String caseCategoryCode,
      Optional<Boolean> isInitial)
      throws SQLException {
    boolean specifiedInitial = isInitial.orElse(false);
    if (specifiedInitial) {
      PreparedStatement st = conn.prepareStatement(getCaseTypesForTiming());
      st.setString(1, domain);
      st.setString(2, courtLocationId);
      st.setString(3, caseCategoryCode);
      st.setString(4, Boolean.toString(specifiedInitial));
      return st;
    }

    PreparedStatement st = conn.prepareStatement(getCaseTypesForCategory());
    st.setString(1, domain);
    st.setString(2, courtLocationId);
    st.setString(3, caseCategoryCode);
    return st;
  }

  private static String getCaseTypesForCategory() {
    return """
        SELECT code, name, casecategory, initial,
        fee, willfileddate, efspcode, location
        FROM casetype WHERE domain=? AND location=? AND casecategory=?""";
  }

  private static String getCaseTypesForTiming() {
    return getCaseTypesForCategory() + " AND initial ILIKE ?";
  }

  public static PreparedStatement prepQueryWithCode(
      Connection conn, String domain, String courtLocationId, String caseTypeCode)
      throws SQLException {
    String withCode =
        """
        SELECT code, name, casecategory, initial,
          fee, willfileddate, efspcode, location
        FROM casetype WHERE domain=? AND location=? AND code=?
        """;
    PreparedStatement st = conn.prepareStatement(withCode);
    st.setString(1, domain);
    st.setString(2, courtLocationId);
    st.setString(3, caseTypeCode);
    return st;
  }

  public static PreparedStatement prepSearchQuery(Connection conn, String domain, String searchTerm)
      throws SQLException {
    String search =
        """
        SELECT DISTINCT name
        FROM casetype
        WHERE domain=? AND name ILIKE ?
        ORDER BY name
        """;
    PreparedStatement st = conn.prepareStatement(search);
    st.setString(1, domain);
    st.setString(2, searchTerm);
    return st;
  }

  public static PreparedStatement prepRetrieveQuery(
      Connection conn, String domain, String caseTypeName) throws SQLException {
    String retrieve =
        """
        SELECT DISTINCT code, location
        FROM casetype
        WHERE domain=? AND name=?
        ORDER BY location
        """;
    PreparedStatement st = conn.prepareStatement(retrieve);
    st.setString(1, domain);
    st.setString(2, caseTypeName);
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
}
