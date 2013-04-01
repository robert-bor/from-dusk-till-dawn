var $leaderboard = {};

$leaderboard.uniqueDistinctions = {};
$leaderboard.uniqueDistinctionsById = {};
$leaderboard.top100 = {};

$leaderboard.readDistinctions = function() {
    $.ajax({
        type: "GET",
        url: "/leaderboard/unique_distinctions",
        data: "",
        async: false,
        success: function (json) {
            $leaderboard.uniqueDistinctions = JSON.parse(json);
        },
        error: function (xhr, error) {
            errorMessage(JSON.parse(xhr.responseText).errorMessage);
        }
    });
};

$leaderboard.readTop100 = function(distinctionId) {
    if (distinctionId == undefined) {
        return;
    }
    $.ajax({
        type: "GET",
        url: "/leaderboard/top_100",
        data: "distinction_id="+distinctionId,
        async: false,
        success: function (json) {
            $leaderboard.top100 = JSON.parse(json);
        },
        error: function (xhr, error) {
            errorMessage(JSON.parse(xhr.responseText).errorMessage);
        }
    });
};

$leaderboard.setupDistinctionIndex = function() {
    $.each($leaderboard.uniqueDistinctions, function(distinctionPos, distinction) {
        $leaderboard.uniqueDistinctionsById[distinction.id] = distinction;
    });
};

$leaderboard.generateDistinctionPanel = function() {
    var output = "";
    output += "<ul>";
    $.each($leaderboard.uniqueDistinctions, function(distinctionPos, distinction) {
        output += $leaderboard.generateDistinction(distinction);
    });
    output += "</ul>";
    return output;
};

$leaderboard.generateDistinction = function(distinction) {
    var output = "<li class='distinction";
    if (distinction.rare) {
        output += " rare";
    }
    output += "' data-distinction='"+distinction.id+"'>";
    output += $leaderboard.generateDistinctionImage(distinction);
    output += "</li>";
    return output;
};

$leaderboard.generateDistinctionImage = function(distinction) {
    var output = "";
    if (distinction.inSprite) {
        output += "<div class='distinction-image distinction-"+distinction.image+"' title='"+distinction.name+"'/>";
    } else {
        output += "<img src='"+iconUrl+distinction.image+".gif' title ='"+distinction.name+"'>";
    }
    return output;
}

$leaderboard.createLeaderboard = function() {
//    $('#leaderboard').html(generateMap());
    var windowHeight = $(window).height();
    var boardWidth = 855;
    var boardHeight = 520;
    var topMargin = boardHeight < windowHeight ? (windowHeight - boardHeight) / 2 : 10;
    $('#leaderboard').css( {'width': boardWidth, 'height': boardHeight, 'margin-left': -boardWidth / 2 - 23, 'top': topMargin } );
};

$leaderboard.getTop100ForDistinction = function(selectedDistinction) {

    var distinction = $leaderboard.uniqueDistinctionsById[selectedDistinction.data('distinction')]
    $('#top-100-for-distinction').html('Top 100 for '+distinction.name);
    // Empty the current columns
    var column = 0;
    for (; column < 5; column++) {
        $('#top_100_col_'+column).html("");
    }
    column = 0;

    $('#selected-distinction-image').html($leaderboard.generateDistinctionImage(distinction));
    $leaderboard.readTop100(distinction.id);

    var output = "";
    var count = 0;
    var previousAmount = -1;
    var rank = 0;

    for (var citizenPos = 0; citizenPos < $leaderboard.top100.length; citizenPos++) {
        if (count % 20 == 0) {
            output += "<div class='position bold'>#</div>";
            output += "<div class='name bold'>Name</div>";
            output += "<div class='amount bold'>Amt</div>";
        }
        var citizen = $leaderboard.top100[citizenPos];
        var highlight = "";
        if (citizen.userGameId == activePlayerId) {
            highlight = " highlight";
        }
        rank++;
        if (citizen.amount == previousAmount) {
            output += "<div class='position'>|</div>";
        } else {
            output += "<div class='position'>"+rank+"</div>";
            previousAmount = citizen.amount;
        }
        output += "<div class='name"+highlight+"'>"+citizen.userName+"</div>";
        output += "<div class='amount"+highlight+"'>"+citizen.amount+"</div>";
        count++;
        if (count % 20 == 0 || count == $leaderboard.top100.length) {
            $('#top_100_col_'+column++).html(output);
            output = "";
        }
    }
};

$leaderboard.openLeaderboard = function() {
    $('#overlay').show();
    $('#leaderboard').show();
};

$leaderboard.closeLeaderboard = function() {
    $('#overlay').hide();
    $('#leaderboard').hide();
};

$leaderboard.handleKeys = function(event) {
    if ($('#leaderboard').is(':hidden')) {
        return true;
    }
    if (event.which == 27) { // escape
        $leaderboard.closeLeaderboard();
    }
};

$(document).ready(function () {

    $('#distinction_panel li.distinction').live({
        click: function() {
            $leaderboard.getTop100ForDistinction($(this));
        }
    });

    $('#leaderboard .close').live({
        click: function(e) {
            $leaderboard.closeLeaderboard();
        }
    });

    $('#overlay').click(function(e) {
        $leaderboard.closeLeaderboard();
    });

    $(document).keydown(function (event) {
        return $leaderboard.handleKeys(event);
    });

    $leaderboard.readDistinctions();
    $leaderboard.setupDistinctionIndex();
    $('#distinction_panel').html($leaderboard.generateDistinctionPanel());
    $leaderboard.createLeaderboard();
});
