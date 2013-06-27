package test

import co.bugu.Event
import co.bugu.EventType

class EventService {

    def serviceMethod() {

    }

    def findOccurrencesDateInRange(Event event, Date startRange, Date endRange){

        def dates = []     //当前事件在时间范围内所有发生的日期的list

        Date dateOccur  //发生的时间
        if(!event.isRecurring){  //不循环事件
            if(event.startDate < endRange && event.startDate > startRange){
                dates.add(event.startDate)
            }
        }else{      //循环事件
            Date dateTemp
            if(event.isRecurring){ //只有循环事件才有下一次出现时间
                if(event.type == EventType.DAILY){
                    dateTemp = this.addDays(startRange, 1)
                    while(dateTemp < endRange){
                        dates.add(dateTemp)
                        dateTemp = this.addDays(dateTemp, 1)
                    }
                }
                if(event.type == EventType.WEEKLY){
                    dateTemp = this.addDays(event.startDate, 7)
                    while(dateTemp < startRange){
                        dates.add(dateTemp)
                        dateTemp = this.addDays(dateTemp, 7)
                    }
                }
                if(event.type == EventType.MONTHLY){
                    dateTemp = this.addMonths(event.startDate, 1)
                    while(dateTemp < startRange){
                        dates.add(dateTemp)
                        dateTemp = this.addMonths(dateTemp, 1)
                    }
                }
                if(event.type == EventType.YEARLY){
                    dateTemp = this.addYears(event.startDate, 1)
                    while(dateTemp < startRange){
                        dates.add(dateTemp)
                        dateTemp = this.addYears(dateTemp, 1)
                    }
                }

            }

        }

        return dates


    }

    Date findNextOccurrenceTime(Event event, Date startRange, Date endRange){
        Date dateTemp
        if(event.isRecurring){ //只有循环事件才有下一次出现时间
            if(event.type == EventType.DAILY){
                dateTemp = this.addDays(startRange, 1)
            }
            if(event.type == EventType.WEEKLY){
                dateTemp = this.addDays(event.startDate, 7)
//                while(dateTemp < startRange){
//                    dateTemp = this.addDays(dateTemp, 7)
//                }
            }
            if(event.type == EventType.MONTHLY){
                dateTemp = this.addMonths(event.startDate, 1)
//                while(dateTemp < startRange){
//                    dateTemp = this.addMonths(dateTemp, 1)
//                }
            }
            if(event.type == EventType.YEARLY){
                dateTemp = this.addYears(event.startDate, 1)
//                while(dateTemp < startRange){
//                    dateTemp = this.addYears(dateTemp, 1)
//                }
            }

            if(dateTemp > endRange){
                dateTemp = null
            }

            return  dateTemp
        }
    }




    Date addDays(Date date, int days){
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.add(Calendar.DATE, days)

        return calendar.getTime()
    }


    Date addMonths(Date date, int months){
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.add(Calendar.MONTH, months)

        return  calendar.getTime()
    }

    Date addYears(Date date, int years){
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.add(Calendar.YEAR, yearss)

        return  calendar.getTime()
    }
}
