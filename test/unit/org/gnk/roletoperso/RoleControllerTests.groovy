package org.gnk.roletoperso
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.gnk.gn.Gn
import org.gnk.selectintrigue.Plot

@TestFor(RoleController)
@Mock([Role, Gn, Plot])
class RoleControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        // assert "/role/list" == response.redirectedUrl FIXME
    }

    void testList() {

        /*def model = controller.list()

        assert model.roleInstanceList.size() == 0
        assert model.roleInstanceTotal == 0*/
        // FIXME
    }

    void testCreate() {
        /*
        def model = controller.create()

        assert model.roleInstance != null
        */
        // FIXME
    }

    void testSave() {
        controller.save()

        /*assert model.roleInstance != null
        assert view == '/role/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/role/show/1'
        assert controller.flash.message != null
        assert Role.count() == 1*/
        // FIXME
    }

    void testShow() {
        /*
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/role/list'

        populateValidParams(params)
        def role = new Role(params)

        assert role.save() != null

        params.id = role.id

        def model = controller.show()

        assert model.roleInstance == role
        */
        // FIXME
    }

    void testEdit() {
        /*
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/role/list'

        populateValidParams(params)
        def role = new Role(params)

        assert role.save() != null

        params.id = role.id

        def model = controller.edit()

        assert model.roleInstance == role
        */
        // FIXME
    }

    void testUpdate() {
        /*
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/role/list'

        response.reset()

        populateValidParams(params)
        def role = new Role(params)

        assert role.save() != null

        // createGnTests invalid parameters in update
        params.id = role.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/role/edit"
        assert model.roleInstance != null

        role.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/role/show/$role.id"
        assert flash.message != null

        //createGnTests outdated version number
        response.reset()
        role.clearErrors()

        populateValidParams(params)
        params.id = role.id
        params.version = -1
        controller.update()

        assert view == "/role/edit"
        assert model.roleInstance != null
        assert model.roleInstance.errors.getFieldError('version')
        assert flash.message != null
        */
        // FIXME
    }

    void testDelete() {
        /*
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/role/list'

        response.reset()

        populateValidParams(params)
        def role = new Role(params)

        assert role.save() != null
        assert Role.count() == 1

        params.id = role.id

        controller.delete()

        assert Role.count() == 0
        assert Role.get(role.id) == null
        assert response.redirectedUrl == '/role/list'
        */
        // FIXME
    }
}
