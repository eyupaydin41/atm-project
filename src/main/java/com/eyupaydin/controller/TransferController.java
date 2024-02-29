package com.eyupaydin.controller;

import com.eyupaydin.dto.TransferDto;
import com.eyupaydin.service.TransferServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransferController {

    TransferServices transferServices;

    public TransferController(TransferServices transferServices) {
        this.transferServices = transferServices;
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Map<String, Boolean>> withdrawMoney(@RequestBody TransferDto transferDto) {
        return transferServices.withdrawMoney(transferDto);
    }

    @PutMapping("/deposit")
    public ResponseEntity<Map<String, Boolean>> depositMoney(@RequestBody TransferDto transferDto) {
        return transferServices.depositMoney(transferDto);
    }

    @PutMapping("/transfer")
    public ResponseEntity<Map<TransferDto, Boolean>> transfer(@RequestBody TransferDto transferDto) {
        return transferServices.transfer(transferDto);
    }


}
