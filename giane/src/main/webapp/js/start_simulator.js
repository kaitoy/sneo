$(document).ready( function() {
  $.subscribe("startSimulator_start_success", function(event, data) {
    $("#startSimulator_start_button").prop("disabled", true);
    $("#startSimulator_stop_button").prop("disabled", false);
  });

  $.subscribe("startSimulator_stop_success", function(event, data) {
    $("#startSimulator_start_button").prop("disabled", false);
    $("#startSimulator_stop_button").prop("disabled", true);
  });
});
