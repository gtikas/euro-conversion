package com.kalaerik.productmng.exceptions;

import javax.persistence.EntityNotFoundException;

public class DataNotFound extends RuntimeException  {
    public DataNotFound(String message) {
        super(message);
    }
}