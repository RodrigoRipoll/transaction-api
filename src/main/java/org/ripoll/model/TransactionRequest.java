package org.ripoll.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(@NotNull Double amount,
                                 @NotBlank String type,
                                 Long parentId) {
    @JsonCreator
    public TransactionRequest(@JsonProperty("amount") Double amount,
                              @JsonProperty("type") String type,
                              @JsonProperty("parent_id")Long parentId) {
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }
}
