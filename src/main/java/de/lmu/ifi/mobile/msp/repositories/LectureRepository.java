package de.lmu.ifi.mobile.msp.repositories;
import de.lmu.ifi.mobile.msp.documents.Lecture;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LectureRepository extends MongoRepository<Lecture, String> {}