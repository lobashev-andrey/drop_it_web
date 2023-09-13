package org.example.web.dto;

import javax.validation.constraints.*;

public class BookIdToRemove {

    @NotNull
    @Positive
    @Digits(integer = 7, fraction = 0)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
