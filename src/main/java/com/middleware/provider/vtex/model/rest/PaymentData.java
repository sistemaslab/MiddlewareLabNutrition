/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.middleware.provider.vtex.model.rest;

import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class PaymentData {
    private ArrayList <Payments> payments ;
    private ArrayList <GiftCards> giftCards ;

    
}
