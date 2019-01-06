package de.lmu.ifi.mobile.msp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.lmu.ifi.mobile.msp.documents.Lecture;

public interface LectureRepository extends MongoRepository<Lecture, String> {
    List<Lecture> findByNameLike(String name);
}