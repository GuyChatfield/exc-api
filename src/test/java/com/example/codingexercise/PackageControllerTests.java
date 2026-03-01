package com.example.codingexercise;

import com.example.codingexercise.model.ProductPackage;
import com.example.codingexercise.repository.PackageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PackageControllerTests {

    private final TestRestTemplate restTemplate;
    private final PackageRepository packageRepository;

    @Autowired
    PackageControllerTests(TestRestTemplate restTemplate, PackageRepository packageRepository) {
        this.restTemplate = restTemplate;
        this.packageRepository = packageRepository;
    }

    @Test
    void createPackage() {
        ResponseEntity<ProductPackage> created = restTemplate.postForEntity("/packages",
                new ProductPackage(null, "Test Name", "Test Desc", Map.of("prod1", 1)), ProductPackage.class);
        assertEquals(HttpStatus.OK, created.getStatusCode(), "Unexpected status code");
        ProductPackage createdBody = created.getBody();
        assertNotNull(createdBody, "Unexpected body");
        assertEquals("Test Name", createdBody.getName(), "Unexpected name");
        assertEquals("Test Desc", createdBody.getDescription(), "Unexpected description");
        assertEquals(List.of("prod1"), createdBody.getProductQuantities().keySet().stream().toList(),
                "Unexpected products");

        ProductPackage productPackage = packageRepository.get(createdBody.getId()).orElse(null);
        assertNotNull(productPackage, "Unexpected package");
        assertEquals(createdBody.getId(), productPackage.getId(), "Unexpected id");
        assertEquals(createdBody.getName(), productPackage.getName(), "Unexpected name");
        assertEquals(createdBody.getDescription(), productPackage.getDescription(), "Unexpected description");
        assertEquals(createdBody.getProductQuantities(), productPackage.getProductQuantities(), "Unexpected products");
    }

    @Test
    void createPackageWithOwner() {
        ProductPackage pkg = new ProductPackage(null, "Owner Test", "Desc", Map.of("prod1", 2));
        pkg.setOwnerUsername("testuser");

        ResponseEntity<ProductPackage> created = restTemplate.postForEntity("/packages", pkg, ProductPackage.class);
        assertEquals(HttpStatus.OK, created.getStatusCode());
        assertNotNull(created.getBody());
        assertEquals("testuser", java.util.Objects.requireNonNull(created.getBody()).getOwnerUsername());
    }

    @Test
    void getPackage() {
        ProductPackage productPackage = packageRepository.create("Test Name 2", "Test Desc 2", Map.of("prod2", 1));
        ResponseEntity<ProductPackage> fetched = restTemplate.getForEntity("/packages/{id}", ProductPackage.class,
                productPackage.getId());
        assertEquals(HttpStatus.OK, fetched.getStatusCode(), "Unexpected status code");
        ProductPackage fetchedBody = java.util.Objects.requireNonNull(fetched.getBody(), "Unexpected body");
        assertEquals(productPackage.getId(), fetchedBody.getId(), "Unexpected id");
        assertEquals(productPackage.getName(), fetchedBody.getName(), "Unexpected name");
        assertEquals(productPackage.getDescription(), fetchedBody.getDescription(), "Unexpected description");
        assertEquals(productPackage.getProductQuantities(), fetchedBody.getProductQuantities(), "Unexpected products");
    }

    @Test
    void getPackage_notFound() {
        ResponseEntity<ProductPackage> fetched = restTemplate.getForEntity("/packages/{id}", ProductPackage.class,
                "nonexistent-id-12345");
        assertEquals(HttpStatus.NOT_FOUND, fetched.getStatusCode());
    }

    @Test
    void listPackages() {
        ProductPackage productPackage1 = packageRepository.create("Test Name 1", "Test Desc 1", Map.of("prod1", 1));
        ProductPackage productPackage2 = packageRepository.create("Test Name 2", "Test Desc 2", Map.of("prod2", 1));

        ResponseEntity<ProductPackage[]> fetched = restTemplate.getForEntity("/packages", ProductPackage[].class);
        assertEquals(HttpStatus.OK, fetched.getStatusCode());
        assertNotNull(fetched.getBody());

        List<String> ids = Arrays.stream(fetched.getBody()).map(ProductPackage::getId).toList();
        assertTrue(ids.contains(productPackage1.getId()));
        assertTrue(ids.contains(productPackage2.getId()));
    }

    @Test
    void listPackagesByOwner() {
        String uniqueOwner = "owner-" + System.currentTimeMillis();
        packageRepository.create("Owned Pkg 1", "Desc", Map.of("prod1", 1), uniqueOwner);
        packageRepository.create("Owned Pkg 2", "Desc", Map.of("prod2", 1), uniqueOwner);
        packageRepository.create("Other Pkg", "Desc", Map.of("prod3", 1), "other-owner");

        ResponseEntity<ProductPackage[]> fetched = restTemplate.getForEntity(
                "/packages?ownerUsername={owner}", ProductPackage[].class, uniqueOwner);

        assertEquals(HttpStatus.OK, fetched.getStatusCode());
        assertNotNull(fetched.getBody());
        assertEquals(2, java.util.Objects.requireNonNull(fetched.getBody()).length);
        for (ProductPackage pkg : java.util.Objects.requireNonNull(fetched.getBody())) {
            assertEquals(uniqueOwner, pkg.getOwnerUsername());
        }
    }

    @Test
    void listAllPackages() {
        ResponseEntity<ProductPackage[]> fetched = restTemplate.getForEntity("/packages/all", ProductPackage[].class);
        assertEquals(HttpStatus.OK, fetched.getStatusCode());
        assertNotNull(fetched.getBody());
    }

    @Test
    void updatePackage() {
        ProductPackage original = packageRepository.create("Original Name", "Original Desc", Map.of("prod1", 1));

        ProductPackage update = new ProductPackage(original.getId(), "Updated Name", "Updated Desc",
                Map.of("prod2", 3));

        ResponseEntity<ProductPackage> updated = restTemplate.exchange(
                "/packages/{id}", HttpMethod.PUT, new HttpEntity<>(update), ProductPackage.class, original.getId());

        assertEquals(HttpStatus.OK, updated.getStatusCode());
        ProductPackage updatedBody = updated.getBody();
        assertNotNull(updatedBody);
        assertEquals("Updated Name", updatedBody.getName());
        assertEquals("Updated Desc", updatedBody.getDescription());
        assertEquals(Map.of("prod2", 3), updatedBody.getProductQuantities());

        // Verify persisted
        ProductPackage persisted = packageRepository.get(original.getId()).orElse(null);
        assertNotNull(persisted);
        assertEquals("Updated Name", persisted.getName());
    }

    @Test
    void deletePackage() {
        ProductPackage pkg = packageRepository.create("To Delete", "Desc", Map.of("prod1", 1));
        String id = pkg.getId();

        // Verify it exists
        assertTrue(packageRepository.get(id).isPresent());

        // Delete
        ResponseEntity<Void> deleted = restTemplate.exchange(
                "/packages/{id}", HttpMethod.DELETE, null, Void.class, id);
        assertEquals(HttpStatus.NO_CONTENT, deleted.getStatusCode());

        // Verify deleted
        assertTrue(packageRepository.get(id).isEmpty());
    }

    @Test
    void deletePackage_notFound() {
        ResponseEntity<Void> deleted = restTemplate.exchange(
                "/packages/{id}", HttpMethod.DELETE, null, Void.class, "nonexistent-id");
        assertEquals(HttpStatus.NOT_FOUND, deleted.getStatusCode());
    }

}
