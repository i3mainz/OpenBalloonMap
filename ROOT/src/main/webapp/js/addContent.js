////////////////////////////
//   functions overview   //
////////////////////////////

// Open Map //
//setEvents(jsonIn)

// Open Find //
//showFinds(name, event_name)
// -->setEvent(jsonIn)
// -->setFinds(jsonIn)
//showHeatmap()
//showTrajektiorien()
//resetTrajektorien()

// GoTo Events //
//showEvents()

// Zoom //
//showBalloon(event_name)
// -->setEventZoomToBalloon(jsonIn)
// -->setFindsZoomToBalloon(jsonIn)


function setEvents(jsonIn) {
	
	resetTrajektorien();
	
	markers_events = null;
    markers_events = L.markerClusterGroup();
    
    displayLayer = L.geoJson(jsonIn, {

		pointToLayer: function(feature, latlng) {

			var marker = L.marker(latlng, {icon: markerIconEvent});
			
			var popupContent = "";
            popupContent += "<b><u>Event</u></b> | <a href=\"javascript:showFinds('" + feature.properties.event_name + "');\">Funde anzeigen</a>";
			popupContent += "<br><br>"+ "<b>Name: </b>"+feature.properties.event_name;
			popupContent += "<br>"+ "<b>Ort: </b>"+feature.properties.location;
			popupContent += "<br>"+ "<b>Date: </b>"+feature.properties.event_timestamp;

            marker.bindPopup(popupContent);
            
			// I3Talk hidden
			if (feature.properties.event_name !== "I3Talk") {
				markers_events.addLayer(marker);
			}

			return L.marker(latlng, {icon: markerIconEvent});
			
        }
    });
    
    map.addLayer(markers_events);
	document.getElementById('obm').innerHTML = "Open Balloon Map - Luftpost im Web &rArr; Eventanzeige";

}

function showFinds(name, event_name) {

	if (event_name===true) {
		name = eventname;
	} else { // set View on Event Klick
		if (name==="Akademische Jahresfeier") {
			setTimeout(function() { map.setView([49.988298, 8.227118], 7); }, 0);
		} else if (name==="Hochzeit Heike und Juergen") {
			setTimeout(function() { map.setView([49.884138, 8.627361], 7); }, 0);
		} else if (name==="FH Info-Tag") {
			setTimeout(function() { map.setView([49.988660, 8.226887], 7); }, 0);
		} else if (name==="Daeumling Sommerfest 2014") {
			setTimeout(function() { map.setView([49.457110, 8.408175], 7); }, 0);
		} else if (name.indexOf("Wissenschaftsmarkt") !==-1) {
			setTimeout(function() { map.setView([49.998949, 8.271531], 7); }, 0);
		} else if (name.indexOf("Info-Tag der Hochschule Mainz") !==-1) {
			setTimeout(function() { map.setView([49.98893, 8.22691], 7); }, 0);
		} else if (name.indexOf("Demo-Event") !==-1) {
			setTimeout(function() { map.setView([53.028889, -9.288333], 7); }, 0);
		} else if (name.indexOf("Pressetermin Schulgeographentag 2016") !==-1) {
			setTimeout(function() { map.setView([50.440211, 7.815161], 7); }, 0);
		} else {
			// Mitte Deutschland
			setTimeout(function() { map.setView([51.163375, 10.447683], 7); }, 0);
		}
	}
	
	if (heatmap===true) {
		
		heatmap = false;
		balloons_control.removeFrom(map);
		
		if (trajektorien===false) {
			trajektorien_control.removeFrom(map);
			heatmap_control.addTo(map);
			trajektorien_control.addTo(map);
		} else { // true
			removeTrajektorienLegends();
			trajektorien_control.removeFrom(map);
			heatmap_control.addTo(map);
			trajektorien_control.addTo(map);
			addTrajektorienLegends();
		}
		
		map.removeLayer(heatmapFinds);
		
	} else {
		document.getElementById('obm').innerHTML = "Open Balloon Map - Luftpost im Web &rArr; Fundanzeige";
		events_control.addTo(map);
		heatmap_control.addTo(map);
		trajektorien_control.addTo(map);
	}
	
	try {
		map.removeLayer(markers_events);
	} catch (e) {
		console.log(e);
	}

	readEventsGeoJSON(OPENBALLOON_URI+"/layer?bereich=openballoon&layer=find_e&attr=event_name&value="+name, setFinds);
	readEventsGeoJSON(OPENBALLOON_URI+"/layer?bereich=openballoon&layer=event&attr=event_name&value="+name, setEvent);
   
}

