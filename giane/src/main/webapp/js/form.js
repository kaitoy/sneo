$(document).ready( function() {
  $.subscribe("createButtonClicked", function(event, data) {
    var formId = event.originalEvent.currentTarget.form.id;
    var modelName = formId.substring(0, formId.lastIndexOf("_form"));
    
    $("#" + modelName + "_id").removeAttr("value");
    $("#" + modelName + "_grid").jqGrid("resetSelection");
    
    $.publish("doCreate_" + modelName);
  });
  
  $.subscribe("updateButtonClicked", function(event, data) {
    var formId = event.originalEvent.currentTarget.form.id;
    var modelName = formId.substring(0, formId.lastIndexOf("_form"));
    
    $.publish("doUpdate_" + modelName);
  });

  $.subscribe("saveButtonClicked", function(event, data) {
    var formId = event.originalEvent.currentTarget.form.id;
    var topic = "doSave_" + formId.substring(0, formId.lastIndexOf("_form"));
    
    $.publish(topic);
  });
});
