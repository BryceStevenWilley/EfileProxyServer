package edu.suffolk.litlab.efspserver.services.cases;

import edu.suffolk.litlab.efspserver.Person;
import edu.suffolk.litlab.efspserver.codes.CaseCategory;
import edu.suffolk.litlab.efspserver.codes.CaseType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CaseResponse {
  List<Person> participants;
  Map<String, Attorney> attorneys;

  CaseCategory caseCategory;
  CaseType caseType;
  String caseTitle;
  LocalDateTime filedDate;
  // TODO(Attorneys)
}
