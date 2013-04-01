package nl.d2n.controller;

import nl.d2n.model.*;
import nl.d2n.service.*;
import nl.d2n.service.actions.MapActionRead;
import nl.d2n.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("map")
public class MapController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapController.class);

    @Autowired
    private MapService mapService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MapHtmlCreator mapHtmlCreator;

    @Autowired
    private UniqueItemManager uniqueItemManager;

    @Autowired
    private UniqueOutsideBuildingManager uniqueOutsideBuildingManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private GameClock gameClock;
    
    @RequestMapping(method = RequestMethod.POST)
    public void signIn(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "key", required = true) String key) throws IOException {
        // This redirects to the regular MAP page with a Bookmarkable URL
        String destination  ="map?key="+key;
        response.sendRedirect(response.encodeRedirectURL(destination));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getMap(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "key", required = false) UserKey key) {
        ModelAndView mav;
        if (key == null) {
            mav = new ModelAndView("signin");
        } else {
            try {
                InfoWrapper infoWrapper = applicationContext.getBean(MapActionRead.class).execute(key);
                Info info = infoWrapper.getInfo();
                mav = new ModelAndView("index");
                mav.addObject("iconUrl", info.getGameHeader().getIconUrl());
                mav.addObject("canvasWidth", mapHtmlCreator.getCanvasWidth(info.getMap().getWidth()));
                mav.addObject("map", mapHtmlCreator.getMap(info, info.getMatrix().getMatrix()));
                mav.addObject("mapZones", info.getMatrix().toJson());
                mav.addObject("bank", new Bank(uniqueItemManager.appendMissingFields(info.getItems())).toJson());
                mav.addObject("categories", GsonUtil.objectToJson(ItemCategory.values()));
                mav.addObject("estimations", GsonUtil.objectToJson(info.getEstimations()));
                mav.addObject("upgrades", GsonUtil.objectToJson(info.getUpgrades()));
                mav.addObject("uniqueItems", new Bank(uniqueItemManager.appendMissingFields(info.getUniqueItems())).toJson());
                mav.addObject("uniqueOutsideBuildings", GsonUtil.objectToJson(uniqueOutsideBuildingManager.getNames()));
                mav.addObject("insideBuildings", GsonUtil.objectToJson(mapService.getBuildingsWithStatus(info.getCity().getBuildings(), info.getGameHeader().getGame().getId())));
                mav.addObject("itemLookup", info.getItemLookupAsJson());
                mav.addObject("distinctions", GsonUtil.objectToJson(profileService.getDistinctions(info.getGameHeader().getGame().getId(), info.getCitizens())));
                mav.addObject("citizens", info.getCitizensAsJson());
                mav.addObject("dangerLevels", GsonUtil.objectToJson(ZoneDanger.getDangersAsMap()));
                mav.addObject("campingTopologies", GsonUtil.objectToJson(CampingTopology.getCampingTopologyMap()));
                mav.addObject("xPosTown", info.getCity().getX());
                mav.addObject("yPosTown", info.getCity().getY());
                mav.addObject("width", info.getMap().getWidth());
                mav.addObject("height", info.getMap().getHeight());
                mav.addObject("city", GsonUtil.objectToJson(info.getCity()));
                mav.addObject("cityId", info.getGameHeader().getGame().getId());
                mav.addObject("cityName", info.getCity().getName());
                mav.addObject("insecure", !info.getGameHeader().isSecure());
                mav.addObject("key", key.getKey());
                mav.addObject("today", info.getGameHeader().getGame().getDay());
                mav.addObject("activePlayer", infoWrapper.getUser().getName());
                mav.addObject("activePlayerId", infoWrapper.getUser().getGameId());
                mav.addObject("upgradedMap", info.isUpgradedMapAvailable());
                mav.addObject("chaos", info.getCity().isChaosMode());
                mav.addObject("stale", infoWrapper.isStale());
                mav.addObject("gameClock", gameClock.isInitialized() ? gameClock.getJavaScriptDate() : "");
            } catch (ApplicationException err) {
                ControllerUtils.logParameters(err, LOGGER, request);
                mav = new ModelAndView("error");
                mav.addObject("errorCode", err.getError().toString());
                mav.addObject("errorMessage", err.getError().getMessage());
            } catch (Exception err) {
                ControllerUtils.logParameters(err.getMessage(), LOGGER, request);
                LOGGER.error("", err);
                mav = new ModelAndView("error");
                mav.addObject("errorCode", D2NErrorCode.SYSTEM_ERROR);
                mav.addObject("errorMessage", D2NErrorCode.SYSTEM_ERROR.getMessage());
            }
        }
        return mav;
    }
}
