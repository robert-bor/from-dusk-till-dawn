package nl.d2n.service;

import nl.d2n.model.ImageBearer;

/**
* The purpose of this class is to register a reused icon to be in a sprite map if it is already there.
* @param <K> the key value used for the unique objects
* @param <UV> the unique object
* @param <NUV> the non-unique object
*/
public abstract class UniqueManagerWithImages<K,UV extends ImageBearer, NUV> extends UniqueManager<K,UV,NUV> {

    protected boolean imageExistsInSpriteMap(String image) {
        for (UV uniqueValue : getMap().values()) {
            if (uniqueValue.getImage().equals(image) && uniqueValue.isInSprite()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected UV setImageInSprite(UV uniqueObject) {
        if (imageExistsInSpriteMap(uniqueObject.getImage())) {
            uniqueObject.setInSprite(true);
        }
        return uniqueObject;
    }
}
