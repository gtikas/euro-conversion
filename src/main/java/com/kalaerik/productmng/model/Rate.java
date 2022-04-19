package com.kalaerik.productmng.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rate implements Serializable {

    //private String rateNumber;
    //private String changeDate;
    //private String state;
    //private String currencyCode;
    private String currency;
    //private int unit;
    private String buyingRate;
    private String middleRate;
    private String sellingRate;

    /*
    "Broj tečajnice": "71",
            "Datum primjene": "12.04.2022",
            "Država": "EMU",
            "Šifra valute": "978",
            "Valuta": "EUR",
            "Jedinica": 1,
            "Kupovni za devize": "7,530933",
            "Srednji za devize": "7,553594",
            "Prodajni za devize": "7,576255"

     */
}
