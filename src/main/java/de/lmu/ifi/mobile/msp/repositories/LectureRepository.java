package de.lmu.ifi.mobile.msp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import de.lmu.ifi.mobile.msp.documents.Lecture;

public interface LectureRepository extends MongoRepository<Lecture, String> {
    List<Lecture> findByNameLike(String name);

    @Query(value = "{ 'events.room' : {$regex: '?0' } }")
    List<Lecture> findByEvents_Room(String room);
}