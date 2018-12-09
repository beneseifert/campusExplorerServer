package de.lmu.ifi.mobile.msp.repositories;

import de.lmu.ifi.mobile.msp.documents.Building;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BuildingRepository extends MongoRepository<Building, String> {}
