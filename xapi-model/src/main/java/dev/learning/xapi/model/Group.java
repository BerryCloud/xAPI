/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * This class represents the xAPI Group object.
 *
 * @author Thomas Turrell-Croft
 * 
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#group">xAPI Group</a>
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonSubTypes({@JsonSubTypes.Type(value = Group.class, name = "Group")})
public class Group extends Actor {

  @Default
  private final ObjectType objectType = ObjectType.GROUP;

  /**
   * The members of this Group.
   */
  @Valid
  @JsonFormat(without = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY})
  private final Agent[] member;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for Group.
   */
  public abstract static class Builder<C extends Group, B extends Builder<C, B>>
      extends Actor.Builder<C, B> {

    // This static class extends the lombok builder.

    /**
     * Consumer Builder for member.
     *
     * @param member The Consumer Builder for member.
     *
     * @return This builder
     *
     * @see Group#member
     */
    public Builder<C, B> addMember(
        @SuppressWarnings("rawtypes") Consumer<Agent.Builder> member) {

      final Agent.Builder<?, ?> builder = Agent.builder();

      member.accept(builder);

      return addMember(builder.build());
    }

    /**
     * Adds a member entry.
     *
     * @param agent The agent to add.
     *
     * @return This builder
     *
     * @see Group#member
     */
    public Builder<C, B> addMember(Agent agent) {

      if (member == null) {
        member = new Agent[] {agent};

        return this;
      }

      final List<Agent> list = new ArrayList<>(Arrays.asList(member));
      list.add(agent);
      member = list.toArray(member);

      return this;

    }

  }

}
