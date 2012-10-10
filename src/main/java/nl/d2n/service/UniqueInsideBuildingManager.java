package nl.d2n.service;

import nl.d2n.dao.UniqueInsideBuildingDao;
import nl.d2n.model.InsideBuilding;
import nl.d2n.model.InsideBuildingResourceCost;
import nl.d2n.model.UniqueInsideBuilding;
import nl.d2n.model.UniqueItem;
import nl.d2n.reader.wiki.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class UniqueInsideBuildingManager extends UniqueManagerWithImages<Integer, UniqueInsideBuilding, InsideBuilding> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueInsideBuildingManager.class);

    @Autowired
    private UniqueInsideBuildingDao uniqueInsideBuildingDao;

    @Autowired
    WikiConstructionDataParser wikiReader;

    @Autowired
    UniqueItemManager uniqueItemManager;

    @PostConstruct
    public void loadWikiInfoFromFile() {
        loadFromWiki(WikiConstructionLoadType.FROM_FILE);
    }
    @Scheduled(cron="0 0 14 * * ?")
    public void loadWikiInfoFromWiki() {
        loadFromWiki(WikiConstructionLoadType.FROM_WIKI);
    }

    public void loadFromWiki(WikiConstructionLoadType loadType) {
        LOGGER.info("Starting load from wiki process: "+loadType);
        for (UniqueInsideBuilding uniqueBuilding : getMap().values()) {
            String url = uniqueBuilding.getUrl();
            if (url == null) {
                continue;
            }
            // http://die2nitewiki.com/wiki/Wall_Upgrade_v1
            // http://die2nitewiki.com/wiki/Data:Wall_Upgrade_v1
            url = modifyUrl(url);
            final WikiConstructionData construction;
            try {
                construction = wikiReader.parse(loadType, uniqueBuilding.getId(), url);
            } catch (WikiConstructionParserException err) {
                LOGGER.error(err.getPage()+" => "+err.getError()+": "+err.getMessage());
                continue;
            }
            uniqueBuilding.setAp(construction.getAp());
            uniqueBuilding.setDefence(construction.getDefence());
            uniqueBuilding.clearResourceCosts();
            for (WikiResourceCost resourceCost : construction.getResourceCosts()) {
                InsideBuildingResourceCost cost = new InsideBuildingResourceCost();
                UniqueItem uniqueItem = uniqueItemManager.findItem(resourceCost.getResource());
                if (uniqueItem == null) {
                    LOGGER.error(url+" => unable to find item "+resourceCost.getResource());
                    continue;
                }
                cost.setItemId(uniqueItem.getId());
                cost.setAmount(resourceCost.getAmount());
                uniqueBuilding.addResourceCost(cost);
            }
            System.out.println(construction);
        }
        LOGGER.info("Ending load from wiki process");
    }

    protected String modifyUrl(String url) {
        int lastSlash = url.lastIndexOf("/");
        return url.substring(0, lastSlash+1) + "Data:" + url.substring(lastSlash+1);
    }

    @Override
    protected List<UniqueInsideBuilding> findUniqueObjects() {
        return this.uniqueInsideBuildingDao.findUniqueInsideBuildings();
    }

    @Override
    protected Integer getKeyFromUniqueObject(UniqueInsideBuilding value) {
        return value.getId();
    }

    @Override
    protected Integer getKeyFromNonUniqueObject(InsideBuilding value) {
        return value.getBuildingId();
    }

    @Override
    protected UniqueInsideBuilding createFromNonUniqueObject(InsideBuilding nonUniqueObject, UniqueInsideBuilding foundUniqueObject) {
        return UniqueInsideBuilding.createFromInsideBuilding(nonUniqueObject, foundUniqueObject);
    }

    @Override
    protected void saveUniqueObject(UniqueInsideBuilding uniqueObject) {
        this.uniqueInsideBuildingDao.save(uniqueObject);
    }

    @Override
    protected boolean mustBeOverwritten(UniqueInsideBuilding uniqueObject, InsideBuilding objectToPersist) {
        return uniqueObject.mustBeOverwrittenBy(objectToPersist);
    }

    @Override
    protected void appendMissingFields(InsideBuilding object, UniqueInsideBuilding uniqueObject) {
        // Left empty on purpose
    }

    public List<InsideBuilding> getAllAlwaysAvailableBuildings() {
        List<InsideBuilding> alwaysAvailableBuildings = new ArrayList<InsideBuilding>();
        for (UniqueInsideBuilding uniqueBuilding : getMap().values()) {
            if (uniqueBuilding.isAlwaysAvailable()) {
                alwaysAvailableBuildings.add(uniqueBuilding.createBuilding());
            }
        }
        return alwaysAvailableBuildings;
    }
}
