<div class="tabbable tabs-left pastSceneScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newPastScene" data-toggle="tab" class="addPastScene">
                <g:message code="redactintrigue.pastScene.addPastScene" default="New object"/>
            </a>
        </li>
        <g:each in="${plotInstance.pastescenes}" status="i5" var="pastScene">
            <li class="leftMenuList">
                <a href="#pastScene_${pastScene.id}" data-toggle="tab">
                    ${pastScene.title}
                </a>
                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer cet scène passée?"
                        data-url="<g:createLink controller="PastScene" action="Delete" id="${pastScene.id}"/>" data-object="pastScene" data-id="${pastScene.id}">
                    <i class="icon-remove pull-right"></i>
                </button>
            </li>
        </g:each>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newPastScene">
            <form name="newPastSceneForm" data-url="">
                %{--<div style="margin:auto">--}%
                <div class="row formRow">
                    <div class="span1">
                        <label for="pastSceneTitle">
                            <g:message code="redactintrigue.pastScene.pastsceneTitle" default="Titre"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:textField name="pastSceneTitle" id="pastSceneTitle" value="" required=""/>
                    </div>
                </div>

                <div class="row formRow">
                    <div class="span1">
                        <label for="pastSceneDatetime">
                            <g:message code="redactintrigue.pastScene.pastsceneDatetime" default="Date and Time"/>
                        </label>
                    </div>

                    <div class="span4">
                        <div class="input-append date datetimepicker">
                            <input data-format="dd/MM/yyyy hh:mm" type="text" id="pastSceneDatetime" name="pastSceneDatetime"/>
                            <span class="add-on">
                                <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                </i>
                            </span>
                        </div>
                    </div>

                    <div class="span1">
                        <label for="pastScenePublic">
                            <g:message code="redactintrigue.pastScene.pastscenePublic" default="Public"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:checkBox name="pastScenePublic" id="pastScenePublic"/>
                    </div>
                </div>

                <div class="row formRow">
                    <div class="span1">
                        <label for="pastScenePlace">
                            <g:message code="redactintrigue.pastScene.pastscenePlace" default="Place"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="pastScenePlace" id="pastScenePlace" from="${['Lieu1', 'Lieu2', 'Lieu3']}"
                                  keys="${['Lieu1', 'Lieu2', 'Lieu3']}" required=""/>
                    </div>
                    <div class="span1">
                        <label for="pastScenePredecessor">
                            <g:message code="redactintrigue.pastScene.pastscenePredecessor" default="Predecessor"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="pastScenePredecessor" id="pastScenePredecessor" from="${['PastScene1', 'PastScene2', 'PastScene3']}"
                                  keys="${['PastScene1', 'PastScene2', 'PastScene3']}" required=""/>
                    </div>
                </div>

                <div class="row formRow text-center">
                    <label for="pastSceneDescription">
                        <g:message code="redactintrigue.pastScene.pastsceneDescription" default="Description"/>
                    </label>
                </div>
                <g:textArea name="pastSceneDescription" id="pastSceneDescription" value="" rows="5" cols="100"/>
                %{--</div>--}%
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertPastScene"/>
            </form>
        </div>

        <g:each in="${plotInstance.pastescenes}" status="i4" var="pastScene">
            <div class="tab-pane" id="pastScene_${pastScene.id}">
                <form name="updatePastScene_${pastScene.id}" data-url="<g:createLink controller="PastScene" action="Update" id="${pastScene.id}"/>">
                    <g:hiddenField name="id" value="${pastScene.id}"/>
                    <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>

                        %{--<div style="margin:auto">--}%
                    <div class="row formRow">
                        <div class="span1">
                            <label for="pastSceneTitle">
                                <g:message code="redactintrigue.pastScene.pastsceneTitle" default="Titre"/>
                            </label>
                        </div>

                        <div class="span4">
                            <g:textField name="pastSceneTitle" id="pastSceneTitle" value="${pastScene.title}" required=""/>
                        </div>
                    </div>

                    <div class="row formRow">
                        <div class="span1">
                            <label for="pastSceneDatetime">
                                <g:message code="redactintrigue.pastScene.pastsceneDatetime" default="Date and Time"/>
                            </label>
                        </div>

                        <div class="span4">
                            <div class="input-append date datetimepicker">
                                <input data-format="dd/MM/yyyy hh:mm" type="text" id="pastSceneDatetime${pastScene.id}" name="pastSceneDatetime"
                                value="${pastScene.absoluteDay}/${pastScene.absoluteMonth}/${pastScene.absoluteYear} ${pastScene.absoluteHour}:${pastScene.absoluteMinute}"/>
                                <span class="add-on">
                                    <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                    </i>
                                </span>
                            </div>
                        </div>

                        <div class="span1">
                            <label for="pastScenePublic">
                                <g:message code="redactintrigue.pastScene.pastscenePublic" default="Public"/>
                            </label>
                        </div>

                        <div class="span4">
                            <g:checkBox name="pastScenePublic" id="pastScenePublic" value="${pastScene.isPublic}"/>
                        </div>
                    </div>

                    <div class="row formRow">
                        <div class="span1">
                            <label for="pastScenePlace">
                                <g:message code="redactintrigue.pastScene.pastscenePlace" default="Place"/>
                            </label>
                        </div>

                        <div class="span4">
                            <g:select name="pastScenePlace" id="pastScenePlace" from="${plotInstance.genericPlaces}" value="${pastScene.genericPlace?.id}"
                                      optionKey="id" required="" optionValue="code" noSelection="${['null':'']}"/>
                        </div>
                        <div class="span1">
                            <label for="pastScenePredecessor">
                                <g:message code="redactintrigue.pastScene.pastscenePredecessor" default="Predecessor"/>
                            </label>
                        </div>

                        <div class="span4">
                            <g:select name="pastScenePredecessor" id="pastScenePredecessor" from="${plotInstance.pastescenes}" value="${pastScene.pastscenePredecessor?.id}"
                                      optionKey="id"  required="" optionValue="title" noSelection="${['null':'']}"/>
                        </div>
                    </div>

                    <div class="row formRow text-center">
                        <label for="pastSceneDescription">
                            <g:message code="redactintrigue.pastScene.pastsceneDescription" default="Description"/>
                        </label>
                    </div>
                    <g:textArea name="pastSceneDescription" id="pastSceneDescription" value="${pastScene.description}" rows="5" cols="100"/>
                    %{--</div>--}%
                    <input type="button" name="Update" data-id="${pastScene.id}" value="Update" class="btn btn-primary updatePastScene"/>
                </form>
            </div>
        </g:each>
    </div>

</div>