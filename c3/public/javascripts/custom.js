var IE10 = (navigator.userAgent.match(/(MSIE 10.0)/g) ? true : false);
if (IE10) {
	$('html').addClass('ie10');
}
var IE11 = (navigator.userAgent.match(/(MSIE 11.0)/g) ? true : false);
if (IE11) {
	$('html').addClass('ie11');
}


// Chris' datepicker
function datepicker(dateFieldId) {
    return $(dateFieldId).datepicker({
        dateFormat: 'dd/mm/yy',
        showButtonPanel: true,
        closeText: 'Clear',
        onClose: function (dateText, inst) {
            if ($(window.event.srcElement).hasClass('ui-datepicker-close'))
            {
                document.getElementById(this.id).value = '';
            }
        }
    });
}

$(function() {

    // view more / view less
    $(".helper-info").css("display", "none");

    $('.helper-more').click(function(){
       var labelText = $(this).text() === $(this).attr('data-close')? $(this).attr('data-initial') : $(this).attr('data-close');

        $(this).toggleClass("helper-less")
        $(this).next(".helper-info").slideToggle();
        $(this).text(labelText);

    });    

    // Add the "focus" value to class attribute 
    $('.block-label').focusin( function() {
        $(this).addClass('focus');
    });

    // Remove the "focus" value to class attribute 
    $('.block-label').focusout( function() {
        $(this).removeClass('focus');
    });

    // Help & Feedback container
    $(".feedback-container").css("display", "none");
    $('.feedback, .feed-close').click(function(){
     	$( ".feedback-container" ).toggle();
    });
         
    // Mobile specific - Nav
    $('#helper-toggle').click(function(){
     	$("#sidebar").toggle();
     	$(this).toggleClass("open-helper");
        return true;
    });
         
    // Non JS message 
    $(".js-message").css("display", "none");
    $(".feed-close").css("display", "block");
     
    // Nino auto jump
	//$('.ni-number input, .sort-code input, .year input').autotab_magic();

    // smooth scroll
    $('a[href^="#"]').bind('click.smoothscroll', function (e) {
        e.preventDefault();
        var target = this.hash,
            $target = $(target);
        $('html, body').animate({
            scrollTop: $(target).offset().top - 40
        }, 750, 'swing', function () {
            window.location.hash = target;
        });
    });

});

$(document).ready(function() {

    if (typeof console !== 'undefined') {
        console.log("document on ready " + areCookiesEnabled() );
    }

    if (areCookiesEnabled())  {
        $('#no-cookies').toggle(false);
        $('#content').toggle(true);
    } else {
        $('#no-cookies').toggle(true);
        $('#content').toggle(false);
    }

});

function areCookiesEnabled(){

    var cookieEnabled = (navigator.cookieEnabled) ? true : false;

    if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled) {
        document.cookie="testcookie";
        cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
    }

    return (cookieEnabled);

}

function trackEvent(category, action, label, value){
    ga('send', 'event', getConfigurationObject(category, action, label, value));
}

function getConfigurationObject(category, action, label, value){
    var configurationObject = {};
    if (typeof category != 'undefined') configurationObject.eventCategory = category;
    if (typeof action != 'undefined') configurationObject.eventAction = action;
    if (typeof label != 'undefined') configurationObject.eventLabel = label;
    if (typeof value != 'undefined') configurationObject.eventValue = value;
    return configurationObject;
}

function trackVirtualPageView(category){
    ga('send', 'pageview', category);
}

function getCookie(c_name) {
    var i,x,y,ARRcookies=document.cookie.split(";");
    for (i=0;i<ARRcookies.length;i++) {
        x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
        y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
        x=x.replace(/^\s+|\s+$/g,"");
        if (x==c_name) {
            return unescape(y);
        }
    }
}

function setCookie(c_name,value) {
    var c_value=escape(value);
    document.cookie=c_name + "=" + c_value;
}

function trackTiming(category, variable, timemeasured){
    if (0 < timemeasured && timemeasured <  36000000) {
        ga('send', 'timing', {
          'timingCategory': category,
          'timingVar': variable,
          'timingValue': timemeasured
        });
    }
}

