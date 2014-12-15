package org.gnk.tag

import org.gnk.administration.DbCoherenceController
import org.springframework.security.access.annotation.Secured
import org.gnk.tag.TagService
import org.springframework.web.context.request.RequestContextHolder

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class TagController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    TagService tagService
    def index() {
        redirect(action: "list", params: "params")
    }

    def list(Integer max, Integer offset, String sort) {
        List<Tag> tags = Tag.list();
        Map<Integer, ArrayList<Tag>> mapTagParent = new HashMap<Integer, ArrayList>();
        for (Tag tag : tags) {
            ArrayList<Tag> tagParent = new ArrayList<Tag>();
            Tag t = tag.parent;
            while (t != null) {
                if (!"".equals(tag.name)) {
                    tagParent.add(t);
                }
                t = t.parent;
            }
            tagParent = tagParent.reverse();
            mapTagParent.put(tag.id, tagParent);
        }
        Tag parent = Tag.get(params.Tag_select as Integer)
        String parentName = parent == null ? "" : parent.name

        if (params.showChildren)
            {
                session.tagParent = parent.id

                List<Tag> resultList = new ArrayList<Tag>()
                resultList.addAll(parent.children)
                [ tagInstanceList: resultList, genericTags: new TagService().getGenericChilds(), tagParent: parentName ]
            }
        else
        {
            max = max ?: 10
            offset = offset ?: 0
            sort = sort ?: 'name'
            params.order = params.order ?: 'asc'

            def resultList = Tag.createCriteria().list(max: max, offset: offset) {
                if (sort.indexOf('tagFamily.') == 0) {
                    tagFamily {
                        order(sort.split('\\.')[1], params.order)
                    }
                } else
                    order(sort, params.order as String)
            }
            [ tagInstanceList: resultList, genericTags: new TagService().getGenericChilds(), tagParent: parentName , listTagParent:mapTagParent]
        }
    }

    def childrenList(Tag t)
    {
        t = t ?: params.Tag_select

        def resultList = findChildren(t)

        [tagInstanceList: resultList]
    }

    def findChildren(Tag t) {

        def tags = Tag.findAllWhere(parent: t)
        def tagsTmp = new ArrayList()
        tagsTmp.addAll(tags)
        if (tagsTmp == null)
            return tags
        for (Tag tag : tagsTmp) {
            tags.addAll(findChildren(tag))
        }
        return tags
    }
	
//	def listFrom(String tagFamily) {
//		TagFamily family
//		List<Tag> result = []
//
//		/* Get the TagFamily */
//		family = TagFamily.createCriteria().get {
//			eq ('value', tagFamily)
//		}
//		/* Get all Tags corresponding to the TagFamily */
//		result = Tag.createCriteria().list {
//			eq ('tagFamily', TagFamily)
//		}
//
//		result
//	}

    def create() {
		
        [tagInstance: new Tag(params)]
    }

    def save() {
		if (params.name.equals(""))
		{
			print "Invalid params"
			flash.message = message(code: 'Erreur : Le nom du tag ne peut etre vide !')
            redirect(action: "list")
            return
		}
		
//		if (params.TagFamily_select.equals(""))
//		{
//			flash.message = message(code: 'Erreur : Il faut choisir un parent pour le tag !')
//			redirect(action: "list")
//			return
//		}

        Tag tagInstance = new Tag(params)
        //Tag parent = Tag.findWhere(name: params.Tag_parent)
        Integer parentId = session.tagParent as Integer
        print("TAGPARENTID: " + parentId)
        Tag parent = Tag.findWhere(id: parentId)

        if (parent == null)
            return

        print("Tag parent: " + params.Tag_parent)
        tagInstance.setParent(parent)
		
		for (Tag tag : Tag.list()) 
		{
			if (tagInstance.name.toLowerCase().equals(tag.name.toLowerCase()))
			{
				print "Tag already exist"
				flash.message = message(code: 'Erreur : Un tag avec le meme nom existe deja !')
	            redirect(action: "list")
	            return
			}
		}

//		TagFamily tagFamilyInstance = null;
//		tagFamilyInstance = TagFamily.get(tagFamilyId)
//
//		if (tagFamilyInstance.equals(null))
//		{
//			print "Family not found"
//			flash.message = message(code: 'Erreur : Cette famille n\'existe pas !')
//            redirect(action: "list")
//            return
//		}
//
//		// Creates the relation between the tag and the family
//		tagInstance.tagFamily = tagFamilyInstance
		
        if (!tagInstance.save(flush: true)) {
            render(view: "create", model: [tagInstance: tagInstance])
            return
        }

        
        flash.messageInfo = message(code: 'adminRef.tag.info.create', args: [tagInstance.name, parent.name])
        redirect(action: "list")
    }

    private void isTagApplicable(Tag tagInstance) {
		// FIXME Changement de Base
		/*
        if (params.isPlotTag) {
            PlotTag plotTag = new PlotTag()
            plotTag.tag = tagInstance
            plotTag.save()
        }
        if (params.isRoleTag) {
            RoleTag roleTag = new RoleTag()
            roleTag.tag = tagInstance
            roleTag.save()
        }
        if (params.isResourceTag) {
            ResourceTag resourceTag = new ResourceTag()
            resourceTag.tag = tagInstance
            resourceTag.save()
        }
        */
    }

