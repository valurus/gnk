$(function(){
    updateRole();

    //ajoute un nouveau role dans la base
    $('.insertRole').click(function() {
        if ($('form[name="newRoleForm"] select[name="roleType"]').val() == "STF") {
            $('form[name="newRoleForm"] input[name="roleCode"]').val("Staff");
        }
        var form = $('form[name="newRoleForm"]');
        var description = $('.richTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.iscreate) {
                    createNotification("success", "Création réussie.", "Votre rôle a bien été ajouté.");
                    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiRole'];
                    var context = {
                        roleId: String(data.role.id),
                        roleName: data.role.code
                    };
                    var html = template(context);
                    $('.roleScreen > ul').append(html);
                    updateRoleRelation(data);
                    initConfirm();
                    initDeleteButton();
                    emptyRoleForm();
                    createNewRolePanel(data);
                    initSearchBoxes();
                    initModifyTag();
                    stopClosingDropdown();
                    if (data.role.type != "STF") {
                        appendEntity("role", data.role.code, "success", "", data.role.id);
                    }
                    var nbRoles = parseInt($('.roleLi .badge').html()) + 1;
                    $('.roleLi .badge').html(nbRoles);
                    initQuickObjects();
                    updateRole();
                    $('form[name="updateRole_' + data.role.id + '"] .btnFullScreen').click(function() {
                        $(this).parent().parent().toggleClass("fullScreenOpen");
                    });
                    var spanList = $('.richTextEditor span.label-default').filter(function() {
                        return $(this).text() == data.role.code;
                    });
                    spanList.each(function() {
                        $(this).removeClass("label-default").addClass("label-success");
                    });
                    $('.roleSelector li[data-id=""]').each(function() {
                        if ($("a", $(this)).html().trim() == data.role.code + ' <i class="icon-warning-sign"></i>') {
                            $(this).remove();
                        }
                    });
                    updateAllDescription($.unique(spanList.closest("form")));
                }
                else {
                    createNotification("danger", "création échouée.", "Votre rôle n'a pas pu être ajouté, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "création échouée.", "Votre rôle n'a pas pu être ajouté, une erreur s'est produite.");
            }
        })
    });
});

