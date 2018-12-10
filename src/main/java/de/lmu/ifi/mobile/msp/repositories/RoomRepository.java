package de.lmu.ifi.mobile.msp.repositories;

import de.lmu.ifi.mobile.msp.documents.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {}
