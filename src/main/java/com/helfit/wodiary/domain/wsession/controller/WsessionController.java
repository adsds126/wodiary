package com.helfit.wodiary.domain.wsession.controller;

import com.helfit.wodiary.domain.wsession.dto.WsessionDto;
import com.helfit.wodiary.domain.wsession.entity.Wsession;
import com.helfit.wodiary.domain.wsession.service.WsessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/v1/wsession")
public class WsessionController {

    @Autowired
    private WsessionService wsessionService;

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody WsessionDto.Add sessionDto) {
        try {
            WsessionDto.Response sessionResponse = wsessionService.createSession(sessionDto);
            return ResponseEntity.ok(sessionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating session: " + e.getMessage());
        }
    }

    @GetMapping("/{date}")
    public ResponseEntity<WsessionDto.Response> getSession(@PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            WsessionDto.Response sessionResponse = wsessionService.getSession(localDate);
            return ResponseEntity.ok(sessionResponse);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
