package de.lmu.ifi.mobile.msp.repositories;

import de.lmu.ifi.mobile.msp.documents.Floor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FloorRepository extends MongoRepository<Floor, String> {}
