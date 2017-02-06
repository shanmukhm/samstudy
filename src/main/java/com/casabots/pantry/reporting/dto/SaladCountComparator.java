/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.dto;


import java.util.Comparator;

public class SaladCountComparator implements Comparator<SaladCount> {

    @Override
    public int compare(SaladCount o1, SaladCount o2) {
        return o1.getCount()-o2.getCount();
    }
}
