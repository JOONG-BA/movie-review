import {useState} from "react";
import {FaStar, FaUser} from "react-icons/fa";

export default function CommentCard({ comment, maxLength }) {
    const visibleText =
        comment.content.length > maxLength
            ? comment.content.slice(0, maxLength) + "..."
            : comment.content;

    return (
        <div className="flex-none w-full p-5 bg-gray-50 rounded shadow-sm text-sm flex flex-col gap-4">
            {/* 작성자 + 별점 */}
            <div className="flex pb-2 border-b justify-between items-center">
                <div className="flex items-center gap-3">
                    <div className="w-8 h-8 flex items-center justify-center bg-gray-200 rounded-full">
                        <FaUser className="text-gray-400 text-sm" />
                    </div>
                    <span className="font-medium text-sm text-gray-600 ">{comment.author}</span>
                </div>
                {comment.rating !== undefined && (
                    <div className="flex gap-1 items-center text-gray-600">
                        <FaStar className="text-yellow-400" size={14} />
                        <span className="text-xs font-semibold">{comment.rating.toFixed(1)}</span>
                    </div>
                )}
            </div>

            {/* 본문 */}
            <p className="text-left min-h-20 mb-4 text-xs text-gray-600 whitespace-pre-line">{visibleText}</p>
        </div>
    );
}