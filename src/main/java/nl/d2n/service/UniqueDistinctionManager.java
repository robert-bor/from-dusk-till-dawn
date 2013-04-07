package nl.d2n.service;

import nl.d2n.dao.UniqueDistinctionDao;
import nl.d2n.model.Distinction;
import nl.d2n.model.Title;
import nl.d2n.model.UniqueDistinction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UniqueDistinctionManager extends UniqueManagerWithImages<String, UniqueDistinction, Distinction> {

    @Autowired
    private UniqueDistinctionDao uniqueDistinctionDao;

    public List<UniqueDistinction> findUniqueObjectsOrdered() {
        List<UniqueDistinction> orderedDistinctions = new ArrayList<UniqueDistinction>(uniqueObjects.values());
        Collections.sort(orderedDistinctions, new DistinctionComparator());
        return orderedDistinctions;
    }

    public class DistinctionComparator implements Comparator<UniqueDistinction> {
        public int compare(UniqueDistinction thisDistinction, UniqueDistinction otherDistinction) {
            if (thisDistinction.isRare() != otherDistinction.isRare()) {
                return -((Boolean)thisDistinction.isRare()).compareTo(otherDistinction.isRare());
            }
            return thisDistinction.getName().compareTo(otherDistinction.getName());
        }
    }

    @Override
    protected List<UniqueDistinction> findUniqueObjects() {
        return this.uniqueDistinctionDao.findUniqueDistinctions();
    }

    @Override
    protected String getKeyFromUniqueObject(UniqueDistinction value) {
        return value.getName();
    }

    @Override
    protected String getKeyFromNonUniqueObject(Distinction value) {
        return value.getName();
    }

    @Override
    protected UniqueDistinction createFromNonUniqueObject(Distinction nonUniqueObject, UniqueDistinction foundUniqueObject) {
        return UniqueDistinction.createFromDistinction(nonUniqueObject, foundUniqueObject);
    }

    @Override
    protected void saveUniqueObject(UniqueDistinction uniqueObject) {
        this.uniqueDistinctionDao.save(uniqueObject);
        uniqueObject.setId(this.uniqueDistinctionDao.find(uniqueObject.getName()).getId());
    }

    @Override
    protected boolean mustBeOverwritten(UniqueDistinction uniqueObject, Distinction objectToPersist) {
        return uniqueObject.mustBeOverwrittenBy(objectToPersist);
    }

    @Override
    protected void appendMissingFields(Distinction object, UniqueDistinction uniqueObject) {
        object.setName(uniqueObject.getName());
        object.setImage(uniqueObject.getImage());
        object.setInSprite(uniqueObject.isInSprite());
        object.setRare(uniqueObject.isRare());
    }

    protected Map<Integer, UniqueDistinction> getMapWithIntegerKeys() {
        Map<Integer, UniqueDistinction> uniqueDistinctions = new TreeMap<Integer, UniqueDistinction>();
        for (UniqueDistinction uniqueDistinction : uniqueObjects.values()) {
            uniqueDistinctions.put(uniqueDistinction.getId(), uniqueDistinction);
        }
        return uniqueDistinctions;
    }

    @Override
    public List<Distinction> appendMissingFields(List<Distinction> distinctions) {
        Map<Integer, UniqueDistinction> uniqueDistinctions = getMapWithIntegerKeys();
        for (Distinction distinction : distinctions) {
            UniqueDistinction uniqueDistinction = uniqueDistinctions.get(distinction.getUniqueDistinctionId());
            appendMissingFields(distinction, uniqueDistinction);
        }
        return distinctions;
    }

    public List<Title> deriveTitlesFromProfile(List<Distinction> distinctions) {
        List<Title> titles = new ArrayList<Title>();
        for (Distinction distinction : distinctions) {
            Title title = distinction.getTitle();
            if (title == null) {
                continue;
            }
            title.setUniqueDistinctionId(get(distinction.getName()).getId());
            titles.add(title);
        }
        return titles;
    }

}
