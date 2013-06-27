import co.bugu.Event
import co.bugu.EventType
import co.bugu.User

class BootStrap {

    def init = { servletContext ->
        def user = new User(userName: 'test', password: 'test'.encodeAsPassword())

        if(user.save(flush: true)){
            println('user has been saved!')
            def eventInstance = new Event(title: 'welcome', description: 'welcom to use my webCalendar',
                    allDay: true, startDate: new Date(), endDate: new Date()).save()

            user.addToEvents(eventInstance).save()
            println('event has been saved!')
        }


    }
    def destroy = {
    }
}
