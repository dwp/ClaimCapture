/**
 * Created by peterwhitehead on 18/03/2016.
 */
$(document).ready(function() {
    // Revert to a previously saved state
    window.addEventListener('popstate', function(event) {
        console.log('popstate fired! location.pathname=' + location.pathname + ' and location.hash=' + location.hash + ' and state=' + JSON.stringify(history, null, 4) + " event state=" + JSON.stringify(event.state, null, 4));
        //fires on hash change which is coming from and going to preview
        if (location.hash) return;
        if (!location.hash && $("#ReturnToCheckYourAnswers").length > 0) {
            $("#ReturnToCheckYourAnswers")[0].click();
        } else if (event && event.state) {
            checkButtonSelected(event.state.nexturl);
        } else {
            console.log('no state goto gov.uk');
            location.href="http://www.gov.uk/carers-allowance/how-to-claim"
        }
    }, false);


    function checkButtonSelected(nexturl) {
        if (nexturl == "back"){
            checkBackButton();
        } else if (nexturl == "forward" && $("button[value='next']").length > 0) {
            console.log('next');
            $("button[value='next']")[0].click();
        } else {
            console.log('no state goto gov.uk');
            location.href="http://www.gov.uk/carers-allowance/how-to-claim"
        }
    }

    function checkBackButton() {
        if ($("#bottomBackButton").length > 0) {
            console.log('bottomBackButton');
            $("#bottomBackButton")[0].click();
        } else if (location.pathname == "/allowance/benefits") {
            console.log('home goto gov.uk');
            location.href="http://www.gov.uk/carers-allowance/how-to-claim"
        } else {
            console.log('next from back');
            $("button[value='next']").length > 0
        }
    }

    history.pushState({nexturl: "back"}, null, null);
    history.pushState({nexturl: "forward"}, null, null);
});