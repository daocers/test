package co.bugu

class Event {
    String title
    String description
    Date startDate      //开始提醒日期 如果不循环就是时间发生日期
    Date endDate        //结束日期
    Boolean allDay
    static belongsTo = [user: User]




    static constraints = {
        title()
        description()
        allDay()
        startDate()
        endDate()
        user(nullable: true)

    }

    def beforeInsert(){
//        beforeExecute();
    }




}

enum EventType {DAILY, WEEKLY, MONTHLY, YEARLY}
