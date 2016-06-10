# Open Balloon Map

Geoinformation für alle – Luftballons im Dienste der Wissenschaft. Unter dem Motto "OpenBalloonMap" vermittelt das i3mainz Grundlagen der Geoinformatik und präsentiert Projekte aus der Umwelt- und Gesundheitsforschung. Die ersten Ergebnisse sind auf der Projektwebsite "openballoonmap.org" einsehbar.

**OpenBallonMap** is a free available balloonmap using Java / JavaScript / JSP and a PostGIS / GeoServer connection.

Here, the sourcecode of the server and client application is published.

The "openballoon" folder is a Netbeans project, if you build it, you will get a WAR file. Run it as "ROOT".

You have to run this WAR file in an Apache Tomcat using a PostgreSQL / PostGIS 2.x database (with an bunch of existing tables and configure the properties in the config.properties file. You need also a GeoServer that gives the data via a WMS / WFS interface.

Want to see the app? [See the application.](http://openballoonmap.org)

Want to see the data? [See the GeoServer Layers.](http://openballoonmap.org/geoserver/web/?wicket%3AbookmarkablePage=%3Aorg.geoserver.web.demo.MapPreviewPage)

More about the project you can find at the [i3mainz](http://i3mainz.hs-mainz.de/en/projekte/openballoonmap) website.

## Licence

The MIT License (MIT)

Copyright (c) 2016 [i3mainz](http://i3mainz.hs-mainz.de/en/institute)**

**Florian Thiery M.Sc.

**Martin Unold M.Sc.

**Axel Kunz M.Sc.

**[i3mainz](http://i3mainz.hs-mainz.de/en/institute) - Institute for Spatial Information and Surveying Technology

**Hochschule Mainz University of Applied Sciences

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
