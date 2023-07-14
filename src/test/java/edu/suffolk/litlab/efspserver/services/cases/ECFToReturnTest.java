package edu.suffolk.litlab.efspserver.services.cases;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import edu.suffolk.litlab.efspserver.PersonNameExamples;
import gov.niem.niem.niem_core._2.PersonNameType;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.domains.Domain;

public class ECFToReturnTest {
  
  //@Test
  //public void testConvertName() {
  //  assertTrue(false);
  //}

  @Property
  @Domain(PersonNameExamples.class)
  boolean allNamesArentBlank(@ForAll PersonNameType pnt) {
    return !ECFToReturn.toName(pnt).getFullName().isBlank();
  }

  @Property
  @Domain(PersonNameExamples.class)
  boolean allTitlesArentBlank(@ForAll PersonNameType pnt) {
    return !ECFToReturn.toName(pnt).getTitleName().isBlank();
  }
}
