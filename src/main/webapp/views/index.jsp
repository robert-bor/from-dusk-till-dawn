<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html>
<html>
	<head>
        <title>From Dusk Till Dawn - Map</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

        <style type="text/css">${canvasWidth}</style>

        <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" href="styles/map-viewer.css?version=7"/>
        <link rel="stylesheet" type="text/css" href="styles/leaderboard.css"/>
        <link rel="stylesheet" type="text/css" href="styles/ruins.css"/>
        <link rel="stylesheet" type="text/css" href="styles/building-image.css"/>
        <link rel="stylesheet" type="text/css" href="styles/job-image.css?version=3"/>
        <link rel="stylesheet" type="text/css" href="styles/item-image.css"/>
        <link rel="stylesheet" type="text/css" href="styles/distinction-image.css?version=2"/>
        <link rel="stylesheet" type="text/css" href="styles/tag-image.css"/>
        <link rel="stylesheet" type="text/css" href="styles/ui.css"/>

        <script type="text/javascript" src="scripts/jquery-1.6.4.min.js"></script>
        <script type="text/javascript" src="scripts/jquery-ui-1.8.16.min.js"></script>
        <script type="text/javascript" src="scripts/jquery.cookie.js"></script>
        <script type="text/javascript" src="scripts/duskdawn.js?version=13"></script>
        <script type="text/javascript" src="scripts/ruins.js?version=1"></script>
        <script type="text/javascript" src="scripts/leaderboard.js"></script>
	</head>
	<body>

     <div class="app">
        <h1><a href="http://d2n.duskdawn.net/">From Dusk Till Dawn</a> > ${cityName} > Day ${today} | ${activePlayer} |
            <a id="leaderboard_link" class="leaderboard_link">Leaderboard</a>
        </h1>

        <div class="box">
            <div id="map" class="page">
                ${map}
            </div>

            <div id="information_panel" class="page">


<c:if test="${insecure}">
                <div id="secure_access">
                    <p>No secure access: access app via D2N</p>
                </div>
</c:if>
<c:if test="${stale}">
                <div id="stale">
                    <p>Showing cached information</p>
                </div>
</c:if>

<%--<c:if test="${insecure}!=true">--%>
                <div id="update_button">
                    <button>Update My Zone</button>
                </div>
