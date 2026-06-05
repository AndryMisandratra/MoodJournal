package com.moodjournal.backend.controller;

import com.moodjournal.backend.dto.request.CreateEntryRequest;
import com.moodjournal.backend.dto.request.UpdateEntryRequest;
import com.moodjournal.backend.dto.response.EntryResponse;
import com.moodjournal.backend.security.JwtTokenProvider;
import com.moodjournal.backend.service.JournalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journal")
@RequiredArgsConstructor
public class JournalController {

    private final JournalService journalService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/entries")
    public ResponseEntity<EntryResponse> createEntry(@Valid @RequestBody CreateEntryRequest request,
                                                     HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        Long userId = tokenProvider.getUserIdFromToken(token);
        EntryResponse response = journalService.createEntry(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        throw new RuntimeException("Missing or invalid Authorization header");
    }

    // Dans JournalController.java

    @GetMapping("/entries")
    public ResponseEntity<List<EntryResponse>> getUserEntries(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        return ResponseEntity.ok(journalService.getUserEntries(userId));
    }

    @PutMapping("/entries/{entryId}")
    public ResponseEntity<EntryResponse> updateEntry(@PathVariable Long entryId,
                                                     @Valid @RequestBody UpdateEntryRequest request,
                                                     HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        EntryResponse response = journalService.updateEntry(userId, entryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long entryId,
                                            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        journalService.deleteEntry(userId, entryId);
        return ResponseEntity.noContent().build();
    }

    // Extraire le userId du token
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = extractToken(request);
        return tokenProvider.getUserIdFromToken(token);
    }

}