package org.gnk.substitution

import org.gnk.parser.GNKDataContainerService
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericPlaceHasTag
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericResourceHasTag
import org.gnk.resplacetime.PlaceHasTag
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.roletoperso.RoleHasRelationWithRole
import org.gnk.substitution.data.*;
import org.gnk.gn.Gn

class InputHandler {

    GnInformation gnInfo
    List<Character> characterList
    List<Resource> resourceList
    List<Place> placeList
    List<Pastscene> pastsceneList
    List<Event> eventList
    org.gnk.ressplacetime.GenericResource genericResource

    public void parseGN(String gnString) {
        // Reader
        GNKDataContainerService gnkDataContainerService = new GNKDataContainerService()
        gnkDataContainerService.ReadDTD(gnString)
        Gn gnInst = gnkDataContainerService.gn
        gnInst.id = -1
        createData(gnInst)
    }

    public void parseGN(Gn gn, List<String> sexes) {
        // Reader
        GNKDataContainerService gnkDataContainerService = new GNKDataContainerService()
        gnkDataContainerService.ReadDTD(gn)
        Gn gnInst = gnkDataContainerService.gn
        changeCharSex(gn, sexes)
        createData(gnInst)
    }

    private void changeCharSex(Gn gn, List<String> sexes) {
        for (org.gnk.roletoperso.Character c in gn.getterCharacterSet()) {
            String sex = sexes.find { it.toString().startsWith(c.getDTDId() + "-") };
            if ((sex != null) && (sex != "false") && (sex != "") && (sex != "NO")) {
                switch (sex.split("-")[1]) {
                    case "Homme":
                        c.setGender("M");
                        break;
                    case "Femme":
                        c.setGender("F");
                        break;
                    case "Neutre":
                        c.setGender("N");
                        break;
                }
            }
        }
        for (org.gnk.roletoperso.Character c in gn.getNonPlayerCharSet()) {
            String sex = sexes.find { it.toString().startsWith(c.getDTDId() + "-") };
            if ((sex != null) && (sex != "false") && (sex != "") && (sex != "NO")) {
                switch (sex.split("-")[1]) {
                    case "Homme":
                        c.setGender("M");
                        break;
                    case "Femme":
                        c.setGender("F");
                        break;
                    case "Neutre":
                        c.setGender("N");
                        break;
                }
            }
        }
    }

    private createData(Gn gnInst) {
        // GnInformation construction
        createGnInformation(gnInst)

        // CharacterList construction
        createCharacterList(gnInst)

        // ResourceList construction
        createResourceList(gnInst)

        // PlaceList construction
        createPlaceList(gnInst)

        // PastsceneList construction
        createPastsceneList(gnInst)

        // EventList construction
        createEventList(gnInst)
    }

    // GnInfo
    private void createGnInformation(Gn gnInst) {
        gnInfo = new GnInformation()

        // Database id
        gnInfo.dbId = gnInst.id
        // Title
        gnInfo.title = gnInst.name
        // Creation date
        gnInfo.creationDate = gnInst.dateCreated
        // Last update date
        gnInfo.lastUpdateDate = gnInst.lastUpdated
        // Nb players
        gnInfo.nbPlayers = gnInst.nbPlayers
        // Universe
        gnInfo.universe = gnInst.univers.name
        // T0 date
        gnInfo.t0Date = gnInst.t0Date
        // Duration
        gnInfo.duration = gnInst.duration
        // Tags
        gnInfo.tagList = []
        for (el in gnInst.gnTags) {
            Tag tagData = new Tag()
            tagData.value = el.key.name
            tagData.family = el.key.parent.name
            tagData.weight = el.value
            gnInfo.tagList.add(tagData)
        }
    }

