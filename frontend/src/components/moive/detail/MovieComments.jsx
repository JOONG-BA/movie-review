import { useEffect, useState } from "react";
import { FaStar, FaUser } from "react-icons/fa";
import CommentCard from "@/components/moive/detail/CommentCard.jsx";
import {Link} from "react-router-dom";

// 💬 더미 데이터
const dummyComments = [

];

export default function MovieComments() {
    const MAX_LENGTH = 60; // 자를 최대 글자 수
    return(
        <div className="mt-8 container">
            <div className="flex items-end my-4">
                <h2 className="flex-1 text-xl text-left font-bold">코멘트</h2>
                { dummyComments.length > 0 ? <Link to="/comments" className="text-xs text-gray-500"> 더보기 </Link> : null }
            </div>
            <div className="flex sm:grid gap-4 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 overflow-hidden ">
                    {dummyComments.length > 0 ? dummyComments.map(comment => (
                        <CommentCard key={comment.id} comment={comment} maxLength={MAX_LENGTH} />
                    )) : <div> 코멘트를 1순위로 달아보세요! 코멘트 달기 링크와 함께 달기 버튼 누르면 모달 띄우기 </div>}
            </div>
        </div>
    );
}

