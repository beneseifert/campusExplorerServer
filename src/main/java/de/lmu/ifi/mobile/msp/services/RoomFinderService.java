package de.lmu.ifi.mobile.msp.services;

import com.google.gson.Gson;
import de.lmu.ifi.mobile.msp.documents.Building;
import de.lmu.ifi.mobile.msp.dto.RoomFinderBuilding;
import de.lmu.ifi.mobile.msp.dto.RoomFinderBuildings;
import de.lmu.ifi.mobile.msp.repositories.BuildingRepository;
import de.lmu.ifi.mobile.msp.repositories.FloorRepository;
import de.lmu.ifi.mobile.msp.repositories.RoomRepository;
import de.lmu.ifi.mobile.msp.util.InputStreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class RoomFinderService {

    private Logger logger = LoggerFactory.getLogger(RoomFinderService.class);

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private RoomRepository roomRepository;

    private Gson gson = new Gson();

    public RoomFinderService() {}

    public void importRoomFinderData() {
        logger.info("deleting old data");
        clearOldData();
        loadBuildingsData();

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



}
