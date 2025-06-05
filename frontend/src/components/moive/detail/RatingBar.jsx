import React, { useState } from "react";
import StarRatingInput from "./StarRatingInput";
import RatingAverage from "./RatingAverage";
import RatingActions from "./RatingActions";
import { ReviewModal } from "@/components/ui/ReviewModal.jsx";

export default function RatingBar({ voteAverage, movieId }) {
    const [average, setAverage] = useState(0.0);
    const [liked, setLiked] = useState(false);
    const [modalOpen, setModalOpen] = useState(false);

    const handleRate = async (rating) => {
        const res = await fetch("/api/rate", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ voteAverage, rating }),
        });
        const data = await res.json();
        setAverage(data.average);
    };

    const handleLike = async () => {
        const res = await fetch("/api/like", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ voteAverage }),
        });
        if (res.ok) {
            setLiked((prev) => !prev);
        }
    };

    return (
        <>
            <div className="flex flex-col lg:flex-row justify-between justify-center items-center w-full mb-5 text-xs text-gray-500">
                <div className="flex w-full flex-col md:flex-row items-center flex-1 pb-5 mb-5 border-b lg:border-b-0 border-b-gray-300 lg:pb-0 lg:mb-0">
                    <StarRatingInput onRate={handleRate} />
                    <RatingAverage average={voteAverage} />
                </div>
                <div>
                    <RatingActions
                        liked={liked}
                        onLike={handleLike}
                        onComment={() => setModalOpen(true)}
                    />
                </div>
            </div>

            <ReviewModal
                open={modalOpen}
                setOpen={setModalOpen}
                movieId={movieId}
            />
        </>
    );
}
