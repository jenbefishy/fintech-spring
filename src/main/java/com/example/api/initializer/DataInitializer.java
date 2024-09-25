package com.example.api.initializer;

import com.example.api.annotation.LogExecutionTime;
import com.example.api.model.CategoryModel;
import com.example.api.model.LocationModel;
import com.example.api.repository.CategoryRepository;
import com.example.api.repository.LocationRepository;
import com.example.api.service.KudagoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@LogExecutionTime
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final KudagoService kudagoService;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public DataInitializer(KudagoService kudagoService,
                           LocationRepository locationRepository, CategoryRepository categoryRepository) {
        this.kudagoService = kudagoService;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting data initialization...");

        try {
            List<LocationModel> locations = kudagoService.fetchLocations();
            logger.info("Fetched {} locations from Kudago API.", locations.size());

            for (LocationModel location : locations) {
                locationRepository.save(location);
            }

            List<CategoryModel> categories = kudagoService.fetchCategories();
            logger.info("Fetched {} categories from Kudago API.", categories.size());

            for (CategoryModel category : categories) {
                categoryRepository.save(category);
            }

            logger.info("Data initialization completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during data initialization: ", e);
        }
    }
}
