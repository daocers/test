package co.bugu

import org.springframework.dao.DataIntegrityViolationException

class UserController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [userInstanceList: User.list(params), userInstanceTotal: User.count()]
    }

    def create() {
        [userInstance: new User(params)]
    }

    def save() {
        def userInstance = new User(params)
        if (!userInstance.save(flush: true)) {
            render(view: "create", model: [userInstance: userInstance])
            return
        }


        flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])
        redirect(action: "show", id: userInstance.id)
    }

    def show() {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
            redirect(action: "list")
            return
        }

        [userInstance: userInstance]
    }

    def edit() {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
            redirect(action: "list")
            return
        }

        [userInstance: userInstance]
    }

    def update() {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (userInstance.version > version) {
                userInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'user.label', default: 'User')] as Object[],
                        "Another user has updated this User while you were editing")
                render(view: "edit", model: [userInstance: userInstance])
                return
            }
        }

        userInstance.properties = params

        if (!userInstance.save(flush: true)) {
            render(view: "edit", model: [userInstance: userInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])
        redirect(action: "show", id: userInstance.id)
    }

    def delete() {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
            redirect(action: "list")
            return
        }

        try {
            userInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])
            redirect(action: "show", id: params.id)
        }
    }

    def register(){

    }

    def regist(RegisterCommand cmd){
        def userInstance = new User(params)

        if (!userInstance.hasErrors() && userInstance.validate() && !cmd.hasErrors()){
            userInstance.password = userInstance.password.encodeAsPassword()
            if (userInstance.save()){
                flash.message =
                    message(code: 'user.register.success', args: [userInstance.userName])

                def eventInstance = new Event(title: 'welcome', description: 'welcome to use my web calendar',
                    allDay: true, startDate: new Date(), endDate: new Date())

                if (eventInstance.save(flush: true)){
                    println("***************")
                    userInstance.addToEvents(eventInstance)

                    session.userId = userInstance.id
                    session.userName = userInstance.userName
                    redirect(controller: 'event', action: 'index')
                    return
                }




            }
        }
        render(view: 'register',model: [userInstance:userInstance,cmd:cmd])

    }

    def login(){

    }

    def loginCheck(){
//        def userInstance = User.findByUserNameAndPassword(params.userName, params.password)
        def userInstance = User.findByUserNameAndPassword(params.userName, params.password.encodeAsPassword())
        if (userInstance){
            if(userInstance.id.equals(session.userId)) {
                redirect(controller: 'event', action: 'index')
                flash.message = 'you have been login'
                return
            }

            session.userId = userInstance.id
            session.userName = userInstance.userName
            flash.message =
                'login Successful'
            redirect(action: 'index', controller: 'event')
        }else{
            flash.message =
                message(code: 'login.failed')
            redirect(action: 'login')
        }

    }

    def logout(){
        session.invalidate()
        redirect(action: 'login')
    }
}

class RegisterCommand{
    String password
    String passwordAgain

    static constraints = {
        passwordAgain(validator:{
            val, obj->
                if(!val.equals(obj.password)){
                    return 'different'
                }
        })
    }
}
