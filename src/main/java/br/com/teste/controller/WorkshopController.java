package br.com.teste.controller;

import br.com.teste.dto.AtaResponseDTO;
import br.com.teste.dto.WorkshopRequestDTO;
import br.com.teste.dto.WorkshopResponseDTO;
import br.com.teste.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workshops")
@RequiredArgsConstructor
public class WorkshopController {

    private final WorkshopService workshopService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkshopResponseDTO> criarWorkshop(@RequestBody WorkshopRequestDTO workshop) {
        WorkshopResponseDTO salvo = workshopService.criarWorkshop(workshop);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{workshopId}/atas/{ataId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AtaResponseDTO> adicionarColaboradorNaAta(
            @PathVariable Long workshopId,
            @PathVariable Long ataId,
            @RequestBody  Long colaboradorId) {
        AtaResponseDTO atualizada = workshopService.adicionarColaboradorNaAta(workshopId, ataId, colaboradorId);
        return ResponseEntity.ok(atualizada);
    }
}
