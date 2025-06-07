import {FaSpinner} from "react-icons/fa";
import {CgSpinner} from "react-icons/cg";

export default function LoadingSpinner() {
    return (
        <div className="fixed inset-0 bg-white z-[100] flex justify-center items-center">
            <CgSpinner className="animate-spin text-black" size={40} style={{ animationDuration: "2s" }} />
        </div>
    );
}