function setFinds(jsonIn) {
	
	markers_finds = null;
    markers_finds = L.markerClusterGroup();
    
    displayLayer = L.geoJson(jsonIn, {

		pointToLayer: function(feature, latlng) {

			var marker = L.marker(latlng, {icon: markerIconFind});

            var popupContent = "";
            popupContent += "<b><u>Fund</u></b>";
			popupContent += "<br>"+ "<b>Fundort: </b>"+feature.properties.location;
			popupContent += "<br>"+ "<b>Date: </b>"+feature.properties.find_timestamp;

            marker.bindPopup(popupContent);
            markers_finds.addLayer(marker);

			return L.marker(latlng, {icon: markerIconFind});
			
        }
    });
    
    map.addLayer(markers_finds);

}

function setEvent(jsonIn) {
	
	marker_event = null;
    marker_event = L.markerClusterGroup();
    
    displayLayer = L.geoJson(jsonIn, {

		pointToLayer: function(feature, latlng) {
			
			eventname = feature.properties.event_name;
			
			var marker = L.marker(latlng, {icon: markerIconEvent});
			
			var popupContent = "";
            popupContent += "<b><u>Event</u></b>";
			popupContent += "<br>"+ "<b>Name: </b>"+feature.properties.event_name;
			popupContent += "<br>"+ "<b>Ort: </b>"+feature.properties.location;
			popupContent += "<br>"+ "<b>Date: </b>"+feature.properties.event_timestamp;

            marker.bindPopup(popupContent);
            marker_event.addLayer(marker);

			return L.marker(latlng, {icon: markerIconEvent});
			
        }
    });
    
    map.addLayer(marker_event);

}

function showEvents() {
	
	if (heatmap===true) {
		map.removeLayer(heatmapFinds);
		balloons_control.removeFrom(map);
		heatmap = false;
	} else {
		heatmap_control.removeFrom(map);
	}
	
	events_control.removeFrom(map);
	trajektorien_control.removeFrom(map);
	map.removeLayer(markers_finds);
	map.removeLayer(marker_event);
	setTimeout(function() { map.setView([MAP_LAT, MAP_LON], MAP_ZOOM); }, 1);
	readEventsGeoJSON(OPENBALLOON_URI+"/layer?bereich=openballoon&layer=event", setEvents);
   
}

function showHeatmap() {

	heatmap = true;
	heatmap_control.removeFrom(map);	
	
	if (trajektorien===false) {
		trajektorien_control.removeFrom(map);
		balloons_control.addTo(map);
		trajektorien_control.addTo(map);
	} else { // true
		removeTrajektorienLegends();
		trajektorien_control.removeFrom(map);
		balloons_control.addTo(map);
		trajektorien_control.addTo(map);
		addTrajektorienLegends();
	}
	
	map.removeLayer(markers_finds);
	map.removeLayer(marker_event);
	
	//Geoserver WMS finds
	//singletile
	var options = {
		layers: 'openballoon:find_e',
		format: "image/png",
		transparent: true,
		filter: "<PropertyIsEqualTo><PropertyName>event_name</PropertyName><Literal>"+eventname+"</Literal></PropertyIsEqualTo>",
		styles: 'heatmap', 
		attribution: ""
	};

	heatmapFinds = new L.ImageOverlay.WMS('/geoserver/openballoon/wms', options);

	map.addLayer(heatmapFinds);

}

//////////////////
// Trajektorien //
//////////////////

