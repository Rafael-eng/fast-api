package br.com.teste.controller;

import br.com.teste.dto.AtaRequestDTO;
import br.com.teste.dto.AtaResponseDTO;
import br.com.teste.entity.Ata;
import br.com.teste.service.AtaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/atas")
@RequiredArgsConstructor
public class AtaController {

    private final AtaService ataService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AtaResponseDTO> criarAta(@RequestBody AtaRequestDTO ata) {
        AtaResponseDTO salva = ataService.criarAta(ata);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @DeleteMapping("/{ataId}/colaboradores/{colaboradorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removerColaboradorDaAta(
            @PathVariable Long ataId,
            @PathVariable Long colaboradorId) {
        ataService.removerColaboradorDaAta(ataId, colaboradorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AtaResponseDTO>> listarAtas(
            @RequestParam(required = false) String workshopNome,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<AtaResponseDTO> response = ataService.listarAtas(workshopNome, data);
        return ResponseEntity.ok(response);
    }
}