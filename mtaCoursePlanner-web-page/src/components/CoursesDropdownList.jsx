import { useState, useEffect } from 'react';
import Papa from "papaparse";
import _ from 'lodash';
import { IoIosRemoveCircleOutline } from "react-icons/io";
import coursesWithExe from '../../public/coursesWithExe.json';
import ToggleSwitch from "./ToggleSwitch.jsx";
// eslint-disable-next-line react/prop-types
const CoursesDropdownList = ({coursesTablePath, query, coursesPrefArr, onCourseChange}) => {


    const [coursesData, setCoursesData] = useState([]); //array of objects - all the courses
    const [filteredCourses, setFilteredCourses] = useState([]); //array of objects - courses after query

    //options for select 2+3
    const [lessonExeOptions, setLessonExeOptions]= useState([{
        lesson_group_code_opt:[],
        exercise_group_code_opt:[]
    }])

    const [validationError, setValidationError] = useState(false);
    const [showDropdown, setShowDropdown] = useState(true);

    const handleToggleButton= (checked) => {
        setShowDropdown(checked);
        if(!checked){
            const temp=[{ course_code_name: "", lesson_code: "", has_exercise: false, exercise_code: "dc" }];
            onCourseChange(temp);
        }
    }
    const handleChangeSelect1= (event, index) => {

        setValidationError(false);
        const temp = _.cloneDeep(coursesPrefArr);
        //const temp=[...coursesPrefArr]
        const newVal=event.target.value;
        temp[index]["course_code_name"]=newVal;

        const hasExe = coursesWithExe["courses-codes"].some(courseCode => (event.target.value).includes(courseCode));
        temp[index]["has_exercise"]=hasExe;
        temp[index]["lesson_code"]="dc";
        temp[index]["exercise_code"]="dc";

        const courseChangeValid = onCourseChange(temp, newVal);
        if (!courseChangeValid)
            setValidationError(true);

        const queryAfterSelect1= (course) => course.course_id_name === event.target.value;
        const queriedDataAfterSelect1 = filteredCourses.filter(queryAfterSelect1);

        // Extract and filter unique course names
        const courseLessonCodes = queriedDataAfterSelect1.map(course => course.group_number);
        const uniqueCodes = [...new Set(courseLessonCodes)];
        const tempOptions=[...lessonExeOptions];
        tempOptions[index]["lesson_group_code_opt"]=[...uniqueCodes];
        tempOptions[index]["exercise_group_code_opt"]=[];
        setLessonExeOptions(tempOptions);
    }

    const handleChangeSelect2= (event, index) => {
        const temp=[...coursesPrefArr]
        const newVal=event.target.value;
        temp[index]["lesson_code"]=newVal;
        onCourseChange(temp);

        const exerciseCode1 = (parseInt(event.target.value, 10) + 1).toString();
        const exerciseCode2 = (parseInt(event.target.value, 10) + 2).toString();
        const tempOptions=[...lessonExeOptions];
        tempOptions[index]["exercise_group_code_opt"]=[exerciseCode1,exerciseCode2];
        setLessonExeOptions(tempOptions);
    }

    const handleChangeSelect3= (event, index) => {
        const newVal=event.target.value;
        //console.log(`New value for index ${index}: ${newVal}`);
        const temp=[...coursesPrefArr];
        temp[index]["exercise_code"]=newVal;
        onCourseChange(temp);
    }

    const handleAdd=() => {
        const newPref = { course_code_name: "", lesson_code: "", has_exercise: false, exercise_code: "dc" };
        onCourseChange([...coursesPrefArr, newPref]);

        const newOpt={ lesson_group_code_opt:[], exercise_group_code_opt:[] }
        setLessonExeOptions([...lessonExeOptions,newOpt]);
    }

    const handleRemoved=(index) => {
        const temp=[...coursesPrefArr]
        temp.splice(index,1);
        onCourseChange(temp);

        const tempOpt=[...lessonExeOptions];
        tempOpt.splice(index,1);
        setLessonExeOptions(tempOpt);
    }


    useEffect(() => {
        // Fetch the CSV file
        fetch(coursesTablePath)
            .then(response => response.text())
            .then(csvText => {
                // Parse the CSV text
                Papa.parse(csvText, {
                    header: true, // Assumes the first row is the header
                    complete: (result) => {
                        // Convert to JSON and set state
                        const json = result.data;
                        setCoursesData(json);
                        //console.log("Converted JSON:", json);
                    },
                    error: (error) => {
                        console.error("Error parsing CSV:", error);
                    }
                });
            })
            .catch(error => {
                console.error("Error fetching CSV file:", error);
            });
    }, [coursesTablePath]);


    useEffect(() => {
        const queriedData = query ? coursesData.filter(query) : coursesData;
        setFilteredCourses(queriedData);
    }, [coursesData, query]);


    const uniqueCourseNames = [...new Set(filteredCourses.map(course => course.course_id_name))];

    return(
        <>
            <ToggleSwitch handleToggleChange={handleToggleButton}/>
            {showDropdown && (<div className= 'my-2'>
                <button
                    onClick={handleAdd}
                    className="bg-indigo-500 hover:bg-indigo-600 text-white text-sm py-1 ml-2 font-medium rounded-full w-1/6 focus:outline-none focus:shadow-outline"
                >
                    הוסף קורס
                </button>
                {/* eslint-disable-next-line react/prop-types */}
                {coursesPrefArr.map((singlePref,index) => {
                    return(
                        <div key= {index} className= 'my-4'>
                            <div className= 'mb-2'>
                                <select className='border rounded w-1/2 py-2 px-3' value={singlePref.course_code_name} required
                                        onChange={(e) => handleChangeSelect1(e,index)}
                                >
                                    <option value="" disabled hidden>
                                        Choose Course
                                    </option>
                                    {uniqueCourseNames.map((name, idx) => (
                                        <option key={idx} value={name}>
                                            {name}
                                        </option>
                                    ))}
                                </select>
                                {/* eslint-disable-next-line react/prop-types */}
                                {coursesPrefArr.length !== 1 && (
                                    <button
                                        onClick={() => handleRemoved(index)}
                                        className="bg-indigo-500 hover:bg-indigo-600 ml-2 rounded-lg w-7 focus:outline-none focus:shadow-outline"
                                    >
                                        <IoIosRemoveCircleOutline className='inline text-xl text-white m-1'/>
                                    </button>
                                )}
                            </div>
                            <div className= 'mb-2'>
                                <select className='border rounded w-1/2 py-2 px-3' value={singlePref.lesson_code} required onChange={(e) => handleChangeSelect2(e,index)}>
                                    <option value="" disabled hidden>
                                        Choose Lesson Group
                                    </option>
                                    <option value="dc">
                                        לא משנה לי
                                    </option>
                                    {lessonExeOptions[index]["lesson_group_code_opt"].map((code, idx) => (
                                        <option key={idx} value={code}>
                                            {code + ' ' + (filteredCourses.find((obj) => obj.group_number === code)?.lecturer_name || '')}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            {/* eslint-disable-next-line react/prop-types */}
                            {coursesPrefArr[index].has_exercise && (<div className= 'mb-2'>
                                {/* eslint-disable-next-line react/prop-types */}
                                <select className='border rounded w-1/2 py-2 px-3' value={coursesPrefArr[index].lesson_code === "dc" ? "dc" : singlePref.exercise_code}
                                        required
                                        onChange={(e) => handleChangeSelect3(e,index)}
                                >
                                    <option value="" disabled hidden>
                                        Choose Exercise Group
                                    </option>
                                    <option value="dc">
                                        לא משנה לי
                                    </option>
                                    {/* eslint-disable-next-line react/prop-types */}
                                    {coursesPrefArr[index].lesson_code !== "dc" && ((coursesPrefArr[index].course_code_name === "נושאים מתקדמים בהסתברות וסטטיסטיקה - 192114" ? [lessonExeOptions[index]["exercise_group_code_opt"][0]] : lessonExeOptions[index]["exercise_group_code_opt"]).map((name, idx) => (
                                        <option key={idx} value={name}>
                                            {name}
                                        </option>
                                    )))}
                                </select>
                            </div>)}
                            <hr
                                className="my-3 h-1 border-t-0 bg-transparent bg-gradient-to-r from-transparent via-indigo-400 to-transparent opacity-50 dark:via-neutral-400"/>
                        </div>
                    )
                })}
            </div>)}
        </>





    )
}


export default CoursesDropdownList
