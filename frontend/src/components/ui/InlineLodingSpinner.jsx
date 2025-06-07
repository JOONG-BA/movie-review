import { CgSpinner } from "react-icons/cg";

export default function InlineLoadingSpinner() {
    return (
        <div className="flex justify-center items-center pt-10">
            <CgSpinner className="animate-spin text-gray-600" size={40} style={{ animationDuration: "1.5s" }} />
        </div>
    );
}
