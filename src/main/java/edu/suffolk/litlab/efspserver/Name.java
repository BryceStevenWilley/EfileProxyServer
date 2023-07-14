package edu.suffolk.litlab.efspserver;

import gov.niem.niem.niem_core._2.ObjectFactory;
import gov.niem.niem.niem_core._2.PersonNameTextType;
import gov.niem.niem.niem_core._2.PersonNameType;
import java.util.stream.Stream;

public class Name {
  private String firstName;
  private String middleName;
  private String lastName;
  private String suffix;
  private String maidenName;
  private String prefix;

  /** Default constructor with all members. */
  public Name(
      String prefix,
      String firstName,
      String middleName,
      String lastName,
      String suffix,
      String maidenName) {
    if (prefix == null) {
      prefix = "";
    }
    this.prefix = prefix;
    if (firstName == null) {
      firstName = "";
    }
    this.firstName = firstName;
    if (middleName == null) {
      middleName = "";
    }
    this.middleName = middleName;
    if (lastName == null) {
      lastName = "";
    }
    this.lastName = lastName;
    if (suffix == null) {
      suffix = "";
    }
    this.suffix = suffix;
    if (maidenName == null) {
      maidenName = "";
    }
    this.maidenName = maidenName;
  }

  public Name(String companyName) {
    this("", companyName, "", "", "", "");
  }

  public Name(String firstName, String middleName, String lastName) {
    this("", firstName, middleName, lastName, "", "");
  }

  /** The full name, with no extra spaces. */
  public String getFullName() {
    if (prefix.isBlank() && firstName.isBlank() && middleName.isBlank() && suffix.isBlank()) {
      return "(No name given)";
    }
    return Stream.of(prefix, firstName, middleName, lastName, suffix)
        .reduce(
            (wd, n) -> {
              if (n.isBlank()) {
                return wd;
              } else if (wd.isBlank()) {
                return n;
              } else {
                return wd + " " + n;
              }
            })
        .get();
  }

  public String getFirstName() {
    return firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public String getLastName() {
    return lastName;
  }

  /** If there's only a first name, then us it, otherwise take only the last name. Used to create
   * the titles of cases, i.e. Jones v. Discover, etc.
   */
  public String getTitleName() {
    if (lastName == null || lastName.isBlank()) {
      if (firstName == null || firstName.isBlank()) {
        return "(Unnamed)";
      }
      return firstName;
    } else {
      return lastName;
    }
  }

  public String getSuffix() {
    return suffix;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getMaidenName() {
    return maidenName;
  }

  private static gov.niem.niem.niem_core._2.PersonNameTextType wrapName(String name) {
    ObjectFactory of = new ObjectFactory();
    PersonNameTextType t = of.createPersonNameTextType();
    t.setValue(name);
    return t;
  }

  /** Returns the PersonNameType XML object from this Name. */
  public gov.niem.niem.niem_core._2.PersonNameType getNameType() {
    ObjectFactory of = new ObjectFactory();
    PersonNameType personName = of.createPersonNameType();
    personName.setPersonGivenName(wrapName(firstName));
    personName.setPersonMaidenName(wrapName(maidenName));
    personName.setPersonMiddleName(wrapName(middleName));
    personName.setPersonSurName(wrapName(lastName));
    personName.setPersonNamePrefixText(wrapName(prefix));
    personName.setPersonNameSuffixText(wrapName(suffix));
    return personName;
  }
}
