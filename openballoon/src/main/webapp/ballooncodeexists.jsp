<%-- 
    Document   : error_find
    Created on : 16.07.2014, 13:34:26
    Author     : Florian Thiery M.Sc.
	Author     : i3mainz - Institute for Spatial Information and Surveying Technology
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>

<head>
	<title>Open Balloon Map | Fehler</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="shortcut icon" href="../img/favicons.png" type="image/png" />
	<link rel="icon" href="../img/favicons.png" type="image/png" />
	<link rel="stylesheet" href="../css/style.css"/>
	<script>
		var OPENBALLOON_URI = "";
		var GET_CODE=null;
		GET_CODE="<%=request.getParameter ("ballooncode")%>";
		var zoomToBalloon = function() {
			window.location.href = OPENBALLOON_URI+"/map?ballooncode=" + GET_CODE;
		}
	</script>
</head>

<body>
	<div class="content">
		<div class="top_block header">
			<div class="link2"><a class="header" href="http://i3mainz.hs-mainz.de/impressum" target="_blank">Impressum</a></div>
			<div class="link1"><a class="header" href="http://i3mainz.hs-mainz.de/de/projekte/openballoonmap" target="_blank">About</a></div>
		</div>
		<div class="logos">
			<div class="logo"><a href="http://i3mainz.hs-mainz.de" target="_blank"></a></div>
			<div class="obm">Open Balloon Map</div>
		</div>
		<form class="info">
			<span style="color: red;">Es ist ein<br />
			Fehler aufgetreten<br /><br /></span>
			Leider ist der<br />
			Balloon-Code<br />
			schon eingegeben worden.<br /><br />
			<a class="box" href="javaScript:zoomToBalloon();">Zu Ihrem Balloon</a>
		</form>
	</div>
</body>

</html>
