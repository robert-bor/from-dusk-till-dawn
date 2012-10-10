package nl.d2n.dao.result;

import nl.d2n.model.Distinction;

import java.util.ArrayList;
import java.util.List;

public class UserWithProfile {

    private List<Distinction> distinctions = new ArrayList<Distinction>();

    private Integer soulPoints = 0;
    
    private String image;
    
    private String description;

    public void setSoulPoints(Integer soulPoints) { this.soulPoints = soulPoints; }
    public void setImage(String image) { this.image = image; }
    public void setDescription(String description) { this.description = description; }
    public void addDistinction(Distinction distinction) { this.distinctions.add(distinction); }

    public List<Distinction> getDistinctions() { return this.distinctions; }
    public Integer getSoulPoints() { return this.soulPoints; }
    public String getImage() { return this.image; }
    public String getDescription() { return this.description; }
}
