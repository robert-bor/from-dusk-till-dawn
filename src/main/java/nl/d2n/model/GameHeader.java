package nl.d2n.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "headers")
@XmlAccessorType(XmlAccessType.FIELD)
public class GameHeader {

    @XmlAttribute
    private String link;

    @XmlAttribute(name = "iconurl")
    private String iconUrl;

    @XmlAttribute
    private boolean secure;

    @XmlAttribute
    private String author;

    @XmlAttribute
    private String language;

    @XmlAttribute
    private String version;

    @XmlAttribute(name = "avatarurl")
    private String avatarUrl;

    @XmlAttribute
    private String generator;

    @XmlElement
    private Game game;

    @XmlElement
    private Owner owner;

    public String getLink() { return this.link; }
    public String getIconUrl() { return this.iconUrl; }
    public boolean isSecure() { return this.secure; }
    public String getAuthor() { return this.author; }
    public String getLanguage() { return this.language; }
    public String getVersion() { return this.version; }
    public String getAvatarUrl() { return this.avatarUrl; }
    public String getGenerator() { return this.generator; }
    public Game getGame() { return this.game; }
    public Owner getOwner() { return this.owner; }

    public void setOwner(Owner owner) { this.owner = owner; }
    public void setSecure(boolean secure) { this.secure = secure; }
}