var trajek_ajf = L.tileLayer.wms("/geoserver/openballoon/wms", {
	layers: 'openballoon:trajektorien_akademischejahresfeier',
	format: 'image/png',
	transparent: true,
	attribution: "Trajektorien &copy; HYSPLIT (HYbrid Single-Particle Lagrangian Integrated Trajectory) Model access via NOAA ARL READY Website"
});
var trajek_infotag = L.tileLayer.wms("/geoserver/openballoon/wms", {
	layers: 'openballoon:trajektorien_fhinfotag',
	format: 'image/png',
	transparent: true,
	attribution: "Trajektorien &copy; HYSPLIT (HYbrid Single-Particle Lagrangian Integrated Trajectory) Model access via NOAA ARL READY Website"
});
var trajek_infotag2 = L.tileLayer.wms("/geoserver/openballoon/wms", {
	layers: 'openballoon:trajektorien_hsinfotag_2015',
	format: 'image/png',
	transparent: true,
	attribution: "Trajektorien &copy; HYSPLIT (HYbrid Single-Particle Lagrangian Integrated Trajectory) Model access via NOAA ARL READY Website"
});
var trajek_hochzeit = L.tileLayer.wms("/geoserver/openballoon/wms", {
	layers: 'openballoon:trajektorien_boochs',
	format: 'image/png',
	transparent: true,
	attribution: "Trajektorien &copy; HYSPLIT (HYbrid Single-Particle Lagrangian Integrated Trajectory) Model access via NOAA ARL READY Website"
});
var trajek_wima14_1 = L.tileLayer.wms("/geoserver/openballoon/wms", {
	layers: 'openballoon:trajektorien_wima14_1',
	format: 'image/png',
	transparent: true,
	attribution: "Trajektorien &copy; HYSPLIT (HYbrid Single-Particle Lagrangian Integrated Trajectory) Model access via NOAA ARL READY Website"
});
var trajek_wima14_2 = L.tileLayer.wms("/geoserver/openballoon/wms", {
	layers: 'openballoon:trajektorien_wima14_2',
	format: 'image/png',
	transparent: true,
	attribution: "Trajektorien &copy; HYSPLIT (HYbrid Single-Particle Lagrangian Integrated Trajectory) Model access via NOAA ARL READY Website"
});
var trajek_sommerfest2014 = L.tileLayer.wms("/geoserver/openballoon/wms", {
	layers: 'openballoon:sommerfest2014',
	format: 'image/png',
	transparent: true,
	attribution: "Trajektorien &copy; HYSPLIT (HYbrid Single-Particle Lagrangian Integrated Trajectory) Model access via NOAA ARL READY Website"
});

