package nl.d2n.service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class UniqueManager<K,UV, NUV> {

    protected Map<K, UV> uniqueObjects = new TreeMap<K, UV>();

    protected abstract List<UV> findUniqueObjects();

    @PostConstruct
    public void refresh() {
        fillMap(findUniqueObjects());
    }

    public void fillMap(List<UV> values) {
        this.uniqueObjects = new TreeMap<K, UV>();
        for (UV value : values) {
            this.uniqueObjects.put(getKeyFromUniqueObject(value), value);
        }
    }

    public List<NUV> appendMissingFields(List<NUV> objects) {
        for (NUV object : objects) {
            UV uniqueObject = uniqueObjects.get(getKeyFromNonUniqueObject(object));
            appendMissingFields(object, uniqueObject);
        }
        return objects;
    }

    protected abstract K getKeyFromUniqueObject(UV value);

    protected abstract K getKeyFromNonUniqueObject(NUV value);

    protected abstract UV createFromNonUniqueObject(NUV nonUniqueObject, UV foundUniqueObject);

    protected abstract void saveUniqueObject(UV uniqueObject);

    protected abstract boolean mustBeOverwritten(UV uniqueObject, NUV objectToPersist);

    protected abstract void appendMissingFields(NUV object, UV uniqueObject);

    public UV get(K id) {
        return uniqueObjects.get(id);
    }

    public Map<K, UV> getMap() {
        return this.uniqueObjects;
    }

    public void checkForExistence(final List<NUV> foundObjects) {
        if (foundObjects == null) {
            return;
        }
        for (NUV foundObject: foundObjects) {
            UV uniqueObject = uniqueObjects.get(getKeyFromNonUniqueObject(foundObject));
            if (    uniqueObject == null ||
                    mustBeOverwritten(uniqueObject, foundObject)) {
                add(setImageInSprite(createFromNonUniqueObject(foundObject, uniqueObject)));
            }
        }
    }

    protected UV setImageInSprite(UV uniqueObject) {
        // This call literally returns the same object; an extending class can use this method
        // to enhance the uniqueObject
        return uniqueObject;
    }

    public void add(final UV uniqueObject) {
        this.uniqueObjects.put(getKeyFromUniqueObject(uniqueObject), uniqueObject);
        saveUniqueObject(uniqueObject);
    }

}