<%--</c:if>--%>
                <div id="update_feedback">
                    <div id="update_message" style="display: none;"></div>

                    <div id="waiting" style="display: none;">
                        <div id="waiting_box">
                            <img src="images/ajax-loader.gif" title="Loader" alt="Loader" class="float_left"/>
                            <p>Please wait</p>
                        </div>
                    </div>
                </div>

                <ul class="overview_tabs">
                    <li id="tab_link_town"><a href="#overview_tab_town">Town <img src="images/town.png"/></a></li>
                    <li id="tab_link_zone" style="display: none;"><a href="#overview_tab_zone">Zone</a></li>
                    <li id="tab_link_item"><a href="#overview_tab_items">Items</a></li>
                    <li id="tab_link_options"><a href="#overview_tab_options">Options</a></li>
                </ul>

                <div class="overview_tab_container">
                    <div id="overview_tab_town" class="overview_tab_content">
                        <ul class="town_tabs">
                            <li id="tab_link_bank"><a href="#town_tab_bank">Bank</a></li>
                            <li id="tab_link_constructions"><a href="#town_tab_constructions">Constructions</a></li>
                            <li id="tab_link_citizens"><a href="#town_tab_citizens">Citizens</a></li>
                            <li id="tab_link_various"><a href="#town_tab_various">Various</a></li>
                        </ul>

                        <div class="town_tab_container">
                            <div id="town_tab_bank" class="town_tab_content">
                                <div id="bank_info" class="zone_info" style="position: relative;">
                                </div>
                            </div>
                            <div id="town_tab_constructions" class="town_tab_content" style="display: none;">
                                <p>
                                    <input type="radio" name="construction_view" value="constructed_buildings" checked="checked">&nbsp;Constructed
                                    <input type="radio" name="construction_view" value="available_buildings">&nbsp;Available
                                    <input type="radio" name="construction_view" value="all_buildings">&nbsp;All Buildings
                                    <img class="toggle_resources" src="images/bloody-eye.png" title="Toggle the showing of building specifics"/>
                                </p>
                                <div id="construction_info_constructed" class="zone_info construction_info" style="position: relative;">
                                </div>
                                <div id="construction_info_available" class="zone_info construction_info" style="display: none;">
                                </div>
                                <div id="construction_info_all" class="zone_info construction_info" style="display: none;">
                                </div>
                            </div>
                            <div id="town_tab_citizens" class="town_tab_content" style="display: none;">
                                <div id="citizen_info" class="zone_info" style="position: relative;">
                                </div>
                            </div>
                            <div id="town_tab_various" class="town_tab_content" style="display: none;">
                                <div id="various_info" class="zone_info" style="position: relative;">
                                </div>
                            </div>
                        </div>

                    </div>
                    <div id="overview_tab_zone" class="overview_tab_content" style="display: none;">
                        <div id="zone_info" class="zone_info" style="position: relative;">
                        </div>
                    </div>
                    <div id="overview_tab_items" class="overview_tab_content" style="display: none;">
                        <p class="enter_item">Search for item: <input id="existing_item_filter" type="text" size="28"></p>
                        <div id="item_search" class="zone_info" style="position: relative;">
                        </div>
                    </div>
                    <div id="overview_tab_options" class="overview_tab_content" style="display: none;">
                        <div id="map_options">
                            <p>Mark ranges</p>
                            <select id="show_range_dropdown">
                                <option value="none" selected>&mdash; None &mdash;</option>
                                <option value="wind_directions">Wind Directions</option>
                                <option value="kilometer_range#3">Watchtower Level 1 (3 Kilometer range)</option>
                                <option value="kilometer_range#6">Watchtower Level 2 (6 Kilometer range)</option>
                                <option value="kilometer_range#10">Watchtower Level 3 (10 Kilometer range)</option>
                                <option value="kilometer_range#2">Hero Rescue (2 Kilometer range)</option>
                                <option value="kilometer_range#11">Heroic Return (11 Kilometer range)</option>
                                <option value="kilometer_range#5">Distant expedition range (6 - 17 Kilometer range)</option>
                                <option value="kilometer_range#17">Expert expedition range (18+ Kilometer range)</option>
                                <option value="kilometer_range#9">Camping for blueprints: 9- km: uncommon / 10+ km: rare</option>
                                <option value="kilometer_range#1">Watchtower Lvl 4 (1 Kilometer teleport home)</option>
                                <option value="kilometer_range#2">Watchtower Lvl 5 (2 Kilometer teleport home)</option>
                                <option value="ap_range#6">6 AP range (typically food or water)</option>
                                <option value="ap_range#9">9 AP range (typically food and water)</option>
                                <option value="ap_range#12">12 AP range (typically food, water and drugs or alcohol)</option>
                                <option value="ap_range#15">15 AP range (typically all standard refreshers)</option>
                            </select>
                            <p><input type="checkbox" id="toggle_me" name="toggle_me" checked/>&nbsp;<img src="images/h_human.gif"/>&nbsp;Show your position</p>
                            <p><input type="checkbox" id="toggle_citizen" name="toggle_citizen" checked/>&nbsp;<img src="images/citizen.png"/>&nbsp;Show citizen positions</p>
                            <p><input type="checkbox" id="toggle_depleted" name="toggle_depleted" checked/>&nbsp;<img src="images/depleted.png"/>&nbsp;Show zone depletion</p>
                            <p><input type="checkbox" id="toggle_depleted_building" name="toggle_depleted_building" checked/>&nbsp;<img src="images/depleted_building.png"/>&nbsp;Show building depletion</p>
                            <p><input type="checkbox" id="toggle_blueprint_available" name="toggle_blueprint_available" checked/>&nbsp;<img src="images/blueprint_available.png"/>&nbsp;Show blueprints available</p>
                            <p><input type="checkbox" id="toggle_zombie_numbers" name="toggle_zombie_numbers"/>&nbsp;<img src="images/h_zombie.gif"/>&nbsp;Show zombie numbers</p>
                            <p><input type="checkbox" id="toggle_visited" name="toggle_visited" checked/>&nbsp;<img src="images/visited.png"/>&nbsp;Show zone visitation</p>
                            <p><input type="checkbox" id="toggle_zone_hover" name="toggle_zone_hover" checked/>&nbsp;<img src="images/mouse_pointer.png"/>&nbsp;Show zone hover</p>
                            <p><input type="checkbox" id="toggle_tags" name="toggle_tags"/>&nbsp;<img src="images/h_bag.gif"/>&nbsp;Show tag markers</p>
                        </div>
                    </div>
                </div>

            </div>

        </div>

        <div id="footer">
            <p align="center"><i>This site is dedicated to <b>Rulesy</b>, who showed the way. Created by <a href="mailto:berzerg.d2n@gmail.com"><b>BerZerg</b></a></i> with help from the <span title="especially Sinsniper!">community</span> and input from <a href='http://d2nwiki.com'>d2nwiki.com</a>.</p>
            <p align="center">Check this <a href="http://annetteannette.net/die2nite/almanac/dusktilldawn.html">guide</a>. Most images and data used on this website are © <a href="http://www.motion-twin.com/english">Motion-Twin</a>. This website is NOT affiliated with <a href="http://www.die2nite.com/">Die2Nite</a>.</p>
        </div>
    </div>

    <div id="item_update_box" style="display: none;">
        <div id="item_update_inner_box">
            <div id="item_filter_box">
                <select id="item_dropdown">
                </select>
                <button id="add_item_from_dropdown" type="button">Add item</button>
                Filter on item name: <input id="item_filter" type="text" size="25"/>
            </div>
            <div>
                <div id="unique_items">
                </div>
                <div id="arrow_box">
                    <img src="images/arrow-right.png">
                </div>
                <div id="drop_box">
                </div>
            </div>
        </div>
    </div>

    <div id="hover_box" style="display: none;">
    </div>

     <div id="soul_hover" style="display: none;">
         <div id="soul_hover_content"></div>
         <div id="soul_hover_footer">
             <img src="images/distinction_footer.gif"/>
         </div>
     </div>

     <div id="overlay" style="display: none;"></div>
     <div id="ruins_explorer" style="display: none;">
         <div id="ruins_location"></div>
         <div id="ruins_map"></div>
         <div id="room_information">
             <div id="ruin_update_status">
                 <div id="ruin_update_message"></div>
             </div>
             <div id="room_header"></div>
             <div id="room_detail"></div>
             <div id="room_actions"></div>
         </div>
         <a class='close' href='#'>Close</a>
     </div>

     <div id="leaderboard" style="display: none;">
     <%--<div id="leaderboard">--%>
         <div id="leaderboard_header">
             <h1>Leaderboard</h1>
         </div>
         <div id="distinction_panel"></div>
         <div id="top_100">
             <hr/>
             <div class="wrapper">
                 <div class="element text"><h2 id="top-100-for-distinction"></h2></div>
                 <div class="element" id="selected-distinction-image"></div>
             </div>
             <div class="column_wrapper">
                 <div id="top_100_col_0" class="column"></div>
                 <div id="top_100_col_1" class="column"></div>
                 <div id="top_100_col_2" class="column"></div>
                 <div id="top_100_col_3" class="column"></div>
                 <div id="top_100_col_4" class="column"></div>
             </div>
         </div>
         <a class='close' href='#'>Close</a>
     </div>


     <script type="text/javascript">

