import {MdOutlineKeyboardArrowRight} from "react-icons/md";

export const NextArrow = (props) => {
    const { className, onClick } = props;
    return (
        <div
            className={`
                absolute z-10 !right-0 translate-x-1/3 top-1/2 -translate-y-1/2
                cursor-pointer text-xl text-black p-1 rounded-full bg-white border-gray-200 border
                text-gray-500 hover:text-gray-900 hover:bg-gray-50
                ${className?.includes("slick-disabled") ? "hidden" : ""}
            `}
            onClick={onClick}
        >
            <MdOutlineKeyboardArrowRight size={22} />
        </div>
    );
};