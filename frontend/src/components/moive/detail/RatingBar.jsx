// src/components/movie/detail/RatingBar.jsx
import React, { useState, useContext, useEffect } from "react";
import StarRatingInput from "./StarRatingInput";
import RatingAverage from "./RatingAverage";
import RatingActions from "./RatingActions";
import { ReviewModal } from "@/components/ui/ReviewModal.jsx";
import { AuthContext } from "@/context/AuthContext.jsx";

export default function RatingBar({ voteAverage, movieId = null, userScore}) {
    // 1) 초기 average를 서버에서 받은 voteAverage로 설정
    const [average, setAverage] = useState(voteAverage); // 전체 평균
    // 2) 서버에 저장된 좋아요 여부를 불러와 초기화 (선택)
    const [liked, setLiked] = useState(false);
    const [modalOpen, setModalOpen] = useState(false);

    const { isLoggedIn } = useContext(AuthContext);
    // 페이지 로드 시, 내가 좋아요 누른 상태인지 확인
    useEffect(() => {

        if (!isLoggedIn || movieId == null) return;

        const token = localStorage.getItem("token");
        Promise.all([
            fetch("/api/users/me/favorites", {
                headers: { "Authorization": `Bearer ${token}` }
            }).then(res => res.ok ? res.json() : []),
        ])
            .then(([favorites]) => {
                setLiked(favorites.some(f => f.movieId === movieId));
            })
            .catch(console.error);
    }, [isLoggedIn, movieId]);

    const handleRate = async (rating) => {
        if (!isLoggedIn) return alert("로그인이 필요합니다!");
        const token = localStorage.getItem("token");
        if (!token) return alert("토큰이 없습니다. 다시 로그인 해 주세요.");

        const payload = { movieId, score: rating };

        const res = await fetch("/api/reviews", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            },
            body: JSON.stringify(payload),
        });

        if (!res.ok) {
            console.error("평점 전송에 실패했습니다.", await res.text());
            return;
        }
        const data = await res.json();
        if (data.average != null) setAverage(data.average);
    };

    const handleLike = async () => {
        if (!isLoggedIn) {
            return alert("로그인이 필요합니다!");
        }
        const token = localStorage.getItem("token");
        if (!token) {
            return alert("토큰이 없습니다. 다시 로그인 해 주세요.");
        }

        // liked 상태에 따라 POST(추가) / DELETE(삭제) 전환
        const method = liked ? "DELETE" : "POST";
        const url = `/api/users/me/favorites/${movieId}`;

        const res = await fetch(url, {
            method,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            },
            // DELETE에서는 body가 필요 없으므로 조건부로 전송
            ...(method === "POST" && { body: JSON.stringify({ movieId }) }),
        });

        if (res.ok) {
            setLiked(prev => !prev);
        } else {
            console.error(`${method} 요청에 실패했습니다.`, await res.text());
        }
    };

    const handleCommentClick = () => {
        if (!isLoggedIn) {
            return alert("로그인이 필요합니다!");
        }
        setModalOpen(true);
    };

    return (
        <>
            <div className="flex flex-col lg:flex-row justify-between items-center w-full mb-5 text-xs text-gray-500">
                <div className="flex w-full flex-col md:flex-row items-center flex-1 pb-5 mb-5 border-b lg:border-b-0 border-b-gray-300 lg:pb-0 lg:mb-0">
                    <StarRatingInput onRate={handleRate} initial={userScore}  />
                    <RatingAverage average={average} />
                </div>
                <div>
                    <RatingActions
                        liked={liked}
                        onLike={handleLike}
                        onComment={handleCommentClick}
                    />
                </div>
            </div>

            <ReviewModal open={modalOpen} setOpen={setModalOpen} movieId={movieId} />
        </>
    );
}
