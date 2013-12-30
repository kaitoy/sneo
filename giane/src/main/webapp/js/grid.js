$(document).ready( function() {
  $.subscribe("rowSelected", function(event, data) {
    $.each($("#" + event.originalEvent.grid[0].id).jqGrid("getRowData", event.originalEvent.id), function(index, value) {
      var fieldId = event.originalEvent.grid[0].id;
      fieldId = fieldId.substring(0, fieldId.lastIndexOf("grid"));
      fieldId += index;
      
      var obj = $("*[id='" + fieldId + "']");
      if (obj.size() != 0) {
        var tagName = obj.get(0).tagName.toLowerCase();
        switch (tagName) {
          case "input":
            if (obj.attr("type").toLowerCase() === "checkbox") {
              if (value === "true") {
                obj.val([obj.attr("value")]);
              }
              else {
                obj.val([]);
              }
              obj.addClass("checked");
            }
            else {
              obj.val(value).addClass("filled");
            }
            break;
          case "select":
            if (!value) {
              obj.children("option").removeAttr("selected");
            }
            else {
              obj.find("option:contains('" + value + "')")
                .attr("selected", "true").addClass("selectedOption");
            }
            break;
          case "textarea":
            obj.val(value).addClass("filled");
            break;
          default:
            break;
        }
      }
    });
  });

  $.subscribe("gridCompleted", function(event, data) {
    var gridId = $(data).attr("id");
    var formId = gridId.substring(0, gridId.lastIndexOf("grid")) + "form";
    
    $("#" + formId + " .filled").val("").removeClass("filled");
    $("#" + formId + " .checked").val([]).removeClass("checked");
    $("#" + formId + " .selectedOption").removeAttr("selected").removeClass("selectedOption");
  });
  
  $.subscribe("gridConfigButtonClicked", function(event, gridDom) {
    var topic = $(gridDom).attr("id");
    topic = topic.substring(0, topic.lastIndexOf("grid"));
    topic += "rowDblClicked";
    $.publish(topic);
  });
  
  $.subscribe("gridDeleteButtonClicked", function(event, gridDom) {
    var grid= $(gridDom);
    var gridId = grid.attr("id");
    var modelName = gridId.substring(0, gridId.lastIndexOf("_grid"));
    $("#" + modelName + "_deletingIdList")
      .val(grid.jqGrid("getGridParam", "selrow"));
      
    $.publish(modelName + "_deleteConfirmation");
  });
});

// formatter
function oneLine(cellvalue, options, rowObject) {
  if (!options.colModel.unformat) {
    options.colModel.unformat = function(c, o, r) {
      var clone = $(r).clone();
      clone.children("span").text("\n");
      return clone.text();
    };
  }
  
  return cellvalue.replace(/\n/g, "<span/>");
}