//    def addTagIntoFamily()
//	{
//		if (params.TagFamily_select.equals("") || params.Tag_select.equals(""))
//		{
//				print "Invalid params"
//				flash.message = message(code: 'Erreur : Il faut choisir un tag et une famille !')
//				redirect(action: "list")
//				return
//		}
//
////		TagFamily tagFamilyInstance = null;
//		Tag tagInstance = null;
////		for (TagFamily tagFamily : TagFamily.list())
////		{
////			if (tagFamily.id == params.TagFamily_select.toInteger())
////			{
////				tagFamilyInstance = tagFamily
////				break
////			}
////		}
//
//		for (Tag tag : Tag.list())
//		{
//			if (tag.id == params.Tag_select.toInteger())
//			{
//				tagInstance = tag
//				break
//			}
//		}
//
//		if (tagFamilyInstance.equals(null) || tagInstance.equals(null))
//		{
//			print "Family or Tag not found"
//			flash.message = message(code: 'Erreur : Tag ou famille invalide !')
//			redirect(action: "list")
//			return
//		}
//
//		tagInstance.tagFamily = tagFamilyInstance
//		tagInstance.save()
//		flash.messageInfo = message(code: 'adminRef.tagIntoTagFamily.info.create', args: [tagInstance.name, tagFamilyInstance.value])
//
//		redirect(action: "list")
//	}

//    def viewChildren() {
//
//        Tag tagInstance = null;
//
//        if (params.Tag_select.equals("")) {
//            print "Invalid params"
//            flash.message = message(code: 'Erreur : Il faut choisir un tag et son parent!')
//            redirect(action: "list")
//            return
//        }
//
//        tagInstance = Tag.findWhere(id: (int) params.Tag_select)
//
//        List<Tag> children = tagInstance.children
//        list(children)
//
//    }
	
    def show(Long id) {
        def tagInstance = Tag.get(id)
        if (!tagInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "list")
            return
        }

        [tagInstance: tagInstance]
    }

    def edit(Long id) {
        def tagInstance = Tag.get(id)
        if (!tagInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "list")
            return
        }

        [tagInstance: tagInstance]
    }

    def update(Long id, Long version) {
        def tagInstance = Tag.get(id)
        if (!tagInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (tagInstance.version > version) {
                tagInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'tag.label', default: 'Tag')] as Object[],
                          "Another user has updated this Tag while you were editing")
                render(view: "edit", model: [tagInstance: tagInstance])
                return
            }
        }

        tagInstance.properties = params

        if (!tagInstance.save(flush: true)) {
            render(view: "edit", model: [tagInstance: tagInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'tag.label', default: 'Tag'), tagInstance.id])
        redirect(action: "show", id: tagInstance.id)
    }

    def delete(Long id) {
        def tagInstance = Tag.get(id)
        if (!tagInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "list")
            return
        }
		// FIXME Changement de Base
		/*
        try {
            tagInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "show", id: id)
        }*/
    }
	
