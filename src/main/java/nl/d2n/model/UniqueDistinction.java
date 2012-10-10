package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "unique_distinctions")
public class UniqueDistinction implements ImageBearer {

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Expose
    @Column(name = "name")
    private String name;

    @Expose
    @Column(name = "rare")
    private boolean rare;

    @Expose
    @Column(name = "image")
    private String image;

    @Expose
    @Column(name = "in_sprite")
    private boolean inSprite;

    public UniqueDistinction() {}
    public UniqueDistinction(String name, boolean rare, String image, boolean inSprite) {
        setName(name);
        setRare(rare);
        setImage(image);
        setInSprite(inSprite);
    }

    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
    public boolean isRare() { return this.rare; }
    public String getImage() { return this.image; }
    public boolean isInSprite() { return this.inSprite; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRare(boolean rare) { this.rare = rare; }
    public void setImage(String image) { this.image = image; }
    public void setInSprite(boolean inSprite) { this.inSprite = inSprite; }

    public boolean mustBeOverwrittenBy(Distinction distinction) {
        if (!getName().equals(distinction.getName())) {
            return true;
        }
        if (!getImage().equals(distinction.getImage())) {
            return true;
        }
        if (!isRare() && distinction.isRare()) {
            return true;
        }
        return false;
    }

    static public UniqueDistinction createFromDistinction(Distinction distinction, UniqueDistinction uniqueDistinction) {
        return new UniqueDistinction(
                distinction.getName(),
                distinction.isRare(),
                distinction.getImage(),
                uniqueDistinction != null && uniqueDistinction.isInSprite() && uniqueDistinction.getImage().equals(uniqueDistinction.getImage())
        );
    }
}
