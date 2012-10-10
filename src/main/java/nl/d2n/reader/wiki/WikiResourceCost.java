package nl.d2n.reader.wiki;

public class WikiResourceCost {
    public Integer amount;
    public String resource;

    // Twisted Plank, Quantity (resQ1) - 6
    public WikiResourceCost(String resource, Integer amount) {
        setResource(resource.replaceAll("_", " "));
        setAmount(amount);
    }

    public String getResource() { return this.resource; }
    public Integer getAmount() { return this.amount; }
    
    public void setAmount(Integer amount) { this.amount = amount; }
    public void setResource(String resource) { this.resource = resource; }

    public String toString() {
        return resource + " X " + amount;
    }
}
