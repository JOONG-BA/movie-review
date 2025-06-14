import { BsBookmarkHeartFill } from "react-icons/bs";
import { CgMathPlus } from "react-icons/cg";
import { BiSolidPencil } from "react-icons/bi";

export default function RatingActions({ liked, onLike, onComment }) {
    const iconSize = 35;
    const iconProps = {
        size: iconSize,
        className:
            "transition ease-in-out duration-200 group-hover:scale-125 group-hover:text-gray-600",
    };

    const buttonProps = {
        className:
            "group flex flex-col space-y-2 items-center justify-center w-20 hover:text-gray-900",
    };

    return (
        <div className="flex justify-end gap-0 text-xs ">
            <button onClick={onLike} {...buttonProps}>
                {liked ? (
                    <BsBookmarkHeartFill {...iconProps}  className="text-pink-600" />
                ) : (
                    <CgMathPlus {...iconProps} />
                )}
                <span>보고싶어요</span>
            </button>
            <button onClick={onComment} {...buttonProps}>
                <BiSolidPencil {...iconProps} />
                <span>코멘트</span>
            </button>
        </div>
    );
}
