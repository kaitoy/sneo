$(document).ready( function() {
  $.subscribe("removeErrors", function(event, data) {
    $(".errorLabel").html("").removeClass("errorLabel");
  });
});

function validation(form, errors) {
  if (errors.errors) {
    $.each(errors.errors, function(index, value) {
      alert(value);
    });
    return;
  }

  if (errors.fieldErrors) {
    $.each(errors.fieldErrors, function(index, value) {
      var elem = $("#" + form[0].id + "_" + index + "Error");
      if(elem) {
        elem.html(value[0]);
        elem.addClass("errorLabel");
      }
    });
  }
}

function checkRowSelection(form, errors) {
  if (errors.errors) {
    $.each(errors.errors, function(index, value) {
      alert(value);
    });
    
    $(".last-breadcrumb")
      .removeClass("update-needed");
    return;
  }
}

