//sample JavaScript to use on a VIVO publication page to find the
//Web of Science identifier and call the service to add it to the
//VIVO page.

$(document).ready(function(){
  processResource();
});

function processResource() {
  var wosId = $.trim($("[id^=wosId-] li").text());
  if (wosId != "") {
    //console.debug(wosId);
    fetchWoS(wosId);
  } else {
    //console.debug('wosId empty');
  };
}


function fetchWoS(wosId) {
  var apiURL = '/wos/amr/ut/' + wosId;
  console.debug(apiURL);
  $.getJSON(apiURL, function (data) {
    //console.debug(data);
    //debugger;
    var count = 0;
    var clickUrl = null;
    if ("timesCited" in data) {
        count = data.timesCited;
        clickUrl = data.citingArticlesURL;
    }
    else {
        clickUrl = data.sourceURL;
    };
    $('section#individual-info header').append('<div class="wos-cited"><span class="wos-badge"><a href="' + clickUrl + '" target="_blank" title="Times Cited via the Web of Science™">' + count + '</a></span>Times Cited via the Web of Science™</small></div>');
  });
}