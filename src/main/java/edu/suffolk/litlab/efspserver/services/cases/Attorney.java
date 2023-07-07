package edu.suffolk.litlab.efspserver.services.cases;

import edu.suffolk.litlab.efspserver.ContactInformation;
import edu.suffolk.litlab.efspserver.Name;

public class Attorney extends Person {
  String attorneyId;
  String barNumber;

  public Attorney(Name name, ContactInformation contactInformation) {
    super(name, contactInformation);
  }
}
