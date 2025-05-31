import React, { useState } from 'react';
import StarRatingInput from './StarRatingInput';
import RatingAverage from './RatingAverage';
import RatingActions from './RatingActions';

export default function RatingBar({ movieId }) {
    const [average, setAverage] = useState(0.0);
    const [liked, setLiked] = useState(false);

    const handleRate = async (rating) => {
        const res = await fetch('/api/rate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ movieId, rating }),
        });
        const data = await res.json();
        setAverage(data.average); // 서버에서 평균 별점 응답
    };

    const handleLike = async () => {
        const res = await fetch('/api/like', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ movieId }),
        });
        if (res.ok) {
            setLiked((prev) => !prev);
        }
    };

    return (
        <div className="flex justify-between justify-center items-center w-full py-5 text-xs text-gray-500">
            <div className="flex items-center flex-1">
                <StarRatingInput onRate={handleRate} />
                <RatingAverage average={average} />
            </div>
            <div className="flex-1">
                <RatingActions liked={liked} onLike={handleLike} />
            </div>
        </div>
    );
}