// Selection Buttons
(function () {
  "use strict";
  var root = this,
      $ = root.jQuery;

  if (typeof GOVUK === 'undefined') { root.GOVUK = {}; }

  var SelectionButtons = function (elmsOrSelector, opts) {
    var $elms;

    this.selectedClass = 'selected';
    this.focusedClass = 'focused';
    if (opts !== undefined) {
      $.each(opts, function (optionName, optionObj) {
        this[optionName] = optionObj;
      }.bind(this));
    }
    if (typeof elmsOrSelector === 'string') {
      $elms = $(elmsOrSelector);
      this.selector = elmsOrSelector;
      this.setInitialState($(this.selector));
    } else {
      this.$elms = elmsOrSelector;
      this.setInitialState(this.$elms);
    }
    this.addEvents();
  };
  SelectionButtons.prototype.addEvents = function () {
    if (typeof this.$elms !== 'undefined') {
      this.addElementLevelEvents();
    } else {
      this.addDocumentLevelEvents();
    }
  };
  SelectionButtons.prototype.setInitialState = function ($elms) {
    $elms.each(function (idx, elm) {
      var $elm = $(elm);

      if ($elm.is(':checked')) {
        this.markSelected($elm);
      }
    }.bind(this));
  };
  SelectionButtons.prototype.markFocused = function ($elm, state) {
    if (state === 'focused') {
      $elm.parent('label').addClass(this.focusedClass);
    } else {
      $elm.parent('label').removeClass(this.focusedClass);
    }
  };
  SelectionButtons.prototype.markSelected = function ($elm) {
    var radioName;

    if ($elm.attr('type') === 'radio') {
      radioName = $elm.attr('name');
      $($elm[0].form).find('input[name="' + radioName + '"]')
        .parent('label')
        .removeClass(this.selectedClass);
      $elm.parent('label').addClass(this.selectedClass);
    } else { // checkbox
      if ($elm.is(':checked')) {
        $elm.parent('label').addClass(this.selectedClass);
      } else {
        $elm.parent('label').removeClass(this.selectedClass);
      }
    }
  };
  SelectionButtons.prototype.addElementLevelEvents = function () {
    this.clickHandler = this.getClickHandler();
    this.focusHandler = this.getFocusHandler({ 'level' : 'element' });

    this.$elms
      .on('click', this.clickHandler)
      .on('focus blur', this.focusHandler);
  };
  SelectionButtons.prototype.addDocumentLevelEvents = function () {
    this.clickHandler = this.getClickHandler();
    this.focusHandler = this.getFocusHandler({ 'level' : 'document' });

    $(document)
      .on('click', this.selector, this.clickHandler)
      .on('focus blur', this.selector, this.focusHandler);
  };
  SelectionButtons.prototype.getClickHandler = function () {
    return function (e) {
      this.markSelected($(e.target));
    }.bind(this);
  };
  SelectionButtons.prototype.getFocusHandler = function (opts) {
    var focusEvent = (opts.level === 'document') ? 'focusin' : 'focus';

    return function (e) {
      var state = (e.type === focusEvent) ? 'focused' : 'blurred';

      this.markFocused($(e.target), state);
    }.bind(this);
  };
  SelectionButtons.prototype.destroy = function () {
    if (typeof this.selector !== 'undefined') {
      $(document)
        .off('click', this.selector, this.clickHandler)
        .off('focus blur', this.selector, this.focusHandler);
    } else {
      this.$elms
        .off('click', this.clickHandler)
        .off('focus blur', this.focusHandler);
    }
  };

  root.GOVUK.SelectionButtons = SelectionButtons;
}).call(this);

$(document).ready(function() {

  // Use GOV.UK selection-buttons.js to set selected
  // and focused states for block labels
  var $blockLabels = $(".block-label input[type='radio'], .block-label input[type='checkbox']");
  new GOVUK.SelectionButtons($blockLabels);

  // Details/summary polyfill
  // See /javascripts/vendor/details.polyfill.js


});
