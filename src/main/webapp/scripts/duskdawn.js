/*
* From Dusk Till Dawn
*/

function SelectedZone(id) {
    this.id=id;
    var matches = this.id.match(/^zone_([\-0-9]+)_([\-0-9]+)$/);
    this.x = parseInt(matches[1]);
    this.y = parseInt(matches[2]);

    this.systemX = function() {
        return this.x + xPosTown;
    };
    this.systemY = function() {
        return yPosTown + this.y;
    };
}
function calculateZoneId(x, y) {
    return "zone_"+x+"_"+y;
}
function generateCoordinatesHtml(x, y) {
    var output = "<div class=\"zone_info_coordinates\">";
    output += "<p>Coordinates: "+x+"/"+y+"</p>";
    output += "<p>"+apFromHome(x,y)+"&nbsp;<img src=\"images/small_pa.gif\"> and "+kmFromHome(x,y)+" km from town</p>";
    output += "</div>";
    return output;
}
function apFromHome(x, y) {
    return Math.abs(x) + Math.abs(y);
}
function kmFromHome(x, y) {
    return Math.round(Math.sqrt(Math.pow(x,2)+Math.pow(y,2)));
}
function generateClickableOutsideBuildingName(name, url) {
    return "<a href='"+url+"' target='_blank'>"+name+"</a>"
}
function generateDigValue(dig) {
    return "("+dig+"&nbsp;<img src=\"images/small_pa.gif\">)";
}
function generateBuildingHtml(building, scoutPeek, buildingDepleted, blueprintDepleted, lastUserActions, showAction) {
    if(building == undefined) {
        return "";
    }
    var output = "<div class=\"zone_info_building\">";
    output += "<h1>";
    if (showAction && building.url != undefined) {
        output += generateClickableOutsideBuildingName(building.name, building.url);
    } else {
        output += building.name;
    }
    if (building.type == -1) {
        output += " "+generateDigValue(building.dig);
        if (showAction) {
            output += " <a href='javascript:togglePeek()'>peek</a>";
            output += "<div id=\"peek\" class=\"peek\" style=\"display: none;\">";
            output += "Peek text: <input id=\"peek_input\" type=\"text\" name=\"peek_text\" value=\""+(scoutPeek == undefined ? "" : scoutPeek) +"\"/>&nbsp;"+generateLink("save", "", "SAVE_PEEK", "save_peek");
            output += "</div>";
        }
    }
    if (buildingDepleted) {
        output += "<img src=\"images/depleted_building.png\">";
    }
    if (showAction && building.type != -1) {
        output += "&nbsp;";
        if (building.type >= 100) {
            output += "| <a href='javascript:openRuinMap(\""+building.name+"\","+cityId+","+currentZone.x+","+currentZone.y+")'>ruin map</a>";
        } else if (buildingDepleted) {
            output += "| "+ generateLink("replenish", "positive", "REPLENISH_BUILDING", null);
        } else {
            output += "| "+ generateLink("deplete", "negative", "DEPLETE_BUILDING", null);
        }
    }
    output += "</h1>";
    if (building.type == -1 && scoutPeek != undefined) {
        output += "<p><i>"+getPeeker(lastUserActions)+" says it's a \""+scoutPeek+"\"</i></p>";
    }
    output += "<p class='flavor'>"+building.flavor+"</p>";
    if (building.type >= 100) { // Explorable building
        output += "<p class='flavor'>This building does not yield a blueprint when you camp it. However, it holds a fascinating mini-game of exploration. Enjoy!</p>";
    } else if (building.type < 100 && blueprintDepleted) {
        output += "<h1>Blueprint has been found&nbsp;";
        if (showAction) {
            output += generateLink("no, it's still here", "positive", "ADD_BLUEPRINT", null);
        }
        output += "</h1>";
    } else {
        output += "<h1><img src=\"images/blueprint_available.png\"> You can earn a blueprint here!&nbsp;";
        if (showAction) {
            output += generateLink("no, it's gone", "negative", "REMOVE_BLUEPRINT", null);
        }
        output += "</h1>";
    }
    output += "</div>";
    return output;
}
function getScoutSenser(lastUserActions) {
    var scoutSenseAction = lastUserActions["SAVE_SCOUT_SENSE"];
    return scoutSenseAction != undefined ? scoutSenseAction.user.name : "Someone";
}
function getPeeker(lastUserActions) {
    var peekAction = lastUserActions["SAVE_PEEK"];
    return peekAction != undefined ? peekAction.user.name : "Someone";
}
function daysAgoAsText(updateDay, today) {
    return updateDay == 0 ? "" : (updateDay == today ? "" : " <i>("+daysAgo(today, updateDay)+" days ago)</i>");
}
//function depletionDaysAgo(depletionUpdateDay, today) {
//    return depletionUpdateDay == 0 ? "" : (depletionUpdateDay == today ? "" : " <i>("+daysAgo(today, depletionUpdateDay)+" days ago)</i>");
//}
function generateDepletionStatus(depleted, lastUserActions, showAction) {
    var output = "<div class=\"zone_info_depletion\">";
    var depletionUpdateDay = lastDepletionUpdateDay(lastUserActions);
    if (depleted) {
        output += "<h1 class=\"depleted\"><img src=\"images/broken.gif\">&nbsp;Zone is depleted"+daysAgoAsText(depletionUpdateDay, today)+"&nbsp;";
        if (showAction) {
            output += generateLink("replenish", "positive", "REPLENISH_ZONE", null);
        }
        output += "</h1>";
    } else {
        output += "<h1 class=\"replenished\">Zone has replenished"+daysAgoAsText(depletionUpdateDay, today)+"&nbsp;";
        if (showAction) {
            output += generateLink("deplete", "negative", "DEPLETE_ZONE", null);
        }
        output += "</h1>";
    }
    output += "</div>";
    return output;
}
function showSpoilerCampingTopology(campingTopologyKey) {
    $('#camping_spoiler').html("Text will be: "+campingTopologies[campingTopologyKey]);
}
function generateCampingTopology(campingTopology, lastUserActions, showAction) {
    var output = "<div class=\"zone_info_camping\">";
    output += "<h1>Camping";
    output += daysAgoAsText(lastCampingUpdateDay(lastUserActions), today);
    if (showAction) {
        output += " | <a href='javascript:toggleCampingTopology()'>update</a>";
    }
    output += "</h1>";
    output += "<p class='flavor'>"+campingTopologies[campingTopology]+"</p>";
    if (showAction) {
        output += "<div id=\"camping_topology\" class=\"update_field\" style=\"display: none;\">";
        output += "<select id='select_camping_topology'>";
        for (var campingTopologyKey in campingTopologies) {
            output += "<option value='"+campingTopologyKey+"'";
            if (campingTopologyKey == campingTopology) {
                output += "selected";
            }
            output += ">";
            output += campingTopologyKey;
            output += "</option>";
        }
        output += "</select>";
        output += "&nbsp;"+generateLink("save", "", "SAVE_CAMPING", null);
        output += "<p id='camping_spoiler' class='flavor'></p>";
        output += "</div>";
    }
    output += "</div>";
    return output;
}
function getCitizen(citizenId) {
    var foundCitizen = null;
    $.each(citizens, function(citizenPos, citizen) {
        if (citizen.id == citizenId) {
            foundCitizen = citizen;
        }
    });
    return foundCitizen;
}
function getCitizensOutside(citizens) {
    var citizensOutside = 0;
    $.each(citizens, function(citizenPos, citizen) {
        if (citizen.outside) {
            citizensOutside++;
        }
    });
    return citizensOutside;
}
function getWaterInTheBank(bank) {
    var waterInTheBank = 0;
    $.each(bank, function(categoryPos, category) {
        if (category.category == 'Food') {
            $.each(category.items, function(itemPos, item) {
                if (item.d2nItemId == '1') {
                    waterInTheBank = item.amount;
                }
            });
        }
    });
    return waterInTheBank;
}
function getZombieEstimationForDay(estimations, day) {
    var estimationText = 'unknown';
    $.each(estimations, function(estimationPos, estimation) {
        if (estimation.day == day) {
            estimationText = estimation.minimum + ' - ' + estimation.maximum + (estimation.maxed != 1 ? " (not maxed)" : "");
        }
    });
    return estimationText;
}
function getImageForUpgrade(upgradeName) {
    switch (upgradeName) {
        case "Pump" :
            return 'images/icon_water.gif';
        case "Great Pit" :
            return 'images/item_shield.gif';
        case "Watchtower" :
            return 'images/watchtower.gif';
        case "Searchtower" :
            return 'images/searchtower.gif';
        case "Workshop" :
            return 'images/workshop.gif';
        default :
            return 'images/question-mark.png';
    }
}