function showTrajektorien() {

	if (trajektorien===true) {
		if (eventname==="Akademische Jahresfeier") {
			map.removeLayer(trajek_ajf);
			set_trajek_ajf = false;
			trajektorien_control_legend.removeFrom(map);
			trajektorien = false;
		} else if (eventname==="Hochzeit Heike und Juergen") {
			map.removeLayer(trajek_hochzeit);
			set_trajek_hochzeit = false;
			trajektorien_control_legend.removeFrom(map);
			trajektorien = false;
		} else if (eventname==="FH Info-Tag") {
			map.removeLayer(trajek_infotag);
			set_trajek_infotag = false;
			trajektorien_control_legend2.removeFrom(map);
			trajektorien = false;
		} else if (eventname==="Info-Tag der Hochschule Mainz") {
			map.removeLayer(trajek_infotag2);
			set_trajek_infotag2 = false;
			trajektorien_control_legend.removeFrom(map);
			trajektorien = false;
		} else if (eventname==="Daeumling Sommerfest 2014") {
			map.removeLayer(trajek_sommerfest2014);
			set_trajek_sommerfest = false;
			trajektorien_control_legend.removeFrom(map);
			trajektorien = false;
		} else if (eventname==="Wissenschaftsmarkt 2014 - Tag 1") {
			map.removeLayer(trajek_wima14_1);
			set_trajek_wima1 = false;
			trajektorien_control_legend3.removeFrom(map);
			trajektorien = false;
		} else if (eventname==="Wissenschaftsmarkt 2014 - Tag 2") {
			map.removeLayer(trajek_wima14_2);
			set_trajek_wima2 = false;
			trajektorien_control_legend3.removeFrom(map);
			trajektorien = false;
		}
	} else {
		if (eventname==="Akademische Jahresfeier") {
			map.addLayer(trajek_ajf);
			set_trajek_ajf = true;
			trajektorien_control_legend.addTo(map);
			trajektorien = true;
		} else if (eventname.indexOf("Hochzeit") !==-1) {
			map.addLayer(trajek_hochzeit);
			set_trajek_hochzeit = true;
			trajektorien_control_legend.addTo(map);
			trajektorien = true;
		} else if (eventname==="FH Info-Tag") {
			map.addLayer(trajek_infotag);
			set_trajek_infotag = true;
			trajektorien_control_legend2.addTo(map);
			trajektorien = true;
		} else if (eventname==="Info-Tag der Hochschule Mainz") {
			map.addLayer(trajek_infotag2);
			set_trajek_infotag2 = true;
			trajektorien_control_legend.addTo(map);
			trajektorien = true;
		} else if (eventname.indexOf("Sommerfest") !==-1) {
			map.addLayer(trajek_sommerfest2014);
			set_trajek_sommerfest = true;
			trajektorien_control_legend.addTo(map);
			trajektorien = true;
		} else if (eventname.indexOf("Tag 1") !==-1) {
			map.addLayer(trajek_wima14_1);
			set_trajek_wima1 = true;
			trajektorien_control_legend3.addTo(map);
			trajektorien = true;
		} else if (eventname.indexOf("Tag 2") !==-1) {
			map.addLayer(trajek_wima14_2);
			set_trajek_wima2 = true;
			trajektorien_control_legend3.addTo(map);
			trajektorien = true;
		} else {
			alert("Leider keine Trajektorien verfügbar!");
			trajektorien = false;
		}
	}
   
}

function resetTrajektorien() {
		
	if (trajektorien===true) {
		if (set_trajek_ajf) {
			trajektorien_control_legend.removeFrom(map);
			map.removeLayer(trajek_ajf);
			set_trajek_ajf = false;
			trajektorien = false;
		} else if (set_trajek_hochzeit) {
			trajektorien_control_legend.removeFrom(map);
			map.removeLayer(trajek_hochzeit);
			set_trajek_hochzeit = false;
			trajektorien = false;
		} else if (set_trajek_infotag) {
			trajektorien_control_legend2.removeFrom(map);
			map.removeLayer(trajek_infotag);
			set_trajek_infotag = false;
			trajektorien = false;
		} else if (set_trajek_infotag2) {
			trajektorien_control_legend.removeFrom(map);
			map.removeLayer(trajek_infotag2);
			set_trajek_infotag2 = false;
			trajektorien = false;
		} else if (set_trajek_sommerfest) {
			trajektorien_control_legend.removeFrom(map);
			map.removeLayer(trajek_sommerfest2014);
			set_trajek_sommerfest = false;
			trajektorien = false;
		} else if (set_trajek_wima1) {
			trajektorien_control_legend3.removeFrom(map);
			map.removeLayer(trajek_wima14_1);
			set_trajek_wima1 = false;
			trajektorien = false;
		} else if (set_trajek_wima2) {
			trajektorien_control_legend3.removeFrom(map);
			map.removeLayer(trajek_wima14_2);
			set_trajek_wima2 = false;
			trajektorien = false;
		}
	}
   
}

function removeTrajektorienLegends() {
	if (set_trajek_ajf) {
		trajektorien_control_legend.removeFrom(map);
	} else if (set_trajek_hochzeit) {
		trajektorien_control_legend.removeFrom(map);
	} else if (set_trajek_infotag) {
		trajektorien_control_legend2.removeFrom(map);
	} else if (set_trajek_infotag2) {
		trajektorien_control_legend.removeFrom(map);
	} else if (set_trajek_sommerfest) {
		trajektorien_control_legend.removeFrom(map);
	} else if (set_trajek_wima1) {
		trajektorien_control_legend3.removeFrom(map);
	} else if (set_trajek_wima2) {
		trajektorien_control_legend3.removeFrom(map);
	}
}

