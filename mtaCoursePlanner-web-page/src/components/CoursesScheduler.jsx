
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import { useState, useEffect } from 'react';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import "./Schedule.css";

const localizer = momentLocalizer(moment);
// eslint-disable-next-line react/prop-types
const CoursesScheduler = ({reqCoursesArrayName, choiceCoursesArrayName, responseData}) => {

    const dayMap = {
        "א": 0, // Sunday
        "ב": 1, // Monday
        "ג": 2, // Tuesday
        "ד": 3, // Wednesday
        "ה": 4, // Thursday
        "ו": 5, // Friday
        "ש": 6  // Saturday
    };

    const [events, setEvents] = useState([]);

    useEffect(() => {
        if(responseData){
            console.log("data res from server:", responseData);
            const dataArray1 = responseData[reqCoursesArrayName] || [];
            const dataArray2 = responseData[choiceCoursesArrayName] || [];

            // Combine the arrays
            const combinedData = [...dataArray1, ...dataArray2];
            const calendarEvents = combinedData.map(course => {
                const { day, start_time, end_time, course_id_name, lesson_or_exercise } = course;

                // Split the time strings into hours and minutes
                const [startHour, startMinute] = start_time.split(':').map(Number);
                const [endHour, endMinute] = end_time.split(':').map(Number);

                const now = new Date();
                const start = new Date(now.setDate(now.getDate() - now.getDay() + dayMap[day]));
                start.setHours(startHour, startMinute, 0);

                const end = new Date(start);
                end.setHours(endHour, endMinute, 0);

                return {
                    title: `${course_id_name} - ${lesson_or_exercise}`,
                    start: start,
                    end: end,
                };
            });
            setEvents(calendarEvents);
        }

    }, [reqCoursesArrayName, choiceCoursesArrayName]);

    const formats = {
        dayFormat: (date, culture, localizer) => localizer.format(date, 'dddd', culture),
        dayHeaderFormat: (date, culture, localizer) => localizer.format(date, 'dddd', culture),
        timeGutterFormat: (date, culture, localizer) => localizer.format(date, 'HH:mm', culture),  // 24-hour time format
        eventTimeRangeFormat: ({ start, end }, culture, localizer) =>
            `${localizer.format(start, 'HH:mm', culture)} - ${localizer.format(end, 'HH:mm', culture)}`,  // Event time range in 24-hour format
    };

    return (
            <Calendar
                localizer={localizer}
                events={events}
                startAccessor="start"
                endAccessor="end"
                defaultView="week"
                views={['week']}
                step={15}
                timeslots={4}
                min={new Date(2023, 0, 1, 8, 0, 0)}  // Start at 8:00 AM
                max={new Date(2023, 0, 1, 21, 0, 0)}  // End at 9:00 PM
                style={{ height: 450 }}
                toolbar={false}  // Hides the toolbar (Today, Back, Next)
                formats={formats}  // Custom formats for day names and time format
            />

    )
}
export default CoursesScheduler