function updateRole() {
    // modifie un role dans la base
    $('.updateRole').click(function() {
        var roleId = $(this).attr("data-id");
        var roleName = $('form[name="updateRole_' + roleId + '"] input[name="roleCode"]').val();
        var roleType = $('form[name="updateRole_' + roleId + '"] select[name="roleType"]').val();
        if (roleType == "STF") {
            $('form[name="updateRole_' + roleId + '"] input[name="roleCode"]').val("Staff");
        }
        if (($('.richTextEditor span.label-success:contains("' + roleName + '")').size() > 0) && (roleType == "STF")) {
            createNotification("danger", "création échouée.", "Ce rôle ne peut pas être staff car il est présent dans des descriptions.");
        }
        else if (($('.relationScreen .accordion-heading span[data-roleid="'+roleId+'"]').size() > 0) && (roleType == "STF")) {
            createNotification("danger", "création échouée.", "Ce rôle ne peut pas être staff car il possède des relations.");
        }
        else {
            if (roleType == "STF") {
                $('.roleSelector li[data-id="'+roleId+'"]').remove();
                $('select[name="relationFrom"] option[value="' + roleId + '"]').remove();
                $('select[name="relationTo"] option[value="' + roleId + '"]').remove();
            }
            else if ($('.roleSelector li[data-id="'+roleId+'"]').size() == 0) {
                appendEntity("role", roleName, "success", "", roleId);
                $('select[name="relationFrom"]').append('<option value="' + roleId + '">' + roleName + '</option>');
                $('select[name="relationTo"]').append('<option value="' + roleId + '">' + roleName + '</option>');
            }
            var form = $('form[name="updateRole_' + roleId + '"]');
            var description = $('.richTextEditor', form).html();
            description = transformDescription(description);
            $('.descriptionContent', form).val(description);
            $.ajax({
                type: "POST",
                url: form.attr("data-url"),
                data: form.serialize(),
                dataType: "json",
                success: function(data) {
                    if (data.object.isupdate) {
                        createNotification("success", "Modifications réussies.", "Votre rôle a bien été modifié.");
                        $('form[name="updateRole_' + roleId + '"] select[name="roleType"] option').removeAttr("selected");
                        $('form[name="updateRole_' + roleId + '"] select[name="roleType"] option[value="'+data.object.type+'"]').attr("selected", "selected");
                        initializeTextEditor();
                        $('.roleScreen .leftMenuList a[href="#role_' + data.object.id + '"]').html(data.object.name);
                        $('.relationScreen .leftMenuList a[href="#roleRelation_' + data.object.id + '"]').html(data.object.name);
                        $('.pastSceneScreen a[href*="#pastsceneRole'+data.object.id +'"]').html(data.object.name);
                        $('.eventScreen a[href*="#eventRole'+data.object.id +'"]').html(data.object.name);
                        $('select[name="relationFrom"] option[value="' + data.object.id + '"]').html(data.object.name);
                        $('select[name="relationTo"] option[value="' + data.object.id + '"]').html(data.object.name);
                        $('select[name="resourceRolePossessor"] option[value="' + data.object.id + '"]').html(data.object.name);
                        $('select[name="resourceRoleFrom"] option[value="' + data.object.id + '"]').html(data.object.name);
                        $('select[name="resourceRoleTo"] option[value="' + data.object.id + '"]').html(data.object.name);
                        $('.relationScreen .accordion-group span[data-roleId="' + data.object.id + '"] span').each(function() {
                            var relationImage = $(this).html();
                            $(this).parent().html(relationImage + " " + data.object.name);
                        });
                        $('.roleSelector li[data-id="' + data.object.id + '"] a').html(data.object.name);
                        $('span.label-success').each(function() {
                            if ($(this).html().trim() == data.object.oldname) {
                                $(this).html(data.object.name);
                            }
                        });
                    }
                    else {
                        createNotification("danger", "Modifications échouées.", "Votre rôle n'a pas pu être modifié, une erreur s'est produite.");
                    }
                },
                error: function() {
                    createNotification("danger", "Modifications échouées.", "Votre rôle n'a pas pu être modifié, une erreur s'est produite.");
                }
            })
        }
    });
}