//	def deleteFamily(Long idTag, Long idFamily) {
//		TagFamily tagFamilyInstance = null;
//		Tag tagInstance = null;
//		for (TagFamily tagFamily : TagFamily.list())
//		{
//			if (tagFamily.id == idFamily)
//			{
//				tagFamilyInstance = tagFamily
//				break
//			}
//		}
//
//		for (Tag tag : Tag.list())
//		{
//			if (tag.id == idTag)
//			{
//				tagInstance = tag
//				break
//			}
//		}
//
//		if (tagFamilyInstance.equals(null) || tagInstance.equals(null))
//		{
//			print "Family or Tag not found"
//			flash.message = message(code: 'Erreur : Famille ou tag incorrecte !')
//			redirect(action: "list")
//			return
//		}
//
//		if (tagInstance.tagFamily.id.equals(idFamily) )
//		{
//			tagInstance.tagFamily = null
//            tagFamilyInstance.removeFromTags(tagInstance)
//            tagFamilyInstance.save(flush: true)
//			tagInstance.save()
//			flash.messageInfo = message(code: 'adminRef.tagIntoTagFamily.info.delete', args: [tagInstance.name, tagFamilyInstance.value])
//
//			redirect(action: "list")
//			return
//		}
//
//		print("The family was not deleted. Possible error ?")
//		redirect(action: "list")
//	}
	
	def deleteTag(Long idTag) {

		def tagInstance = Tag.get(idTag)
		print "name " + tagInstance.name
		tagInstance.delete()
		flash.messageInfo = message(code: 'adminRef.tag.info.delete', args: [tagInstance.name])
		redirect(action: "list")
	}
	
	def stats(){
		render(view: "statistics")
	}

    def editRelevantTag(int id){

        Tag tag = Tag.findById(id)
        TagRelevant tagRelevant = tag.getTagRelevant()
        if(params.checkboxRelevantPlace){
            tagRelevant.relevantPlace = true;
        } else {
            tagRelevant.relevantPlace = false;
        }
        if(params.checkboxRelevantFirstName){
            tagRelevant.relevantFirstname = true;
        } else {
            tagRelevant.relevantFirstname = false;
        }
        if(params.checkboxRelevantLastname){
            tagRelevant.relevantLastname = true;
        } else {
            tagRelevant.relevantLastname = false;
        }
        if(params.checkboxRelevantPlot){
            tagRelevant.relevantPlot= true;
        } else {
            tagRelevant.relevantPlot = false;
        }
        if(params.checkboxRelevantResource){
            tagRelevant.relevantResource = true;
        } else {
            tagRelevant.relevantResource = false;
        }
        if(params.checkboxRelevantRole){
            tagRelevant.relevantRole = true;
        } else {
            tagRelevant.relevantRole = false;
        }
        redirect(action : "list")
    }

    def statistics(){
        List<Tag> tagUnivers = tagService.universTagQuery;
        Map<Integer, ArrayList<Tag>> mapTagParent = new HashMap<Integer, ArrayList>();
        List<Tag> tags = Tag.list();
        for (Tag tag : tags) {
            ArrayList<Tag> tagParent = new ArrayList<Tag>();
            Tag t = tag.parent;
            while (t != null) {
                if (!"".equals(tag.name)) {
                    tagParent.add(t);
                }
                t = t.parent;
            }
            tagParent = tagParent.reverse();
            mapTagParent.put(tag.id, tagParent);
        }
        [listTagParent:mapTagParent, tagUniverse: tagUnivers]
    }
}
