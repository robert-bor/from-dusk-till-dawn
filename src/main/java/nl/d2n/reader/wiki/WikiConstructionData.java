package nl.d2n.reader.wiki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WikiConstructionData {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikiConstructionData.class);

    private Integer id;
    private String name;
    private Integer ap;
    private Integer defence;
    private List<WikiResourceCost> resourceCosts = new ArrayList<WikiResourceCost>();

    public WikiConstructionData(String page, List<String> lines) {
        WikiConstructionDataLine dataLines = new WikiConstructionDataLine(page, lines);
        setId(getInteger(dataLines, "ID"));
        setName(getText(dataLines, "Name"));
        setAp(getInteger(dataLines, "AP cost"));
        setDefence(getInteger(dataLines, "Defence"));
        setResources(dataLines);
    }

    protected void setResources(WikiConstructionDataLine dataLines) {
        Integer numberOfResources = getInteger(dataLines, "Number of resources");
        for (int pos = 0; pos < numberOfResources; pos++) {
            try {
                resourceCosts.add(
                        new WikiResourceCost(
                                dataLines.getResourceValue("(res"+(pos+1)+")"),
                                dataLines.getAmountValue("(res"+(pos+1)+")")

                ));
            } catch (WikiConstructionDataLineException err) {
                logException(err);
            }
        }
    }

    public Integer getInteger(WikiConstructionDataLine dataLines, String field) {
        try {
            return dataLines.getInteger(field);
        } catch (WikiConstructionDataLineException err) {
            logException(err);
            return null;
        }
    }

    public String getText(WikiConstructionDataLine dataLines, String field) {
        try {
            return dataLines.getText(field);
        } catch (WikiConstructionDataLineException err) {
            logException(err);
            return null;
        }
    }

    protected void logException(WikiConstructionDataLineException err) {
        LOGGER.error(
                "Page: "+err.getPage()+", "+
                "Error: "+err.getError()+", "+
                (err.getField()==null?"":"Field: "+err.getField()+", )"+
                (err.getLine()==null?"":"["+err.getLine()+"]")));
    }

    public List<WikiResourceCost> getResourceCosts() { return this.resourceCosts; }
    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
    public Integer getAp() { return this.ap; }
    public Integer getDefence() { return this.defence; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAp(Integer ap) { this.ap = ap; }
    public void setDefence(Integer defence) { this.defence = defence; }

    @SuppressWarnings({"StringConcatenationInsideStringBufferAppend"})
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(
            "ID: "+id+", "+
            "Name: "+name+", "+
            "Defence: "+defence+", "+
            "AP: "+ap+", "+
            "Resources: [");
        for (WikiResourceCost resourceCost : getResourceCosts()) {
            buffer.append(resourceCost.toString()+" ");
        }
        buffer.append("]");
        return buffer.toString();
    }
}
