package nl.d2n.controller;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.UpdateAction;
import nl.d2n.model.UserKey;
import nl.d2n.service.actions.BuildingActionLock;
import nl.d2n.service.actions.BuildingActionUnlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("town")
public class TownController extends AbstractController {

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(method = RequestMethod.POST, value= "lock_building")
    public void lockBuilding(HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key,
                            @RequestParam(value = "city", required = false) Integer city,
                            @RequestParam(value = "building", required = false) Integer building,
                            @RequestParam(value = "day", required = false) Integer day
            ) throws IOException, ApplicationException {
        applicationContext.getBean(BuildingActionLock.class).execute(key, city, building, day);
    }

    @RequestMapping(method = RequestMethod.POST, value= "unlock_building")
    public void unlockBuilding(HttpServletResponse response,
                             @RequestParam(value = "key", required = true) UserKey key,
                             @RequestParam(value = "city", required = false) Integer city,
                             @RequestParam(value = "building", required = false) Integer building,
                             @RequestParam(value = "day", required = false) Integer day
            ) throws IOException, ApplicationException {
        applicationContext.getBean(BuildingActionUnlock.class).execute(key, city, building, day);
    }

}
