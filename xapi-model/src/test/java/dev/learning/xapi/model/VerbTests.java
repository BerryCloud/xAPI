/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * Verb Tests.
 *
 * @author Lukáš Sahula
 * @author Thomas Turrell-Croft
 */
@DisplayName("Verb tests")
class VerbTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  /*
   * Deserialization tests.
   */

  @Test
  void whenDeserializingVerbThenResultIsInstanceOfVerb() throws IOException {

    final File file = ResourceUtils.getFile("classpath:verb/verb.json");

    // When Deserializing Verb
    final Verb result = objectMapper.readValue(file, Verb.class);

    // Then Result Is Instance Of Verb
    assertThat(result, instanceOf(Verb.class));

  }

  @Test
  void whenDeserializingVerbThenIdIsExpected() throws IOException {

    final File file = ResourceUtils.getFile("classpath:verb/verb.json");

    // When Deserializing Verb
    final Verb result = objectMapper.readValue(file, Verb.class);

    // Then Id Is Expected
    assertThat(result.getId(), is(URI.create("http://adlnet.gov/expapi/verbs/answered")));

  }

  /*
   * Serialization tests.
   */

  @Test
  void whenSerializingVerbThenResultIsEqualToExpectedJson() throws IOException {

    final Verb verb = Verb.builder()

        .id(URI.create("http://adlnet.gov/expapi/verbs/answered"))

        .addDisplay(Locale.ENGLISH, "answered")

        .build();

    // When Serializing Verb
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(verb));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(ResourceUtils.getFile("classpath:verb/verb.json"))));

  }



  @Test
  void whenSerializingVerbWithoutDisplayThenResultIsEqualToExpectedJson() throws IOException {

    final Verb verb = Verb.builder()

        .id(URI.create("http://adlnet.gov/expapi/verbs/answered"))

        .build();

    // When Serializing Verb Without Display
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(verb));

    // Then Result Is Equal To Expected Json
    assertThat(result, is(
        objectMapper.readTree(ResourceUtils.getFile("classpath:verb/verb_without_display.json"))));

  }



  /*
   * toString Test.
   */

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final Verb verb =
        objectMapper.readValue(ResourceUtils.getFile("classpath:verb/verb.json"), Verb.class);

    // When Calling ToString
    final String result = verb.toString();

    // Then Result Is Expected
    assertThat(result,
        is("Verb(id=http://adlnet.gov/expapi/verbs/answered, display={en=answered})"));

  }

  /*
   * Constructor tests.
   */

  @Test
  void givenVerbIsConstructedWithIdAndNameWhenGettingDisplayForLocaleUNDThenResultIsExpected() {

    // Given Verb Is Constructed With Id And Name
    final Verb verb = new Verb("http://adlnet.gov/expapi/verbs/answered", "answered");

    // When Getting Display For Locale UND
    final var result = verb.getDisplay().get(Locale.forLanguageTag("UND"));

    // Then Result Is Expected
    assertThat(result, is("answered"));
  }

  @Test
  void givenVerbIsConstructedWithIdAndNameWhenGettingIdThenIdIsExpected() {

    // Given Verb Is Constructed With Id And Name
    final Verb verb = new Verb("http://adlnet.gov/expapi/verbs/answered", "answered");

    // When Getting Id
    final var result = verb.getId();

    // Then Id Is Expected
    assertThat(result, is(URI.create("http://adlnet.gov/expapi/verbs/answered")));
  }

  @Test
  void givenVerbIsConstructedWithIdWhenGettingIdThenIdIsExpected() {

    // Given Verb Is Constructed With Id
    final Verb verb = new Verb("http://adlnet.gov/expapi/verbs/answered");

    // When Getting Id
    final var result = verb.getId();

    // Then Id Is Expected
    assertThat(result, is(URI.create("http://adlnet.gov/expapi/verbs/answered")));
  }

  /*
   * Builder Tests
   */

  @Test
  void whenBuildingVerbWithTwoDisplayValuesThenDisplayLanguageMapHasTwoEntries() {

    // When Building Verb With Two Display Values
    final Verb verb = Verb.builder()

        .id("http://adlnet.gov/expapi/verbs/answered")

        .addDisplay(Locale.ENGLISH, "answered")

        .addDisplay(Locale.GERMAN, "antwortete")

        .build();

    // Then Display Language Map Has Two Entries
    assertThat(verb.getDisplay(), aMapWithSize(2));

  }

  /*
   * Equality tests.
   */

  @Test
  void givenVerbWithoutDisplayPropertyWhenTestingEqualityWithVerbWithDisplayPropertyThenVerbsAreEqual() {

    // Given Verb Without Display Property
    final Verb verb = Verb.builder()

        .id(URI.create("http://adlnet.gov/expapi/verbs/answered"))

        .build();

    // When Testing Equality With Verb With Display Property
    final boolean result = verb.equals(Verb.builder()

        .id("http://adlnet.gov/expapi/verbs/answered")

        .addDisplay(Locale.ENGLISH, "answered")

        .build());

    // Then Verbs Are Equal
    assertThat(result, is(true));

  }

  @Test
  void givenVerbWithGermanDisplayPropertyWhenTestingEqualityWithVerbWithEnglishDisplayPropertyThenVerbsAreEqual() {

    // Given Verb With German Display Property
    final Verb verb = Verb.builder()

        .id("http://adlnet.gov/expapi/verbs/answered")

        .addDisplay(Locale.GERMAN, "antwortete")

        .build();

    // When Testing Equality With Verb With English Display Property
    final boolean result = verb.equals(Verb.builder()

        .id("http://adlnet.gov/expapi/verbs/answered")

        .addDisplay(Locale.ENGLISH, "answered")

        .build());

    // Then Verbs Are Equal
    assertThat(result, is(true));

  }

  @Test
  void givenVerbWithIdWhenTestingEqualityWithVerbWithDifferentIdThenVerbsAreNotEqual() {

    // Given Verb With Id
    final Verb verb = Verb.builder()

        .id("http://adlnet.gov/expapi/verbs/answered")

        .build();

    // When Testing Equality With Verb With Different Id
    final boolean result = verb.equals(Verb.builder()

        .id("http://adlnet.gov/expapi/verbs/interacted")

        .build());

    // Then Verbs Are Not Equal
    assertThat(result, not(true));

  }

  /*
   * Method tests
   */

  @Test
  void givenVerbHasIdVoidedWhenCallingIsVoidedThenResultIsTrue() {

    // Given Verb Has Id Voided
    final Verb verb = (Verb.builder()

        .id("http://adlnet.gov/expapi/verbs/voided")

        .build());

    // When Calling isVoided
    final boolean result = verb.isVoided();

    // Then Result Is True
    assertThat(result, is(true));

  }

  @Test
  void givenVerbDoesNotHaveIdVoidedWhenCallingIsVoidedThenResultIsFalse() {

    // Given Verb Does Not Have Id Voided
    final Verb verb = (Verb.builder()

        .id("http://adlnet.gov/expapi/verbs/answered")

        .build());

    // When Calling isVoided
    final boolean result = verb.isVoided();

    // Then Result Is False
    assertThat(result, is(false));

  }

  @Test
  public void whenValidatingVerbWithAllRequiredPropertiesThenConstraintViolationsSizeIsZero() {

    final Verb verb = Verb.builder().id("http://adlnet.gov/expapi/verbs/answered")
        .addDisplay(Locale.ENGLISH, "answered").build();

    // When Validating Interaction Component With All Required Properties
    final Set<ConstraintViolation<Verb>> constraintViolations = validator.validate(verb);

    // Then ConstraintViolations Size Is Zero
    assertThat(constraintViolations, hasSize(0));

  }

  @Test
  public void whenValidatingVerbWithoutIdThenConstraintViolationsSizeIsOne() {

    final Verb verb = Verb.builder().addDisplay(Locale.ENGLISH, "answered").build();

    // When Validating Interaction Component Without Id
    final Set<ConstraintViolation<Verb>> constraintViolations = validator.validate(verb);

    // Then ConstraintViolations Size Is One
    assertThat(constraintViolations, hasSize(1));

  }

}
