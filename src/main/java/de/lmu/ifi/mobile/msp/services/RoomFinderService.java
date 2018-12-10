package de.lmu.ifi.mobile.msp.services;

import com.google.gson.Gson;
import de.lmu.ifi.mobile.msp.documents.Building;
import de.lmu.ifi.mobile.msp.documents.Floor;
import de.lmu.ifi.mobile.msp.dto.RoomFinderBuilding;
import de.lmu.ifi.mobile.msp.dto.RoomFinderBuildings;
import de.lmu.ifi.mobile.msp.dto.RoomFinderFloor;
import de.lmu.ifi.mobile.msp.repositories.BuildingRepository;
import de.lmu.ifi.mobile.msp.repositories.FloorRepository;
import de.lmu.ifi.mobile.msp.repositories.RoomRepository;
import de.lmu.ifi.mobile.msp.util.InputStreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class RoomFinderService {

    private Logger logger = LoggerFactory.getLogger(RoomFinderService.class);

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Value("classpath*:roomfinder-import/unique-building-parts/*.json")
    private Resource[] floorFiles;

    private Gson gson = new Gson();

    public RoomFinderService() {}

    public void importRoomFinderData() {
        clearOldData();
        logger.info("deleted old data");
        loadBuildingsData();
        logger.info("loaded building data");
        loadFloorData();
        logger.info("loaded floor data");

        logger.info("completed buildings import");
    }

    private void clearOldData() {
        roomRepository.deleteAll();
        floorRepository.deleteAll();
        buildingRepository.deleteAll();
    }

    private void loadBuildingsData() {
        Resource resource = new ClassPathResource("roomfinder-import/buildings.json");
        try {
            InputStream inputStream = resource.getInputStream();
            String buildingFile = InputStreamUtil.inputStreamToString(inputStream);
            RoomFinderBuildings buildings = gson.fromJson(buildingFile, RoomFinderBuildings.class);
            for (RoomFinderBuilding roomFinderBuilding : buildings.getBuildings()) {
                Building building = Building.fromRoomFinderBuilding(roomFinderBuilding);
                buildingRepository.save(building);
            }
        } catch(IOException e) {
            logger.info("Problem while reading building data");
        }
    }

    private void loadFloorData() {
        for (Resource floorFile : floorFiles) {
            try {
                String floorFileData = InputStreamUtil.inputStreamToString(floorFile.getInputStream());
                Map<String, Map<String, String>> floorData = gson.fromJson(floorFileData, Map.class);
                for (Map.Entry<String, Map<String, String>> entry : floorData.entrySet()) {
                    Floor floor = Floor.fromArbitraryMap(entry.getValue(), entry.getKey());
                    if (floor != null) {
                        floorRepository.save(floor);
                    } else {
                        logger.info("skipped floor because fields where not set");
                    }
                }
            } catch (IOException e) {
                logger.info("Problem while reading unique building part file");
            } catch (ClassCastException e) {
                logger.info("Building file is not of type Map<String, RoomFinderFloor>");
            }
        }
    }



}