function addTrajektorienLegends() {
	if (set_trajek_ajf) {
		trajektorien_control_legend.addTo(map);
	} else if (set_trajek_hochzeit) {
		trajektorien_control_legend.addTo(map);
	} else if (set_trajek_infotag) {
		trajektorien_control_legend2.addTo(map);
	} else if (set_trajek_infotag2) {
		trajektorien_control_legend.addTo(map);
	} else if (set_trajek_sommerfest) {
		trajektorien_control_legend.addTo(map);
	} else if (set_trajek_wima1) {
		trajektorien_control_legend3.addTo(map);
	} else if (set_trajek_wima2) {
		trajektorien_control_legend3.addTo(map);
	}
}

//////////////////
// Zoom Balloon //
//////////////////

function showBalloon(event_name) {
	
	document.getElementById('obm').innerHTML = "Open Balloon Map - Luftpost im Web &rArr; Fundanzeige";
	
	if (heatmap===true) {
		map.removeLayer(heatmapFinds);
		balloons_control.removeFrom(map);
		heatmap_control.addTo(map);
		heatmap = false;
		trajektorien_control.addTo(map);
		trajektorien = false;
	} else {
		events_control.addTo(map);
		heatmap_control.addTo(map);
		trajektorien_control.addTo(map);
		trajektorien = false;
	}
	
	readEventsGeoJSON(OPENBALLOON_URI+"/layer?bereich=openballoon&layer=find_e&attr=event_name&value="+event_name, setFindsZoomToBallon);
	readEventsGeoJSON(OPENBALLOON_URI+"/layer?bereich=openballoon&layer=event&attr=event_name&value="+event_name, setEventZoomToBallon);
}

function setFindsZoomToBallon(jsonIn) {
	
	markers_finds = null;
    markers_finds = L.markerClusterGroup();
    
    displayLayer = L.geoJson(jsonIn, {

		pointToLayer: function(feature, latlng) {

			var marker = L.marker(latlng, {icon: markerIconFind});

            var popupContent = "";
            popupContent += "<b><u>Fund</u></b>";
			popupContent += "<br>"+ "<b>Fundort: </b>"+feature.properties.location;
			popupContent += "<br>"+ "<b>Date: </b>"+feature.properties.find_timestamp;

            marker.bindPopup(popupContent);
            markers_finds.addLayer(marker);
			
			return L.marker(latlng, {icon: markerIconFind});
			
        }
    });
    
    // Zoom to Ballon in the Center
	setTimeout(function() { map.setView([GET_LAT, GET_LON], 9); }, 0);
	
	map.addLayer(markers_finds);

}

function setEventZoomToBallon(jsonIn) {
	
	marker_event = null;
    marker_event = L.markerClusterGroup();
    
    displayLayer = L.geoJson(jsonIn, {

		pointToLayer: function(feature, latlng) {
			
			eventname = feature.properties.event_name;
			
			var marker = L.marker(latlng, {icon: markerIconEvent});
			
			var popupContent = "";
            popupContent += "<b><u>Event</u></b>";
			popupContent += "<br>"+ "<b>Name: </b>"+feature.properties.event_name;
			popupContent += "<br>"+ "<b>Ort: </b>"+feature.properties.location;
			popupContent += "<br>"+ "<b>Date: </b>"+feature.properties.event_timestamp;

            marker.bindPopup(popupContent);
            marker_event.addLayer(marker);

			return L.marker(latlng, {icon: markerIconEvent});
			
        }
    });
    
    map.addLayer(marker_event);

}

///////////////////////////
//   general functions   //
///////////////////////////

function readEventsGeoJSON(url, callback) {
	$.ajax({
		beforeSend: function(req) {
			req.setRequestHeader("Accept","json");
		},
		dataType: "json",
		url: url,
		error: function(jqXHR, textStatus, errorThrown) {
			console.log("error:"+errorThrown);
		},
		success: function (json) {
			callback(json);
		}
	});
}