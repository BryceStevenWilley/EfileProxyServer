package edu.suffolk.litlab.efspserver.docassemble;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.hubspot.algebra.Result;

import edu.suffolk.litlab.efspserver.Name;
import edu.suffolk.litlab.efspserver.services.FilingError;
import edu.suffolk.litlab.efspserver.services.InfoCollector;
import edu.suffolk.litlab.efspserver.services.InterviewVariable;

public class NameDocassembleDeserializer {

  private static Optional<String> getStringMember(JsonNode obj, String memberName) {
    if (obj.has(memberName) 
        && obj.get(memberName).isTextual()) {
      return Optional.of(obj.get(memberName).asText());
    }
    return Optional.empty();
  }


  public static Result<Name, FilingError> fromNode(JsonNode node, InfoCollector collector) {
    if (node == null) {
      InterviewVariable var = collector.requestVar(
          "name", "The full name of the person", "IndividualName");
      collector.addRequired(var);
      if (collector.finished()) {
        FilingError err = FilingError.missingRequired(var);
        return Result.err(err);
      }
    }
    if (!node.isObject()) {
      FilingError err = FilingError.malformedInterview(
          "Can't parse person with name that's not a JSON object: " + node.get("name").toPrettyString());
      return Result.err(err);
    }
    if (!node.has("first")) {
      InterviewVariable var = collector.requestVar("name.first", 
          "The first name of a person / name of a business", "text");
      collector.addRequired(var);
      if (collector.finished()) {
        FilingError err = FilingError.missingRequired(var);
        return Result.err(err);
      }
    }

    Name name = new Name(
          "", // TODO(brycew): where should we handle prefixes? Are they always empty?
          getStringMember(node, "first").orElse(""),
          getStringMember(node, "middle").orElse(""),
          getStringMember(node, "last").orElse(""),
          getStringMember(node, "suffix").orElse(""),
          "" // TODO(brycew): where would Maiden name go, if asked for?
    );
    
    return Result.ok(name);
  }
}
