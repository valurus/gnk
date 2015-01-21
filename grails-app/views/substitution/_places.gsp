<div class="row-fluid">
    <div class="span4"><legend>Lieux</legend></div>

    <div class="span1"><span class="badge badge-important" id="placesPercentage">0 %</span></div>

    <div class="span2"><a id="runSubPlacesButton" class="btn btn-info"><i class="icon-play icon-white"></i> LANCER</a>
    </div>

    <div class="span2"><a href="#modalFusionPlace" role="button" id="fusionbuttonmodal" class="btn btn-warning" data-toggle="modal"><i
            class="icon-play icon-white"></i> FUSION</a>
    </div>

    <div class="span1" id="placesLoader" style="display: none; float : right;"><g:img dir="images/substitution"
                                                                                      file="loader.gif" width="30"
                                                                                      height="30"/></div>
</div>

<div id="subPlacesAlertContainer">
</div>

<table id="placeTable" class="table table-striped">
    <thead>
    <tr class="upper">
        <th style="text-align: center;">#</th>
        <th>code</th>
        <th>Type</th>
        <th>Plot Name</th>
        <th>tags</th>
        <th>comment</th>
        <th>nom</th>
        <th style="text-align: center;">
            A RELANCER <input id="restartPlaceAll" type="checkbox" disabled="true" style="float: right;">
        </th>
    </tr>
    </thead>
    <tbody id="fusiontbodyplace" data-url="<g:createLink controller='substitution' action='merged'/>">
    <g:each id="loopPlaceList" status="i" in="${placeList}" var="place"  >
        <tr id="place${place.id}_plot${place.plotId}" class="placeUnity">
            <!-- # -->
            <td style="text-align: center;">${i + 1}</td>
            <!-- Code -->
            <td>${place.code}</td>
            <!-- Type -->
            <td>${place.objectType}</td>
            <!-- Plot Name -->
            <td>${place.plotName}</td>
            <!-- Tags -->
            <td>
                <ul class="unstyled">
                    <g:each status="j" in="${place.tagList}" var="tag">
                        <li><strong class="cap">${tag.value.encodeAsHTML()}</strong>
                            (<span class="cap">${tag.family.encodeAsHTML()}</span> / ${tag.weight.encodeAsHTML()})</li>
                    </g:each>
                </ul>
            </td>
            <!-- Comment -->
            <td>${place.comment.encodeAsHTML()}</td>
            <!-- Place -->
            <td class="place">
                <input type="radio" name="${place}Radio" id="generatedPlace" checked><select class="bold"
                                                                                             disabled="disabled"
                                                                                             isEmpty="true"></select><br>
                <input type="radio" name="${place}Radio" id="writtenPlace"><input type="text" id="placeWritten" disabled="disabled"
                                                                                  class="written">
                <input type="text" id="customPlace" class="written" disabled="disabled" placeholder="Add a custom place">
                %{--<a class="btn unban" title="Débannir" disabled="true"><i class="icon-arrow-left"></i></a>--}%
                <button class="btn customPlace" title="Create the custom place" type="button" data-plot-id="${place.plotId}" data-id="${place.id}"><i class="icon-arrow-left"></i></button>
            </td>
            <!-- Restart place -->
            <td class="restartPlace" style="text-align: center;">
                <input type="checkbox" name="option" value="unlock" disabled="true">
            </td>
        </tr>
    </g:each>
    <tbody>
</table>

<!-- Modal Views -->
<div id="modalFusionPlace" class="modal hide fade" style="width: 800px; margin-left: -400px;"
     tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <h3>Fusion</h3>
    </div>
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

    <div class="modal-body">
        <g:form action="merged">
            <div class="row">
                <input type="hidden" id="placeList" value="${placeList}">
                <input type="hidden" id="gnId" value="${gnId}">


                <div class="span3">
                    <label><g:message code="redactintrigue.place.mergeablePlace1"/></label>
                    <select name="placeMergeable1" class="placeMergeable" id="placeMergeable1" data-url="<g:createLink controller="substitution" action="getMergeablePlaces"/>">
                    <option id="reset1" value="-1"></option>
                    <g:each in="${placeList}" var="p">
                        <option value="${p.code}">${p.code}</option>
                    </g:each>
                </select><br/>
                    <div id="com1">

                    </div>
                </div>


                <div class="span3">
                    <label><g:message code="redactintrigue.place.mergeablePlace2"/></label>
                    <select name="placeMergeable2" class="placeMergeable" disabled="disabled" id="placeMergeable2">
                    <option id="reset2" value="-1"></option>
                </select><br/>
                    <div id="com2">

                    </div>
                </div>
            </div>
            <button id="fusionButton" class="btn btn-primary" data-dismiss="modal" aria-hidden="true">Fusionner</button>
        </g:form>
    </div>

    <div class="modal-footer">
    </div>
</div>

<!--g:render template="modalViewPlaces" /-->

<g:javascript src="substitution/subPlaces.js"/>

