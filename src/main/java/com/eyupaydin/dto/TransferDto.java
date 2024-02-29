package com.eyupaydin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

    private String senderAccNo;
    private String receiverAccNo;
    private Double amount;

}
