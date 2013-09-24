$(function() {
    $('.datepicker').pickadate({
        selectYears: true,
        selectMonths: true,
        hiddenSuffix: '.hidden',
        formatSubmit: 'yyyy,mm,dd'
    });
});