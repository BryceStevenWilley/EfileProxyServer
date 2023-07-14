package edu.suffolk.litlab.efspserver;

import gov.niem.niem.niem_core._2.PersonNameTextType;
import gov.niem.niem.niem_core._2.PersonNameType;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Provide;
import net.jqwik.api.domains.DomainContextBase;

public class PersonNameExamples extends DomainContextBase {

  @Provide
  Arbitrary<PersonNameTextType> nameTexts() {
    return Arbitraries.strings().map((x) -> {
      var pntt = new PersonNameTextType();
      pntt.setValue(x);
      return pntt;
    });
  }

  @Provide
  Arbitrary<PersonNameType> names() {
    Arbitrary<PersonNameTextType> firsts = Arbitraries.defaultFor(PersonNameTextType.class);
    Arbitrary<PersonNameTextType> middles = Arbitraries.defaultFor(PersonNameTextType.class);
    Arbitrary<PersonNameTextType> lasts = Arbitraries.defaultFor(PersonNameTextType.class);
    return Combinators.combine(firsts, middles, lasts).as((f, m, l) -> {
      var pnt = new PersonNameType();
      pnt.setPersonGivenName(f);
      pnt.setPersonMiddleName(m);
      pnt.setPersonSurName(l);
      return pnt;
    });
  }

}
