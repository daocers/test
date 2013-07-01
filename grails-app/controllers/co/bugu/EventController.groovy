package co.bugu


import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON
import java.text.SimpleDateFormat

class EventController {

    def eventService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
//        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        List eventInstanceList = Event.findAllByUser(User.get(session.userId))
        [eventInstanceList: eventInstanceList, eventInstanceTotal: Event.count()]
    }

    def listAsJson() {
        def  startRange
        if (params.start){
            startRange = new Date(Long.parseLong(params.start))
        }

        def endRange
        if (params.end){
            endRange = new Date(Long.parseLong(params.end))
        }

        def userInstance = User.get(session.userId)
        if (!userInstance){
            flash.message = 'please login'
            redirect(controller: 'user', action: 'login')
            return
        }
        def events = Event.withCriteria {
            and{
                between("startDate", startRange, endRange)
                user{
                    eq('id', session.userId)
                }
            }

        }
//        println(session.userId + "))))))))")
//        println(User.get(session.userId).userName + "((((((")
//        def events = Event.findByUser(User.get(session.userId))

        def eventList = []//声明一个list，对查询到的event进行封装

        events.each { event ->
                eventList << [
                        id: event.id,
                        title: event.title,
                        start: event.startDate,
                        end: event.endDate,
                        description: event.description
                ]
        }

//        println(eventList.size() + "ooooooooooooo")
        render eventList as JSON
    }

    def create() {
        def eventInstance = new Event(params)
        def userInstance = User.get(session.userId)
        userInstance.addToEvents(eventInstance)
        [eventInstance: new Event(params)]
    }

    def save() {

        def eventInstance = new Event(params)
        def now = new Date()
        eventInstance.startDate.hours = now.getHours()
        eventInstance.endDate.hours = now.getHours()
        if (!eventInstance.save(flush: true)) {
            println(eventInstance.errors)
            redirect(view: "create", model: [eventInstance: eventInstance])
            return
        }

        def userInstance = User.get(session.userId)
        userInstance.addToEvents(eventInstance)

        def jsonEvent = [:]
        if (request.xhr){

            jsonEvent.eventId = eventInstance.id
            render jsonEvent as JSON
            return

        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'event.label', default: 'Event'), eventInstance.id])
        redirect(action: "show", id: eventInstance.id)
    }

    def show() {
        def eventInstance = Event.get(params.id)
        if (!eventInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "list")
            return
        } else{

            def model = [eventInstance: eventInstance]
            if (request.xhr){      //ajax请求
//                println(request.requestURI)
                render(template: "showPopup", model: model)
            }  else{
                model
            }
        }


    }

    def edit() {
        def eventInstance = Event.get(params.id)
        if (!eventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "list")
            return
        }

        [eventInstance: eventInstance]
    }

    def update() {
        def eventInstance = Event.get(params.id)
        if (!eventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (eventInstance.version > version) {
                eventInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'event.label', default: 'Event')] as Object[],
                          "Another user has updated this Event while you were editing")
                render(view: "edit", model: [eventInstance: eventInstance])
                return
            }
        }

        eventInstance.properties = params

        if (!eventInstance.save(flush: true)) {
            render(view: "edit", model: [eventInstance: eventInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'event.label', default: 'Event'), eventInstance.id])
        redirect(action: "show", id: eventInstance.id)
    }

    def delete() {
        def eventInstance = Event.get(params.id)
        if (!eventInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "index")
            return
        }

        try {
            eventInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "show", id: params.id)
        }
    }

    def ajaxEdit(){
        def eventInstance = Event.get(params.id)

        def result = [:]
        if (!eventInstance){
            render  "not.found"
        }  else{
            render eventInstance as JSON
        }

    }

    def eventShow(Integer max){

        params.max = Math.min(max ?: 10, 100)
        [eventInstanceList: Event.list(params), eventInstanceTotal: Event.count()]

    }

    def ajaxUpdate(){

        def eventInstance = Event.get(params.id)

        def data=[:]

        if (eventInstance){
            eventInstance.properties = params
            if (eventInstance.save()){
                data.msg = 'Update Success'
            }
        }
        else{
            data.msg = 'Update failed'
        }
    }

    def ajaxDelete(){
            println(params.id)
        def eventInstance = Event.get(params.get(Long.parseLong(params.id)))

        def data = [:]

        if (eventInstance){
            eventInstance.delete()

            data.msg = 'this event has been deleted'

        }else{
            data.msg = "this event doesn't found in database"
            return false
        }

    }

    def deleteWithJson(){
        def result = [result: 'success', message:'the event has been deleted']
        def eventInstance = Event.get(params.id)
        if(eventInstance){
            try {
                eventInstance.delete()
                flash.message = "event deleted"
//                redirect(action: 'list')
            }catch (org.springframework.dao.DataIntegrityViolationException e){
                result.result = "fail"
                result.message = "database error"
            }
        } else{
            result.result = "fail"
            result.message = "event not found"
        }

        render result as JSON

    }

    def updateWithJson(){

        def result = [result: 'success', message:'the event has been update']

        def eventInstance = Event.get(params.id)



        if (eventInstance){
            def now = new Date()
            try{
                eventInstance.properties = params
                eventInstance.startDate.hours = now.hours
                eventInstance.endDate.hours = now.hours

//                result.result = 'fail'
//                result.message = 'update failed'
            }catch (Exception e){
                result.result = 'fail'
                result.message = 'database error'


            }
        }else{
            println(eventInstance.title + "********")
            result.result = 'fail'
            result.message = 'can not find this event in database'
        }

        render result as JSON

    }


}
