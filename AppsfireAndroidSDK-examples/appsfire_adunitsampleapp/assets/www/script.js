
function getParam ( sname )
{
  var params = window.location.search.substr(location.search.indexOf("?")+1);
  var sval = "";
  params = params.split("&");
    // split param and value into individual pieces
    for (var i=0; i<params.length; i++)
       {
         temp = params[i].split("=");
         if ( [temp[0]] == sname ) { sval = temp[1]; }
       }
  return sval;
}

window.onload=function(){
		//var scrshot = getParam("img");
		//document.getElementById("appScrShot").src=getParam("appScrShot"),
		//alert(document.getElementById("appScrShot").src);
		//alert(document.getElementById("appName").innerHTML));
		var height = window.innerHeight,
		width = window.innerWidth,
		marginLeft = ((width - 600) / 2),
		marginTop = ((height - 1010) / 2),
		iOS = ( navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false ),
		android = ( navigator.userAgent.match(/Android/g) ? true : false ),
		wrapper = document.getElementById("wrapper"),
		mask = document.getElementById("mask"),
		iphone = document.getElementById("iphone"),
		price = document.getElementById("price"),
		lenghtDescription = document.getElementById("description");
		lenghtDescriptionVal = document.getElementById("description").innerHTML,
		descriptionSplite = lenghtDescriptionVal.substr(0,79),
		lenghtDescription.innerHTML= descriptionSplite;

	if(iOS === false || android == true) {
		if(window.innerHeight > window.innerWidth){
			
			//document.getElementById("description").innerHTML = getParam("description");
			//document.getElementById("appScrShot").src = "http://a5.mzstatic.com/eu/r30/Purple/v4/46/39/18/463918dc-dcab-5216-d7ce-921cfd88201b/screen568x568.jpeg", //getParam("appScrShot")
			JS2AndroidCallHandler.adPreDisplay();
	    	showSushi();
			scalePrice();
	    	translateIphone();
	    	JS2AndroidCallHandler.adDisplayed();
		}
		
		// Event Open when the page is load

		// Event Close
		document.getElementById('close').onclick=function(){
			JS2AndroidCallHandler.adPreDismiss();
			hideSushi();
			JS2AndroidCallHandler.adDismissed();
		};

		window.addEventListener("orientationchange", function() {

			var orientation = window.orientation;

			if( (orientation == '90') || (orientation == '-90') ){
				hideSushi();
			}
		}, false);
	}


	function showSushi(){
		document.getElementById("wrapper").style.marginLeft = marginLeft + "px";
		document.getElementById("wrapper").style.marginTop = marginTop + "px";
		wrapper.className = wrapper.className + "fadeIn";
		mask.className = mask.className + "fadeIn";
	}
	function hideSushi(){
		
		//wrapper.className = "";
		//mask.className = ""
		wrapper.className = "fadeOut";
		mask.className = "fadeOut";
		//wrapper.className = wrapper.className + "fadeOut";
		//mask.className = mask.className + "fadeOut";
		
	}
	function translateIphone(){
		iphone.className = iphone.className + "translateIphone";
	}
	function scalePrice(){
		price.className = price.className + "scalePrice";
		
	}
};