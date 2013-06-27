package co.bugu

class User {
    String userName
    String password

    static  hasMany = [events: Event]

    static constraints = {
        userName(blank: false, nullable: false)
        password(blank: false, nullable: false, password: true)
        events(nullable: true)
    }
}
