<%@ page contentType="text/html; charset=UTF-8" session="false" %>

<!DOCTYPE html>
<html>
	<head>
        <title>From Dusk Till Dawn - Signin</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

        <script type="text/javascript" src="scripts/jquery-1.6.4.min.js"></script>
        <script type="text/javascript" src="scripts/galleria/galleria-1.2.5.min.js"></script>
        <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" href="styles/map-viewer.css"/>
        <link rel="stylesheet" type="text/css" href="styles/duskdawn-site.css"/>
	</head>
	<body>
        <div id="duskdawn-site">

            <div class="signin">
                <form name="input" action="map" method="post">
                    <p>Key: <input type="text" name="key" size="40" title="You must use the Die2Nite key here" value=""/></p>
                    <p><input type="submit" value="Log in" /></p>
                </form>
            </div>

            <div class="content">

                <h1>From Dusk Till Dawn</h1>

                <p>Welcome to From Dusk Till Dawn, an unaffiliated companion site to <a href="http://www.die2nite.com/">Die2Nite</a></p>
                
                <h2>Die2Nite</h2>

                <p>The game <a href="http://www.die2nite.com/">Die2Nite</a> is nothing short of spectacular in its genre.
                You take on the role of a resident in a post-apocalyptic town surrounded by a huge starving horde of
                undead, hungering for your flesh. Death is all but certain, the only thing you can do is delay it as long as
                possible. But beware, not everyone is as civic as you are and some are definitely set to gain even if it
                costs you.</p>

                <p>Every day the horde grows stronger and every day the danger grows. Prepare to embark on courageous
                journeys, killing zombies in a dance of death, explore long-abandoned ruins, organize the community
                to defend itself, or just plain put yourself at number one. Die2Nite is a social game where death happens
                over and over again. Only the soul remains with your most remarkable accomplishments imbued for all to
                see.</p>

                <p align="center"><a href="http://www.die2nite.com?ref=BerZerg"><img src="images/landing-page/welcome-to-d2n.jpg"/></a></p>
                <p align="center">Sign up here if you like what you see!</p>

                <h2>What this site does</h2>
                
                <p>This site is a companion site to Die2Nite. Players can keep track of data which they cannot maintain
                in the game itself. It helps players to organize what they have found in the World Beyond and it keeps
                players in touch with what is happening around them.</p>

                <p>For a really excellent guide on using this site, be sure to read this
                <a href="http://annetteannette.net/die2nite/almanac/dusktilldawn.html">walkthrough</a>.</p>

                <p>Want to know more? Check out the <a href="#tour">Tour</a> below.</p>

                <h2 id="faq">FAQ</h2>
                <ul class="question-list">
                    <li><a href="#activate_duskdawn">How do I enable <i>From Dusk Till Dawn</i> for my Die2Nite account?</a></li>
                    <li><a href="#start_duskdawn">How do I start <i>From Dusk Till Dawn</i>?</a></li>
                    <li><a href="#first_steps">Great, I'm in! Now what?</a></li>
                    <li><a href="#plugins">What's with the Firefox/Chrome plugins?</a></li>
                    <li><a href="#d2n_cache">I have whacked lotsa zeds, but the app doesn't show it - why?!?!</a></li>
                    <li><a href="#lag">Why does the site seem a bit slower around attack time?</a></li>
                    <li><a href="#wiki">Does <i>From Dusk Till Dawn</i> work with a Die2Nite wiki?</a></li>
                    <li><a href="#new_functionality">Are you still developing new functionality?</a></li>
                    <li><a href="#how_it_works">How does <i>From Dusk Till Dawn</i> work?</a></li>
                    <li><a href="#dd_site">What does your site run on?</a></li>
                    <li><a href="#why_do_this">BerZerg, are you mad? Why do you do this?</a></li>
                </ul>

                <div id="activate_duskdawn" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;How do I enable <i>From Dusk Till Dawn</i> for my Die2Nite account?</p>
                </div>
                <div class="answer">
                    <p>You will have to go through the following steps:</p>
                    <ul>
                        <li>Step 1: Go to the <b>Your Soul</b> tab</li>
                        <li>Step 2: Go to the <b>Settings</b> tab</li>
                        <li>Step 3: Check <b>Authorise external applications</b></li>
                        <li>Step 4: Click <b>Save my settings</b></li>
                    </ul>
                    <img class="answer" width="500" src="images/landing-page/enable-duskdawn.png">
                </div>

                <div id="start_duskdawn" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;How do I start <i>From Dusk Till Dawn</i>?</p>
                </div>
                <div class="answer">
                    <p>First make sure that <i>From Dusk Till Dawn</i> has been <a href="#activate_duskdawn">activated</a> for your
                        user account in Die2Nite.
                    </p>
                    <p>Then choose the little computer symbol in the top-left corner. This is known as <b>The Directory</b>. Here you
                        choose <i>From Dusk Till Dawn</i>.</p>
                    <img class="answer" src="images/landing-page/choose-dd.png">
                    <p>Die2Nite will ask you to confirm your choice. Select <b>Proceed to : From Dusk Till Dawn</b></p>
                    <img class="answer" src="images/landing-page/confirm-dd.png">
                </div>

                <div id="first_steps" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;Great, I'm in! Now what?</i>?</p>
                </div>
                <div class="answer">
                    <p>The <b>Update My Zone</b> button is your friend. A good <i>best practice</i> is to press it once on entering
                        a new zone and once again, just before exiting. It takes a bit of practice, but this will get you the best
                        long-term results with regards to quality of data.</p>
                </div>

                <div id="plugins" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;What's with the Firefox/Chrome plugins?</p>
                </div>
                <div class="answer">
                    <p>From the Die2Nite community has come the initiative to deal with the fact that a number of different Map
                        tools were running at the same time, but not sharing the same master data source. The result is that
                        a town would have to preferably use one tool, or risk difficult coordination of activities. To deal
                        with this, two plugins have been created (one for Firefox and one for Chrome) which automatically
                        update all four major map tools simultaneously.</p>
                    <p>The <a href="https://addons.mozilla.org/en-US/firefox/addon/die2nite-agent/">Firefox Plugin</a> has
                        been written by <b>isaaclw</b>.</p>
                    <p>The <a href="https://chrome.google.com/webstore/detail/mcbnodoolliadkflmgoebfepeehmelnj">Chrome Plugin</a> has
                        been written by <b>simast</b>.</p>
                    <p>Note that the plugins only take care of the automatic updating of the information that is returned by
                        the Die2Nite XML feed. The plugins do not take into account the specifics for a map application, such
                        as scout peeks and available buildings. Neither do the plugins allow a more fine-grained control of
                        the data in a zone. That said, the plugins are an excellent way for those with little time and/or those
                        in towns with various map apps in use, to keep tabs on their own ventures.</p>
                </div>

                <div id="d2n_cache" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;I have whacked lotsa zeds, but the app doesn't show it - why?!?!</p>
                </div>
                <div class="answer">
                    <p>The Die2Nite server has to deal with a lot of load, so the people from Motion Twin have decided to
                        implement the sensible policy to work with caches on their server's side. The consequence of this
                        policy is that information gets refreshed approximately every minute. So to answer your question, wait
                        a bit and refresh your browser window to get the latest information.
                    </p>
                </div>

                <div id="lag" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;Why does the site seem a bit slower around attack time?</p>
                </div>
                <div class="answer">
                    <p>One hour before and one hour after the attack is the busiest time for Die2Nite. Most things happen
                        during this period and also the American and European continents are both active around these
                        times. Busy, busy, busy. Also for the server.</p>
                    <p>In the past these traffic jam hours have caused significant performance issues for the site. After the
                        hardware upgrade, the performance issues have not reoccured. That said, during these hours the site
                        can be slightly slower than at other times.</p>
                </div>

                <div id="wiki" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;Does <i>From Dusk Till Dawn</i> work with a Die2Nite wiki?</p>
                </div>
                <div class="answer">
                    <p>Yes, not just *A* wiki, but *THE* <a href="http://die2nitewiki.com/wiki/Main_Page">Die2Nite wiki</a>. The
                        players contributing to this wiki aim for perfection and <i>From Dusk Till Dawn</i> gratefully makes use
                        of that by adding links for an in-depth explanation on Die2Nite objects.</p>
                </div>

                <div id="new_functionality" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;Are you still developing new functionality?</p>
                </div>
                <div class="answer">
                    <p>Yes. It may not always move with the speed that you would like, but, hey, that's a hobby project for you :)
                        There is a thread in the world forum, called <i>From Dusk Till Dawn, aka Duskdawn</i>, where new functionality
                        is proposed and discussed. Be sure to chip in there, if you want something.</p>
                </div>

                <div id="how_it_works" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;How does <i>From Dusk Till Dawn</i> work?</p>
                </div>
                <div class="answer">
                    <p><i>From Dusk Till Dawn</i> only works because Motion Twin (the creators of Die2Nite) have enabled their
                        application for external applications.</p>

                    <p>Whenever the application is loaded, or whenever a zone is updated (throught the <b>Update My Zone</b> action),
                    From Dusk Till Dawn places a call to the Die2Nite server to retrieve the latest information. This information is
                    then read and interpreted and used to update the map information.</p>

                    <p>Although <i>From Dusk Till Dawn</i> can read from its own caches, for the longer term, it is dependent
                        on the Die2Nite server for its operation. <b>It cannot exist without access to the Die2Nite server.</b></p>
                </div>

                <div id="dd_site" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;What does your site run on?</p>
                </div>
                <div class="answer">
                    <p>The site runs on a MySQL database. The database is shielded with a Java backend, unlocked by a REST
                        (Amazon-style) interface. The backend stack consists of Spring and JPA/Hibernate and various supporting
                        frameworks. The frontend is pure HTML/CSS/JavaScript (mainly JQuery).</p>
                    <p>The platform of the site is run on a VPS by <a href="http://www.xlshosting.nl/">XLS Hosting</a>, a
                        dutch Cloud party with a very seductive VPS offer. The server is equipped with 1 GB internal memory, 2
                        Cores (4.4 GHz), 150 GB bandwidth and a 10 GB harddisk. Currently, about 25% of the harddisk is being
                        utilized. The database already contains millions of records.</p>
                </div>

                <div id="why_do_this" class="question">
                    <p><img class="question_mark" src="images/landing-page/question-mark.png"/>&nbsp;BerZerg, are you mad? Why do you do this?</p>
                </div>
                <div class="answer">
                    <p>Technically speaking, I'm not mad. My reasons for running this site are to keep my programming skills
                        up-to-date with something tangible, something people actually use. I have learned a lot about running
                        a site under considerable load and all the performance tuning that requires. Also, this is a good
                        chance to get some hands-on frontend (HTML, CSS, JavaScript/JQuery) knowledge and a refresher
                        for some backend technologies (Java, Spring, JPA/Hibernate, ehcache, Maven, GSon, Log4J, Mysql, HSQLDB,
                        Quartz, SLF4J, JUnit, AspectJ). In my daytime job I program too little, so I need this.</p>
                    <p>Furthermore, I love to create something that has value for people. Seeing how this site is used and
                        how much it is appreciated is its own reward. Thanks for the trust and encouragement, y'all!</p>
                </div>

                <h2 id="tour">A Little Tour</h2>
                <p>The slideslow below gives an idea of what the application can mean for you as a Die2Nite player.</p>
                <div id="gallery">
                    <img src="images/landing-page/map.png" title="The Game Map" alt="This is the central view - the area around your town, also known as The World Beyond. You can use mouse and cursor keys to move around."/>
                    <img src="images/landing-page/zone-hover.png" title="Hover over a zone" alt="When you hover over a zone, you will get the detailed view of what is there."/>
                    <img src="images/landing-page/zone-info.png" title="Select a zone" alt="After selecting a zone, you get the same detailed view, but now with options to change data."/>
                    <img src="images/landing-page/bank.png" title="The Bank" alt="Your town has a communal repository, called The Bank. This is where you deposit your finds and extract others that you need. Keeping tabs on the contents of the Bank helps in the coordination of the scavenge effort for more items."/>
                    <img src="images/landing-page/buildings-in-twb.png" title="Buildings in the World Beyond" alt="The World Beyond holds many buildings once inhabited by human beings. These buildings hold treasures useful for boosting the town's longevity. With this view, you can coordinate the town's efforts to explore the ruins."/>
                    <img src="images/landing-page/citizens.png" title="Citizens" alt="Citizens can be inside or outside of town, they can be active or inactive. Check here to see where they are and how active they have been."/>
                    <img src="images/landing-page/citizen-profile.png" title="Soul" alt="It is very useful to know what other citizens have done -- do they have a 'good' soul, or do they have a tendency to go for themselves? You can quickly scan your fellow citizens."/>
                    <img src="images/landing-page/constructions.png" title="Constructions" alt="Your town can build various buildings with all kinds of benefits. The construction view helps you coordinate what needs to be focused on next."/>
                    <img src="images/landing-page/looking-for-batteries.png" title="Search items" alt="Together with other citizens you build up a database of items you have found. The item search helps you to find what you need, so you can send out someone to fetch the item."/>
                    <img src="images/landing-page/various.png" title="Town information" alt="The zombie horde attacks every night! The town needs to prepare for this nightly horror and still deal with other facts of life as well -- such as a worsening water crisis!"/>
                    <img src="images/landing-page/options.png" title="Options" alt="Players have their own preferences of what they like to see in their map tool. In the options tab, a player can finetune their view the way they want it to be. The settings are maintained in browser cookies."/>
                    <img src="images/landing-page/exploring-ruins.png" title="Ruins" alt="Ruins can be mapped out using the Ruin Explorer. With the arrow keys and by adding rooms and corridors, one can keep track of what has been explored, where the bad guys are and what rooms are locked or unlocked."/>
                </div>

                <h2>Creator</h2>
                <p>This application has been created by <b><a href="mailto:berzerg.d2n@gmail.com">BerZerg</a></b> with help from
                the D2N Community. Special thanks are extended to SinSniper (Oval Office) for his prizeless help. The site is dedicated
                to Rulesy, the founder of the First map app for the english Die2Nite -- a big thank you.</p>

                <hr class="footer"/>
            </div>

        </div>

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

        <script>
            Galleria.loadTheme('scripts/galleria/galleria.classic.js');
            $("#gallery").galleria({
                width: 640,
                height: 500
            });
        </script>


	</body>
</html>
