package com.eyupaydin.service;

import com.eyupaydin.dto.TransferDto;
import com.eyupaydin.model.Transfer;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TransferServices {

    public ResponseEntity<Map<String,Boolean>> withdrawMoney(TransferDto transferDto);
    public ResponseEntity<Map<String,Boolean>> depositMoney(TransferDto transferDto);
    public ResponseEntity<Map<TransferDto, Boolean>> transfer(TransferDto transferDto);

    public Transfer DtoToEntity(TransferDto transferDto);
    public TransferDto EntityToDto(Transfer transfer);

}