// supprime un role dans la base
function removeRole(object) {
    var liObject = object.parent();
    var name = $.trim($("a", liObject).html());
    var isRolePresentInDescriptions = false;
    $('.richTextEditor span.label-success').each(function() {
        if ($(this).html() == name) {
            isRolePresentInDescriptions = true;
        }
    });
    if (isRolePresentInDescriptions) {
        createNotification("danger", "suppression impossible.", "Votre rôle est utilisé dans certaines descriptions."
            + " Veuillez supprimer l'utilisation de ce rôle dans les descriptions avant de supprimer l'entité rôle.");
    }
    else {
        $.ajax({
            type: "POST",
            url: object.attr("data-url"),
            dataType: "json",
            success: function(data) {
                if (data.object.isdelete) {
                    liObject.remove();
                    $('select[name="relationFrom"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('select[name="relationTo"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('select[name="resourceRolePossessor"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('select[name="resourceRoleFrom"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('select[name="resourceRoleTo"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('.relationScreen .leftMenuList a[href="#roleRelation_' + object.attr("data-id") + '"]').parent().remove();
                    $('.relationScreen #roleRelation_' + object.attr("data-id")).remove();
                    $('.relationScreen .accordion-group[data-roleTo="' + object.attr("data-id") + '"]').remove();
                    $('.pastSceneScreen a[href*="#pastsceneRole' + object.attr("data-id") +'"]').parent().remove();
                    $('.pastSceneScreen div[id*="pastsceneRole' + object.attr("data-id") + '"]').remove();
                    $('.eventScreen a[href*="#eventRole' + object.attr("data-id") +'"]').parent().remove();
                    $('.eventScreen div[id*="eventRole' + object.attr("data-id") + '"]').remove();
                    var nbRoles = parseInt($('.roleLi .badge').html()) - 1;
                    $('.roleLi .badge').html(nbRoles);
                    $('.addRole').trigger("click");
                    $('.roleSelector li[data-id="' + object.attr("data-id") + '"]').remove();
                    $('form[name="updateRole_' + object.attr("data-id") + '"]').remove();
                    $('.richTextEditor span.label-success').each(function() {
                        if ($(this).html() == name) {
                            $(this).remove();
                        }
                    });
                    $('.numberRelation').html($('.relationScreen .accordion-group').size());
                    createNotification("success", "Supression réussie.", "Votre rôle a bien été supprimé.");
                }
                else {
                    createNotification("danger", "suppression échouée.", "Votre rôle n'a pas pu être supprimé, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "suppression échouée.", "Votre rôle n'a pas pu être supprimé, une erreur s'est produite.");
            }
        });
    }
}

//vide le formulaire d'ajout de role
function emptyRoleForm() {
    $('form[name="newRoleForm"] input[type="text"]').val("");
    $('form[name="newRoleForm"] input[type="number"]').val("");
    $('form[name="newRoleForm"] textarea').val("");
    $('form[name="newRoleForm"] input[type="checkbox"]').attr('checked', false);
    $('form[name="newRoleForm"] #roleType option[value="PJ"]').attr("selected", "selected");
    $('form[name="newRoleForm"] .chooseTag').parent().addClass("invisible");
    $('form[name="newRoleForm"] .banTag').parent().addClass("invisible");
    $('form[name="newRoleForm"] .tagWeightInput').val(50);
    $('form[name="newRoleForm"] .tagWeightInput').attr('disabled','disabled');
    $('form[name="newRoleForm"] .search-query').val("");
    $('form[name="newRoleForm"] .modalLi').show();
    $('form[name="newRoleForm"] #roleRichTextEditor').html("");
}

// créé un tab-pane du nouveau role
function createNewRolePanel(data) {
    Handlebars.registerHelper('toLowerCase', function(value) {
        return new Handlebars.SafeString(value.toLowerCase());
    });
    var audaciousFn;
    Handlebars.registerHelper('recursive', function(children, options) {
        var out = '';
        if (options.fn !== undefined) {
            audaciousFn = options.fn;
        }
        children.forEach(function(child){
            out = out + audaciousFn(child);
        });
        return out;
    });
    Handlebars.registerHelper('encodeAsHtml', function(value) {
        value = convertHTMLRegisterHelper(value);
        return new Handlebars.SafeString(value);
    });
    Handlebars.registerHelper('pastSceneTime', function(pastscene) {
        var res = "";
        var globalList = buildDateList(pastscene);
        res = buildRelativeString(globalList, pastscene, res);
        if (globalList.relativeList.length != 0 && globalList.absoluteList.length != 0) {
            res += ", ";
        }
        res = buildAbsoluteString(globalList, res, pastscene);
        res += " - ";
        return res;
    });
    var template = Handlebars.templates['templates/redactIntrigue/rolePanel'];
    var context = {
        role: data.role,
        roleTagList: data.roleTagList
    };
    var html = template(context);
    $('.roleScreen > .tab-content').append(html);
    var plotFullscreenEditable = $('.plotScreen .fullScreenEditable').first();
    $('.btn-group', plotFullscreenEditable).clone().prependTo('#role_' + data.role.id + ' .fullScreenEditable');
    $('#role_' + data.role.id + ' #roleType option[value="'+ data.role.type +'"]').attr("selected", "selected");
    for (var key in data.role.tagList) {
        $('#roleTagsModal_' + data.role.id + " #roleTags" + data.role.id + "_" + data.role.tagList[key].id).attr('checked', 'checked');
        $('#roleTagsModal_' + data.role.id + " #roleTagsWeight" + data.role.id + "_" + data.role.tagList[key].id).val(data.role.tagList[key].weight);
    }
    $('#roleTagsModal_' + data.role.id + ' li').each(function() {
        hideTags($('input[type="checkbox"]', $(this)).attr("id"), $(".tagWeight input", $(this)).attr("id"));
    });

    $('.chooseTag').click(function() {
        $('input', $(this).parent().prev()).val(101);
    });

    $('.banTag').click(function() {
        $('input', $(this).parent().next()).val(-101);
    });
    $('.pastSceneScreen div[id*="pastsceneRolesModal"]').each(function() {
        var pastsceneId = $(this).attr("id");
        pastsceneId = pastsceneId.replace("pastsceneRolesModal", "");
        template = Handlebars.templates['templates/redactIntrigue/addRoleInPastScene'];
        context = {
            roleId: data.role.id,
            roleCode: data.role.code,
            pastsceneId: pastsceneId
        };
        html = template(context);
        $(".tab-content", $(this)).append(html);
        $(".leftUl", $(this)).append('<li><a href="#pastsceneRole'+data.role.id+'_'+pastsceneId+'" data-toggle="tab">'+data.role.code+'</a></li>')
    });
    $('.btn-group', plotFullscreenEditable).clone().prependTo('.pastSceneScreen div[id*="pastsceneRole' + data.role.id + '"] .fullScreenEditable');

    $('.pastSceneScreen div[id*="pastsceneRole' + data.role.id + '"] .fullScreenEditable .btnFullScreen').click(function() {
        $(this).parent().parent().toggleClass("fullScreenOpen");
    });
    $('.eventScreen div[id*="eventRolesModal"]').each(function() {
        var eventId = $(this).attr("id");
        eventId = eventId.replace("eventRolesModal", "");
        $(".leftUl", $(this)).append('<li><a href="#eventRole'+data.role.id+'_'+eventId+'" data-toggle="tab">'+data.role.code+'</a></li>');
        template = Handlebars.templates['templates/redactIntrigue/addRoleInEvent'];
        context = {
            roleId: data.role.id,
            roleCode: data.role.code,
            eventId: eventId,
            resourceList: data.role.resourceList
        };
        html = template(context);
        $(".tab-content:first", $(this)).append(html);
//        $('.eventScreen #eventRolesModal tbody').first().clone().appendTo($('#eventRole'+data.role.id+'_'+eventId+' table', $(this)));
    });
    $('.btn-group', plotFullscreenEditable).clone().prependTo('.eventScreen div[id*="eventRole' + data.role.id + '"] .fullScreenEditable');

    $('.eventScreen div[id*="eventRole' + data.role.id + '"] .fullScreenEditable .btnFullScreen').click(function() {
        $(this).parent().parent().toggleClass("fullScreenOpen");
    });
    initializePopover();
}

function updateRoleRelation(data) {
    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiRoleRelation'];
    var context = {
        roleId: String(data.role.id),
        roleName: data.role.code
    };
    var html = template(context);
    $('.relationScreen > ul').append(html);
    $('select[name="relationFrom"]').append('<option value="' + data.role.id + '">' + data.role.code + '</option>');
    $('select[name="relationTo"]').append('<option value="' + data.role.id + '">' + data.role.code + '</option>');
    $('select[name="resourceRolePossessor"]').append('<option value="' + data.role.id + '">' + data.role.code + '</option>');
    $('select[name="resourceRoleFrom"]').append('<option value="' + data.role.id + '">' + data.role.code + '</option>');
    $('select[name="resourceRoleTo"]').append('<option value="' + data.role.id + '">' + data.role.code + '</option>');
    $('.relationScreen .tab-content').append('<div class="tab-pane" id="roleRelation_'+data.role.id+'">'
    + '<div class="accordion" id="accordionRelation'+data.role.id+'"></div></div>');
}