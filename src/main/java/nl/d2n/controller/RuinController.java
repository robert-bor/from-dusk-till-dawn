package nl.d2n.controller;

import nl.d2n.model.*;
import nl.d2n.service.RuinService;
import nl.d2n.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("ruin")
public class RuinController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuinController.class);

    @Autowired
    private RuinService ruinService;

    @RequestMapping(method = RequestMethod.GET)
    public void getRuin(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key,
                            @RequestParam(value = "city", required = true) Integer city,
                            @RequestParam(value = "x", required = true) Integer x,
                            @RequestParam(value = "y", required = true) Integer y
            ) throws IOException, ApplicationException {
        Ruin ruin = ruinService.getRuin(key, city, x, y);
        writeJsonStringToResponse(GsonUtil.objectToJson(ruin), response);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void saveRoom(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "key", required = true) UserKey key,
                         @RequestParam(value = "city", required = true) Integer city,
                         @RequestParam(value = "x_zone", required = true) Integer xZone,
                         @RequestParam(value = "y_zone", required = true) Integer yZone,
                         @RequestParam(value = "x_room", required = true) Integer xRoom,
                         @RequestParam(value = "y_room", required = true) Integer yRoom,
                         @RequestParam(value = "action", required = true) RuinAction action,
                         @RequestParam(value = "direction", required = false) RuinDirection direction,
                         @RequestParam(value = "door", required = false) Door door,
                         @RequestParam(value = "door_key", required = false) Key doorKey,
                         @RequestParam(value = "zombies", required = false) Integer zombies
            ) throws IOException, ApplicationException {
        switch (action) {
            case ADD_ROOM :
                ruinService.addRoom(key, city, xZone, yZone, xRoom, yRoom);
                break;
            case DELETE_ROOM :
                ruinService.deleteRoom(key, city, xZone, yZone, xRoom, yRoom);
                break;
            case ADD_CORRIDOR :
                assert(direction != null);
                ruinService.addCorridor(key, city, xZone, yZone, xRoom, yRoom, direction);
                break;
            case DELETE_CORRIDOR :
                assert(direction != null);
                ruinService.deleteCorridor(key, city, xZone, yZone, xRoom, yRoom, direction);
                break;
            case SET_DOOR :
                assert(door != null);
                ruinService.setDoor(key, city, xZone, yZone, xRoom, yRoom, door);
                break;
            case SET_KEY :
                assert(door != null);
                ruinService.setKey(key, city, xZone, yZone, xRoom, yRoom, doorKey);
                break;
            case SET_ZOMBIES :
                assert(zombies != null);
                ruinService.setZombies(key, city, xZone, yZone, xRoom, yRoom, zombies);
                break;
        }
    }

}
