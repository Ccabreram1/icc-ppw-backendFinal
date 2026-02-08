package ec.edu.ups.icc.portafolio.modules.portfolios.controllers;

import ec.edu.ups.icc.portafolio.modules.portfolios.dtos.PortfolioRequestDto;
import ec.edu.ups.icc.portafolio.modules.portfolios.dtos.PortfolioResponseDto;
import ec.edu.ups.icc.portafolio.modules.portfolios.services.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public ResponseEntity<Page<PortfolioResponseDto>> getAllPortfolios(Pageable pageable) {
        return ResponseEntity.ok(portfolioService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponseDto> getPortfolioById(@PathVariable Long id) {
        return ResponseEntity.ok(portfolioService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PortfolioResponseDto> getPortfolioByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(portfolioService.findByUserId(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROGRAMMER')")
    public ResponseEntity<PortfolioResponseDto> createPortfolio(
            @Valid @RequestBody PortfolioRequestDto portfolioDto) {
        PortfolioResponseDto createdPortfolio = portfolioService.create(portfolioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPortfolio);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @portfolioSecurity.isOwner(#id)")
    public ResponseEntity<PortfolioResponseDto> updatePortfolio(
            @PathVariable Long id,
            @Valid @RequestBody PortfolioRequestDto portfolioDto) {
        return ResponseEntity.ok(portfolioService.update(id, portfolioDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @portfolioSecurity.isOwner(#id)")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        portfolioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/speciality/{speciality}")
    public ResponseEntity<List<PortfolioResponseDto>> getPortfoliosBySpeciality(
            @PathVariable String speciality) {
        return ResponseEntity.ok(portfolioService.findBySpeciality(speciality));
    }

    @GetMapping("/available")
    public ResponseEntity<List<PortfolioResponseDto>> getAvailablePortfolios() {
        return ResponseEntity.ok(portfolioService.findAvailablePortfolios());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PortfolioResponseDto>> searchPortfolios(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String speciality,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            Pageable pageable) {
        return ResponseEntity.ok(portfolioService.search(
                name, speciality, minExperience, maxExperience, pageable));
    }
}