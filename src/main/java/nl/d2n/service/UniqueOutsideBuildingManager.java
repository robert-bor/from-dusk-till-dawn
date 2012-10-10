package nl.d2n.service;

import nl.d2n.dao.UniqueOutsideBuildingDao;
import nl.d2n.model.OutsideBuilding;
import nl.d2n.model.UniqueOutsideBuilding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UniqueOutsideBuildingManager extends UniqueManager<Integer, UniqueOutsideBuilding, OutsideBuilding> {

    @Autowired
    private UniqueOutsideBuildingDao uniqueOutsideBuildingDao;

    @Override
    protected List<UniqueOutsideBuilding> findUniqueObjects() {
        return this.uniqueOutsideBuildingDao.findUniqueOutsideBuildings();
    }

    @Override
    protected Integer getKeyFromUniqueObject(UniqueOutsideBuilding value) {
        return value.getId();
    }

    @Override
    protected Integer getKeyFromNonUniqueObject(OutsideBuilding value) {
        return value.getType();
    }

    @Override
    protected UniqueOutsideBuilding createFromNonUniqueObject(OutsideBuilding nonUniqueObject, UniqueOutsideBuilding foundUniqueObject) {
        return UniqueOutsideBuilding.createFromOutsideBuilding(nonUniqueObject, foundUniqueObject);
    }

    @Override
    protected void saveUniqueObject(UniqueOutsideBuilding uniqueObject) {
        this.uniqueOutsideBuildingDao.save(uniqueObject);
    }

    @Override
    protected boolean mustBeOverwritten(UniqueOutsideBuilding uniqueObject, OutsideBuilding objectToPersist) {
        return uniqueObject.mustBeOverwrittenBy(objectToPersist);
    }

    @Override
    protected void appendMissingFields(OutsideBuilding object, UniqueOutsideBuilding uniqueObject) {
        // Empty on purpose -- this situation never occurs
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<String>();
        for (UniqueOutsideBuilding building : getMap().values()) {
            names.add(building.getName());
        }
        Collections.sort(names);
        return names;
    }
}
