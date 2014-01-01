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
    var gridId = modelName + "_grid";
    
    if ($("#" + gridId).jqGrid("getGridParam", "selarrrow").length !== 1) {
      $("#commonDialog_form_titleKey").val("dialog.title.error");
      $("#commonDialog_form_textKey").val("dialog.text.select.a.row");
      $.publish("showCommonDialog");
    }
    else {
      $.publish("doUpdate_" + modelName);
    }
  });

  $.subscribe("saveButtonClicked", function(event, data) {
    var formId = event.originalEvent.currentTarget.form.id;
    var topic = "doSave_" + formId.substring(0, formId.lastIndexOf("_form"));
    
    $.publish(topic);
  });
  
  $.subscribe("startButtonClicked", function(event, data) {
    var formId = event.originalEvent.currentTarget.form.id;
    var topic = "doStart_" + formId.substring(0, formId.lastIndexOf("_form"));
    
    $.publish(topic);
  });
  
  $.subscribe("stopButtonClicked", function(event, data) {
    var formId = event.originalEvent.currentTarget.form.id;
    var topic = "doStop_" + formId.substring(0, formId.lastIndexOf("_form"));
    
    $.publish(topic);
  });
});
