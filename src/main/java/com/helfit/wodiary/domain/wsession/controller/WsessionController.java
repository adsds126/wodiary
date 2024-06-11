package com.helfit.wodiary.domain.wsession.controller;

import com.helfit.wodiary.domain.user.entity.UserPrincipal;
import com.helfit.wodiary.domain.wsession.dto.WsessionDto;
import com.helfit.wodiary.domain.wsession.repository.WsessionRepository;
import com.helfit.wodiary.domain.wsession.service.WsessionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wsession")
public class WsessionController {
    private final WsessionService wsessionService;

    @PostMapping
    public ResponseEntity<?> createWsession(@RequestBody WsessionDto.Add sessionDto, @AuthenticationPrincipal UserPrincipal principal) {
        try {
            sessionDto.setUserId(principal.getId());
            WsessionDto.Response sessionResponse = wsessionService.createSession(sessionDto);
            return ResponseEntity.ok(sessionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating session: " + e.getMessage());
        }
    }
    @GetMapping("/{date}")
    public ResponseEntity<WsessionDto.Response> getWsession(@PathVariable String date) {
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

    @DeleteMapping("/{wsessionId}")
    public ResponseEntity<?> deleteWsession(@PathVariable LocalDate wsessionId) {
        try {
            wsessionService.deleteWsession(wsessionId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Wsession: " + e.getMessage());
        }
    }

    @PostMapping("/copy")
    public ResponseEntity<?> copyWsession(@RequestBody WsessionDto.WsessionCopyDto copyDto) {
        try {
            WsessionDto.Response newWsession = wsessionService.copyWsession(copyDto.getSourceDate(), copyDto.getTargetDate());
            return ResponseEntity.ok(newWsession);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error copying Wsession: " + e.getMessage());
        }
    }
}
