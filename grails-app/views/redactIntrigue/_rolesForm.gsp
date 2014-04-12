<%@ page import="org.gnk.selectintrigue.Plot" %>
<g:hiddenField name="screenStep" value="1"/>

<div class="tabbable tabs-left">

    <ul class="nav nav-tabs" style="height:400pt;width:175pt;overflow-y: auto;overflow-x: hidden;">
        <li class="active leftMenuList">
            <a href="#newRole" data-toggle="tab" class="addRole">
            Nouveau rôle
            </a>
        </li>
        <g:each in="${plotInstance.roles}" status="i5" var="role">
            <li class="leftMenuList tt">
                <a href="#role_${role.id}" data-toggle="tab">
                    ${role.code}
                </a>
                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer ce rôle?"
                        data-url="<g:createLink controller="Role" action="Delete" id="${role.id}"/>" data-object="role">
                    <i class="icon-remove pull-right"></i>
                </button>
            </li>
        </g:each>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newRole">
            <g:form name="newRoleForm" url="[controller: 'role', action: 'save']">
                <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                <div style="margin:auto">
                    <div class="row formRow">
                        <div class="span1">
                            <label for="roleCode">
                                <g:message code="redactintrigue.role.roleCode" default="Role code"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:textField name="roleCode" id="roleCode" value="" required=""/>
                        </div>
                    </div>
                    <div class="row formRow">
                        <div class="span1">
                            <label for="rolePipi">
                                <g:message code="redactintrigue.role.rolePipi" default="PIPI"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:field type="number" name="rolePipi" id="rolePipi" value="" required=""/>
                        </div>
                        <div class="span1">
                            <label for="rolePipr">
                                <g:message code="redactintrigue.role.rolePipr" default="PIPR"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:field type="number" name="rolePipr" id="rolePipr" value="" required=""/>
                        </div>
                    </div>
                    <div class="row formRow">
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.generalDescription.tags" default="Tags"/>
                            </label>
                        </div>
                        <div class="span4">
                            <a href="#roleTagsModal" class="btn" data-toggle="modal">Choisir les tags</a>
                        </div>
                        <div class="span1">
                            <label for="roleType">
                                <g:message code="redactintrigue.role.roleType" default="Type"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:select name="roleType" id="roleType" from="${['PJ', 'PNJ', 'PHJ']}"
                                      keys="${['PJ', 'PNJ', 'PHJ']}" required=""/>
                        </div>
                    </div>
                    <div class="row formRow">
                        <div class="span1">
                            <label for="rolePastScene">
                                <g:message code="redactintrigue.role.rolePastScene" default="Past Scene"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:select name="rolePastScene" id="rolePastScene" from="${['PastScene1', 'PastScene2', 'PastScene3']}"
                                      keys="${['PastScene1', 'PastScene2', 'PastScene3']}" required=""/>
                        </div>
                        <div class="span1">
                            <label for="roleEvent">
                                <g:message code="redactintrigue.role.roleEvent" default="Evenement"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:select name="roleEvent" id="roleEvent" from="${['Evenement1', 'Evenement2', 'Evenement3']}"
                                      keys="${['Evenement1', 'Evenement2', 'Evenement3']}" required=""/>
                        </div>
                    </div>
                    <div class="row formRow text-center">
                        <label for="roleDescription">
                            <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                        </label>
                    </div>
                    <g:textArea name="roleDescription" id="roleDescription" value="" rows="5" cols="100"/>
                </div>

                <div id="roleTagsModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>

                        <h3 id="myModalLabel">Tags</h3>
                    </div>

                    <div class="modal-body">
                        <ul>
                            <g:each in="${roleTagList}" status="i2" var="roleTagInstance">
                                <li class="modalLi">
                                    <label for="roleTags_${roleTagInstance.id}">
                                        <g:checkBox name="roleTags_${roleTagInstance.id}" id="roleTags_${roleTagInstance.id}"
                                        checked="false"/>
                                        ${fieldValue(bean: roleTagInstance, field: "name")}
                                    </label>
                                </li>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <g:submitButton name="Insert" value="Insert" class="btn btn-primary"/>
            </g:form>
        </div>

        <g:each in="${plotInstance.roles}" status="i4" var="role">
            <div class="tab-pane" id="role_${role.id}">
                <form name="updateRole_${role.id}" data-url="<g:createLink controller="Role" action="Update" id="${role.id}"/>">
                %{--<g:form name="updateRole_${role.id}" data-url="<g:createLink controller='Role' action='Update' id='${role.id}' />">--}%
                    <g:hiddenField name="id" value="${role.id}"/>
                    <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>

                    <div style="margin:auto">
                        <div class="row formRow">
                            <div class="span1">
                                <label for="roleCode">
                                    <g:message code="redactintrigue.role.roleCode" default="Role code"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:textField name="roleCode" id="roleCode" value="${role.code}" required=""/>
                            </div>
                        </div>
                        <div class="row formRow">
                            <div class="span1">
                                <label for="rolePipi">
                                    <g:message code="redactintrigue.role.rolePipi" default="PIPI"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:field type="number" name="rolePipi" id="rolePipi" value="${role.pipi}" required=""/>
                            </div>
                            <div class="span1">
                                <label for="rolePipr">
                                    <g:message code="redactintrigue.role.rolePipr" default="PIPR"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:field type="number" name="rolePipr" id="rolePipr" value="${role.pipr}" required=""/>
                            </div>
                        </div>
                        <div class="row formRow">
                            <div class="span1">
                                <label>
                                    <g:message code="redactintrigue.generalDescription.tags" default="Tags"/>
                                </label>
                            </div>
                            <div class="span4">
                                <a href="#roleTagsModal_${role.id}" class="btn" data-toggle="modal">Choisir les tags</a>
                            </div>
                            <div class="span1">
                                <label for="roleType">
                                    <g:message code="redactintrigue.role.roleType" default="Type"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:select name="roleType" id="roleType" from="${['PJ', 'PNJ', 'PHJ']}"
                                          keys="${['PJ', 'PNJ', 'PHJ']}" value="${role.type}" required=""/>
                            </div>
                        </div>
                        <div class="row formRow">
                            <div class="span1">
                                <label for="rolePastScene">
                                    <g:message code="redactintrigue.role.rolePastScene" default="Past Scene"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:select name="rolePastScene" id="rolePastScene" from="${['PastScene1', 'PastScene2', 'PastScene3']}"
                                          keys="${['PastScene1', 'PastScene2', 'PastScene3']}" required=""/>
                            </div>
                            <div class="span1">
                                <label for="roleEvent">
                                    <g:message code="redactintrigue.role.roleEvent" default="Evenement"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:select name="roleEvent" id="roleEvent" from="${['Evenement1', 'Evenement2', 'Evenement3']}"
                                          keys="${['Evenement1', 'Evenement2', 'Evenement3']}" required=""/>
                            </div>
                        </div>
                        <div class="row formRow text-center">
                            <label for="roleDescription">
                                <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                            </label>
                        </div>
                        <g:textArea name="roleDescription" id="roleDescription" value="${role.description}" rows="5" cols="100"/>
                    </div>

                    <div id="roleTagsModal_${role.id}" class="modal hide fade" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>

                            <h3 id="myModalLabel${role.id}">Tags</h3>
                        </div>

                        <div class="modal-body">
                            <g:each in="${roleTagList}" status="i3" var="roleTagInstance">
                                <li class="modalLi">
                                    <label>
                                        <g:checkBox name="roleTags_${roleTagInstance.id}" id="roleTags_${roleTagInstance.id}"
                                        checked="${role.hasRoleTag(roleTagInstance)}"/> ${fieldValue(bean: roleTagInstance, field: "name")}
                                    </label>
                                </li>
                            </g:each>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>
                    <input type="button" name="Update" data-id="${role.id}" value="Update" class="btn btn-primary updateRole"/>
                    %{--<g:submitButton name="Update" value="Update" class="btn btn-primary updateRole"/>--}%
                    %{--<g:actionSubmit class="delete" controller="role" action="delete"--}%
                                    %{--value="${message(code: 'default.button.delete.label', default: 'Delete')}"--}%
                                    %{--formnovalidate=""--}%
                                    %{--onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>--}%
                %{--</g:form>--}%
                </form>
            </div>
        </g:each>
    </div>
</div>