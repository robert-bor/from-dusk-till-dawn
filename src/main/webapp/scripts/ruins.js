"use strict";

/*global $ */

var ruins = {};
var currentRoom;
var ruinDimensions;

var ruinDirections = {
    "NORTH" : {
        "inverse" : "SOUTH",
        "x": 0,
        "y": 1
    },
    "EAST" : {
        "inverse" : "WEST",
        "x": 1,
        "y": 0
    },
    "SOUTH" : {
        "inverse" : "NORTH",
        "x": 0,
        "y": -1
    },
    "WEST" : {
        "inverse" : "EAST",
        "x": -1,
        "y": 0
    }
};

var keys = {
    "UNKNOWN" : {
        "inSprite": false,
        "name" : "Unknown key",
        "image": "question-mark.png"
    },
    "MAGNETIC_KEY" : {
        "inSprite": true,
        "name" : "Magnetic key",
        "image": "magneticKey"
    },
    "BUMP_KEY" : {
        "inSprite": true,
        "name" : "Bump key",
        "image": "bumpKey"
    },
    "BOTTLE_OPENER" : {
        "inSprite": true,
        "name" : "Bottle opener",
        "image": "classicKey"
    }
};

function IgnoreEntity(thisX, thisY, thatX, thatY) {
    this.type = thatX == undefined || thatY == undefined ? "ROOM" : "CORRIDOR";
    this.thisX = thisX;
    this.thisY = thisY;
    this.thatX = thatX;
    this.thatY = thatY;

    this.ignore = function(currentRoom, otherRoom) {
        if (this.type == "ROOM") {
            return otherRoom.x == this.thisX && otherRoom.y == this.thisY;
        } else {
            return ((currentRoom.x == this.thisX && currentRoom.y == this.thisY && otherRoom.x == this.thatX && otherRoom.y == this.thatY) ||
                    (currentRoom.x == this.thatX && currentRoom.y == this.thatY && otherRoom.x == this.thisX && otherRoom.y == this.thisY));
        }
    };
}
function RoomWithCoordinates(id) {
    this.id=id;
    var matches = this.id.match(/^room_([\-0-9]+)_([\-0-9]+)$/);
    this.x = parseInt(matches[1], 10);
    this.y = parseInt(matches[2], 10);
}
function getRoomId(x,y) {
    return "room_"+x+"_"+y;
}
function RuinDimensions(ruinsMap) {
    var x, y, xKey, yKey;

    for (xKey in ruinsMap) {
        x = parseInt(xKey, 10);
        if (this.minX == undefined || x < this.minX) {
            this.minX = x;
        }
        if (this.maxX == undefined || x > this.maxX) {
            this.maxX = x;
        }

        for (yKey in ruinsMap[x]) {
            y = parseInt(yKey, 10);
            if (this.minY == undefined || y < this.minY) {
                this.minY = y;
            }
            if (this.maxY == undefined || y > this.maxY) {
                this.maxY = y;
            }
        }
    }
    this.minX--;
    this.maxX++;
    this.minY--;
    this.maxY++;
}
function getRoom(x,y) {
    return ruins.ruin[x] == undefined ? null : ruins.ruin[x][y];
}
function getRoomType(room, x, y) {
    if (room == undefined) {
        if (neighboursHaveCorridors(x,y)) {
            return "unknown_room"
        } else {
            return "no_room"
        }
    } else {
        return "known_room";
    }
}
function neighbourHasCorridor(x, y, direction) {
    var neighbouringRoom = getRoom(x + ruinDirections[direction].x, y + ruinDirections[direction].y);
    return neighbouringRoom == undefined ? false : neighbouringRoom.corridors[ruinDirections[direction].inverse];
}
function neighboursHaveCorridors(x, y) {
    var corridors_leading_to_room = false;
    for (var direction in ruinDirections) {
        corridors_leading_to_room |= neighbourHasCorridor(x, y, direction);
    }
    return corridors_leading_to_room;
}
function generateCorridorOnMap(room, direction) {
    var output = "";
    if (room.corridors[direction]) {
        output += "<div id='corridor_"+direction+"_"+room.x+"_"+room.y+"' class='corridor_"+direction+"'></div>";
    }
    return output;
}
function generateRoomOnMap(x, y) {
    var room = getRoom(x,y);
    var output = "";
    output += "<li id='room_"+x+"_"+y+"' class='room_space";
    output += " "+getRoomType(room, x, y);
    output += "'>";
    if (room != undefined) {
        room.dependencies = [];
        for (var direction in ruinDirections) {
            output += generateCorridorOnMap(room, direction);
        }
        output += "<div id='room_"+x+"_"+y+"' class='room";
        if (room.zombies > 0) {
            output += " danger";
        }
        output += "'></div>";
        if (roomIsEntrance(room)) {
            output += "<p class='symbol' title='Contains the entrance/exit'>E</p>"
        } else if (room.door == "LOCKED") {
            output += "<p class='symbol' title='Contains a locked door'>L</p>";
        } else if (room.door == "UNLOCKED") {
            output += "<p class='symbol' title='Contains an unlocked door'>U</p>";
        }
    }
    output += "</li>";
    return output;
}
function generateMap() {
    ruinDimensions = new RuinDimensions(ruins.ruin);
    var output = "";
    output += generateHorizontalRuinHeader();
    for (var y = ruinDimensions.maxY; y >= ruinDimensions.minY; y--) {
        output += "<ul class='room_row'>";
        output += "<div class='headery headery_"+y+"'><p class='coordinates'>"+y+"</p></div>";
        for (var x = ruinDimensions.minX; x <= ruinDimensions.maxX; x++) {
            output += generateRoomOnMap(x,y);
        }
        output += "<div class='headery headery_"+y+"'><p class='coordinates'>"+y+"</p></div>";
        output += "</ul>";
    }
    output += generateHorizontalRuinHeader();
//    output += generateLegend();
    return output;
}
//function generateLegend() {
//    var output = "";
//    output += "<ul class='room_row'>";
//    output += "<li class='room_space known_room'><div class='room'></div>=known room</li>";
//    output += "<li class='room_space known_room'><div class='room'></div><p class='symbol'>U</p>=contains unlocked door</li>";
//    output += "</ul>";
//    return output;
//}
function generateHorizontalRuinHeader() {
    var output = "";
    output += "<ul class='room_row'>";
    output += "<div class='corner'></div>";
    for (var pos = ruinDimensions.minX; pos <= ruinDimensions.maxX; pos++) {
        output += "<div class='headerx headerx_"+pos+"'><p class='coordinates'>"+pos+"</p></div>";
    }
    output += "<div class='corner'></div>";
    output += "</ul>";
    return output;
}
function selectRoom(roomElement) {
    if (roomElement.hasClass('no_room')) {
        return;
    }
    $('#ruins_map .room_space').removeClass('selected');
    currentRoom = new RoomWithCoordinates(roomElement.attr('id'));
    var selectedRoom = $('#'+getRoomId(currentRoom.x,currentRoom.y));
    selectedRoom.addClass('selected');
    $('#room_header').html("<h2>Coordinates "+currentRoom.x+"/"+currentRoom.y+"</h2>");
    $('#room_detail').html(generateRoomDetails(selectedRoom, currentRoom.x, currentRoom.y));
    $('#room_actions').html(generateRoomActions(selectedRoom, currentRoom.x, currentRoom.y));
}
function getNeighbouringRoom(x, y, direction) {
    return getRoom(x + ruinDirections[direction].x, y + ruinDirections[direction].y);
}
function paintCorridor(currentRoom, direction) {
    var output = "";
    var neighbouringRoom = getNeighbouringRoom(currentRoom.x, currentRoom.y, direction);
    if (!currentRoom.corridors[direction]) {
        output += "<img class='add add_"+direction+"' src='images/add-icon.png' title='Add a corridor to the "+direction.toLowerCase()+"'/>";
    } else {
        output += "<div class='corridor_box corridor_"+direction+"'></div>";
        if (corridorMayBeRemoved(currentRoom, neighbouringRoom)) {
            output += "<img class='delete delete_"+direction+"' src='images/delete-icon.png' title='Delete this corridor from the "+direction.toLowerCase()+"'/>";
        }
    }
    return output;
}
function getImageElement(keyType) {
    return keys[keyType].inSprite ?
        "<div class='item-image item-"+keys[keyType].image+"' title='"+keys[keyType].name+"'/>" :
        "<div><img src='images/"+keys[keyType].image+"' title='"+keys[keyType].name+"'/></div>";
}
function generateRoomActions(selectedRoomElement, x, y) {
    if (selectedRoomElement.hasClass('unknown_room')) {
        return "";
    }
    var output = "";
    var selectedRoom = getRoom(x,y);

    if (selectedRoom.door == "NONE" && !roomIsEntrance(selectedRoom)) {
        output += "<div class='block'>";
        output += "<div class='description'>Add a <a class='add set_door_locked'>Locked</a> or <a class='add set_door_unlocked'>Unlocked</a> door</div>";
        output += "</div>";
    }

    if (selectedRoom.zombies == 0) {
        output += "<div class='block'>";
        output += "<div class='description'>Set zombies to <a class='add set_zombies_one'>1</a>, <a class='add set_zombies_two'>2</a>, <a class='add set_zombies_three'>3</a> or <a class='add set_zombies_four'>4</a>";
        output += "</div>";
    }

    if (selectedRoom.door == "LOCKED") {
        var key = selectedRoom.key;
        output += "<div class='block'>";
        if (key == "UNKNOWN") {
            output += "<div class='description'>It is not known which key fits the lock</div>";
        } else {
            output += getImageElement(key);
            output += "<div class='description'>Unlock with a <b>"+keys[key].name+"</b></div>";
        }
        output += "</div>";

        output += "<div class='block'>";
        output += "<div>Change the key to:</div>";
        for (var keyText in keys) {
            if (key != keyText) {
                output += getImageElement(keyText);
                output += "<div class='description'><a class='add set_key_"+keyText+"'>"+keys[keyText].name+"</a></div>";
            }
        }
        output += "</div>";
    }
    return output;
}
function generateRoomDetails(selectedRoomElement, x, y) {
    var output = "";
    var selectedRoom = getRoom(x,y);
    if (selectedRoomElement.hasClass('unknown_room')) {
        output += "<div class='room_box faded'></div>";
        output += "<img class='add add_room' src='images/add-icon.png' title='Add this tile to the map'/>";
    } else if (selectedRoomElement.hasClass('known_room')) {
        output += "<div class='room_box'>";
        if (roomIsEntrance(selectedRoom)) {
            output += "<img class='entrance' src='images/door-to-outside.jpg' title='Entrance to the ruin'/>";
        } else if (selectedRoom.door == "LOCKED") {
            output += "<img class='add locked_door' src='images/locked-door.png' title='Locked door. Click to unlock'/>";
            output += "<img class='delete delete_door' src='images/delete-icon.png' title='Remove this door from the room'/>";
        } else if (selectedRoom.door == "UNLOCKED") {
            output += "<img class='add unlocked_door' src='images/unlocked-door.png' title='Unlocked door. Click to lock'/>";
            output += "<img class='delete delete_door' src='images/delete-icon.png' title='Remove this door from the room'/>";
        }
        if (selectedRoom.zombies > 0) {
            output += generateZombieDetails(selectedRoom);
        }
        output += "</div>";
        for (var direction in ruinDirections) {
            output += paintCorridor(selectedRoom, direction);
        }
        if (!roomIsEntrance(selectedRoom) && !hasDependentNeighbours(selectedRoom)) {
            output += "<img class='delete delete_room' src='images/delete-icon.png' title='Remove this tile from the map'/>";
        }
    }
    return output;
}
function generateZombieDetails(room) {
    var output = "";
    output += "<img class='zombie' src='images/zombie-large.png' title='Zombies have been detected here. Bring weapons.'/>";
    output += "<img class='delete delete_zombies' src='images/delete-icon.png' title='Remove the zombies from this room'/>";
    if (room.zombies > 1) {
        output += "<div class='zombie_number' title='Number of zombies spotted in the room'>";
        output += "<p>"+room.zombies+"</p>";
        output += "</div>";
    }
    return output;
}
function changeRoomSelection(event) {
    if ($('#ruins_map').is(':hidden')) {
        return true;
    }
    var x = currentRoom.x;
    var y = currentRoom.y;
    if (event.which == 27) { // escape
        closeRuinExplorer();
    }
    if (event.which >= 37 && event.which <= 40) {
        var roomChanged = false;
        if (event.which == '37' && x > ruinDimensions.minX) {
            x -= 1;
            roomChanged = true;
        }
        if (event.which == '38' && y < ruinDimensions.maxY) {
            y += 1;
            roomChanged = true;
        }
        if (event.which == '39' && x < ruinDimensions.maxX) {
            x += 1;
            roomChanged = true;
        }
        if (event.which == '40' && y > ruinDimensions.minY) {
            y -= 1;
            roomChanged = true;
        }
        if (roomChanged) {
            $("#"+getRoomId(x,y)).click();
        }
        return false;
    }
    return true;
}
function setZombies(x, y, zombies) {
    if (!executeRuinCommand('SET_ZOMBIES', x, y, zombies)) {
        return;
    }
    var currentRoom = getRoom(x,y);
    currentRoom.zombies = zombies;
    generateMapAndSelectRoom(x,y);
    successRuinMessage();
}
function setDoor(x, y, doorType) {
    if (!executeRuinCommand('SET_DOOR', x, y, doorType)) {
        return;
    }
    var currentRoom = getRoom(x,y);
    currentRoom.door = doorType;
    generateMapAndSelectRoom(x,y);
    successRuinMessage();
}
function setKey(x, y, keyType) {
    if (!executeRuinCommand('SET_KEY', x, y, keyType)) {
        return;
    }
    var currentRoom = getRoom(x,y);
    currentRoom.key = keyType;
    $("#"+getRoomId(x,y)).click();
    successRuinMessage();
}
function addRoom(x, y) {
    if (!executeRuinCommand('ADD_ROOM', x, y)) {
        return;
    }
    if (ruins.ruin[currentRoom.x] == undefined) {
        ruins.ruin[currentRoom.x] = {};
    }
    var newRoom = ruins.ruin[currentRoom.x][currentRoom.y] = {
        "corridors": {
            "WEST":  false,
            "NORTH": false,
            "EAST":  false,
            "SOUTH": false
        },
        "door": "NONE",
        "key": "UNKNOWN",
        "zombies": 0,
        "x" : x,
        "y" : y
    };
    for (var direction in ruinDirections) {
        connectCorridor(newRoom, direction);
    }
    generateMapAndSelectRoom(x, y);
    successRuinMessage();
}
function connectCorridor(currentRoom, direction) {
    var neighbouringRoom = getNeighbouringRoom(currentRoom.x, currentRoom.y, direction);
    if (neighbouringRoom != undefined) {
        setCorridors(currentRoom, direction, true);
    }
}
function addCorridor(x, y, direction) {
    if (!executeRuinCommand('ADD_CORRIDOR', x, y, direction)) {
        return;
    }
    setCorridors(getRoom(x,y), direction, true);
    generateMapAndSelectRoom(x, y);
    successRuinMessage();
}
function deleteCorridor(x, y, direction) {
    if (!executeRuinCommand('DELETE_CORRIDOR', x, y, direction)) {
        return;
    }
    setCorridors(getRoom(x,y), direction, false);
    generateMapAndSelectRoom(x, y);
    successRuinMessage();
}
function setCorridors(room, direction, addCorridor) {
    room.corridors[direction] = addCorridor;
    var neighbouringRoom = getNeighbouringRoom(room.x, room.y, direction);
    if (neighbouringRoom != undefined) {
        neighbouringRoom.corridors[ruinDirections[direction].inverse] = addCorridor;
    }
}
function deleteRoom(x, y) {
    if (!executeRuinCommand('DELETE_ROOM', x, y)) {
        return;
    }
    delete ruins.ruin[x][y];
    var yColumnEmpty = true;
    for (var currentY = ruinDimensions.maxY; currentY >= ruinDimensions.minY; currentY--) {
        if (getRoom(x, currentY) != undefined) {
            yColumnEmpty = false;
        }
    }
    if (yColumnEmpty) {
        delete ruins.ruin[x];
    }
    generateMapAndSelectRoom(x, y);
    successRuinMessage();
}
function generateMapAndSelectRoom(x, y) {
    $('#ruins_map').html(generateMap());
    var mapWidth = (Math.abs(ruinDimensions.minX-ruinDimensions.maxX)+3)*20+1;
    $('#ruins_map').css( {'width': mapWidth } );
    var viewerWidth = mapWidth + 250;
    var mapHeight = (Math.abs(ruinDimensions.minY-ruinDimensions.maxY)+3)*20+30;
    if (mapHeight < 420) {
        mapHeight = 420;
    }
    var windowHeight = $(window).height();
    var topMargin = mapHeight < windowHeight ? (windowHeight - mapHeight) / 2 : 10;
    $('#ruins_explorer').css( {'width': viewerWidth, 'height': mapHeight, 'margin-left': -viewerWidth/2, 'top': topMargin } );
    $("#"+getRoomId(x,y)).click();
}
function highlightRuinHeaders(zoneElement, showHeaders) {
    var hoverZone = new RoomWithCoordinates(zoneElement.attr('id'));
    if (showHeaders) {
        $("#ruins_map .headerx_"+hoverZone.x).addClass("over");
        $("#ruins_map .headery_"+hoverZone.y).addClass("over");
    } else {
        $("#ruins_map .headerx").removeClass("over");
        $("#ruins_map .headery").removeClass("over");
    }
}
function roomIsEntrance(room) {
    return room == undefined ? false : room.x == 0 && room.y == 0;
}
function corridorMayBeRemoved(currentRoom, neighbouringRoom) {
    if (neighbouringRoom == undefined) {
        return true;
    }
    var ignoreEntity = new IgnoreEntity(currentRoom.x, currentRoom.y, neighbouringRoom.x, neighbouringRoom.y);
    return canTraceRouteToEntrance(currentRoom, ignoreEntity) &&
           canTraceRouteToEntrance(neighbouringRoom, ignoreEntity);
}
function hasDependentNeighbours(room) {
    var hasDependentNeighbours = false;
    for (var direction in ruinDirections) {
        var currentRoom = getNeighbouringRoom(room.x, room.y, direction);
        hasDependentNeighbours |= !canTraceRouteToEntrance(currentRoom, new IgnoreEntity(room.x, room.y, null, null));
    }
    return hasDependentNeighbours;
}
function canTraceRouteToEntrance(room, ignoreEntity) {
    if (room == undefined || roomIsEntrance(room)) {
        return true;
    }
    // Reset all rooms
    resetAllRooms();

    var roomsToVisit = [];
    addRoomsToVisit(roomsToVisit, room, ignoreEntity);

    while (roomsToVisit.length != 0) {
        var newRoomsToVisit = [];
        for (var roomPos in roomsToVisit) {
            room = roomsToVisit[roomPos];
            // Found the entrance -- all clear!
            if (roomIsEntrance(room)) {
                return true;
            }
            addRoomsToVisit(newRoomsToVisit, room, ignoreEntity);
            room.alreadyVisited = true;
        }
        roomsToVisit = newRoomsToVisit;
    }
    return false;
}
function addRoomsToVisit(roomsToVisit, currentRoom, ignoreEntity) {
    for (var currentDirection in ruinDirections) {
        if (!currentRoom.corridors[currentDirection]) {
            continue;
        }
        var roomToVisit = getNeighbouringRoom(currentRoom.x, currentRoom.y, currentDirection);
        if (roomToVisit == undefined || ignoreEntity.ignore(currentRoom, roomToVisit) || roomToVisit.alreadyVisited) {
            continue;
        }
        roomsToVisit[roomsToVisit.length] = roomToVisit;
    }
}
function resetAllRooms() {
    for (var y = ruinDimensions.maxY; y >= ruinDimensions.minY; y--) {
        for (var x = ruinDimensions.minX; x <= ruinDimensions.maxX; x++) {
            var roomToReset = getRoom(x, y);
            if (roomToReset == undefined) {
                continue;
            }
            roomToReset.alreadyVisited = false;
        }
    }
}
function executeRuinCommand(action, xRoom, yRoom, extraParameter) {

    var argument;
    switch (action) {
        case "ADD_CORRIDOR":
        case "DELETE_CORRIDOR":
            argument = "&direction="+extraParameter;
            break;
        case "SET_DOOR":
            argument = "&door="+extraParameter;
            break;
        case "SET_KEY":
            argument = "&door_key="+extraParameter;
            break;
        case "SET_ZOMBIES":
            argument = "&zombies="+extraParameter;
            break;
    }

    var success = false;
    $.ajax({
        type: "POST",
        url: "/ruin",
        data:
            "key="+key+"&"+
            "action="+action+"&"+
            "city="+ruins.cityId+"&"+
            "x_zone="+ruins.x+"&"+
            "y_zone="+ruins.y+"&"+
            "x_room="+xRoom+"&"+
            "y_room="+yRoom+
            (argument == undefined ? "" : argument),
        async: false,
        success: function (json) {
            success = true;
        },
        error: function (xhr, error) {
            errorRuinMessage(JSON.parse(xhr.responseText).errorMessage);
        }
    });
    return success;
}
function successRuinMessage() {
    $('#ruins_map .corner').addClass('success').delay(3000).queue(function(next) {
        $(this).removeClass('success');
        next();
    });
    $('#ruin_update_message').removeClass().addClass('success').text("Update successful").show(500);
    $('#ruin_update_message').delay(3000).fadeOut('slow');
}
function errorRuinMessage(errorMessage) {
    $('#ruins_map .corner').addClass('error').delay(3000).queue(function(next) {
        $(this).removeClass('error');
        next();
    });
    $('#ruin_update_message').removeClass().addClass('error').text(errorMessage).show(500);
    $('#ruin_update_message').delay(3000).fadeOut('slow');
}

