package org.gnk.parser.resource

import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericResourceHasTag
import org.gnk.roletoperso.Role
import org.w3c.dom.CDATASection
import org.w3c.dom.Document
import org.w3c.dom.Element

class GenericResourceXMLWriterService {

    /* Exposed Methods */

    def Element getGenericResourceElement(Document doc, GenericResource genericResource, Role role) {
        Element genericResourceElt = getGenericResourceRootElement(doc, genericResource, role);

        genericResourceElt.appendChild(getGenericResourceTagsElement(doc, genericResource));
        genericResourceElt.appendChild(getGenericResourceCommentElement(doc, genericResource));
        if (genericResource.isIngameClue()) {
            genericResourceElt.appendChild(getGenericResourceTitleElement(doc, genericResource));
            genericResourceElt.appendChild(getGenericResourceDescriptionElement(doc, genericResource));
        }

        return genericResourceElt;
    }
    /* !Exposed Methods */

    /* Construction Methods */

    private Element getGenericResourceRootElement(Document doc, GenericResource genericResource, Role role) {
        Element rootElt = doc.createElement("GENERIC_RESOURCE")

        if (!genericResource.DTDId)
            genericResource.DTDId = genericResource.id
        rootElt.setAttribute("id", genericResource.DTDId.toString())
        rootElt.setAttribute("code", genericResource.code)
        if (genericResource.objectType != null)
            rootElt.setAttribute("object_type_id", genericResource.objectType.id.toString())
        else
            rootElt.setAttribute("object_type_id", "null")
        if (role != null) {
            if (role.DTDId == null || role.DTDId < 0)
                role.DTDId = role.id
            assert (role.DTDId != null && role.DTDId >= 0)
            rootElt.setAttribute("owner_role_id", role.DTDId.toString())
        }
        return rootElt
    }


    private Element getGenericResourceTagsElement(Document doc, GenericResource genericResource) {
        Element tagsElt = doc.createElement("TAGS");

        if (genericResource.extTags) {
            for (GenericResourceHasTag genericResourceHasTag : genericResource.extTags) {
                Element tag = doc.createElement("TAG");
                tag.setAttribute("value", genericResourceHasTag.tag.name)
                tag.setAttribute("type", genericResourceHasTag.tag.parent.name)
                tag.setAttribute("weight", genericResourceHasTag.weight.toString())
                tagsElt.appendChild(tag)
            }
        }

        return tagsElt;
    }

    private Element getGenericResourceCommentElement(Document doc, GenericResource genericResource) {
        Element commentElt = doc.createElement("COMMENT")

        if (genericResource.comment) {
            CDATASection commentData = doc.createCDATASection("COMMENT")
            commentData.setData(genericResource.comment)
            commentElt.appendChild(commentData)
        }

        return commentElt
    }

    // Ingame clue Title et description
    private Element getGenericResourceTitleElement(Document doc, GenericResource genericResource) {
        Element titleElt = doc.createElement("TITLE")

        if (genericResource.title) {
            CDATASection titleData = doc.createCDATASection("TITLE")
            titleData.setData(genericResource.title)
            titleElt.appendChild(titleData)
        }

        return titleElt
    }


    private Element getGenericResourceDescriptionElement(Document doc, GenericResource genericResource) {
        Element descriptionElt = doc.createElement("DESCRIPTION")

        if (genericResource.description) {
            CDATASection descriptionData = doc.createCDATASection("DESCRIPTION")
            descriptionData.setData(genericResource.description)
            descriptionElt.appendChild(descriptionData)
        }

        return descriptionElt
    }
}
