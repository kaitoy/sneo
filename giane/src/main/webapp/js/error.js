$(document).ready( function() {
  $.subscribe("createError", function(event, data) {
    alert("Failed to create");
  });

  $.subscribe("updateError", function(event, data) {
    alert("Failed to update");
  });

  $.subscribe("setError", function(event, data) {
    alert("Failed to set");
  });
  
  $.subscribe("deleteError", function(event, data) {
    alert("Failed to delete");
  });
});
