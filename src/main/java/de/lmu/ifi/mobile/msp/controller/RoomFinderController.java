package de.lmu.ifi.mobile.msp.controller;

import de.lmu.ifi.mobile.msp.services.RoomFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController()
public class RoomFinderController {

    @Autowired
    private RoomFinderService roomFinderService;

    @PostConstruct
    public void importRoomFinderData() {
        roomFinderService.importRoomFinderData();
    }

}
