package edu.suffolk.litlab.efspserver.services.cases;

import edu.suffolk.litlab.efspserver.ContactInformation;
import edu.suffolk.litlab.efspserver.Name;

public class Person {
  private final Name name;
  private final ContactInformation contactInfo;

  public Person(Name name, ContactInformation contactInfo) {
    this.name = name;
    this.contactInfo = contactInfo;
  }

  public Name getName() {
    return name;
  }

  public ContactInformation getContactInformation() {
    return contactInfo;
  }
}
