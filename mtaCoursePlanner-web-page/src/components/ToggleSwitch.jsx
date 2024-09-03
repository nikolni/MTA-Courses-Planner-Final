import { Switch } from "antd";
import { FaXmark } from "react-icons/fa6";
import { FaCheck } from "react-icons/fa";
// eslint-disable-next-line react/prop-types
const ToggleSwitch = ({handleToggleChange}) => {
    return (
        <div>
            <Switch className='ml-2 mb-1 px-0.5'
                    defaultChecked={true}
                    checkedChildren={<FaCheck className='inline text-sm text-white m-1'/>}
                    unCheckedChildren={<FaXmark className='inline text-sm text-white m-1'/>}
                    onChange={(checked) => {handleToggleChange(checked)}}
            />
        </div>
    )
}
export default ToggleSwitch