var iconUrl = "${iconUrl}";
var zoneMap = ${mapZones}
var bank = ${bank}
var categories = ${categories}
var uniqueItems = ${uniqueItems}
var uniqueOutsideBuildings = ${uniqueOutsideBuildings}
var itemLookup = ${itemLookup}
var distinctions = ${distinctions}
var citizens = ${citizens}
var insideBuildings = ${insideBuildings}
var estimations = ${estimations}
var city = ${city}
var upgrades = ${upgrades}
var dangerLevels = ${dangerLevels}
var campingTopologies = ${campingTopologies}
var width = ${width};
var height = ${height};
var xPosTown = ${xPosTown};
var yPosTown = ${yPosTown};
var key = "${key}";
var today = ${today};
var cityId = ${cityId};
var activePlayer = "${activePlayer}";
var activePlayerId = "${activePlayerId}";
var upgradedMap = ${upgradedMap};
var chaos = ${chaos};
var gameClock = "${gameClock}";
var currentZone;
var itemsToUpdate;
var itemSearchIndex;
var hoverMoved = false;
var showConstructionInfo = true;
var selectedSearchItems = {};
var constructionLines = [];
var outsideBuildings = [];

        </script>

        <!-- Google Analytics -->
        <script type="text/javascript">

    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-24863655-1']);
    _gaq.push(['_trackPageview']);

    (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();

        </script>

	</body>
</html>
