package com.mounanga.customerservice.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PageModel<T> {
    private int page;
    private int totalPages;
    private int size;
    private long totalElements;
    private int numberOfElements;
    private int number;
    private List<T> content;
    private boolean hasContent;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;
}
