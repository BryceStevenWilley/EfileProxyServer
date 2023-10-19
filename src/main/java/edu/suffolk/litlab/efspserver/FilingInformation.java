package edu.suffolk.litlab.efspserver;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilingInformation {
  private static Logger log = LoggerFactory.getLogger(FilingInformation.class);

  private String courtLocationId;
  /** Plaintiffs that are new to the case; not in the case info before this filing */
  private List<Person> newPlaintiffs = List.of();

  private List<Person> newDefendants = List.of();
  private Map<String, PartyId> varToPartyId = Map.of();

  /**
   * A list of attorney UIDs.
   *
   * <p>For Tyler at least, the Attorneys are registered in the system beforehand. In that case,
   * these are the GUIDs of the Attorneys in that system.
   *
   * <p>TODO(brycew-later): this isn't super general, and relies on registering Attorneys elsewhere.
   * Make it more generic if possible.
   */
  private List<String> attorneyIds = List.of();

  /**
   * A map from party Ids (in the person Objs) to a list of their Attorneys
   *
   * <p>A party not present in the map means you don't know who the attorney is (or for subsequent
   * filings, don't want to modify any attorneys). A party with an empty list means the party is Pro
   * Se.
   */
  private Map<PartyId, List<String>> partyToAttorneyIds = Map.of();

  /**
   * List of service contacts to add to the case. Contacts are mostly an ID of the contact already
   * registered within the EFM system.
   */
  private List<CaseServiceContact> serviceContacts = List.of();

  /** A tracking ID from the EFM system for the existing case. For subsequent filing. */
  private Optional<String> prevCaseId = Optional.empty();
  /** Existing docket number, NOT from EFM system. For subsequent filing into non-indexed cases. */
  private Optional<String> caseDocketNumber = Optional.empty();

  // TODO(brycew-later): refactor so this is selected only in Tyler stuff
  private String caseCategoryCode;
  private String caseTypeCode;
  private String caseSubtypeCode;
  private String paymentId;
  private List<FilingDoc> filingDocs = List.of();
  private Optional<LocalDate> returnDate = Optional.empty();

  /** Information about the original case if this is an appeals court case. */
  private Optional<LowerCourtInfo> lowerCourtInfo = Optional.empty();

  private Person leadContact;

  private JsonNode miscInfo;

  /**
   * Gets all of the peole who are listed by filer ids in the filing docs. This corresponds to the
   * `users` variable in Docassemble interviews.
   *
   * @return The People who are filers in the filing documents
   */
  public List<PartyId> getFilers() {
    List<PartyId> filers = new ArrayList<PartyId>();
    filingDocs.forEach(d -> filers.addAll(d.getFilingPartyIds()));
    return filers;
  }

  /**
   * TODO(brycew): continue: always get some DA specific contact info for the filing, they need to
   * enter it there
   *
   * @return
   */
  public Person getLeadContact() {
    return leadContact;
  }

  public String makeCaseTitle() {
    if (getNewPlaintiffs().size() > 0 && getNewDefendants().size() > 0) {
      return getNewPlaintiffs().get(0).getName().getTitleName()
          + " v. "
          + getNewDefendants().get(0).getName().getTitleName();
    } else if (getNewPlaintiffs().size() > 0) {
      return "In the matter of " + getNewPlaintiffs().get(0).getName().getTitleName();
    } else if (getNewDefendants().size() > 0) {
      return "In the matter of " + getNewDefendants().get(0).getName().getTitleName();
    } else {
      log.warn(
          "Cannot guess title of the case (not existing case, no plaintiffs or defendants)"
              + " (filing "
              + getFilings().get(0).getFilingComments()
              + ")\nUsing backup of lead contact");
      return "On Behalf of " + getLeadContact().getName().getFullName();
    }
  }

  public String getCourtLocation() {
    return courtLocationId;
  }

  public List<Person> getNewPlaintiffs() {
    return newPlaintiffs;
  }

  public List<Person> getNewDefendants() {
    return newDefendants;
  }

  public Map<String, PartyId> getVarToPartyId() {
    return varToPartyId;
  }

  public List<String> getAttorneyIds() {
    return attorneyIds;
  }

  public Map<PartyId, List<String>> getPartyAttorneyMap() {
    return partyToAttorneyIds;
  }

  /**
   * We only store the mapping from Party to attorneys (since parties can have multiple attorneys,
   * but not vice-versa.
   */
  public List<PartyId> getPartyRepByAttorney(String attorneyId) {
    List<PartyId> parties = new ArrayList<>();
    for (Map.Entry<PartyId, List<String>> partyToAttorneys : partyToAttorneyIds.entrySet()) {
      if (partyToAttorneys.getValue().contains(attorneyId)) {
        parties.add(partyToAttorneys.getKey());
      }
    }
    return parties;
  }

  public String getCaseCategoryCode() {
    return caseCategoryCode;
  }

  public String getCaseTypeCode() {
    return caseTypeCode;
  }

  public List<CaseServiceContact> getServiceContacts() {
    return serviceContacts;
  }

  public String getCaseSubtypeCode() {
    return caseSubtypeCode;
  }

  public List<FilingDoc> getFilings() {
    return filingDocs;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public Optional<String> getPreviousCaseId() {
    return prevCaseId;
  }

  public Optional<String> getCaseDocketNumber() {
    return caseDocketNumber;
  }

  public boolean isInitialFiling() {
    return prevCaseId.isEmpty() && caseDocketNumber.isEmpty();
  }

  public Optional<LocalDate> getReturnDate() {
    return returnDate;
  }

  public JsonNode getMiscInfo() {
    return miscInfo;
  }

  public Optional<LowerCourtInfo> getLowerCourtInfo() {
    return lowerCourtInfo;
  }

  public void setCourtLocation(String courtLocationId) {
    this.courtLocationId = courtLocationId;
  }

  public void setNewPlaintiffs(List<Person> plaintiffs) {
    this.newPlaintiffs = plaintiffs;
  }

  public void setNewDefendants(List<Person> defendants) {
    this.newDefendants = defendants;
  }

  public void setVarToPartyId(Map<String, PartyId> varToPartyId) {
    this.varToPartyId = Map.copyOf(varToPartyId);
  }

  public void setAttorneyIds(List<String> attorneyIds) {
    this.attorneyIds = attorneyIds;
  }

  public void setPartyAttorneyMap(Map<PartyId, List<String>> partyToAttorneyIds) {
    this.partyToAttorneyIds = partyToAttorneyIds;
  }

  public void setServiceContacts(List<CaseServiceContact> serviceContacts) {
    this.serviceContacts = serviceContacts;
  }

  public void setCaseCategoryCode(String caseCategoryCode) {
    this.caseCategoryCode = caseCategoryCode;
  }

  public void setPreviousCaseId(String id) {
    this.prevCaseId = Optional.ofNullable(id);
  }

  public void setCaseDocketNumber(String num) {
    this.caseDocketNumber = Optional.ofNullable(num);
  }

  public void setCaseTypeCode(String caseTypeCode) {
    this.caseTypeCode = caseTypeCode;
  }

  public void setCaseSubtypeCode(String caseSubtypeId) {
    this.caseSubtypeCode = caseSubtypeId;
  }

  public void setLeadContact(Person leadContact) {
    this.leadContact = leadContact;
  }

  public void setFilings(List<FilingDoc> filingDocs) {
    this.filingDocs = filingDocs;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

  public void setReturnDate(Optional<LocalDate> returnDate) {
    this.returnDate = returnDate;
  }

  public void setMiscInfo(JsonNode node) {
    this.miscInfo = node;
  }

  public void setLowerCourtInfo(LowerCourtInfo info) {
    this.lowerCourtInfo = Optional.ofNullable(info);
  }

  public void setLowerCourtInfo(Optional<LowerCourtInfo> info) {
    this.lowerCourtInfo = info;
  }
}
