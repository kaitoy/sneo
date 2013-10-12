$(document).ready( function() {
  $.subscribe("association_group", function(event, data) {
    var gridId = event.originalEvent.delegateTarget.id.slice("remove_from_".length);
    var otherGridId;
    var associateButtonClicked;

    if (gridId.indexOf("unassociated") != -1) {
      associateButtonClicked = true;
      otherGridId = gridId.substring(0, gridId.indexOf("unassociated")) + "associated";
      otherGridId = otherGridId + gridId.slice(otherGridId.length + 2);
    }
    else {
      associateButtonClicked = false;
      otherGridId = gridId.substring(0, gridId.indexOf("associated")) + "unassociated";
      otherGridId = otherGridId + gridId.slice(otherGridId.length - 2);
    }

    var grid = $("#" + gridId);
    var otherGrid = $("#" + otherGridId);

    var data = new Array();
    var delRowNums = new Array();
    $.each(grid.jqGrid("getGridParam", "selarrrow"), function(index, value) {
      data.push(grid.jqGrid("getRowData", value));
      delRowNums.push(value);
    });

    otherGrid.jqGrid("addRowData", "id", data, "last");

    for (var i = 0; i < delRowNums.length; i ++) {
      grid.jqGrid("delRowData", delRowNums[i]);
    }

    var associatedGridId;
    var associatedGrid;
    if (associateButtonClicked) {
      associatedGridId = otherGridId;
      associatedGrid = otherGrid;
    }
    else {
      associatedGridId = gridId;
      associatedGrid = grid;

      otherGrid.trigger("reloadGrid");

      var expandedGroupIdsBox = $("#" + otherGridId + "_expanded_groups");
      if (expandedGroupIdsBox.text().indexOf("ghead") != -1) {
        var expandedGroupIds = expandedGroupIdsBox.text().split(",");
        for (var i = 0; i < expandedGroupIds.length; i++) {
          otherGrid.jqGrid("groupingToggle", expandedGroupIds[i]);
        }
      }
    }

    var associatedGridRowIds = new Array();
    $.each(associatedGrid.jqGrid("getDataIDs"), function(index, value) {
      associatedGridRowIds.push(associatedGrid.jqGrid("getRowData", value)["id"]);
    });

    $("#" + associatedGridId + "_id_list").attr("value", associatedGridRowIds.join(","));
  });

  $.subscribe("groupClicked", function(event, data) {
    var expandedGroupIdsBox = $("#" + this.activeElement.id + "_expanded_groups");

    var expandedGroupIds;
    if (expandedGroupIdsBox.text().indexOf("ghead") == -1) {
      expandedGroupIds = new Array(0);
    }
    else {
      expandedGroupIds = expandedGroupIdsBox.text().split(",");
    }

    var groupId = event.originalEvent.groupid;
    if (event.originalEvent.collapsed) {
      for (var i = 0; i < expandedGroupIds.length; i++) {
        if (expandedGroupIds[i] == groupId) {
          expandedGroupIds.splice(i, 1);
          break;
        }
      }
    }
    else {
      expandedGroupIds.push(groupId);
    }

    expandedGroupIdsBox.text(expandedGroupIds);
  });

  var sorting = false;

  $.subscribe("groupGridSorted", function(event, data) {
    sorting = true;
  });

  $.subscribe("groupGridCompleted", function(event, data) {
    if (sorting) {
      var expandedGroupIdsBox = $("#" + data.id + "_expanded_groups");
      if (expandedGroupIdsBox.text().indexOf("ghead") != -1) {
        var expandedGroupIds = expandedGroupIdsBox.text().split(",");
        for (var i = 0; i < expandedGroupIds.length; i++) {
          $("#" + data.id).jqGrid("groupingToggle", expandedGroupIds[i]);
        }
      }

      sorting = false;
    }
  });
});

