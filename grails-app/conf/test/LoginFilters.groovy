package test

class LoginFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                    def actionPassedList = ['login', 'loginCheck', 'logout', 'register', 'regist','save']
                    if(!session.userId && !actionPassedList.contains(actionName)){
                        redirect(controller: 'user', action: 'login')
                    }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
