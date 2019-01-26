package de.lmu.ifi.mobile.msp.services;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import de.lmu.ifi.mobile.msp.documents.Lecture;
import de.lmu.ifi.mobile.msp.repositories.LectureRepository;
import de.lmu.ifi.mobile.msp.util.InputStreamUtil;

@Service
public class LectureService {

    private Logger logger = LoggerFactory.getLogger(LectureService.class);

    @Autowired
    private LectureRepository lectureRepository;

    private Gson gson = new Gson();

    public LectureService() {}

    public void importLSFData() {
        clearOldData();
        logger.info("deleted old data");
        loadLSFData();
        logger.info("loaded lsf data");
        logger.info("completed import");
    }

    private void clearOldData() {
        lectureRepository.deleteAll();
    }

    private void loadLSFData() {
        Resource resource = new ClassPathResource("lsf-import/lectures.json");
        try {
            InputStream inputStream = resource.getInputStream();
            String lectureFile = InputStreamUtil.inputStreamToString(inputStream);
            Type listType = new TypeToken<Collection<Lecture>>(){}.getType();
            List<Lecture> lectures = gson.fromJson(lectureFile, listType);
            for (Lecture lecture : lectures) {
                lectureRepository.save(lecture);
            }
        } catch(IOException e) {
            logger.info("problem while reading lsf data");
        }
    }

}
