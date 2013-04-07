package nl.d2n.service;

import nl.d2n.dao.UniqueTitleDao;
import nl.d2n.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UniqueTitleManager extends UniqueManager<String, UniqueTitle, Title> {

    @Autowired
    private UniqueTitleDao uniqueTitleDao;

    @Override
    protected List<UniqueTitle> findUniqueObjects() {
        return this.uniqueTitleDao.findUniqueTitles();
    }

    @Override
    protected String getKeyFromUniqueObject(UniqueTitle value) {
        return value.getName();
    }

    @Override
    protected String getKeyFromNonUniqueObject(Title value) {
        return value.getName();
    }

    @Override
    protected UniqueTitle createFromNonUniqueObject(Title nonUniqueObject, UniqueTitle foundUniqueObject) {
        return UniqueTitle.createFromTitle(nonUniqueObject);
    }

    @Override
    protected void saveUniqueObject(UniqueTitle uniqueObject) {
        this.uniqueTitleDao.save(uniqueObject);
    }

    @Override
    protected boolean mustBeOverwritten(UniqueTitle uniqueObject, Title objectToPersist) {
        return uniqueObject.mustBeOverwrittenBy(objectToPersist);
    }

    @Override
    protected void appendMissingFields(Title object, UniqueTitle uniqueObject) {
        // Empty on purpose -- this situation never occurs
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<String>();
        for (UniqueTitle building : getMap().values()) {
            names.add(building.getName());
        }
        Collections.sort(names);
        return names;
    }

}
