<%-- 
    Document   : index
    Created on : 13.08.2014, 12:59:26
    Author     : Florian Thiery M.Sc.
	Author     : i3mainz - Institute for Spatial Information and Surveying Technology
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
	<head>
		<title>Open Balloon Map | Luftpost im Web - Geoinformation für alle</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="shortcut icon" href="img/favicons.png" type="image/png" />
		<link rel="icon" href="img/favicons.png" type="image/png" />
		<link rel="stylesheet" href="css/style.css"/>
		<link rel="stylesheet" href="css/jquery-ui.css"/>
		<link rel="stylesheet" href="css/leaflet.css" />
		<link rel="stylesheet" href="css/MarkerCluster.css" />
		<link rel="stylesheet" href="css/MarkerCluster.Default.css" />
		<link rel="stylesheet" href="css/leaflet-measure.css" />
		<link rel="stylesheet" href="css/L.Control.MousePosition.css" />
		<script type="text/javascript" src="js/jquery.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui.js"></script>
		<script type="text/javascript" src="js/leaflet.js"></script>
		<script type="text/javascript" src="js/leaflet.markercluster-src.js"></script>
		<script type="text/javascript" src="js/Leaflet.singletilewms.js"></script>
		<script type="text/javascript" src="js/Control.FullScreen.js"></script>
		<script type="text/javascript" src="js/leaflet-measure.js"></script>
		<script type="text/javascript" src="js/L.Control.MousePosition.js"></script>
		<script type="text/javascript" src="js/addContent.js"></script>
		<script>
			var OPENBALLOON_URI = "";
		</script>
	</head>
	<body>

		<div class="content">
			<div class="top_block header">
				<div class="lupe"><a href="javaScript:search();"></a></div>
				<div class="search"><input id="ballooncode" type="text" maxlength="12" class="searchinput" value="Code eingeben" onFocus="if (this.value != '') {
							this.value = '';
							this.style.backgroundColor = '#dbdbdb';
						}" onkeydown="if (event.keyCode == 13) {
									search();
									return false;
								}"></div>
				<div class="link2"><a class="header" href="http://i3mainz.hs-mainz.de/impressum" target="_blank">Impressum</a></div>
				<div class="link1"><a class="header" href="http://i3mainz.hs-mainz.de/de/projekte/openballoonmap" target="_blank">About</a></div>
			</div>
			<div class="logos">
				<div class="logo"><a href="http://i3mainz.hs-mainz.de" target="_blank"></a></div>
				<div class="obm" id="obm">Open Balloon Map</div>
			</div>
			<div class="background map" id="map"></div>	
		</div>

		<script>
			function search() {
				if (document.getElementById('ballooncode').value == "" || document.getElementById('ballooncode').value == null) {
					alert("Bitte Balloon Code eingeben!");
				} else {
					window.location.href = OPENBALLOON_URI + "?ballooncode=" + document.getElementById('ballooncode').value;
				}
			}
		</script>
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

	<script>

		// Parse Parameter using JSP
		var GET_EVENT = null;
		var GET_LAT = null;
		var GET_LON = null;
		var GET_FUNCTION = null;
		var GET_NR = null;
		GET_EVENT = "<%=request.getParameter("event")%>";
		GET_LAT = "<%=request.getParameter("lat")%>";
		GET_LON = "<%=request.getParameter("lon")%>";
		GET_FUNCTION = "<%=request.getParameter("function")%>";
		GET_NR = "<%=request.getParameter("nr")%>";

		console.info("nr: " + GET_NR + " | event: " + GET_EVENT);
		//console.info("event:"+GET_EVENT+" lat:"+GET_LAT+" lon:"+GET_LON+" function:"+GET_FUNCTION);

		// Funktionen
		var heatmap = false;
		var trajektorien = false;
		var eventname = "none";

		// Trajektorien
		var set_trajek_ajf = false;
		var set_trajek_hochzeit = false;
		var set_trajek_infotag = false;
		var set_trajek_infotag2 = false;
		var set_trajek_sommerfest = false;
		var set_trajek_wima1 = false;
		var set_trajek_wima2 = false;

		// Map Events View
		// Biblis ==> setView([49.688137, 8.453191], 8)
		// Mitte Deutschland ==> setView([51.163375, 10.447683], 7)
		var MAP_LAT = 49.688137;
		var MAP_LON = 8.453191;
		var MAP_ZOOM = 8;

		// Layer
		var markers_events;
		var marker_event;
		var markers_finds;
		var heatmapFinds;

		// Leaflet Map
		var map = L.map('map', {zoomControl: false}).setView([MAP_LAT, MAP_LON], MAP_ZOOM);

		// Add our zoom control manually where we want to
		var zoomControl = L.control.zoom({
			position: 'topleft'
		});
		map.addControl(zoomControl);
		
		// measure tool
		var measureControl = L.control.measure({position: 'topleft', activeColor: '#FF0000', completedColor: '#FF0000', primaryLengthUnit: 'kilometers', primaryAreaUnit: 'sqmeters'});
		measureControl.addTo(map);

		//OpenStreetMap Humanitarian
		L.tileLayer('http://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png', {
			maxZoom: 17,
			attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, Tiles courtesy of <a href="http://hot.openstreetmap.org/" target="_blank">Humanitarian OpenStreetMap Team</a>'
		}).addTo(map);
		
		// add scale and coordinates
		L.control.scale().addTo(map);
		L.control.mousePosition().addTo(map);

		//OpenStreetMap normal
		/*L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
			maxZoom: 17,
			attribution: 'Map data &copy; OpenStreetMap'
		}).addTo(map);*/
	
		
		//OpenStreetMap BlackAndWhite
		/*L.tileLayer('http://{s}.tiles.wmflabs.org/bw-mapnik/{z}/{x}/{y}.png', {
			attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
		}).addTo(map);*/

		// Marker

		var markerIconEvent = L.icon({
			iconUrl: 'img/balloon_blue.png',
			iconAnchor: [15, 40],
			popupAnchor: [0, 0]
		});
		var markerIconFind = L.icon({
			iconUrl: 'img/balloon_red.png',
			iconAnchor: [15, 40],
			popupAnchor: [0, 0]
		});
		var markerIconFind2 = L.icon({
			iconUrl: 'img/balloon_green.png',
			iconAnchor: [15, 40],
			popupAnchor: [0, 0]
		});

		//var eventsanzeige_control = L.control({position: 'topright'});

		//eventsanzeige_control.onAdd = function(map) {
		//var div = L.DomUtil.create('div', 'info legenddd');
		//div.innerHTML += '<img src="img/map_events.png" width="100" alt="">';
		//return div;
		//};	

		//var fundeanzeige_control = L.control({position: 'topright'});

		//fundeanzeige_control.onAdd = function(map) {
		//var div = L.DomUtil.create('div', 'info legenddd');
		//div.innerHTML += '<img src="img/map_funde.png" width="100" alt="">';
		//return div;
		//};	

		var events_control = L.control({position: 'topright'});

		events_control.onAdd = function(map) {
			var div = L.DomUtil.create('div', 'info legenddd');
			div.innerHTML += '<a href="javascript:showEvents()"><img src="img/bt_events.png" width="100" alt=""></a>';
			return div;
		};

		var heatmap_control = L.control({position: 'topright'});

		heatmap_control.onAdd = function(map) {
			var div = L.DomUtil.create('div', 'info legenddd');
			div.innerHTML += "<a href=\"javascript:showHeatmap()\"><img src=\"img/bt_heatmap.png\" width=\"100\" alt=\"\"></a>";
			return div;
		};

		var balloons_control = L.control({position: 'topright'});

		balloons_control.onAdd = function(map) {
			var div = L.DomUtil.create('div', 'info legenddd');
			div.innerHTML += "<a href=\"javascript:showFinds('',true)\"><img src=\"img/bt_balloons.png\" width=\"100\" alt=\"\"></a>";
			return div;
		};

		var trajektorien_control = L.control({position: 'topright'});

		trajektorien_control.onAdd = function(map) {
			var div = L.DomUtil.create('div', 'info legenddd');
			div.innerHTML += "<a href=\"javascript:showTrajektorien('',true)\"><img src=\"img/bt_trajektorien.png\" width=\"100\" alt=\"\"></a>";
			return div;
		};

		var trajektorien_control_legend = L.control({position: 'topright'});

		trajektorien_control_legend.onAdd = function(map) {
			var div = L.DomUtil.create('div', 'info legenddd');
			div.innerHTML += "<img src=\"img/map_trajektorien1.png\" width=\"100\" alt=\"\">";
			return div;
		};

		var trajektorien_control_legend2 = L.control({position: 'topright'});

		trajektorien_control_legend2.onAdd = function(map) {
			var div = L.DomUtil.create('div', 'info legenddd');
			div.innerHTML += "<img src=\"img/map_trajektorien2.png\" width=\"100\" alt=\"\">";
			return div;
		};

		var trajektorien_control_legend3 = L.control({position: 'topright'});

		trajektorien_control_legend3.onAdd = function(map) {
			var div = L.DomUtil.create('div', 'info legenddd');
			div.innerHTML += "<img src=\"img/map_trajektorien3.png\" width=\"100\" alt=\"\">";
			return div;
		};

		map.closePopup();

		if (GET_FUNCTION == "showBalloon") {
			document.getElementById('ballooncode').style.backgroundColor = '#BCEE68';
			document.getElementById('ballooncode').value = "Balloon gefunden";
			showBalloon(GET_EVENT);
		} else if (GET_FUNCTION == "noBalloon") {
			document.getElementById('ballooncode').style.backgroundColor = '#FF3030';
			document.getElementById('ballooncode').value = "nicht gefunden";
			readEventsGeoJSON(OPENBALLOON_URI + "/layer?bereich=openballoon&layer=event", setEvents);
		} else if (GET_FUNCTION == "showEvent") {
			document.getElementById('ballooncode').style.backgroundColor = '#BCEE68';
			document.getElementById('ballooncode').value = "Event gefunden";
			showFinds(GET_EVENT);
		} else if (GET_FUNCTION == "noEvent") {
			document.getElementById('ballooncode').style.backgroundColor = '#FF3030';
			document.getElementById('ballooncode').value = "nicht gefunden";
			readEventsGeoJSON(OPENBALLOON_URI + "/layer?bereich=openballoon&layer=event", setEvents);
		}
		else {
			// Get Events
			readEventsGeoJSON(OPENBALLOON_URI + "/layer?bereich=openballoon&layer=event", setEvents);
		}

    </script>
</html>
