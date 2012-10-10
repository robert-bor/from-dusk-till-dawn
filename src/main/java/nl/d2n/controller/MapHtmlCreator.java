package nl.d2n.controller;

import com.sun.istack.internal.Nullable;
import nl.d2n.model.Info;
import nl.d2n.model.Zone;
import nl.d2n.model.ZoneDanger;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapHtmlCreator {

    public String getMap(final Info info, Zone[][] gameMatrix) {
        StringBuilder map = new StringBuilder();
        map.append("\n");
        map.append(getVerticalDemarcations(info.getMap().getWidth(), info.getCity().getX()));
        for (int yPos = 0; yPos < info.getMap().getHeight(); yPos++) {
            map.append(getMapRow(info, gameMatrix, yPos));
        }
        map.append(getVerticalDemarcations(info.getMap().getWidth(), info.getCity().getX()));
        return map.toString();
    }

    public String getCanvasWidth(final int width) {
        int canvasWidth = (width+2)*20;
//        return "#options_helpers { display: block; }#map { width: "+canvasWidth+"px; }#wrapper { width: "+(canvasWidth+30)+"px;}";
        return "#map { width: "+(canvasWidth+80)+"px; } .app { width: "+(canvasWidth+360)+"px;}";
    }

    protected String getMapRow(final Info info, Zone[][] gameMatrix, final int yPos) {
        StringBuilder mapRow = new StringBuilder();
        mapRow.append("   <ul>\n");
        int realYPos = MapUtil.getRealYPos(yPos, info.getCity().getY());
        mapRow.append(placeListItem(null, new String[] { "headery", "headery_"+realYPos }, Integer.toString(realYPos)));
        for (int xPos = 0; xPos < info.getMap().getWidth(); xPos++) {
            int realXPos = MapUtil.getRealXPos(xPos, info.getCity().getX());
            Zone zone = gameMatrix[xPos][yPos];
            mapRow.append(placeListItem("zone_"+realXPos+"_"+realYPos, zone.getHtmlClasses(), null));
        }
        mapRow.append(placeListItem(null, new String[] { "headery", "headery_"+realYPos }, Integer.toString(realYPos)));
        mapRow.append("   </ul>\n");
        return mapRow.toString();
    }
    protected String getVerticalDemarcations(final int width, final int xPosTown) {
        StringBuilder demarcation = new StringBuilder();
        demarcation.append("   <ul>\n");
        demarcation.append(placeListItem(null, new String[] { "corner" }, null));
        for (int xPos = 0; xPos < width; xPos++) {
            int realXPos = MapUtil.getRealXPos(xPos, xPosTown);
            demarcation.append(placeListItem(null, new String[] { "headerx", "headerx_"+realXPos }, Integer.toString(realXPos)));
        }
        demarcation.append(placeListItem(null, new String[] { "corner" }, null));
        demarcation.append("   </ul>\n");
        return demarcation.toString();
    }
    protected String placeListItem(@Nullable final String id, final String[] classItems, @Nullable final String value) {
        StringBuilder listItem = new StringBuilder();
        listItem.append("      ");
        listItem.append("<li ");
        if (id != null) {
            listItem.append("id=\"").append(id).append("\" ");
        }
        listItem.append("class=\"");
        listItem.append(StringUtils.join(classItems, " "));
        listItem.append("\">");
        if (value != null) {
            listItem.append(value);
        }
        listItem.append("</li>\n");
        return listItem.toString();
    }

}
