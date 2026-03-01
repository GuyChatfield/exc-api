package com.example.codingexercise.controller;

import com.example.codingexercise.model.ProductPackage;
import com.example.codingexercise.repository.PackageRepository;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PackageController {

    private final PackageRepository packageRepository;

    public PackageController(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/packages")
    public ProductPackage create(@RequestBody ProductPackage newProductPackage) {
        return packageRepository.create(newProductPackage.getName(), newProductPackage.getDescription(),
                newProductPackage.getProductQuantities(), newProductPackage.getOwnerUsername());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/packages")
    public List<ProductPackage> list(@RequestParam(required = false) String ownerUsername) {
        if (ownerUsername != null && !ownerUsername.isBlank()) {
            return packageRepository.listByOwnerUsername(ownerUsername);
        }

        return packageRepository.list();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/packages/all")
    public List<ProductPackage> listAll() {
        return packageRepository.list();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/packages/{id}")
    public ProductPackage update(@PathVariable String id, @RequestBody ProductPackage updatedProductPackage) {
        return packageRepository.update(id, updatedProductPackage);
    }

    @DeleteMapping("/packages/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = packageRepository.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/packages/{id}")
    public ResponseEntity<ProductPackage> get(@PathVariable String id) {
        return packageRepository.get(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));
    }

}