<script type="text/javascript">
    $(document).ready(function () {
        // PlacesJSON
        placesJSON = initPlacesJSON();

        isSubPlacesRunning = false;

        initPlacesEvents("${g.createLink(controller:'substitution', action:'getSubPlaces')}")

        $('.customPlace').click(function(){

            var input = $(this).prev();
            var content = input.val();
            var placesList = $("select", $(this).parent());
            var genericId = $(this).attr("data-id");
            var plotId = $(this).attr("data-plot-id");

            if (content != "") {
                var place = new Object();
                // Gn id
                place.gnId = ${gnInfo.dbId}
                    // Gn plot id
                place.gnPlotId = plotId;
                // HTML id
                place.htmlId = "place"+genericId+"_plot" + plotId;
                // Code
                place.code = content;
                // BEGIN Tags LOOP
                place.tags = new Array();
                placesJSON.places.push(place);

//                var placeArray = placesJSON.places;
//                for (var key in placeArray) {
//                    var place = placeArray[key];
//                    var proposedNames = place.proposedNames
//                    for (var key in proposedNames) {
//                        var name = proposedNames[key];
//                        proposedNames[key] = name.substring(0, name.lastIndexOf(" -"));
//                    }
//                }

                placesList.append($("<option>").attr("value", content).text(content));
                $(this).prev().val('');
            }
        });
    });

    $("#fusionbuttonmodal").click(function () {
        var reset1 = $("#reset1");
        var reset2 = $("#reset2");
        reset1.prop("selected", true)
        reset2.prop("selected", true)
        var com2 = $("#com2");
        var com1 = $("#com1");
        com2.html("");
        com1.html("")
    });

    $("#fusionButton").click(function () {
        var loop = $("#loopPlaceList");
        var useless = $("#fusiontbodyplace");
        var place1 = $("#placeMergeable1");
        var place2 =$("#placeMergeable2");
        $.ajax({
            type: "POST",
            url: useless.attr("data-url"),
            dataType: "json",
            data: {place1 : place1.val(), place2 : place2.val()},
            success: function (place) {
//                loop.attr("in",placeList)
                $(".placeUnity td:contains("+place1.val()+")").closest("tr").remove();
                $(".placeUnity td:contains("+place2.val()+")").closest("tr").remove();
                var template = Handlebars.templates['templates/substitution/place'];
                var context = {
                    place: place,
                    i : $(".placeUnity").size() + 1
                };
                var html = template(context);
                $('#fusiontbodyplace').append(html);
                var count = 1;
                $('.placeUnity').each(function() {
                    $("td:first-child", this).html(count);
                    count++;
                });
            }
        })
    });
    $("#placeMergeable2").change(function () {
        var place2 = $("#placeMergeable2");
        var com2 = $("#com2");
        if(place2.val() != "-1"){
            var tags2 = $(".placeUnity td:contains("+place2.val()+")").next().next().html();
            var comment2 = $(".placeUnity td:contains("+place2.val()+")").next().html();
            com2.html("Comment : <br/>"+comment2 +"<br/><br/>" + "Liste des Tags : <br/>"+tags2)
        }
        else
        {
            com2.html("")
        }
    });
    $("#placeMergeable1").change(function () {
        var place1 = $("#placeMergeable1");
        var place2 = $("#placeMergeable2");
        var placel = $("#placeList");
        var com1 = $("#com1");

        $('option:not([value="-1"])', place2).remove();
        if(place1.val() != "-1"){
            place2.prop("disabled", false);
            var comment1 = $(".placeUnity td:contains("+place1.val()+")").next().html();
            var tags1 = $(".placeUnity td:contains("+place1.val()+")").next().next().html();
            com1.html("Comment : <br/>"+comment1 + "<br/><br/>" + "Liste des Tags : <br/>"+ tags1)
        }
        else
        {
            place2.prop("disabled", true);
            com1.html("")
        }
        $.ajax({
            type: "POST",
            url: place1.attr("data-url"),
            dataType: "json",
            data: { place1: place1.val(), placel : placel.val()},
            success: function (placeList) {
                $(placeList).each(function () {
                    place2.append('<option value="' + this.code + '">' + this.code + '</option>');
                });
            }
        })
    });
    function initPlacesJSON() {
        var jsonObject = new Object();
        // Universe
        jsonObject.universe = "${gnInfo.universe}";

        // BEGIN Places LOOP
        var placeArray = new Array();
        <g:each status="i" in="${placeList}" var="place">
        var place = new Object();
        // Gn id
        place.gnId = "${place.id}"
        // Gn plot id
        place.gnPlotId = "${place.plotId}"
        // Gn plot
        place.plotName = "${place.plotName}"
        // HTML id
        place.htmlId = "place${place.id}_plot${place.plotId}"
        // Code
        place.code = "${place.code}"

        // BEGIN Generic Tags LOOP
        var tagArray = new Array();
        <g:each status="j" in="${place.tagList}" var="tag">
        var tag = new Object();
        tag.value = "${tag.value}";
        tag.family = "${tag.family}";
        tag.weight = "${tag.weight}";
        tagArray.push(tag);
        </g:each>
        // END Tags LOOP
        if (tagArray.length > 0) {
            place.tags = tagArray;
        }
        placeArray.push(place);
        </g:each>
        // END Places LOOP

        jsonObject.places = placeArray;
        return jsonObject;
    }
</script>

