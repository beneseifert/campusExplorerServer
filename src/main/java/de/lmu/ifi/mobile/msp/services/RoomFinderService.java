package de.lmu.ifi.mobile.msp.services;

import de.lmu.ifi.mobile.msp.repositories.BuildingRepository;
import de.lmu.ifi.mobile.msp.repositories.FloorRepository;
import de.lmu.ifi.mobile.msp.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomFinderService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private RoomRepository roomRepository;

    public RoomFinderService() {}

    public void importRoomFinderData() {
        clearOldData();


    }

    private void clearOldData() {
        roomRepository.deleteAll();
        floorRepository.deleteAll();
        buildingRepository.deleteAll();
    }

}
