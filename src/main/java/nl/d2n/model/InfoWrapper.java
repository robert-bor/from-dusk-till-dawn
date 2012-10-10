package nl.d2n.model;

public class InfoWrapper {

    private Info info;
    private User user;
    private InfoStatus status = InfoStatus.READ_FROM_SITE;
    private String xml;

    public InfoWrapper() {}
    public InfoWrapper(Info info) {
        setInfo(info);
    }

    public Info getInfo() { return this.info; }
    public User getUser() { return this.user; }
    public InfoStatus getStatus() { return this.status; }
    public boolean isStale() { return this.status.isStale(); }
    public boolean requiresEnhancing() { return this.status.requiresEnhancing(); }
    public String getXml() { return this.xml; }

    public void setInfo(Info info) { this.info = info; }
    public void setUser(User user) { this.user = user; }
    public void setStatus(InfoStatus status) { this.status = status; }
    public void setXml(String xml) { this.xml = xml; }
}
