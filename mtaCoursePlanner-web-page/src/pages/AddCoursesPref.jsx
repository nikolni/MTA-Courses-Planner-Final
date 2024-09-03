import {useState } from 'react';
import CoursesDropdownList from "../components/CoursesDropdownList.jsx";


// eslint-disable-next-line react/prop-types
const AddCoursesPref = ({ addPrefSubmit }) => {

    const [selectedCoursesData, setSelectedCoursesData] = useState({
        reqCoursesAlist: [{ course_code_name: "", lesson_code: "", has_exercise: false, exercise_code: "dc" }],
        reqCoursesBlist: [{ course_code_name: "", lesson_code: "", has_exercise: false, exercise_code: "dc" }],
        choiceCoursesAlist: [{ course_code_name: "", lesson_code: "", has_exercise: false, exercise_code: "dc" }],
        choiceCoursesBlist: [{ course_code_name: "", lesson_code: "", has_exercise: false, exercise_code: "dc" }]
    });

    const [studentId, setStudentId]= useState('');

    const handleCourseChange = (dropdown, updatedData,choice1Val) => {

        if(choice1Val !== "" && Object.values(selectedCoursesData).some(courseList =>
            courseList.some(course => course.course_code_name === choice1Val)
        ))
            return false;

        setSelectedCoursesData(prevState => ({
            ...prevState,
            [dropdown]: updatedData,
        }));
        return true;
    };
    const submitForm = (e) => {
        e.preventDefault();
        const newStudentPref = {
            selectedCoursesData,
            studentId
        };
        console.log("click on submit", newStudentPref);
        addPrefSubmit(newStudentPref);
    };

    const queryA = (course) => course.semester === 'א' && course.lesson_or_exercise === 'שיעור';
    const queryB = (course) => course.semester === 'ב' && course.lesson_or_exercise === 'שיעור';


    return (
        <section className='bg-indigo-50'>
            <div className='container m-auto max-w-2xl py-24'>
                <div className='bg-white px-6 py-8 mb-4 shadow-md rounded-md border m-4 md:m-0'>
                    <form onSubmit={submitForm}>
                        <h2 className='text-3xl text-center font-semibold mb-6'>Add Your Schedules Preferences</h2>
                        <div className='mb-4'>
                            <label htmlFor='studentID' className='block text-gray-700 font-bold mb-2'>
                                הכנס תעודת זהות
                            </label>
                            <input
                                id='studentID'
                                name='studentID'
                                className='border rounded w-full py-2 px-3 mb-2'
                                required
                                value={studentId}
                                onChange={(e) => {
                                    const value = e.target.value;
                                    // Check if the input is a number and has at most 9 digits
                                    if (/^\d{0,9}$/.test(value)) {
                                        setStudentId(value);
                                    }
                                }}
                                pattern="\d{9}" // Enforces 9 digits on form submission
                                title="הזן תעודת זהות תקינה"
                            />
                        </div>
                        <div>
                            <h3 className="text-2xl text-center font-semibold mb-3 text-gray-800 dark:text-gray-200 tracking-wide leading-tight">
                                סמסטר א
                            </h3>
                            <hr
                                className="my-3 h-2 border-t-0 bg-transparent bg-gradient-to-r from-transparent via-indigo-600 to-transparent opacity-50 dark:via-neutral-400"/>
                            <div>
                                <label
                                    htmlFor='reqCourseName'
                                    className='block text-gray-700 text-center font-bold mb-2'
                                >
                                    קורסי חובה רצויים
                                </label>
                            </div>
                            <CoursesDropdownList
                                coursesTablePath={'/cs_courses_required.csv'}
                                query={queryA}
                                coursesPrefArr={selectedCoursesData.reqCoursesAlist}
                                onCourseChange={(updatedData, choice1="") => handleCourseChange("reqCoursesAlist", updatedData, choice1)}
                            />
                            <div>
                                <label
                                    htmlFor='reqCourseName'
                                    className='block text-gray-700 text-center font-bold mb-2'
                                >
                                    קורסי בחירה רצויים
                                </label>
                            </div>
                            <CoursesDropdownList
                                coursesTablePath={'/cs_courses_choice.csv'}
                                query={queryA}
                                coursesPrefArr={selectedCoursesData.choiceCoursesAlist}
                                onCourseChange={(updatedData, choice1="") => handleCourseChange("choiceCoursesAlist", updatedData, choice1)}
                            />
                        </div>
                        <div>
                            <h3 className="text-2xl text-center font-semibold mb-3 text-gray-800 dark:text-gray-200 tracking-wide leading-tight">
                                סמסטר ב
                            </h3>                            <hr
                                className="my-3 h-2 border-t-0 bg-transparent bg-gradient-to-r from-transparent via-indigo-600 to-transparent opacity-50 dark:via-neutral-400"/>
                            <div>
                                <label
                                    htmlFor='reqCourseName'
                                    className='block text-gray-700 text-center font-bold mb-2'
                                >
                                    קורסי חובה רצויים
                                </label>
                            </div>
                            <CoursesDropdownList
                                coursesTablePath={'/cs_courses_required.csv'}
                                query={queryB}
                                coursesPrefArr={selectedCoursesData.reqCoursesBlist}
                                onCourseChange={(updatedData, choice1="") => handleCourseChange("reqCoursesBlist", updatedData, choice1)}
                            />
                            <div>
                                <label
                                    htmlFor='reqCourseName'
                                    className='block text-gray-700 text-center font-bold mb-2'
                                >
                                    קורסי בחירה רצויים
                                </label>
                            </div>
                            <CoursesDropdownList
                                coursesTablePath={'/cs_courses_choice.csv'}
                                query={queryB}
                                coursesPrefArr={selectedCoursesData.choiceCoursesBlist}
                                onCourseChange={(updatedData, choice1="") => handleCourseChange("choiceCoursesBlist", updatedData, choice1)}
                            />
                        </div>

                        <div>
                            <button
                                className='bg-indigo-500 hover:bg-indigo-600 text-white font-bold py-2 px-4 rounded-full w-full focus:outline-none focus:shadow-outline'
                                type='submit'
                            >
                                בנה מערכת
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    );
};
export default AddCoursesPref;