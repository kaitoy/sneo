$(document).ready( function() {
  $.subscribe("removeErrors", function(event, data) {
    $(".giane-form-error-message").unbind().hide();
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
        elem.html(value.pop());
        elem.click(function () {
          $(this).unbind().hide();
        });
        elem.show();
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

