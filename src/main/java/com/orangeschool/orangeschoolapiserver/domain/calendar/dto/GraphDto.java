package com.orangeschool.orangeschoolapiserver.domain.calendar.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GraphDto {

    private Integer year;
    private Integer month;
    private String name;
    private Long amount;
    private String date;
}
