package nl.d2n.controller;

import nl.d2n.model.*;
import nl.d2n.service.MapService;
import nl.d2n.service.actions.*;
import nl.d2n.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("zone")
public class ZoneController extends AbstractController {

    @Autowired
    private MapService mapService;

    @Autowired
    ApplicationContext applicationContext;

    @AuthenticateExternalApplication(checkSite = true, allow = {ExternalApplication.NITELIGHT})
    @RequestMapping(method = RequestMethod.GET)
    public void getZoneInfo(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "sk", required = true) String siteKey,
                            @RequestParam(value = "city", required = true) Integer city,
                            @RequestParam(value = "x", required = false) Integer x,
                            @RequestParam(value = "y", required = false) Integer y
            ) throws IOException, ApplicationException {
        if (x != null && y != null) {
            writeJsonStringToResponse(GsonUtil.objectToJson(mapService.getZone(city, x, y)), response);
        } else {
            writeJsonStringToResponse(GsonUtil.objectToJson(mapService.getZones(city)), response);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value= "deplete_zone")
    public void depleteZone(HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key,
                            @RequestParam(value = "x", required = true) Integer x,
                            @RequestParam(value = "y", required = true) Integer y,
                            @RequestParam(value = "day", required = true) Integer day
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionDepleteZone.class).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="replenish_zone")
    public void replenishZone(HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key,
                            @RequestParam(value = "x", required = true) Integer x,
                            @RequestParam(value = "y", required = true) Integer y,
                            @RequestParam(value = "day", required = true) Integer day
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionReplenishZone.class).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="visit_zone")
    public void visitZone(HttpServletResponse response,
                              @RequestParam(value = "key", required = true) UserKey key,
                              @RequestParam(value = "x", required = true) Integer x,
                              @RequestParam(value = "y", required = true) Integer y,
                              @RequestParam(value = "day", required = true) Integer day
    ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionVisit.class).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="unvisit_zone")
    public void unvisitZone(HttpServletResponse response,
                              @RequestParam(value = "key", required = true) UserKey key,
                              @RequestParam(value = "x", required = true) Integer x,
                              @RequestParam(value = "y", required = true) Integer y,
                              @RequestParam(value = "day", required = true) Integer day
    ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionUnvisit.class).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="deplete_building")
    public void depleteBuilding(HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key,
                            @RequestParam(value = "x", required = true) Integer x,
                            @RequestParam(value = "y", required = true) Integer y,
                            @RequestParam(value = "day", required = true) Integer day
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionDepleteBuilding.class).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="replenish_building")
    public void replenishBuilding(HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key,
                            @RequestParam(value = "x", required = true) Integer x,
                            @RequestParam(value = "y", required = true) Integer y,
                            @RequestParam(value = "day", required = true) Integer day
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionReplenishBuilding.class).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="remove_blueprint")
    public void removeBlueprint(HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key,
                            @RequestParam(value = "x", required = true) Integer x,
                            @RequestParam(value = "y", required = true) Integer y,
                            @RequestParam(value = "day", required = true) Integer day
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionRemoveBlueprint.class).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="add_blueprint")
    public void addBlueprint(HttpServletResponse response,
                             @RequestParam(value = "key", required = true) UserKey key,
                             @RequestParam(value = "x", required = true) Integer x,
                             @RequestParam(value = "y", required = true) Integer y,
                             @RequestParam(value = "day", required = true) Integer day
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionAddBlueprint.class).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="save_camping")
    public void saveCampingTopology(HttpServletResponse response,
                             @RequestParam(value = "key", required = true) UserKey key,
                             @RequestParam(value = "x", required = true) Integer x,
                             @RequestParam(value = "y", required = true) Integer y,
                             @RequestParam(value = "day", required = true) Integer day,
                             @RequestParam(value = "camping_topology", required = true) CampingTopology campingTopology
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionCampingTopology.class)
                        .setCampingTopology(campingTopology).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="save_zombies")
    public void saveZombies(HttpServletResponse response,
                             @RequestParam(value = "key", required = true) UserKey key,
                             @RequestParam(value = "x", required = true) Integer x,
                             @RequestParam(value = "y", required = true) Integer y,
                             @RequestParam(value = "day", required = true) Integer day,
                             @RequestParam(value = "zombies", required = false) Integer zombies
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionZombies.class)
                        .setZombies(zombies).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="save_scout_sense")
    public void saveScoutSense(HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key,
                            @RequestParam(value = "x", required = true) Integer x,
                            @RequestParam(value = "y", required = true) Integer y,
                            @RequestParam(value = "day", required = true) Integer day,
                            @RequestParam(value = "scout_sense", required = false) Integer scoutSense
    ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionScoutSense.class)
                        .setScoutSense(scoutSense).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="save_peek")
    public void savePeekText(HttpServletResponse response,
                             @RequestParam(value = "key", required = true) UserKey key,
                             @RequestParam(value = "x", required = true) Integer x,
                             @RequestParam(value = "y", required = true) Integer y,
                             @RequestParam(value = "day", required = true) Integer day,
                             @RequestParam(value = "peek_text", required = true) String peekText
    ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionPeekText.class)
                        .setPeekText(peekText).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value="save_items")
    public void saveItems(HttpServletResponse response,
                             @RequestParam(value = "key", required = true) UserKey key,
                             @RequestParam(value = "x", required = true) Integer x,
                             @RequestParam(value = "y", required = true) Integer y,
                             @RequestParam(value = "day", required = true) Integer day,
                             @RequestParam(value = "items", required = false) String[] items
    ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionItems.class)
                        .setItems(Item.convertKeysToItems(items)).execute(key, x, y, day)), response);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void updateMyZone(HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionAutomaticUpdate.class).execute(key)), response);
    }

    @RequestMapping(method = RequestMethod.POST, value= "extended")
    public void updateZone(HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key,
                            @RequestParam(value = "zone_depleted", required = false) Boolean zoneDepleted,
                            @RequestParam(value = "zombies", required = false) Integer zombies,
                            @RequestParam(value = "items", required = false) String[] items,
                            @RequestParam(value = "building_depleted", required = false) Boolean buildingDepleted,
                            @RequestParam(value = "blueprint_available", required = false) Boolean blueprintAvailable,
                            @RequestParam(value = "camping_topology", required = false) CampingTopology campingTopology,
                            @RequestParam(value = "peek_text", required = false) String peekText
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {

        writeJsonStringToResponse(GsonUtil.objectToJson(
                applicationContext.getBean(ZoneActionExtendedUpdate.class)
                        .setBlueprintAvailable(blueprintAvailable)
                        .setCampingTopology(campingTopology)
                        .setItems(Item.convertKeysToItems(items))
                        .setZombies(zombies)
                        .setZoneDepleted(zoneDepleted)
                        .execute(key)), response);
    }

    public void setMapService(MapService mapService) { this.mapService = mapService; }
}
