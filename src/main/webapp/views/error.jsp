<%@ page contentType="text/html; charset=UTF-8" session="false" %>

<!DOCTYPE html>
<html>
	<head>
        <title>From Dusk Till Dawn - Error Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

        <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" href="styles/map-viewer.css"/>
	</head>
	<body>
        <h1>From Dusk Till Dawn > Error</h1>
        <p>${errorCode}: ${errorMessage}</p>

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
