package br.com.teste.controller;

import br.com.teste.dto.ColaboradorRequestDTO;
import br.com.teste.dto.ColaboradorResponseDTO;
import br.com.teste.service.ColaboradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/colaboradores")
@RequiredArgsConstructor
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColaboradorResponseDTO> criarColaborador(@RequestBody ColaboradorRequestDTO colaborador) {
        ColaboradorResponseDTO salvo = colaboradorService.criarColaborador(colaborador);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}