function generateVariousLine(image, text, amount) {
    return generateVariousLine(image, text, amount, null, null);
}
function generateVariousLine(image, text, amount, extraClass, imageTitle) {
    var output = "<p class='image'>";
    if (image == '') {
        output += '&nbsp';
    } else {
        output += "<img ";
        if (imageTitle != undefined) {
            output += "title='"+imageTitle+"'";
        }
        output += "src='"+image+"'/>";
    }
    output += "</p>";
    output += "<p class='description ";
    output += (extraClass != undefined ? " "+extraClass : "");
    output += "'>"+text+"</p>";
    output += "<p class='";
    if (isNaN(amount)) {
        output += "text";
        if (extraClass != undefined) {
            output += " "+extraClass;
        }
    } else {
        output += "amount";
    }
    output += "'>"+amount+"</p>";
    return output;
}
function generateVariousInfoHtml(citizens) {
    var citizensOutside = getCitizensOutside(citizens);
    var output ="";
    output += "<h2>Buildings in the World Beyond</h2>";
    $.each(outsideBuildings, function(buildingPos, building) {
        output += generateVariousLine(
                building.type >= 100 ? 'images/empty.png' : (building.bluePrintRetrieved ? 'images/blueprint-gone.png' : 'images/blueprint-big.png'),
                generateBuildingName(building),
                generateSpanWithSelectableCoordinates(building.x, building.y),
                "building_outside",
                building.type >= 100 ? '' : (building.bluePrintRetrieved ? 'Blueprint has already been retrieved' : 'Get this blueprint by camping here'));
    });
    output += "<hr/>";
    output += "<h2>Aaargh time</h2>";
    output += "<p"+(city.doorOpen ? " class='danger'" : "")+ "><img src='images/icon_gates.gif'/>&nbsp;The gates are <b>"+(city.doorOpen ? "open" : "closed")+"</b></p>";
    output += "<p"+(citizensOutside > 0 ? " class='danger'" : "")+ "><img src='images/h_human.gif'/>&nbsp;There are <b>"+citizensOutside+"</b> citizens outside </p>";
    output += "<hr/>";
    var waterInTheWell = city.water;
    var waterInTheBank = getWaterInTheBank(bank);
    output += "<h2>Water</h2>";
    output += generateVariousLine('images/h_well.gif', "Water in the well", waterInTheWell);
    output += generateVariousLine('images/icon_water.gif', "Water in the bank", waterInTheBank);
    output += generateVariousLine('', "Total", waterInTheWell+waterInTheBank);
    output += "<hr/>";
    if (!city.hard) {
        output += "<h2>Defense</h2>";
        output += generateVariousLine('images/item_shield.gif', "Base Defense", city.defense.base);
        output += generateVariousLine('images/item_shield.gif', "Defensive Items (x"+city.defense.itemsMultiplier+")", city.defense.items);
        output += generateVariousLine('images/item_shield.gif', "Citizen Guardians", city.defense.citizenGuardians);
        output += generateVariousLine('images/item_shield.gif', "Citizen Homes", city.defense.citizenHomes);
        output += generateVariousLine('images/item_shield.gif', "Upgrades", city.defense.upgrades);
        output += generateVariousLine('images/item_shield.gif', "Buildings", city.defense.buildings);
        output += generateVariousLine('', "Total", city.defense.total);
        output += "<hr/>";
        output += "<h2>Horde attack</h2>";
        output += generateVariousLine('images/h_zombie.gif', "Tonight's attack", getZombieEstimationForDay(estimations, today));
        output += generateVariousLine('images/h_zombie.gif', "Tomorrow's attack", getZombieEstimationForDay(estimations, today+1));
        output += "<hr/>";
        output += "<h2>Building upgrades</h2>";
        $.each(upgrades, function(upgradePos, upgrade) {
            output += generateVariousLine(getImageForUpgrade(upgrade.name), upgrade.name, "Level "+upgrade.level);
        });
    }
    return output;
}
function generateBuildingName(building) {
    var output = "<span class='"+calculateZoneId(building.x, building.y)+"'";
    if (building.type == -1 && building.peek != undefined && building.peek != "") {
        output += " title='Peeked by "+building.peeker+"'";
    } else if (building.type != -1) {
        output += " title='"+removeQuotes(building.flavor)+"'";
    }
    output += ">";

    if (building.type == -1) {
        if (building.peek != undefined && building.peek != "") {
            output += "<i>"+building.peek+"</i>";
        } else {
            output += building.name;
        }
        output += " <span title='AP costs to unbury building'>"+generateDigValue(building.dig)+"</span>";
    } else {
        output += (building.url != undefined ? generateClickableOutsideBuildingName(building.name, building.url) : building.name);
    }
    output += "</span>";
    if (building.depleted) {
        output += "&nbsp;<img src=\"images/depleted_building.png\" title=\"Building depleted\">";
    }
    return output;
}
function generateConstructionInfoHtml(insideBuildings, excludeStatus) {
    var output = "<table id='construction_overview_table'>";
    output += "<tr><th>Name</th><th>&nbsp;</th></tr>";
    constructionLines = [];
    generateConstructionInfoChildNodes(0, insideBuildings, excludeStatus);
    $.each(constructionLines, function(pos, tableRow) {
        output += tableRow;
    });
    output += "</table>";
    output += "<p class='die2nitewiki'><i>All specific information comes directly from <a href='http://die2nitewiki.com/wiki/Main_Page'>die2nitewiki.com</a></i></p>";
    return output;
}
function removeQuotes(flavor) {
    return flavor.replace(/'/g, "");
}
function generateConstructionInfoChildNodes(level, insideBuildingNode, excludeStatus) {
    $.each(insideBuildingNode, function(buildingPos, building) {
        if (excludeStatus != undefined && $.inArray(building.status, excludeStatus) != -1) {
            return;
        }
        var output = "";
        output += "<tr class='building "+building.status.toLowerCase()+"'";
        output += " title='";
        if (building.status == 'AVAILABLE') {
            output += "Construction is available as a blueprint";
        } else if (building.status == 'NOT_AVAILABLE') {
            output += "Construction not yet discovered";
        } else {
            output += "Constructed and available for use";
        }
        if (building.flavor != undefined) {
            output += " &mdash; "+removeQuotes(building.flavor);
        }
        output += "'>";
        output += "<td class='name'>";
        var first = true;
        for (var i = 0; i < level; i++) {
            if (i == level - 1) {
                output += "<img class='building-line"+(first?" first":"")+"' src='images/folder-corner.png'/>";
            } else {
                output += "<img class='building-line"+(first?" first":"")+"' src='images/empty.png'/>";
            }
            first = false;
        }

        // If the item has been stored in the building sprite, use that. If not, use the image from the D2N site
        if (building.inSprite) {
            output += "<div class='building-line building-image"+(first?" first":"")+" building-"+building.image+"'/>";
        } else {
            output += "<img class='building-line building-image"+(first?" first":"")+"' src='"+iconUrl+building.image+".gif'/>&nbsp;";
        }

        output += "<p>";
        if (building.url != undefined) {
            output += "<a href='"+building.url+"' target='_blank'>"+building.name+"</a>";
        } else {
            output += building.name;
        }
        output += "</p>";

        if ((building.defence != undefined && building.defence > 0) || building.ap != undefined) {
            output += "<div class='construction-info-group'>";
            output += "<ul>";
            output += "<li class='construction-info'>&nbsp;|</li>";
        }
        if (building.temporary) {
            output += "<li class='construction-info'><img src='images/temporary.gif' title='This construction only lasts for a day'/></li>";
        }
        if (building.defence != undefined && building.defence > 0) {
            output += "<li class='construction-info' title='Defence value added to the town\'s defence><p>" + building.defence+ "</p><img src=\"images/item_shield.gif\"></li>";
        }
        if (building.ap != undefined) {
            output += "<li class='construction-info' title='AP cost to construct the building'><p>" + building.ap + "</p><img src=\"images/small_pa.gif\"></li>";
        }
        for (resourcePos = 0; resourcePos < building.requiredResources.length; resourcePos++) {
            var amount = building.requiredResources[resourcePos].amount;
            var item = itemLookup[building.requiredResources[resourcePos].itemId];
            output += "<li class='construction-info' title='"+item.name+"'>";
            output += "<p>" + amount + "</p>";
            if (item.inSprite) {
                output += "<div class='construction-info-image item-"+item.image+"'/>";
            } else {
                output += "<img src='"+iconUrl+"item_"+item.image+".gif'/>";
            }
            output += "</li>";
        }
        if ((building.defence != undefined && building.defence > 0) || building.ap != undefined) {
            output += "</ul>";
            output += "</div>";
        }

        output += "</td>";
        output += "<td class='lock'>";
        if (building.status == 'AVAILABLE' && building.alwaysAvailable != true) {
            output += "<a href='javascript:updateBuildingAvailability(\"LOCK_BUILDING\", "+building.id+")'><img src='images/unlock.png' title='Undiscover this construction as a blueprint'/></a>";
        } else if (building.status == 'NOT_AVAILABLE') {
            output += "<a href='javascript:updateBuildingAvailability(\"UNLOCK_BUILDING\", "+building.id+")'><img src='images/lock.png' title='Discover this construction as a blueprint'/></a>";
        } else {
            output += "&nbsp;";
        }
        output += "</td>";
        output += "</tr>";
        constructionLines[constructionLines.length] = output;
        generateConstructionInfoChildNodes(level+1, building.childBuildings, excludeStatus);
    });
}
function generateVisitationHtml(visited, showAction) {
    var output = "<div class=\"zone_info_visited\"><p>";
    if (!visited) {
        output += "<img src='images/visited.png'/> ";
        output += "Zone has not been visited ";
        if (showAction) {
            output += generateLink("yes, it has", "positive", "VISIT_ZONE", null);
        }
    } else {
        output += "Zone has been visited ";
        if (showAction) {
            output += generateLink("no, it hasn\'t", "negative", "UNVISIT_ZONE", null);
        }
    }
    output += "</p></div>";
    return output;
}
function generateTagHtml(tag) {
    if (tag == undefined) {
        return "";
    }
    var output = "<div class=\"zone_info_tag\">";
    output += "<h1>Tag</h1>";
    output += "<div class='tag-image tag-"+tag.serial+"' title='"+tag.description+"'/>";
    output += "<p>&nbsp;"+tag.description+"</p>";
    output += "</div>";
    return output;
}
function generateCitizenHtml(citizens) {
    if (citizens.length == 0) {
        return "";
    }
    var output = "<div class=\"zone_info_citizens\">";
    output += "<h1>Citizens</h1>";
    $.each(citizens, function(citizenPos, citizen) {
        output += generateIndividualCitizenHtml(citizen);
    });
    output += "</div>";
    return output;
}
function generateIndividualCitizenHtml(citizen) {
    var output = "<div class='job-image job-";
    output += citizen.jobText == "Unknown" ? "question-mark" : citizen.image;
    output += "' title='"+citizen.jobText+"'/>";
    output += "<p>&nbsp;"+ citizen.name+"</p>";
    if (citizen.specialImage != undefined) {
        output += "&nbsp;<div class='special-distinction distinction-"+citizen.specialImage+"' title='"+citizen.specialDescription+"'/>";
    }
    return output;
}
function generateZombieHtml(zombieAmount, scoutSense, danger, today, showAction, lastUpdateActions, status) {
    var output = "<div class=\"zone_info_zombies\">";
    output += "<h1>Zombies";
    if (showAction) {
        output += "&nbsp;<a href='javascript:toggleZombieObservation()'>update</a> | <a href='javascript:toggleScoutSenseObservation()'>scout sense</a>";
        output += "<div id=\"zombie_observation\" class=\"update_field observation_entry\" style=\"display: none;\">";
        output += "Zombie amount: <input size=\"10\" type=\"number\" name=\"zombie_amount\" value=\"\"/>&nbsp;"+generateLink("save", "", "SAVE_ZOMBIES", null);
        output += "</div>";
        output += "<div id=\"scout_sense_observation\" class=\"update_field observation_entry\" style=\"display: none;\">";
        output += "Sensed amount: <input size=\"10\" type=\"number\" name=\"scout_sense_amount\" value=\"\"/>&nbsp;"+generateLink("save", "", "SAVE_SCOUT_SENSE", null);
        output += "</div>";
    }
    output += "</h1>";
    var zombieAmountAccurate = isZombieAmountAccurate(upgradedMap, lastUpdateActions, today, status);
    output += "<p><img src=\"images/h_zombie.gif\">&nbsp;"+(zombieAmount == -1 || !zombieAmountAccurate ? dangerLevels[danger].amountNormal : zombieAmount);
    if (zombieAmount != -1 && !zombieAmountAccurate) {
        output += " &mdash; <i>was "+zombieAmount+" <img src=\"images/h_zombie.gif\"> "+daysAgo(today, lastZombieUpdateDay(lastUpdateActions))+" days ago</i>";
    }
    output += "</p>";
    if (scoutSense != undefined && scoutSense != -1) {
        output += "<div class='job-image job-vest_on'/>&nbsp;<p class='scout_sense'>"+
                getScoutSenser(lastUpdateActions)+" sensed "+
                scoutSense+" <img src=\'images/h_zombie.gif'> "+
                daysAgo(today, lastScoutSenseUpdateDay(lastUpdateActions))+" days ago"+
                (showAction ? " "+generateLink("remove", "negative", "DELETE_SCOUT_SENSE", null):"")+
                "</p>";
    }
    output += "</div>";
    return output;
}
function daysAgo(now, then) {
    return now - then;
}
function generateItemHtml(items, generateId, showAction) {
    var output = "<div class=\"zone_info_items\">";
    output += "<h1>Items on the ground&nbsp;";
    if (showAction) {
        output += "<a href='javascript:enterItemUpdateMode()'>update</a>";
    }
    output += "</h1>";
    if (items == undefined || items.length == 0) {
        output += "<p>-</p>";
    } else {
        output += generateItemsOnTheGroundHtml(items, generateId);
    }
    output += "</div>";

    return output;
}
function generateItemsOnTheGroundHtml(items, generateId) {

    var output = "<div class=\"zone_info_items_box\">";
    output += "<ul>";

    var orderedItems = orderItemsByName(items);
    for (itemPos = 0; itemPos < orderedItems.length; itemPos++) {
        output += generateItem(orderedItems[itemPos].item, true, generateId, "item_", false);
    }
    output += "</ul>";
    output += "</div>";
    return output;
}
function getFullItem(item) {
    var fullItem = itemLookup[item.d2nItemId];
    if (fullItem == undefined) {
        alert("Error, could not find item "+item.d2nItemId);
    }
    return fullItem;
}
function orderItemsByName(items) {
    var orderedItems = [];
    $.each(items, function(itemId, item) {
        var fullItem = getFullItem(item);
        orderedItems[orderedItems.length] = { "name": getItemFullName(fullItem, item.breakable), "item": item };
    });
    orderedItems = orderedItems.sort(sortItemsByName);
    return orderedItems;
}

// There are three sources of information:
// 1. Upgraded Map is always correct, unless the zones are undiscovered or discovered_not_visited_today
// 2. Update My Zone
// 3. Manual zombie number update
function isZombieAmountAccurate(upgradedMap, lastUpdateActions, today, status) {
    var accurate = true;
    accurate = upgradedMap && status == 'DISCOVERED_AND_VISITED_TODAY';
    accurate |= checkIfActionHappenedToday(lastUpdateActions["UPDATE_ZONE"], today);
    accurate |= checkIfActionHappenedToday(lastUpdateActions["UPDATE_ZONE_EXTENDED"], today);
    accurate |= checkIfActionHappenedToday(lastUpdateActions["SAVE_ZOMBIES"], today);
    return accurate;
}
function lastScoutSenseUpdateDay(lastUserActions) {
    return highestUpdateDay(lastUserActions, [ "SAVE_SCOUT_SENSE" ] );
}
function lastZombieUpdateDay(lastUserActions) {
    return highestUpdateDay(lastUserActions, [ "UPDATE_ZONE", "UPDATE_ZONE_EXTENDED", "SAVE_ZOMBIES" ] );
}
function lastCampingUpdateDay(lastUserActions) {
    return highestUpdateDay(lastUserActions, [ "SAVE_CAMPING" ] );
}
function lastDepletionUpdateDay(lastUserActions) {
    return highestUpdateDay(lastUserActions, [ "UPDATE_ZONE", "UPDATE_ZONE_EXTENDED", "REPLENISH_ZONE", "DEPLETE_ZONE" ] );
}
function highestUpdateDay(lastUserActions, actionTypes) {
    var highestDay = 0;
    $.each(actionTypes, function(pos, actionType) {
        var action = lastUserActions[actionType];
        if (action != undefined) {
            highestDay = Math.max(action.day, highestDay);
        }
    });
    return highestDay;
}
function checkIfActionHappenedToday(action, today) {
    return action != undefined && action.day == today;
}
function findLastUpdateAction(lastUpdateActions) {
    var lastUpdateAction;
    $.each(lastUpdateActions, function(action, updateInfo) {
        if (lastUpdateAction == undefined || updateInfo.updated > lastUpdateAction.updated) {
            lastUpdateAction = updateInfo;
        }
    });
    return lastUpdateAction;
}
function generateUpdateHtml(lastUpdateActions) {
    if (lastUpdateActions == undefined) {
        return "";
    }
    var lastUpdateAction = findLastUpdateAction(lastUpdateActions);
    if (lastUpdateAction == undefined) {
        return "";
    }
    var output = "<div class=\"zone_info_update\">";
    output += "<p>Last update: day "+lastUpdateAction.day+" by <b>"+lastUpdateAction.user.name+"</b> ("+lastUpdateAction.updated+")</p>";
    output += "</div>";
    return output;
}
function generateHoverFooter(showAction) {
    if (showAction) {
        return "";
    }
    return "<div class=\"zone_hover_footer\"><p>&lt;&lt;Click the zone to select and have edit options&gt;&gt;<p></div>";
}
function generateLink(text, colorClass, action, linkId) {
    var output = "<a ";
    if (linkId != undefined) {
        output += "id='"+linkId+"' ";
    }
    output += "href='javascript:updateFieldInZone("+currentZone.x+","+currentZone.y+",\""+action+"\")' class='"+colorClass+"'>"+text+"</a>";
    return output;
}
function getItemParams(items) {
    if (items == undefined) {
        return "";
    }
    var output = "";
    for (itemPos = 0; itemPos < items.length; itemPos++) {
        output += "&items="+items[itemPos]
    }
    return output;
}
function saveItems() {
    updateFieldInZone(currentZone.x, currentZone.y, "SAVE_ITEMS");
}
function updateItemsToArray(items) {
    var itemArray = [];
    for (itemPos = 0; itemPos < items.length; itemPos++) {
        var item = items[itemPos]
        itemArray[itemPos] = item.d2nItemId+(item.broken?"B":"")+"-"+item.amount;
    }
    return itemArray;
}
function determineDanger(dangerLevels, zombieAmount) {
    var determinedDangerLevel = "UNKNOWN";
    $.each(dangerLevels, function(dangerLevel, dangerDetails) {
        if (    dangerDetails.lowTreshold <= zombieAmount &&
                (dangerDetails.highTreshold == undefined || dangerDetails.highTreshold >= zombieAmount)) {
            determinedDangerLevel = dangerLevel;
        }
    });
    return determinedDangerLevel;
}
function updateBuildingForAvailability(action, buildingId) {
    updateBuildingsForAvailability(insideBuildings, action, buildingId);
    generateConstructionTabs(insideBuildings);
}
function updateBuildingsForAvailability(buildings, action, buildingId) {
    $.each(buildings, function(buildingPos, building) {
        if (building.id == buildingId) {
            building.status = (action == 'LOCK_BUILDING' ? 'NOT_AVAILABLE' : 'AVAILABLE');
        }
        updateBuildingsForAvailability(building.childBuildings, action, buildingId);
    });
}
function waitMessage() {
    $('#waiting').show(500);
    $('#update_message').hide(0);
}
function successMessage() {
    $('#waiting').hide(500);
    $('.corner').addClass('success').delay(3000).queue(function(next) {
        $(this).removeClass('success');
        next();
    });
    $('#update_message').removeClass().addClass('success').text("Update successful").show(500);
    $('#update_message').delay(3000).fadeOut('slow');
}
function errorMessage(errorMessage) {
    $('#waiting').hide(500);
    $('.corner').addClass('error').delay(3000).queue(function(next) {
        $(this).removeClass('error');
        next();
    });
    $('#update_message').removeClass().addClass('error').text(errorMessage).show(500);
    $('#update_message').delay(3000).fadeOut('slow');
}
function updateBuildingAvailability(action, buildingId) {
    waitMessage();
    $.ajax({
        type: "POST",
        url: "/town/"+action.toLowerCase(),
        data:
            "key="+key+"&city="+cityId+"&building="+buildingId+"&day="+today,
        async: false,
        success: function (json) {
            switch (action) {
                case "UNLOCK_BUILDING":
                    break;
                case "LOCK_BUILDING":
                    break;
            }
            updateBuildingForAvailability(action, buildingId);
            successMessage();
        },
        error: function (xhr, error) {
            errorMessage(JSON.parse(xhr.responseText).errorMessage);
        }
    });
}
function updateFieldInZone(x, y, action) {
    waitMessage();
    var zone = zoneMap[x][y];
    var peekText = null;
    var campingTopology = null;
    var zombies = null;
    var scoutSense = null;
    var items = null;
    switch (action) {
        case "SAVE_PEEK":
            peekText = $('input[name=peek_text]').val();
            break;
        case "SAVE_ZOMBIES":
            zombies = $('input[name=zombie_amount]').val();
            break;
        case "SAVE_SCOUT_SENSE":
            scoutSense = $('input[name=scout_sense_amount]').val();
            break;
        case "DELETE_SCOUT_SENSE":
            action = "SAVE_SCOUT_SENSE";
            scoutSense = -1;
            break;
        case "SAVE_CAMPING":
            campingTopology = $('#select_camping_topology').val();
            break;
        case "SAVE_ITEMS":
            items = updateItemsToArray(itemsToUpdate);
            break;
    }

    $.ajax({
        type: "POST",
        url: "/zone/" + action.toLowerCase(),
        data:
            "key="+key+"&x="+x+"&y="+y+"&day="+today+
                    (peekText == undefined ? "" : "&peek_text="+peekText)+
                    (zombies == undefined ? "" : "&zombies="+zombies)+
                    (scoutSense == undefined ? "" : "&scout_sense="+scoutSense)+
                    (campingTopology == undefined ? "" : "&camping_topology="+campingTopology)+
                    getItemParams(items),
        async: false,
        success: function (json) {
            //noinspection SpellCheckingInspection
            switch (action) {
                case "ADD_BLUEPRINT":
                    zone.bluePrintRetrieved = false;
                    break;
                case "DEPLETE_BUILDING":
                    zone.buildingDepleted = true;
                    break;
                case "DEPLETE_ZONE":
                    zone.zoneDepleted = true;
                    break;
                case "REMOVE_BLUEPRINT":
                    zone.bluePrintRetrieved = true;
                    break;
                case "REPLENISH_BUILDING":
                    zone.buildingDepleted = false;
                    break;
                case "REPLENISH_ZONE":
                    zone.zoneDepleted = false;
                    break;
                case "SAVE_ITEMS":
                    zone.items = itemsToUpdate.slice();
                    break;
                case "SAVE_CAMPING":
                    zone.campingTopology = campingTopology;
                    break;
                case "SAVE_PEEK":
                    zone.scoutPeek = peekText;
                    break;
                case "SAVE_ZOMBIES":
                    zone.zombies = zombies;
                    zone.danger = determineDanger(dangerLevels, zone.zombies);
                    break;
                case "SAVE_SCOUT_SENSE":
                    zone.scoutSense = scoutSense;
                    break;
                case "VISIT_ZONE":
                    zone.visited = true;
                    break;
                case "UNVISIT_ZONE":
                    zone.visited = false;
                    break;
            }
            var updatedZone = JSON.parse(json).zoneUpdate;
            zone.lastUserActions = updatedZone.lastUserActions;
            $("#"+calculateZoneId(x,y)).click();
            updateImagesInZoneAlsoDelete(zone,x,y,activePlayer);
            updateSearchItems();
            repaintMarkers();
            updateBuildingList();
            successMessage();
        },
        error: function (xhr, error) {
            errorMessage(JSON.parse(xhr.responseText).errorMessage);
        }
    });
}
updateZone = function (key) {
    waitMessage();
    var zone = null;
    $.ajax({
        type: "POST",
        url: "/zone",
        data: "key="+key,
        async: false,
        success: function (json) {
            successMessage();
            zone = JSON.parse(json);
        },
        error: function (xhr, error) {
            errorMessage(JSON.parse(xhr.responseText).errorMessage);
        }
    });
    return zone
};
function generateSpanWithSelectableCoordinates(x, y) {
    return "<span class='"+calculateZoneId(x, y)+"' title='Click to select the zone'>"+generateSelectableCoordinates(x, y)+"</span>";
}
function generateSelectableCoordinates(x, y) {
    return "<a href='javascript:selectZoneWithCoordinates("+x+","+y+")'>"+x+"/"+y+"</a>";
}
function generateCitizenOverviewHtml(citizens) {
    var output = "<table id='citizen_overview_table'>";

    output += "<tr><th>Name</th><th>Zone</th><th>Activity</th><th>Last update</th></tr>";
    $.each(citizens, function(citizenPos, citizen) {
        output += "<tr>";
        output += "<td id='"+citizen.id+"' class='name'>"+generateIndividualCitizenHtml(citizen)+"</td>";
        if (citizen.outside && citizen.x != undefined && citizen.y != undefined) {
            output += "<td class='coordinates'>"+generateSpanWithSelectableCoordinates(citizen.x, citizen.y)+"</td>";
        } else if (citizen.outside) {
            output += "<td class='coordinates'>unknown</td>";
        } else {
            output += "<td class='coordinates'>inside</td>";
        }
        output += "<td class='activity'>"+getActivityStars(citizen.activityRating)+"</td>";
        output += "<td class='last_update'>"+(citizen.lastUpdated?citizen.lastUpdated:"unknown")+"</td>";
        output += "</tr>";
    });
    output += "</table>";
    return output;
}
function getActivityStars(rating) {
    var wholeStars = Math.floor(rating / 2);
    var halfStar = rating % 2 == 1;
    var output = "";
    for (i = 0; i < wholeStars; i++) {
        output += "<img src='images/gold_star.png'/>";
    }
    if (halfStar) {
        output += "<img src='images/gold_star_half.png'/>";
    }
    return output;
}
function generateBankHtml(bank, divClass, showAmount, generateId, idPrefix, addWholeMarker) {
    var output = "<div id=\"bank_panel\" class=\"bank_panel "+divClass+"\">";

    $.each(bank, function(categoryPos, categoryGroup) {
        var category = categoryGroup.category;
        var items = categoryGroup.items;
        if (!$.isEmptyObject(items)) {
            output += "<div class=\"bank_category\">";
            output += "<p ";
            if (categoryGroup.categoryKey != undefined) {
                output += "id=\"CAT_"+categoryGroup.categoryKey+"\"";
            }
            output += "class=\"category\">"+category+"</p>";
            output += "<ul>";
            $.each(items, function(itemPos, item) {
                output += generateItem(item, showAmount, generateId, idPrefix, addWholeMarker);
            });
            output += "</ul>";
            output += "</div>";
        }
    });
    output += "</div>";
    if (divClass == 'found_items') {
        output += "<p><a href='javascript:deselectSearchItems()'>Deselect all items</a></p>";
    }
    return output;
}
function generateItem(item, showAmount, generateId, idPrefix, addWholeMarker) {
    var fullItem = getFullItem(item);
    var output = "<li class=\"item";
    if (item.broken) {
        output += " broken";
    }
    output += "\"";
    if (generateId) {
        output += " id=\""+idPrefix+itemToCompoundItemKey(item, addWholeMarker)+"\"";
    }
    output += " title='"+getItemFullName(fullItem, item.broken)+"'";
    output += ">";

    // If the item has been stored in the item sprite, use that. If not, use the image from the D2N site
    if (fullItem.inSprite) {
        output += "<div class='item-image item-"+fullItem.image+"' title='"+getItemFullName(fullItem, item.broken)+"'/>";
    } else {
        output += "<img src='"+iconUrl+"item_"+fullItem.image+".gif' title ='"+getItemFullName(fullItem, item.broken)+"'/>";
    }

    if (showAmount) {
        output += "<p class='item-amount'>"+item.amount+"</p>";
    }
    output += "</li>";
    return output;
}
function getItemFullName(item, broken) {
    return item.name+(broken?" (broken)":"")+
            (item.poisoned?" (poisoned)":"")+
            (item.presetBp?" (preset)":"");
}
function generateItemUpdater() {
    generateItemUpdaterFromBoxHtml();
    generateItemUpdaterDropdownList();
}
function sortItemsByName(a, b) {
    return b["name"] < a["name"] ? 1 : b["name"] > a["name"] ? -1 : 0;
}
function generateItemUpdaterDropdownList() {
    var orderedItems = [];
    $.each(itemLookup, function(itemId, item) {
        orderedItems[orderedItems.length] = { "name": getItemFullName(item, false), "key": "item_"+itemId };
        if (item.breakable) {
            orderedItems[orderedItems.length] = { "name": getItemFullName(item, false), "key": "item_"+itemId+"B" };
        }
    });
    orderedItems = orderedItems.sort(sortItemsByName);
    var outputArray = [];
    $.each(orderedItems, function(itemPosition, item) {
        outputArray[outputArray.length] = "<option value='"+item.key+"'>"+item.name+"</option>";
    });
    $('#item_dropdown').html(outputArray.join(""));
}
function generateItemUpdaterFromBoxHtml() {
    var output = "<h1>Click on item to add to ground</h1>";
    output += generateBankHtml(uniqueItems,"item_updater",false,true,"item_", false);
    $('#item_update_box #unique_items').html(output);
}
function generateItemUpdaterToBoxHtml(items) {
    var output = "<h1>Click on item to remove from ground</h1>";
    output += generateItemsOnTheGroundHtml(items, true);
    $('#item_update_box #drop_box').html(output);
}
function filterExistingItemsOnSnippet(itemNameSnippet) {
    deselectSearchItems();
    if (itemNameSnippet == undefined || itemNameSnippet == "") {
        return;
    }
    itemNameSnippet = itemNameSnippet.toLowerCase();
    $.each(itemSearchIndex, function(compoundItemId, zones) {
        var itemId = parseCompoundItemKey(compoundItemId).id;
        if (itemLookup[itemId].name.toLowerCase().indexOf(itemNameSnippet) != -1) {
            $('#item_search #'+compoundItemId).click();
        }
    });
}
function filterItemsOnSnippet(itemNameSnippet) {
    $("#unique_items .item").removeClass("filtered");
    if (itemNameSnippet == undefined || itemNameSnippet == "") {
        return;
    }
    itemNameSnippet = itemNameSnippet.toLowerCase();
    $.each(itemLookup, function(d2nItemId, item) {
        if (item.name.toLowerCase().indexOf(itemNameSnippet) != -1) {
            $("#item_"+d2nItemId).addClass('filtered');
            $("#item_"+d2nItemId+"B").addClass('filtered');
        }
    });
}
function updateSearchItems() {
    itemSearchIndex = generateItemSearchIndex(zoneMap);
    var itemsToSearch = convertMapToArrayForBankGeneration(getSearchDropDownEntries(itemSearchIndex));
    $('#item_search').html(generateBankHtml(itemsToSearch, "found_items", true, true, "", true));
    $.each(selectedSearchItems, function(element, ignore) {
        $("#"+element).addClass('selected');
    });
}
function parseCompoundItemKey(compoundItemId) {
    var matches = compoundItemId.match(/^([\-0-9]+)([BW])$/);
    var itemId = parseInt(matches[1]);
    var broken = matches[2] == "B";
    return { "id": itemId, "broken": broken };
}
function convertMapToArrayForBankGeneration(searchDropDownEntries) {
    var itemsToSearch = [];
    $.each(categories, function(categoryPos, category) {
        var items = searchDropDownEntries[category];
        itemsToSearch[categoryPos] = [];
        itemsToSearch[categoryPos].categoryKey = category;
        itemsToSearch[categoryPos].category = keyToReadableForm(category);
        itemsToSearch[categoryPos].items = items;
    });
    return itemsToSearch;
}
function keyToReadableForm(key) {
    key = key.replace(/_/g, ' ');
    return key.substr(0,1) + key.substr(1).toLowerCase();
}
function getSearchDropDownEntries(itemSearchIndex) {
    var searchDropDownEntries = {};
    $.each(itemSearchIndex, function(compoundItemId, zones) {
        // Place the category
        var key = parseCompoundItemKey(compoundItemId);

        var item = itemLookup[key.id];
        var category = item.category;
        var categorySlot = searchDropDownEntries[category];
        if (categorySlot == undefined) {
            searchDropDownEntries[category] = {};
            categorySlot = searchDropDownEntries[category];
        }
        // Place the item in the category
        categorySlot[compoundItemId] = { "d2nItemId": key.id, "amount": countItemsInZones(zones), "broken": key.broken };
    });
    return searchDropDownEntries;
}
function getSortedKeys(keys) {
    var orderedKeys = [];
    var count = 0;
    $.each(keys, function(key, payload) {
        orderedKeys[count++] = key;
    });
    orderedKeys.sort();
    return orderedKeys;
}
function updateSearchDropDown(searchDropDownEntries) {
    var output = "<option value=\"0\" selected>&mdash; None &mdash;</option>";
    $.each(getSortedKeys(searchDropDownEntries), function(categoryPos, categoryName) {
        output += ("<option value=\"CAT_"+categoryName+"\">"+categoryName+"</option>");
        $.each(getSortedKeys(searchDropDownEntries[categoryName]), function(itemPos, itemName) {
            var item = searchDropDownEntries[categoryName][itemName];
            output += ("<option value=\""+generateCompoundItemId(item.id, item.broken)+"\">&nbsp;&nbsp;"+itemName+(item.broken?" (broken)":"")+" x "+item.amount+"</option>");
        });
    });
    $('#search_item_dropdown').html(output);
}
function countItemsInZones(zones) {
    var count = 0;
    $.each(zones, function(pos, zone) {
        count += zone.amount;
    });
    return count;
}
function generateCompoundItemId(id, broken) {
    return id + (broken ? "B" : "W");
}
function sortItemsByAmount(a, b) {
    return b["amount"] - a["amount"];
}
function generateItemSearchIndex(zoneMap) {
    // Build up the basic index
    var itemSearchIndex = {};
    $.each(zoneMap, function(x, column) {
        $.each(column, function(y, zone) {
            $.each(zoneMap[x][y].items, function(itemPos, item) {
                // Make sure there's an item slot
                var key = generateCompoundItemId(item.d2nItemId, item.broken);
                var searchIndexSlot = itemSearchIndex[key];
                if (searchIndexSlot == undefined) {
                    itemSearchIndex[key] = [];
                    searchIndexSlot = itemSearchIndex[key];
                }
                searchIndexSlot[searchIndexSlot.length] = { "x": x, "y": y, "amount": item.amount, "broken": item.broken };
            });
        });
    });
    // Determine the key/amount pairs, ordered by amount
    var itemAmount = [];
    $.each(itemSearchIndex, function(itemKey, items) {
        var amount = 0;
        $.each(items, function(itemPos, item) {
            amount += item.amount;
        });
        itemAmount[itemAmount.length] = { "key": itemKey, "amount": amount };
    });
    itemAmount = itemAmount.sort(sortItemsByAmount);
    // Build up the index again, but now sorted by amount
    var newItemSearchIndex = {};
    $.each(itemAmount, function(itemAmountPos, item) {
        newItemSearchIndex[item.key] = itemSearchIndex[item.key];
    });
    return newItemSearchIndex;
}
isCitizenInZone = function(citizens, activePlayer) {
    for (citizenPos = 0; citizenPos < citizens.length; citizenPos++) {
        if (citizens[citizenPos].name == activePlayer) {
            return true;
        }
    }
    return false;
};
function updateCitizensInZones(chaos, zoneMap, citizens) {
    removeCitizensFromZones(zoneMap);
    if (!chaos) {
        addCitizensToZones(zoneMap, citizens);
    }
    repaintMapMarkers(zoneMap, activePlayer);
}
function removeCitizensFromZones(zoneMap) {
    $.each(zoneMap, function(x, column) {
        $.each(column, function(y, zone) {
            zone.citizens = [];    
        });
    });
}
function addCitizensToZones(zoneMap, citizens) {
    $.each(citizens, function(citizenPos, citizen) {
        if (citizen.outside && citizen.x != undefined && citizen.y != undefined && !(citizen.x == 0 && citizen.y == 0)) {
            var zone = zoneMap[citizen.x][citizen.y];
            zone.citizens[zone.citizens.length] = citizen;
        }
    });
}
function repaintMapMarkers(zoneMap, activePlayer) {
    $.each(zoneMap, function(x, column) {
        $.each(column, function(y, zone) {
            updateImagesInZoneAlsoDelete(zone, x, y, activePlayer);
        });
    });
    repaintMarkers();
}
function updateImagesInZoneAlsoDelete(zone, x, y, activePlayer) {
    removeElementsFromZone(zone,x,y);
    updateImagesInZone(zone,x,y,activePlayer);
}
function updateImagesInZone(zone, x, y, activePlayer) {
    var zoneId = calculateZoneId(x,y);
    // ZOMBIE NUMBERS
    var zombieAmountAccurate = isZombieAmountAccurate(upgradedMap, zone.lastUserActions, today, zone.discoveryStatus);
    var zombieNumberShown = false;
    if (dangerLevels[zone.danger].showTwoDigits) {
        addTextToZone(zoneId, "zombie", zone.zombies == -1  || !zombieAmountAccurate ?
                dangerLevels[zone.danger].amountTwoDigits : zone.zombies, false);
        zombieNumberShown = true;
    }
    // SCOUT SENSE
    if (!zombieNumberShown && zone.scoutSense != undefined && zone.scoutSense != -1) {
        addTextToZone(zoneId, "scout_sense", zone.scoutSense, false);
    }
    // TAG
    if (zone.tag != undefined) {
        addDivToZone(zoneId, "tag", zone.tag.serial);
    }
    // YOU
    if (isCitizenInZone(zone.citizens, activePlayer)) {
        addImageToZone(zoneId, "me", "h_human.gif");
    }
    // VISITED
    if (!zone.visited && !(x == 0 && y == 0)) {
        addImageToZone(zoneId, "visited", "mask.png");
    }
    // DEPLETED
    if (zone.zoneDepleted) {
        addImageToZone(zoneId, "depleted", "depleted.png");
    }
    // OTHER CITIZENS
    if (zone.citizens.length > 0) {
        addImageToZone(zoneId, "citizen", "citizen.png");
    }
    // BLUEPRINT AVAILABLE
    if (zone.building != undefined && !zone.bluePrintRetrieved) {
        addImageToZone(zoneId, "blueprint_available", "blueprint_available.png");
    }
    // BUILDING DEPLETION
    if (zone.buildingDepleted) {
        addImageToZone(zoneId, "depleted_building", "depleted_building.png");
    }
}
function removeElementsFromZone(zone, x, y) {
    var zoneId = calculateZoneId(x,y);
    removeElementFromZone(zoneId, "zombie");
    removeElementFromZone(zoneId, "scout_sense");
    removeElementFromZone(zoneId, "visited");
    removeElementFromZone(zoneId, "me");
    removeElementFromZone(zoneId, "depleted");
    removeElementFromZone(zoneId, "citizen");
    removeElementFromZone(zoneId, "depleted_building");
    removeElementFromZone(zoneId, "blueprint_available");
}
function addTextToZone(zoneId, textType, text, show) {
    var zoneDiv = document.getElementById(zoneId);
    var newText = document.createElement('p');
    newText.setAttribute('id', zoneId+"_"+textType);
    if (!show) {
        newText.setAttribute('style', 'display: none;');
    }
    newText.setAttribute('class', textType+"_marker");
    newText.appendChild(document.createTextNode(text));
    zoneDiv.appendChild(newText);
}
function addDivToZone(zoneId, imageType, serial) {
    var zoneDiv = document.getElementById(zoneId);
    var newDiv = document.createElement('div');
    newDiv.setAttribute('class', "tag-image tag-"+serial+" "+imageType+"_marker");
    zoneDiv.appendChild(newDiv);
}
function addImageToZone(zoneId, imageType, imageSrc) {
    var zoneDiv = document.getElementById(zoneId);
    var newImage = document.createElement('img');
    newImage.setAttribute('id', zoneId+"_"+imageType);
    newImage.setAttribute('class', imageType+"_marker");
    newImage.setAttribute('src', "images/"+imageSrc);
    zoneDiv.appendChild(newImage);
}
function removeElementFromZone(zoneId, imageType) {
    var imageId = zoneId+"_"+imageType;
    var zone = document.getElementById(zoneId);
    var image = document.getElementById(imageId);
    if (image != undefined) {
        zone.removeChild(image);
    }
}
function toggleConstructionView() {
    $(".construction_info").hide();
    if ($("input[name=construction_view]:checked").val() == 'constructed_buildings') {
        $("#construction_info_constructed").show();
    } else if ($("input[name=construction_view]:checked").val() == 'available_buildings') {
        $("#construction_info_available").show();
    } else {
        $("#construction_info_all").show();
    }
}
function toggleDepletedMarkers() {
    toggleMarkers($('input[name=toggle_depleted]').is(':checked'), 'img.depleted_marker');
}
function toggleMeMarkers() {
    toggleMarkers($('input[name=toggle_me]').is(':checked'), 'img.me_marker');
}
function toggleCitizenMarkers() {
    toggleMarkers($('input[name=toggle_citizen]').is(':checked'), 'img.citizen_marker');
}
function toggleDepletedBuildingMarkers() {
    toggleMarkers($('input[name=toggle_depleted_building]').is(':checked'), 'img.depleted_building_marker');
}
function toggleBlueprintAvailableMarkers() {
    toggleMarkers($('input[name=toggle_blueprint_available]').is(':checked'), 'img.blueprint_available_marker');
}
function toggleZombieNumberMarkers() {
    var showZombieNumbers = $('input[name=toggle_zombie_numbers]').is(':checked');
    toggleMarkers(showZombieNumbers, 'p.zombie_marker');
    toggleMarkers(showZombieNumbers, 'p.scout_sense_marker');
}
function toggleVisitedMarkers() {
    toggleMarkers($('input[name=toggle_visited]').is(':checked'), 'img.visited_marker');
}
function toggleMailIcon() {
    toggleMarkers($('input[name=toggle_mail_link]').is(':checked'), '#oval_office_mail');
}
function toggleTagMarkers() {
    toggleMarkers($('input[name=toggle_tags]').is(':checked'), 'div.tag_marker');
}
function showHoverInformation() {
    return $('input[name=toggle_zone_hover]').is(':checked');
}
function toggleMarkers(on, markers) {
    if (on) {
        $(markers).show();
    } else {
        $(markers).hide();
    }
}
function togglePeek() {
    $('#peek').toggle();
    $('#peek_input').select().focus();
}
function toggleZombieObservation() {
    $('#zombie_observation').toggle();
    if ($('#zombie_observation').is(':visible')) {
        $('#zombie_observation input').focus();
    }
}
function toggleScoutSenseObservation() {
    $('#scout_sense_observation').toggle();
    if ($('#scout_sense_observation').is(':visible')) {
        $('#scout_sense_observation input').focus();
    }
}
function toggleCampingTopology() {
    $('#camping_topology').toggle();
    if ($('#camping_topology').is(':visible')) {
        $('#select_camping_topology').focus();
    }
}
function compoundItemKeyToItem(compoundItemKey) {
    var matches = compoundItemKey.match(/^item_([\-0-9]+)([B]?)$/);
    var id = parseInt(matches[1]);
    var broken = matches[2] ? true : false;

    return { d2nItemId: id, amount: 0, broken: broken };
}
function itemToCompoundItemKey(item, addWholeMarker) {
    return item.d2nItemId+(item.broken?"B":(addWholeMarker?"W":""));
}
function removeItemFromDropBox(item) {
    var foundItem = searchItemInDropBox(item);
    foundItem.amount--;
    if (foundItem.amount <= 0) {
        removeItemFromDropBoxArray(item);
    }
    updateDropBox();
}
function addItemToDropBox(item) {
    var foundItem = searchItemInDropBox(item);
    if (foundItem == undefined) {
        // add item to the array
        foundItem = addItemToDropBoxArray(item);
    }
    foundItem.amount++;
    updateDropBox();
}
function searchItemInDropBox(item) {
    for (itemPos = 0; itemPos < itemsToUpdate.length; itemPos++) {
        var currentItem = itemsToUpdate[itemPos];
        if (currentItem.d2nItemId == item.d2nItemId &&
            currentItem.broken == item.broken) {
            return currentItem;
        }
    }
    return null;
}
function addItemToDropBoxArray(item) {
    return itemsToUpdate[itemsToUpdate.length] = item;
}
function removeItemFromDropBoxArray(item) {
    var index = -1;
    for (itemPos = 0; itemPos < itemsToUpdate.length; itemPos++) {
        var currentItem = itemsToUpdate[itemPos];
        if (currentItem.d2nItemId == item.d2nItemId &&
            currentItem.broken == item.broken) {
            index = itemPos;
            break;
        }
    }
    if (index > -1) {
        itemsToUpdate.splice(index, 1);
    }
}
function updateDropBox() {
    generateItemUpdaterToBoxHtml(itemsToUpdate);
    $('#drop_box .item').click(function () {
        removeItemFromDropBox(compoundItemKeyToItem($(this).attr('id')));
    });
}
function enterItemUpdateMode() {
    $('#item_update_box').dialog('option', 'title', 'Update items in zone ' + currentZone.x + '/' + currentZone.y);
    $('#item_update_box').dialog('open');
    $('#item_filter').focus();
    itemsToUpdate = zoneMap[currentZone.x][currentZone.y].items.slice();
    updateDropBox();
}
function repaintMarkers() {
    toggleDepletedMarkers();
    toggleDepletedBuildingMarkers();
    toggleMeMarkers();
    toggleCitizenMarkers();
    toggleBlueprintAvailableMarkers();
    toggleZombieNumberMarkers();
    toggleVisitedMarkers();
    toggleMailIcon();
    toggleTagMarkers();
    toggleConstructionView();
}
function generateSoulPage(distinctions, citizen) {
    var output = "<div id='soul_hover_inner'>";
    output += "<h1>"+citizen.name+"</h1>";
    if (citizen.soulPoints != 0) {
        output += "<h2>Soul score: <b>"+citizen.soulPoints+"</b> points</h2>";
    }
    if (distinctions == undefined || distinctions.length == 0) {
        output += "<h2>No information available on citizen</h2></div>";
        return output;
    }
    output += "<ul>";
    $.each(distinctions, function(distinctionPos, distinction) {
        output += generateDistinction(distinction);
    });
    output += "</ul>";
    output += "</div>";
    return output;
}
function generateDistinction(distinction) {
    var output = "<li class='distinction";
    if (distinction.rare) {
        output += " rare";
    }
    output += "'>";

    output += "<p>"+distinction.amount+"</p>";
    if (distinction.inSprite) {
        output += "<div class='distinction-image distinction-"+distinction.image+"' title='"+distinction.name+"'/>";
    } else {
        output += "<img src='"+iconUrl+distinction.image+".gif' title ='"+distinction.name+"'>";
    }

    output += "</li>";
    return output;
}
function generateZoneInformation(selectedZone, showAction) {
    var zone = zoneMap[selectedZone.x][selectedZone.y];
    var output = "";
    output += generateCoordinatesHtml(selectedZone.x, selectedZone.y);
    output += generateBuildingHtml(zone.building, zone.scoutPeek, zone.buildingDepleted, zone.bluePrintRetrieved, zone.lastUserActions, showAction);
    output += generateCampingTopology(zone.campingTopology, zone.lastUserActions, showAction);
    output += generateDepletionStatus(zone.zoneDepleted, zone.lastUserActions, showAction);
    output += generateVisitationHtml(zone.visited, showAction);
    output += generateTagHtml(zone.tag);
    output += generateCitizenHtml(zone.citizens);
    output += generateZombieHtml(zone.zombies, zone.scoutSense, zone.danger, today, showAction, zone.lastUserActions, zone.discoveryStatus);
    output += generateItemHtml(zone.items, false, showAction);
    output += generateUpdateHtml(zone.lastUserActions);
    output += generateHoverFooter(showAction);
    return output;
}
function selectZoneWithCoordinates(x, y) {
    $("#"+calculateZoneId(x,y)).click();
}
function selectZone(zoneElement) {
    if ($('#information_panel').is(':hidden')) {
        return;
    }
    $('#map .zone').removeClass('selected');
    zoneElement.addClass('selected');
    currentZone = new SelectedZone(zoneElement.attr('id'));
    var selectedTabId = $(".overview_tab_content:visible").attr('id');
    var preferredTab = "";
    if (currentZone.x == 0 && currentZone.y == 0) {
        $("#tab_link_zone").hide();
        preferredTab = "li#tab_link_town";
    } else {
        $("#tab_link_zone").show();
        preferredTab = "li#tab_link_zone";
        $('#information_panel #zone_info').html(generateZoneInformation(currentZone, true));
        $("input#peek_input").autocomplete({ source: uniqueOutsideBuildings, minLength: 2 });
    }
    highlightOverviewTab($(preferredTab));
    repaintMapHeaders(zoneElement);
    hideHoverBox();
}
function repaintMapHeaders(zoneElement) {
    highlightMapHeaders(zoneElement, false);
    highlightMapHeaders(zoneElement, true);
}
function highlightMapHeaders(zoneElement, showHeaders) {
    var hoverZone = new SelectedZone(zoneElement.attr('id'));
    if (showHeaders) {
        $("#map .headerx_"+hoverZone.x).addClass("over");
        $("#map .headery_"+hoverZone.y).addClass("over");
    } else {
        $("#map .headerx").removeClass("over");
        $("#map .headery").removeClass("over");
    }
}
function changeZoneSelection(event) {

    // When the focus is on certain elements, we DO NOT want the map zone to be changed -- it's confusing
    var focusElement = $(document.activeElement).attr('id');
    var focusComponentType = $(document.activeElement).attr('localName');
    if (    focusElement == "search_item_dropdown" ||
            focusElement == "show_range_dropdown" ||
            focusElement == "select_camping_topology" ||
            focusElement == "peek_input" ||
            focusElement == "existing_item_filter" ||
            focusComponentType == "input") {
        return true;
    }

    if (currentZone == undefined) {
        return true;
    }
    if (    $('#information_panel').is(':hidden') ||
            $('#item_update_box').is(':visible')) {
        return true;
    }
    var x = currentZone.x;
    var y = currentZone.y;
    if (event.which >= 37 && event.which <= 40) {
        var zoneChanged = false;
        if (event.which == '37' && x > -xPosTown) {
            x -= 1;
            zoneChanged = true;
        }
        if (event.which == '38' && y < yPosTown) {
            y += 1;
            zoneChanged = true;
        }
        if (event.which == '39' && x < width - xPosTown - 1) {
            x += 1;
            zoneChanged = true;
        }
        if (event.which == '40' && y > yPosTown - height + 1) {
            y -= 1;
            zoneChanged = true;
        }
        if (zoneChanged) {
            $("#"+calculateZoneId(x,y)).click();
        }
        return false;
    }
    return true;
}
function updateMyZone() {
    var zone = updateZone(key);
    var x = zone.x;
    var y = zone.y;
    citizens = zone.citizens;
    zoneMap[x][y] = zone.zoneUpdate;

    var zoneDiv = "#"+calculateZoneId(x,y);

    updateCitizensInZones(chaos, zoneMap, citizens);
    $('#citizen_info').html(generateCitizenOverviewHtml(citizens, true));
    updateSearchItems();

    $(zoneDiv).removeClass();
    for (classPos = 0; classPos < zone.classes.length; classPos++) {
        $(zoneDiv).addClass(zone.classes[classPos]);
    }
    showRange();
    if (currentZone != undefined && x == currentZone.x && y == currentZone.y) {
        $(zoneDiv).click();
    }
    highlightZone(x, y);
}
function moveHoverBox(e, hoverBox) {
    var hover_box = $("#"+hoverBox);
    var offsetXY = 18;
    var x = e.clientX+offsetXY;
    if (hoverBox == 'soul_hover') {
        x = e.clientX-offsetXY-hover_box.width();
    }
    var y = e.clientY+offsetXY;
    if ( y > $(window).height() - hover_box.height() ) {
        y -= hover_box.height()+offsetXY*2;
        if ( y < offsetXY*2) {
            y = $(window).height() / 2 - hover_box.height() / 2;
        }
    }
    hover_box.css( {'left':x , 'top' : y } );
    hoverMoved = true;
}
function hideHoverBox() {
    $("#hover_box").hide();
    hoverMoved = false;
}
function showHoverBox(e, zoneElement) {
    if (!hoverMoved) { moveHoverBox(e, 'hover_box'); }
    var selectedZone = new SelectedZone(zoneElement);
    if (selectedZone.x == 0 && selectedZone.y == 0) {
        return;
    }
    $("#hover_box").html(generateZoneInformation(selectedZone, false));
    $("#hover_box").show();
    hoverMoved = false;
}
function findItemsWithCategory(category) {
    var allZones = [];
    var count = 0;
    $.each(itemSearchIndex, function(itemId, zones) {
        var key = parseCompoundItemKey(itemId);
        var item = itemLookup[key.id];
        if (item.category == category) {
            $.each(zones, function(zonePos, zone) {
                allZones[count++] = zone;
            });
        }
    });
    return allZones;
}
function highlightOverviewTab(selectedTab) {
    highlightTab(selectedTab, "overview");
}
function highlightTownTab(selectedTab) {
    highlightTab(selectedTab, "town");
}
function highlightTab(selectedTab, tabGroup) {
    $("."+tabGroup+"_tab_content").hide();
    $("ul."+tabGroup+"_tabs li").removeClass("active");
    selectedTab.addClass("active");

    var activeTab = selectedTab.find("a").attr("href");
    $(activeTab).show();
    if ($(activeTab).attr("id") == "overview_tab_items") {
        $('#existing_item_filter').focus();
    }
    return false;
}
function deselectSearchItems() {
    $("#item_search .selected").removeClass('selected');
    selectedSearchItems = {};
    highlightFoundItems();
}
function highlightFoundElement(itemId) {
    var zones;
    if (itemId.indexOf("CAT_") == 0) {
        zones = findItemsWithCategory(itemId.substring(4));
    } else {
        zones = itemSearchIndex[itemId];
    }
    if (zones != undefined) {
        $.each(zones, function(pos, zone) {
            $("#"+calculateZoneId(zone.x, zone.y)).addClass('contains_item');
        });
    }
}
function highlightFoundItems() {
    $(".zone").removeClass('contains_item');
    $("#item_search .selected").each(function(pos, element) {
        highlightFoundElement($(element).attr('id'));
    });
}
function selectForSearching(searchElement) {
    if (searchElement.hasClass('selected')) {
        searchElement.removeClass('selected');
        delete selectedSearchItems[searchElement.attr('id')];
    } else {
        searchElement.addClass('selected');
        selectedSearchItems[searchElement.attr('id')] = {};
    }
}
function showRange() {
    var range = $('#show_range_dropdown').val();
    var matches = range.match(/^([a-z_]+)#?([0-9]*)$/);
    var rangeType = matches[1];
    var rangeDistance = 0;
    if (matches[2] != "") {
        rangeDistance = parseInt(matches[2]);
    }

    // Remove all existing range range markers
    $(".zone").removeClass('range_border_top');
    $(".zone").removeClass('range_border_bottom');
    $(".zone").removeClass('range_border_left');
    $(".zone").removeClass('range_border_right');

    // Determine the range marker for a zone
    $.each(zoneMap, function(x, column) {
        $.each(column, function(y, zone) {
            zone.range = "out";
            switch (rangeType) {
                case "ap_range" :
                    zone.range = apFromHome(x,y) <= rangeDistance ? "in" : "out";
                    break;
                case "kilometer_range" :
                    zone.range = kmFromHome(x,y) <= rangeDistance ? "in" : "out";
                    break;
                case "wind_directions" :
                    zone.range = getWindDirection(x,y);
                    break;
            }
        });
    });

    // Color the borders of the affected zones -- just check for different range labels, that's all
    $.each(zoneMap, function(x, column) {
        $.each(column, function(y, zone) {
            setRangeBorderForZone(x, y, zone, getNeighbouringZone(zoneMap,x,y-1), 'range_border_bottom');
            setRangeBorderForZone(x, y, zone, getNeighbouringZone(zoneMap,x-1,y), 'range_border_left');
        });
    });
}
function getWindDirection(x, y) {
    if (x == 0 && y == 0) return "city";
    if (x > Math.floor(y/2) && y > Math.floor(x/2)) return "ne";
    if (x > Math.floor(-y/2) && -y > Math.floor(x/2)) return "se";
    if (-x > Math.floor(y/2) && y > Math.floor(-x/2)) return "nw";
    if (-x > Math.floor(-y/2) && -y > Math.floor(-x/2)) return "sw";
    if (Math.abs(x) > Math.abs(y)) return (x > 0) ? "e" : "w";
    return (y > 0) ? "n" : "s";
}
function getNeighbouringZone(zoneMap, x, y) {
    return zoneMap.hasOwnProperty(x) ? zoneMap[x][y] : undefined;
}
function setRangeBorderForZone(x, y, thisZone, otherZone, classToSet) {
    if (otherZone == undefined) {
        return;
    }
    if (thisZone.range != otherZone.range) {
        $("#"+calculateZoneId(x, y)).addClass(classToSet);
    }
}
function readFromCookies() {
    readCookieCheckbox('toggle_me', true);
    readCookieCheckbox('toggle_citizen', true);
    readCookieCheckbox('toggle_depleted', true);
    readCookieCheckbox('toggle_depleted_building', true);
    readCookieCheckbox('toggle_blueprint_available', true);
    readCookieCheckbox('toggle_zombie_numbers', false);
    readCookieCheckbox('toggle_visited', false);
    readCookieCheckbox('toggle_zone_hover', true);
    readCookieCheckbox('toggle_mail_link', true);
    readCookieDropdown('show_range_dropdown', 'none');
}
function storeCookie(cookieName, value) {
    $.cookie(cookieName, value, { expires: 365 });
}
function readCookie(cookieName) {
    return $.cookie(cookieName);
}
function readCookieDropdown(dropdown, defaultValue) {
    var dropdownValue = readCookie(dropdown);
    $('#'+dropdown).val(dropdownValue == null ? defaultValue : dropdownValue);
}
function readCookieCheckbox(checkBox, defaultValue) {
    var checkBoxValue = readCookie(checkBox);
    $('input[name='+checkBox+']').attr('checked', (checkBoxValue != null ? ( checkBoxValue == "true" ) : defaultValue));
}
function storeCookieCheckbox(checkBox) {
    storeCookie(checkBox, $('input[name='+checkBox+']').is(':checked'));
}
function generateConstructionTabs(insideBuildings) {
    $('#construction_info_constructed').html(generateConstructionInfoHtml(insideBuildings, [ "NOT_AVAILABLE", "AVAILABLE" ]));
    $('#construction_info_available').html(generateConstructionInfoHtml(insideBuildings, [ "NOT_AVAILABLE" ]));
    $('#construction_info_all').html(generateConstructionInfoHtml(insideBuildings, null));
}
function showSoulPage(e, citizenId) {
    $('#soul_hover_content').html(generateSoulPage(distinctions[citizenId], getCitizen(citizenId)));
    moveHoverBox(e, 'soul_hover');
    $("#soul_hover").show();
}
function extractOutsideBuildings(zoneMap) {
    var buildings = [];
    $.each(zoneMap, function(x, column) {
        $.each(column, function(y, zone) {
            if (zone.building != undefined) {
                buildings[buildings.length] = {
                    "name" : zone.building.name,
                    "x" : x,
                    "y" : y,
                    "type" : zone.building.type,
                    "dig" : zone.building.dig,
                    "flavor" : zone.building.flavor,
                    "url" : zone.building.url,
                    "depleted" : zone.buildingDepleted,
                    "bluePrintRetrieved" : zone.bluePrintRetrieved,
                    "peek" : zone.scoutPeek,
                    "peeker" : getPeeker(zone.lastUserActions)
                };
            }
        });
    });
    outsideBuildings = buildings.sort(sortBuildingsByName);
}
function sortBuildingsByName(a, b) {
    return a["name"] < b["name"] ? -1 : (a["name"] > b["name"] ? 1 : 0);
}
function updateBuildingList() {
    extractOutsideBuildings(zoneMap);
    $('#various_info').html(generateVariousInfoHtml(citizens));
}
function highlightZone(x, y) {
    var zoneDiv = "#"+calculateZoneId(x, y);
    $(zoneDiv).fadeTo('slow', 0.05);
    $(zoneDiv).fadeTo('slow', 1.00);
}
function highlightZoneFromId(classValue) {
    var zone = new SelectedZone(classValue);
    highlightZone(zone.x, zone.y);
}
function toggleShowingBuildingSpecifics() {
    showConstructionInfo = !showConstructionInfo;
    if (showConstructionInfo) {
        $('.construction-info-group').show();
    } else {
        $('.construction-info-group').hide();
    }
}
function checkMail() {
    $.ajax({
        type: "GET",
        url: "/mail",
        data: "key="+key,
        async: true,
        success: function (json) {
            mailAlert = JSON.parse(json);
            $("#oval_office_reactivate").hide();
            $("#oval_office_link").show();
            if (mailAlert.messages > 0 || mailAlert.invitations > 0) {
                $("#mail_icon").attr("src", "images/mail_alert.gif");
                $("#oval_office_link").attr("title", "You have "+mailAlert.messages+" messages and "+mailAlert.invitations+" invitations waiting for you in Oval Office. Click here to go to Oval Office");
            } else {
                $("#mail_icon").attr("src", "images/mail.png");
                $("#oval_office_link").attr("title", "Go to Oval Office to check your mail");
            }
            $(function(){
                var count = 3500; // Valid for 60 minutes, take a bit off
                countdown = setInterval(function() {
                    if (count == 0) {
                        $("#oval_office_link").hide();
                        $("#oval_office_reactivate").show();
                        $("#mail_expired").attr("src", "images/mail-expired.png");
                        $("#oval_office_reactivate").attr("title", "Your Oval Office key has expired. Click to reactivate");
                    }
                    count--;
                }, 1000);
            });
            $("#oval_office_link").attr("href", mailAlert.oourl);
        },
        error: function (xhr, error) {
            $("#oval_office_link").hide();
            $("#oval_office_reactivate").show();
            $("#mail_expired").attr("src", "images/mail-error.png");
            $("#oval_office_reactivate").attr("title", "Error: "+JSON.parse(xhr.responseText).errorMessage+". Click to try again.");
        }
    });
}

$(document).ready(function () {

    $('#item_update_box').dialog({
        autoOpen: false,
        resizable: true,
        modal: true,
        width: 800,
        buttons: {
            "Save": function () {
                $(this).dialog("close");
                saveItems();
            },
            "Cancel": function () {
                $(this).dialog("close");
            }
        }
    });

    $('#citizen_overview_table .name').live({
        mouseover: function(e) {
            showSoulPage(e, $(this).attr('id'));
        },
        mouseout: function() {
            $("#soul_hover").hide();
        }
    });

    $('#map .zone').live({
        click: function() {
            selectZone($(this));
        },
        mouseover: function(e) {
            highlightMapHeaders($(this), true);
            if (showHoverInformation()) { showHoverBox(e, $(this).attr('id')); }
        },
        mouseout: function() {
            highlightMapHeaders($(this), false);
            if (showHoverInformation()) { hideHoverBox(); }
        },
        mousemove: function(e) {
            if (showHoverInformation()) { moveHoverBox(e, 'hover_box'); }
        }
    });

    $('#citizen_overview_table .coordinates span').live({
        mouseover: function(e) {
            highlightZoneFromId($(this).attr('class'));
        }
    });

    $('.building_outside span').live({
        mouseover: function(e) {
            highlightZoneFromId($(this).attr('class'));
        }
    });

    $('#item_search .category, .item').live({
        click : function () {
            selectForSearching($(this));
            highlightFoundItems();
        }
    });

    generateItemUpdater();
    $('#unique_items .item').click(function () {
        addItemToDropBox(compoundItemKeyToItem($(this).attr('id')));
        $('#item_filter').focus();
    });

    $(document).keydown(function (event) {
        if ($('#ruins_map').is(':hidden')) {
            return changeZoneSelection(event);
        }
    });

    $('#update_button button').click(function () {
        updateMyZone();
    });

    $('#save_items_button button').click(function () {
        saveItems();
    });

    $('#add_item_from_dropdown').click(function () {
        addItemToDropBox(compoundItemKeyToItem($('#item_dropdown option:selected').val()));
        $('#item_dropdown').focus();
    });

    $('#item_filter').live('keyup', function () {
        filterItemsOnSnippet($(this).val());
    });

    $('#existing_item_filter').live('keyup', function () {
        filterExistingItemsOnSnippet($(this).val());
    });

    $('#toggle_depleted').click(function () {
        storeCookieCheckbox($(this).attr('id'));
        toggleDepletedMarkers();
    });

    $('#toggle_depleted_building').click(function () {
        storeCookieCheckbox($(this).attr('id'));
        toggleDepletedBuildingMarkers();
    });

    $('#toggle_me').click(function () {
        storeCookieCheckbox($(this).attr('id'));
        toggleMeMarkers();
    });

    $('#toggle_citizen').click(function () {
        storeCookieCheckbox($(this).attr('id'));
        toggleCitizenMarkers();
    });

    $('#toggle_blueprint_available').click(function () {
        storeCookieCheckbox($(this).attr('id'));
        toggleBlueprintAvailableMarkers();
    });

    $('#toggle_zombie_numbers').click(function () {
        storeCookieCheckbox($(this).attr('id'));
        toggleZombieNumberMarkers();
    });

    $('#toggle_visited').click(function () {
        storeCookieCheckbox($(this).attr('id'));
        toggleVisitedMarkers();
    });

    $('#toggle_zone_hover').click(function () {
        storeCookieCheckbox($(this).attr('id'));
    });

    $('#toggle_mail_link').click(function () {
        storeCookieCheckbox($(this).attr('id'));
        toggleMailIcon();
    });

    $('#toggle_tags').click(function () {
        storeCookieCheckbox($(this).attr('id'));
        toggleTagMarkers();
    });


    $('#show_range_dropdown').live('change', function () {
        storeCookie("show_range_dropdown", $(this).val());
        showRange();
    });

    $('#select_camping_topology').live('change', function () {
        showSpoilerCampingTopology($(this).val());
    });

    $("ul.overview_tabs li").click(function() {
        highlightOverviewTab($(this));
        return false;
    });

    $("ul.town_tabs li").click(function() {
        highlightTownTab($(this));
        return false;
    });

    $("#town_tab_constructions .toggle_resources").click(function() {
        toggleShowingBuildingSpecifics();
    });

    $("input[name=construction_view]:radio").change(function() {
        toggleConstructionView();
    });

    $("#oval_office_reactivate").click(function() {
        checkMail();
        return false;
    });

    // Default page load actions
    highlightOverviewTab($("li#tab_link_town"));
    highlightTownTab($("li#tab_link_bank"));
    $('#bank_info').html(generateBankHtml(bank, "bank", true, false, null, false));
    $('#citizen_info').html(generateCitizenOverviewHtml(citizens, true));
    updateBuildingList();
    generateConstructionTabs(insideBuildings);
    $("#"+calculateZoneId(0,0)).click();
    updateCitizensInZones(chaos, zoneMap, citizens, activePlayer);
    updateSearchItems();
    readFromCookies();
    repaintMarkers();
    showRange();
    checkMail();
    // "2012-02-18T18:10:05";
//    var d = Date.parse(gameClock);
//    alert(new Date(d));
});