    // CharacterList
    private void createCharacterList(Gn gnInst) {
        characterList = []

        for (character in gnInst.characterSet) {
            Character characterData = new Character()

            // Id
            characterData.id = character.DTDId
            // Gender
            characterData.gender = character.gender
            // Type
            characterData.type = character.type

            // TagList
            characterData.tagList = []
            for (el in character.getTags()) {
                Tag tagData = new Tag()
                tagData.value = el.key.name
                tagData.family = el.key.parent.name
                tagData.weight = el.value
                characterData.tagList.add(tagData)
            }

            // RoleList
            characterData.roleList = []

            // RelationList
            characterData.relationList = []
            String r1 = ""
            String r2 = ""
            for (RoleHasRelationWithRole rrr : character.getRelations(false)?.keySet()) {
                if ((rrr.getterRoleRelationType().name).equals("Filiation")
                        || (rrr.getterRoleRelationType().name).equals("Parent (direct)")
                        || (rrr.getterRoleRelationType().name).equals("Mariage")) {
                    RelationCharacter relationChar = new RelationCharacter()
                    r1 = gnInst.getAllCharacterContainingRole(rrr.getterRole1())?.DTDId
                    r2 = gnInst.getAllCharacterContainingRole(rrr.getterRole2())?.DTDId
                    if (!r1.equals("") && !r2.equals("")) {
                        relationChar.type = rrr.getterRoleRelationType().name
                        relationChar.role1 = r1
                        relationChar.role2 = r2
                        relationChar.isHidden = rrr.isHidden
                        relationChar.isBijective = rrr.isBijective
                        characterData.relationList.add(relationChar)
                    }
                }
            }

            characterList.add(characterData)
        }
        for (character in gnInst.nonPlayerCharSet) {
            Character characterData = new Character()

            // Id
            characterData.id = character.DTDId
            // Gender
            characterData.gender = character.gender
            // Type
            characterData.type = character.type

            // TagList
            characterData.tagList = []
            for (el in character.getTags()) {
                Tag tagData = new Tag()
                tagData.value = el.key.name
                tagData.family = el.key.parent.name
                tagData.weight = el.value
                characterData.tagList.add(tagData)
            }

            // RoleList
            characterData.roleList = []

            // RelationList
            characterData.relationList = []
            String r1 = ""
            String r2 = ""
            for (RoleHasRelationWithRole rrr : character.getRelations(false)?.keySet()) {
                if ((rrr.getterRoleRelationType().name).equals("Filiation")
                        || (rrr.getterRoleRelationType().name).equals("Parent (direct)")
                        || (rrr.getterRoleRelationType().name).equals("Mariage")) {
                    RelationCharacter relationChar = new RelationCharacter()
                    r1 = gnInst.getAllCharacterContainingRole(rrr.getterRole1())?.DTDId
                    r2 = gnInst.getAllCharacterContainingRole(rrr.getterRole2())?.DTDId
                    if (!r1.equals("") && !r2.equals("")) {
                        relationChar.type = rrr.getterRoleRelationType().name
                        relationChar.role1 = r1
                        relationChar.role2 = r2
                        relationChar.isHidden = rrr.isHidden
                        relationChar.isBijective = rrr.isBijective
                        characterData.relationList.add(relationChar)
                    }
                }
            }
            characterList.add(characterData)
        }
    }

    // ResourceList
    private void createResourceList(Gn gnInst) {
        resourceList = []

        // Iterate generic resources
        for (plot in gnInst.selectedPlotSet) {
            String plotId = plot.DTDId as String

            for (GenericResource genericResource : plot.genericResources) {
                Resource resource = new Resource()
                // Id
                resource.id = genericResource.DTDId
                // Plot id
                resource.plotId = plotId
                // Code
                resource.code = genericResource.code
                // Comment
                resource.comment = genericResource.comment
                // Plot name
                resource.plot = plot.name
                // ObjectType
                resource.objectType = genericResource.objectType.type

                // TagList
                resource.tagList = []
                if (genericResource.extTags) {
                    for (GenericResourceHasTag genericResourceHasTag : genericResource.extTags) {
                        Tag tagData = new Tag()

                        tagData.value = genericResourceHasTag.tag.name
                        tagData.family = genericResourceHasTag.tag.parent.name
                        tagData.weight = genericResourceHasTag.weight as Integer

                        resource.tagList.add(tagData)
                    }
                }
                resourceList.add(resource)
            }
        }
    }

