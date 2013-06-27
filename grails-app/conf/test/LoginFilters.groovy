package test

class LoginFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                    def actionPassedList = ['login', 'loginCheck', 'logout', 'register', 'save']
                    if(!session.userId && !actionPassedList.contains(actionName)){
                        redirect(controller: 'user', action: 'login')
                        return
                    }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
