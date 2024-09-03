import { useState, useRef } from 'react';
import CoursesScheduler from "../components/CoursesScheduler.jsx";
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
// eslint-disable-next-line react/prop-types
const AllSchedulesPage = ({responseData}) => {


    const [selectedSemester, setSelectedSemester] = useState('');
    const handleChange = (event) => {
        setSelectedSemester(event.target.value);
    };

    const getCoursesProps = () => {
        switch (selectedSemester) {
            case 'B':
                return {
                    reqCoursesArrayName: 'requiredSemesterB',
                    choiceCoursesArrayName: 'choiceSemesterB',
                    // eslint-disable-next-line react/prop-types
                    changes: responseData?.changesB || [],
                    // eslint-disable-next-line react/prop-types
                    errors: responseData?.errorsB || []
                };
            case 'A':
                return {
                    reqCoursesArrayName: 'requiredSemesterA',
                    choiceCoursesArrayName: 'choiceSemesterA',
                    // eslint-disable-next-line react/prop-types
                    changes: responseData?.changesA || [],
                    // eslint-disable-next-line react/prop-types
                    errors: responseData?.errorsA || []
                };
            default:
                return null;
        }
    };

    const coursesProps = getCoursesProps();

    const printRef = useRef();
    const generatePDF = () => {
        const input = printRef.current;

        html2canvas(input).then((canvas) => {
            const imgData = canvas.toDataURL('image/png');
            const pdf = new jsPDF('landscape', 'mm', 'a4',true);
            const pdfWidth = pdf.internal.pageSize.getWidth();
            const pdfHeight = pdf.internal.pageSize.getHeight();
            const imgWidth = canvas.width;
            const imgHeight = canvas.height;
            const ratio = Math.min(pdfWidth / imgWidth, pdfHeight / imgHeight);
            const imgX= (pdfWidth - imgWidth * ratio) / 2;
            const imgY= 30;
            pdf.addImage(imgData, 'PNG', imgX, imgY, imgWidth*ratio, imgHeight*ratio);

            pdf.save('your_scheduler.pdf');
        });
    };

    return (
        <div>
            {responseData ? (
                    <div ref={printRef} id="content-to-print">
                        <div className="flex items-center">
                            <select className="border rounded py-2 px-3 mb-2 mr-4" value={selectedSemester} onChange={handleChange}>
                                <option value="">Select a semester</option>
                                <option value="A">Semester A</option>
                                <option value="B">Semester B</option>
                            </select>
                            {coursesProps && (
                                <button
                                    className="bg-indigo-500 hover:bg-indigo-600 text-white font-bold py-2 px-4 rounded-full focus:outline-none focus:shadow-outline"
                                    onClick={generatePDF}
                                >
                                    Download as PDF
                                </button>
                            )}
                        </div>
                        {coursesProps && (
                            <>
                                <CoursesScheduler
                                    reqCoursesArrayName={coursesProps.reqCoursesArrayName}
                                    choiceCoursesArrayName={coursesProps.choiceCoursesArrayName}
                                    responseData={responseData}
                                />
                                <div className="mt-1">
                                    <h3 className="text-lg font-semibold ml-3">Changes</h3>
                                    {coursesProps.changes.length > 0 ? (
                                        <ul className="list-disc list-inside ml-3">
                                            {coursesProps.changes.map((change, index) => (
                                                <li key={index}>{change}</li>
                                            ))}
                                        </ul>
                                    ) : (
                                        <p className='ml-3'>No changes available for this semester.</p>
                                    )}
                                    <h3 className="text-lg font-semibold ml-3 mt-1">Errors</h3>
                                    {coursesProps.errors.length > 0 ? (
                                        <ul className="list-disc list-inside ml-3">
                                            {coursesProps.errors.map((error, index) => (
                                                <li key={index}>{error}</li>
                                            ))}
                                        </ul>
                                    ) : (
                                        <p className='ml-3'>No errors available for this semester.</p>
                                    )}
                                </div>
                            </>
                        )}
                    </div>
            ) : (
                <p className="my-4 text-xl text-center text-black">No Schedules To Show</p>
            )}
        </div>
    );

};
export default AllSchedulesPage;
