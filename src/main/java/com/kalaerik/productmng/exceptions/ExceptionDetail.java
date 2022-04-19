package com.kalaerik.productmng.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionDetail {
    private Date timestamp;
    private String message;
    private String details;

    public ExceptionDetail() {

    }
}