function openRuinMap(location, cityId, x, y) {
    $.ajax({
        type: "GET",
        url: "/ruin",
        data: "key="+key+"&city="+cityId+"&x="+x+"&y="+y,
        async: false,
        success: function (json) {
            ruins = JSON.parse(json);
            generateMapAndSelectRoom(0, 0);
            $('#ruins_location').html("<h1>"+location+" @ "+ruins.x+" / "+ruins.y+"</h1>");
            $('#overlay').show();
            $('#ruins_explorer').show();
        },
        error: function (xhr, error) {
            errorMessage(JSON.parse(xhr.responseText).errorMessage);
        }
    });
}
function closeRuinExplorer() {
    $('#overlay').hide();
    $('#ruins_explorer').hide();
}

$(document).ready(function () {

    $('#ruins_map .room_space').live({
        click: function() {
            selectRoom($(this));
        },
        mouseover: function(e) {
            highlightRuinHeaders($(this), true);
        },
        mouseout: function() {
            highlightRuinHeaders($(this), false);
        }
    });

    $('#room_detail .delete').live({
        click: function(e) {
            if ($(this).hasClass('delete_room')) {
                deleteRoom(currentRoom.x, currentRoom.y);
            } else if ($(this).hasClass('delete_door')) {
                setDoor(currentRoom.x, currentRoom.y, 'NONE');
            } else if ($(this).hasClass('delete_zombies')) {
                setZombies(currentRoom.x, currentRoom.y, 0);
            }

            for (var direction in ruinDirections) {
                if ($(this).hasClass('delete_'+direction)) {
                    deleteCorridor(currentRoom.x, currentRoom.y, direction);
                }
            }

            e.preventDefault();
        }
    });

    $('#room_detail .add').live({
        click: function(e) {
            if ($(this).hasClass('add_room')) {
                addRoom(currentRoom.x, currentRoom.y);
            } else if ($(this).hasClass('unlocked_door')) {
                setDoor(currentRoom.x, currentRoom.y, 'LOCKED');
            } else if ($(this).hasClass('locked_door')) {
                setDoor(currentRoom.x, currentRoom.y, 'UNLOCKED');
            }

            for (var direction in ruinDirections) {
                if ($(this).hasClass('add_'+direction)) {
                    addCorridor(currentRoom.x, currentRoom.y, direction);
                }
            }
            e.preventDefault();
        }
    });

    $('#room_actions .add').live({
        click: function(e) {
            if ($(this).hasClass('set_door_locked')) {
                setDoor(currentRoom.x, currentRoom.y, 'LOCKED');
            } else if ($(this).hasClass('set_door_unlocked')) {
                setDoor(currentRoom.x, currentRoom.y, 'UNLOCKED');
            } else if ($(this).hasClass('set_zombies_one')) {
                setZombies(currentRoom.x, currentRoom.y, 1);
            } else if ($(this).hasClass('set_zombies_two')) {
                setZombies(currentRoom.x, currentRoom.y, 2);
            } else if ($(this).hasClass('set_zombies_three')) {
                setZombies(currentRoom.x, currentRoom.y, 3);
            } else if ($(this).hasClass('set_zombies_four')) {
                setZombies(currentRoom.x, currentRoom.y, 4);
            }

            for (var keyType in keys) {
                if ($(this).hasClass('set_key_'+keyType)) {
                    setKey(currentRoom.x, currentRoom.y, keyType);
                }
            }
            e.preventDefault();
        }
    });

    $('#overlay').click(function(e) {
        closeRuinExplorer();
    });

    $('#ruins_explorer .close').live({
        click: function(e) {
            closeRuinExplorer();
        }
    });

    $(document).keydown(function (event) {
        return changeRoomSelection(event);
    });
});
