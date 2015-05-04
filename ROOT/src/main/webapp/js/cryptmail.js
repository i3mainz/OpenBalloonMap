/**
*	CryptMail
*
*	Simple Javascript Email-Address crypter / uncrypter.
*	(C) 2005 KLITSCHE.DE // DIRK ALBAN ADLER
*	http://cryptmail.klitsche.org
*	
*	CryptMail is published under the CC-GNU LGPL
*	http://creativecommons.org/licenses/LGPL/2.1/
*
*	It is provided as is. No warrenties. No support.	
*/
function mailto (s)	
{
	document.location.href =  "mailto:" + unCryptMail(s);
}

function unCryptMail (r)  
{
	r = unescape (r);
	var l = r.length;
	var o = "";
	for (i = 0; i < l; i++)
	{
		o += String.fromCharCode (r.charCodeAt (i) - 1);
	}
	return o; 
}

function cryptMail (r) 
{
	var l = r.length;
	var o = "";
	for (i = 0; i < l; i++)
	{
		o += String.fromCharCode (r.charCodeAt (i) + 1);
	}
	return escape (o);
}