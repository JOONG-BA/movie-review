import React, { useEffect, useState } from 'react';
import StarRatingInput from './StarRatingInput';
import RatingAverage from './RatingAverage';
import RatingActions from './RatingActions';
import {addFavorite, checkIsFavorite, deleteFavorite} from "@/pages/api/favoriteApi.js";

export default function RatingBar({ movieId, voteAverage }) {
    const [average, setAverage] = useState(voteAverage ?? 0.0);
    const [liked, setLiked] = useState(false);
    const [loading, setLoading] = useState(true);

    //최초 로딩 시 보고싶어요 여부 확인
    useEffect(() => {
        async function fetchFavoriteStatus() {
            try {
                const isFav = await checkIsFavorite(movieId);
                setLiked(isFav);
            } catch (e) {
                if (e.message.includes("401")) {
                    // 로그인 안 된 경우 로그인 모달 띄우기
                } else {
                    console.error("즐겨찾기 여부 확인 실패:", e);
                }
            } finally {
                setLoading(false);
            }
        }

        fetchFavoriteStatus();
    }, [movieId]);

    const handleRate = async (rating) => {
        const res = await fetch('/api/rate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ voteAverage, rating }),
        });
        const data = await res.json();
        setAverage(data.average);
    };

    //즐겨찾기 추가/삭제
    const handleLike = async () => {
        try {
            if (liked) {
                await deleteFavorite(movieId);
            } else {
                await addFavorite(movieId);
            }
            setLiked(prev => !prev);
        } catch (e) {
            if (e.message.includes('401')) {
                // setShowLoginModal(true);
                console.log("asddas");
            } else {
                alert(e.message);
            }
        }
    };

    return (
        <div className="flex flex-col lg:flex-row justify-between items-center w-full mb-5 text-xs text-gray-500">
            <div className="flex w-full flex-col md:flex-row items-center flex-1 pb-5 mb-5 border-b lg:border-b-0 border-b-gray-300 lg:pb-0 lg:mb-0">
                <StarRatingInput onRate={handleRate} />
                <RatingAverage average={average} />
            </div>
            <div>
                {!loading && (
                    <RatingActions liked={liked} onLike={handleLike} />
                )}
            </div>
        </div>
    );
}
