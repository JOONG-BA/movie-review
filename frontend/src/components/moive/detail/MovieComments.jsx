import { useEffect, useState } from "react";
import { FaStar, FaUser } from "react-icons/fa";
import CommentCard from "@/components/moive/detail/CommentCard.jsx";
import {Link} from "react-router-dom";

// 💬 더미 데이터
const dummyComments = [
    {
        id: 1,
        movieId: 101,
        author: "무비러버",
        content: "기생충은 진짜 명작이에요.",
        createdAt: "2024-12-01T14:23:00Z",
        rating: 4.5
    },
    {
        id: 2,
        movieId: 101,
        author: "익명",
        content: "마지막 반전에서 숨 멎는 줄... 봉준호 감독 진짜 천재!",
        createdAt: "2024-12-02T10:11:45Z",
        rating: 4.0
    },
    {
        id: 3,
        movieId: 101,
        author: "감성파괴자",
        content: "배우들 연기가 미쳤어요. 송강호는 진짜 레전드. 배우들 연기가 미쳤어요. 송강호는 진짜 레전드. 배우들 연기가 미쳤어요. 송강호는 진짜 레전드. 배우들 연기가 미쳤어요. 송강호는 진짜 레전드.",
        createdAt: "2024-12-03T09:30:10Z",
        rating: 4.3
    },
    {
        id: 4,
        movieId: 101,
        author: "아무말대잔치",
        content: "이게 현실일 수도 있다는 게 너무 무서웠어요...",
        createdAt: "2024-12-04T08:47:22Z",
        rating: 3.8
    },
    {
        id: 5,
        movieId: 101,
        author: "시네필",
        content: "해외에서 왜 그렇게 반응했는지 알겠더라구요.",
        createdAt: "2024-12-05T15:05:55Z",
        rating: 4.2
    }
];

export default function MovieComments() {
    const MAX_LENGTH = 60; // 자를 최대 글자 수

    return (
        <div className="mt-8 container">
            <div className="flex items-end mb-4">
                <h2 className="flex-1 text-xl text-left font-bold">코멘트</h2>
                <Link href="/comments" className="text-xs text-gray-500">
                    더보기
                </Link>
            </div>
            <div className="flex sm:grid gap-4 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 overflow-hidden ">
                {dummyComments.map(comment => (
                    <CommentCard key={comment.id} comment={comment} maxLength={MAX_LENGTH} />
                ))}
            </div>
        </div>
    );
}

