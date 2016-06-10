<%-- 
    Document   : find
    Created on : 16.07.2014, 13:34:26
    Author     : Florian Thiery M.Sc.
	Author     : i3mainz - Institute for Spatial Information and Surveying Technology
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>

	<head>
		<title>Open Balloon Map | Balloon gefunden</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="shortcut icon" href="../img/favicons.png" type="image/png" />
		<link rel="icon" href="../img/favicons.png" type="image/png" />
		<link rel="stylesheet" href="../css/style.css"/>
		<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/themes/redmond/jquery-ui.css"/>
		<link rel="stylesheet" href="http://tompi.github.io/jeoquery/jeoquery.css" />
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/jquery-ui.min.js"></script>
		<script src="../js/jeoquery.js"></script>
		<script src="../js/cryptmail.js"></script>
		<script>
			var init = function() {
				jeoquery.defaultData.userName = 'tompi';
				$("#location").jeoCityAutoComplete({callback: setCity});
				$("#timestamp").datepicker({dateFormat: "yy-mm-dd"});
			};
			var setCity = function(city) {
				$("#lon").val(city.lng);
				$("#lat").val(city.lat);
			};
			var validate = function() {
				if ($("#nr").val() && $("#location").val() && $("#lat").val() && $("#timestamp").val())
					return true;
				alert("Bitte alle drei Felder mit korrekten Werten belegen!");
				return false;
			};
		</script>
		<script>
			var OPENBALLOON_URI = "";
		</script>
	</head>

	<body onload="init()">

		<div class="content">
			<div class="top_block header">
				<div class="link2"><a class="header" href="http://i3mainz.hs-mainz.de/impressum" target="_blank">Impressum</a></div>
				<div class="link1"><a class="header" href="http://i3mainz.hs-mainz.de/de/projekte/openballoonmap" target="_blank">About</a></div>
			</div>
			<div class="logos">
				<div class="logo"><a href="http://i3mainz.hs-mainz.de" target="_blank"></a></div>
				<div class="obm">Open Balloon Map</div>
			</div>
			<form accept-charset="utf-8" action="/AddFind" method="POST" onsubmit="return validate()">
				<input name="lon" id="lon" type="hidden" />
				<input name="lat" id="lat" type="hidden" />
				<b>Balloon gefunden?</b><br /><br />
				Balloon-Code<br />
				<input class="center" name="nr" id="nr" /> <br />
				Wo wurde der <br />
				Balloon gefunden? <br />
				<input class="left" name="location" id="location" /> <br />
				Wann war das? <br />
				<input class="center" name="timestamp" id="timestamp" /> <br />
				<input type="submit" value="Balloon-Fund mitteilen" />
			</form>
			<form class="info">
				<b>Probleme?</b><br /><br />
				Kontaktieren Sie uns per <a href="javascript:mailto('gmpsjbo/uijfszAgi.nbjo%7B/ef')">Email</a><br />
			</form>
			<form class="info">
				<b>Ort nicht gefunden?</b><br /><br />
				Tragen Sie bitte einen gr&ouml;&szlig;eren Ort in der N&auml;he ein.<br />
			</form>
		</div>
		<!-- Piwik -->
		<script type="text/javascript">
			var _paq = _paq || [];
			_paq.push(['trackPageView']);
			_paq.push(['enableLinkTracking']);
			(function() {
				var u = "//piwik.hs-mainz.de/";
				_paq.push(['setTrackerUrl', u + 'piwik.php']);
				_paq.push(['setSiteId', 5]);
				var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
				g.type = 'text/javascript';
				g.async = true;
				g.defer = true;
				g.src = u + 'piwik.js';
				s.parentNode.insertBefore(g, s);
			})();
		</script>
		<noscript><p><img src="//piwik.hs-mainz.de/piwik.php?idsite=5" style="border:0;" alt="" /></p></noscript>
		<!-- End Piwik Code -->
	</body>

</html>