    // PlaceList
    private void createPlaceList(Gn gnInst) {
        placeList = []

        for (plot in gnInst.selectedPlotSet) {
            String plotId = plot.DTDId as String
            for (pastScene in plot.pastescenes) {
                GenericPlace genericPlace = pastScene.genericPlace
                if (genericPlace != null && !isGenericPlaceInList(placeList, plotId, genericPlace.DTDId as String)) {
                    placeList.add(createPlace(genericPlace, plotId))
                }
            }
            for (event in plot.events) {
                GenericPlace genericPlace = event.genericPlace
                if (genericPlace != null && !isGenericPlaceInList(placeList, plotId, genericPlace.DTDId as String)) {
                    placeList.add(createPlace(genericPlace, plotId))
                }
            }
        }
    }

    private Boolean isGenericPlaceInList(List<Place> placeList, String plotId, String genericPlaceId) {
        for (genericPlace in placeList) {
            if (genericPlace.plotId == plotId && genericPlace.id == genericPlaceId) {
                return true;
            }
        }
        return false;
    }

    public Place createPlace(GenericPlace genericPlace, String plotId) {
        Place place = new Place();
        // Id
        place.id = genericPlace.DTDId
        // Plot id
        place.plotId = plotId
        // Code
        place.code = genericPlace.code
        // Comment
        place.comment = genericPlace.comment
        // ObjectType
        place.objectType = genericPlace.objectType.type
//        // In Gane
//        place.isInGame = genericPlace.

        // TagList
        place.tagList = []
        if (genericPlace.extTags) {
            for (GenericPlaceHasTag genericPlaceHasTag : genericPlace.extTags) {
                Tag tagData = new Tag()
                tagData.value = genericPlaceHasTag.tag.name
                tagData.family = genericPlaceHasTag.tag.parent.name
                tagData.weight = genericPlaceHasTag.weight as Integer

                place.tagList.add(tagData)
            }
        }

        // Plot
        def plot = org.gnk.selectintrigue.Plot.get(plotId)
        // Plot name
        place.plotName = plot.name

        return place
    }

    // Pastscene
    private void createPastsceneList(Gn gnInst) {
        pastsceneList = []

        for (plot in gnInst.selectedPlotSet) {
            String plotId = plot.DTDId as String
            for (pastscene in plot.pastescenes) {
                Pastscene pastsceneData = new Pastscene()

                // Id
                pastsceneData.id = pastscene.DTDId
                // Plot id
                pastsceneData.plotId = plotId
                //Plot name
                pastsceneData.plotName = plot.name
                // Title
                pastsceneData.title = pastscene.title
                // Relative time
                pastsceneData.relativeTime = pastscene.timingRelative
                pastsceneData.relativeTimeUnit = pastscene.unitTimingRelative
                // Absolute time
                pastsceneData.absoluteYear = pastscene.dateYear
                pastsceneData.absoluteMonth = pastscene.dateMonth
                pastsceneData.absoluteDay = pastscene.dateDay
                pastsceneData.absoluteHour = pastscene.dateHour
                pastsceneData.absoluteMin = pastscene.dateMinute

                pastsceneData.isYearAbsolute = pastscene.isAbsoluteYear
                pastsceneData.isMonthAbsolute = pastscene.isAbsoluteMonth
                pastsceneData.isDayAbsolute = pastscene.isAbsoluteDay
                pastsceneData.isHourAbsolute = pastscene.isAbsoluteHour
                pastsceneData.isMinuteAbsolute = pastscene.isAbsoluteMinute

                pastsceneList.add(pastsceneData)
            }
        }
    }

    // Event
    private void createEventList(Gn gnInst) {
        eventList = []

        for (plot in gnInst.selectedPlotSet) {
            String plotId = plot.DTDId as String
            for (event in plot.events) {
                Event eventData = new Event()

                // Id
                eventData.id = event.DTDId
                // Plot id
                eventData.plotId = plotId
                //Plot name
                eventData.plotName = plot.name
                // Title
                eventData.title = event.name
                // Is planned
                eventData.isPlanned = event.isPlanned
                // Timing
                eventData.timing = event.timing

                eventList.add(eventData)
            }
        }
    }
}
