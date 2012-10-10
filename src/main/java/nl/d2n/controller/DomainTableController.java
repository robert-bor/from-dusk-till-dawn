package nl.d2n.controller;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.AuthenticateExternalApplication;
import nl.d2n.model.CampingTopology;
import nl.d2n.model.ExternalApplication;
import nl.d2n.service.UniqueDistinctionManager;
import nl.d2n.service.UniqueInsideBuildingManager;
import nl.d2n.service.UniqueItemManager;
import nl.d2n.service.UniqueOutsideBuildingManager;
import nl.d2n.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("domain")
public class DomainTableController extends AbstractController {

    @Autowired
    private UniqueInsideBuildingManager uniqueInsideBuildingManager;

    @Autowired
    private UniqueItemManager uniqueItemManager;

    @Autowired
    private UniqueOutsideBuildingManager uniqueOutsideBuildingManager;

    @Autowired
    private UniqueDistinctionManager uniqueDistinctionManager;

    @AuthenticateExternalApplication(checkSite = true, allow = {
            ExternalApplication.WIKI,
            ExternalApplication.WASTELAND_CARTOGRAPHER,
            ExternalApplication.MAP_VIEWER,
            ExternalApplication.OVAL_OFFICE })
    @RequestMapping(method = RequestMethod.GET, value="inside_buildings")
    public void getInsideBuildings(
            HttpServletResponse response,
            @RequestParam(value = "sk", required = true) String siteKey) throws ApplicationException, IOException {
        writeJsonStringToResponse(GsonUtil.objectToJson(uniqueInsideBuildingManager.getMap()), response);
    }

    @AuthenticateExternalApplication(checkSite = true, allow = {
            ExternalApplication.WIKI,
            ExternalApplication.WASTELAND_CARTOGRAPHER,
            ExternalApplication.MAP_VIEWER,
            ExternalApplication.OVAL_OFFICE })
    @RequestMapping(method = RequestMethod.GET, value="items")
    public void getItems(
            HttpServletResponse response,
            @RequestParam(value = "sk", required = true) String siteKey) throws ApplicationException, IOException {
        writeJsonStringToResponse(GsonUtil.objectToJson(uniqueItemManager.getMap()), response);
    }

    @RequestMapping(method = RequestMethod.GET, value="items_by_image")
    public void getItemIdsByImageName(
            HttpServletResponse response) throws ApplicationException, IOException {
        writeJsonStringToResponse(GsonUtil.objectToJson(uniqueItemManager.getItemIdsByImage()), response);
    }

    @RequestMapping(method = RequestMethod.GET, value="camping_topologies_by_text")
    public void getCampingTopologiesByStartText(
            HttpServletResponse response) throws ApplicationException, IOException {
        writeJsonStringToResponse(GsonUtil.objectToJson(CampingTopology.getTopologyByStartText()), response);
    }

    @AuthenticateExternalApplication(checkSite = true, allow = {
            ExternalApplication.WIKI,
            ExternalApplication.WASTELAND_CARTOGRAPHER,
            ExternalApplication.MAP_VIEWER,
            ExternalApplication.OVAL_OFFICE })
    @RequestMapping(method = RequestMethod.GET, value="outside_buildings")
    public void getOutsideBuildings(
            HttpServletResponse response,
            @RequestParam(value = "sk", required = true) String siteKey) throws ApplicationException, IOException {
        writeJsonStringToResponse(GsonUtil.objectToJson(uniqueOutsideBuildingManager.getMap()), response);
    }

    @AuthenticateExternalApplication(checkSite = true, allow = {
            ExternalApplication.WIKI,
            ExternalApplication.WASTELAND_CARTOGRAPHER,
            ExternalApplication.MAP_VIEWER,
            ExternalApplication.OVAL_OFFICE })
    @RequestMapping(method = RequestMethod.GET, value="distinctions")
    public void getDistinctions(
            HttpServletResponse response,
            @RequestParam(value = "sk", required = true) String siteKey) throws ApplicationException, IOException {
        writeJsonStringToResponse(GsonUtil.objectToJson(uniqueDistinctionManager.getMap()), response);
    }

}
