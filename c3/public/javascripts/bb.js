/**
 * Created by peterwhitehead on 18/03/2016.
 */
$(document).ready(function() {
    // Revert to a previously saved state
    window.addEventListener('popstate', function(event) {
        //console.log('popstate fired! location.pathname=' + location.pathname + ' and location.hash=' + location.hash + ' and state=' + JSON.stringify(history, null, 4) + " event state=" + JSON.stringify(event.state, null, 4));
        //fires on hash change which is coming from and going to preview
        if (location.hash && !event) return;
        if (!location.hash && $("#ReturnToCheckYourAnswers").length > 0 && event && event.state) {
            $("#ReturnToCheckYourAnswers")[0].click();
        } else if (event && event.state) {
            checkButtonSelected(event.state.nexturl);
        } else if (!history)  {
            //console.log('no history goto gov.uk');
            location.href=landingURL
        }
    }, false);


    function checkButtonSelected(nexturl) {
        if (nexturl == "back"){
            checkBackButton();
        } else if (nexturl == "forward" && $("button[value='next']").length > 0) {
            //console.log('next');
            $("button[value='next']")[0].click();
        } else {
            //console.log('no state goto gov.uk');
            location.href=landingURL
        }
    }

    function checkBackButton() {
        if ($("#bottomBackButton").length > 0) {
            //console.log('bottomBackButton');
            $("#bottomBackButton")[0].click();
        } else if (location.pathname.indexOf("/allowance/benefits") > -1 || location.pathname.indexOf("/circumstances/report-changes/change-selection") > -1) {
            //console.log('home goto gov.uk');
            location.href=landingURL
        } else {
            //console.log('next from back');
            $("button[value='next']")[0].click();
        }
    }

    if (window.history && window.history.pushState) {
        history.pushState({nexturl: "back"}, null, null);
        history.pushState({nexturl: "forward"}, null, null);
    }
});