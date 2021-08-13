package edu.suffolk.litlab.efspserver.docassemble;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hubspot.algebra.Result;
import com.opencsv.exceptions.CsvValidationException;
import edu.suffolk.litlab.efspserver.Address;
import edu.suffolk.litlab.efspserver.FilingInformation;
import edu.suffolk.litlab.efspserver.LegalIssuesTaxonomyCodes;
import edu.suffolk.litlab.efspserver.Person;
import edu.suffolk.litlab.efspserver.services.FilingError;
import edu.suffolk.litlab.efspserver.services.InfoCollector;
import edu.suffolk.litlab.efspserver.services.InterviewToFilingEntityConverter;
import edu.suffolk.litlab.efspserver.services.JsonExtractException;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocassembleToFilingEntityConverter extends InterviewToFilingEntityConverter {

  private static Logger log = LoggerFactory.getLogger(DocassembleToFilingEntityConverter.class); 
  private LegalIssuesTaxonomyCodes codes;
  
  public DocassembleToFilingEntityConverter(InputStream taxonomyCsv) 
      throws CsvValidationException, IOException {
    codes = new LegalIssuesTaxonomyCodes(taxonomyCsv);
  }

  @Override
  public Result<FilingInformation, FilingError> traverseInterview(String interviewContents,
      InfoCollector collector) {
    SimpleModule module = new SimpleModule();
    //module.addDeserializer(FilingDoc.class, 
    //    new FilingDocDocassembleJacksonDeserializer()); 
    module.addDeserializer(Address.class, new AddressDocassembleJacksonDeserializer(Address.class));
    module.addDeserializer(Person.class, new PersonDocassembleJacksonDeserializer(Person.class));
    module.addDeserializer(FilingInformation.class, 
        new FilingInformationDocassembleJacksonDeserializer(FilingInformation.class, codes, collector));
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(module);
    try {
      FilingInformation info = mapper.readValue(interviewContents, FilingInformation.class);
      if (info == null) {
        return Result.err(FilingError.malformedInterview("The interview contents were null"));
      }
      return Result.ok(info);  
    } catch (JsonExtractException ex) {
      log.warn("Got extract Exception: " + ex);
      return Result.err(ex.getError());
    } catch (JsonProcessingException ex) {
      log.warn("Parsing Exception: " + ex);
      return Result.err(FilingError.malformedInterview("JsonParsing Exception: " + ex));
    }
  }

}
