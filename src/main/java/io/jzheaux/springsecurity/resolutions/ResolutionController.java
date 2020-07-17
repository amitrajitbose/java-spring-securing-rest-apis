package io.jzheaux.springsecurity.resolutions;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ResolutionController {
    private final ResolutionRepository resolutions;

    public ResolutionController(ResolutionRepository resolutions) {
        this.resolutions = resolutions;
    }

    @GetMapping("/resolutions")
    @PreAuthorize("hasAuthority('resolution:read')")
    @PostFilter("filterObject.owner == authentication.name")
    public Iterable<Resolution> read() {
        return this.resolutions.findAll();
    }

    @GetMapping("/resolution/{id}")
    @PreAuthorize("hasAuthority('resolution:read')")
    @PostAuthorize("returnObject.orElse(null)?.owner == authentication.name")
    public Optional<Resolution> read(@PathVariable("id") UUID id) {
        return this.resolutions.findById(id);
    }

    @PostMapping("/resolution")
    @PreAuthorize("hasAuthority('resolution:write')")
    public Resolution make(@CurrentUsername String owner, @RequestBody String text) {
        Resolution resolution = new Resolution(text, owner);
        return this.resolutions.save(resolution);
    }

    @PutMapping(path = "/resolution/{id}/revise")
    @PreAuthorize("hasAuthority('resolution:write')")
    @Transactional
    public Optional<Resolution> revise(@PathVariable("id") UUID id, @RequestBody String text) {
        this.resolutions.revise(id, text);
        return read(id);
    }

    @PutMapping("/resolution/{id}/complete")
    @PreAuthorize("hasAuthority('resolution:write')")
    @Transactional
    public Optional<Resolution> complete(@PathVariable("id") UUID id) {
        this.resolutions.complete(id);
        return read(id);
    }

    @GetMapping("/health")
    public String healthCheck() {
        int mb = 1024 * 1024;
        Runtime instance = Runtime.getRuntime();
        String responseString = "App Working Successfully\n";
        long totalMemory = instance.totalMemory() / mb;
        long freeMemory = instance.freeMemory() / mb;
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = instance.maxMemory() / mb;
        int availableProcessors = instance.availableProcessors();
        responseString += "***** Heap utilization statistics [MB] *****\n";
        responseString += "Total : " + totalMemory + "\n";
        responseString += "Free : " + freeMemory + "\n";
        responseString += "Used : " + usedMemory + "\n";
        responseString += "Max : " + maxMemory + "\n";
        responseString += "Available Processors: " + availableProcessors + "\n";
        return responseString;
    }
}
