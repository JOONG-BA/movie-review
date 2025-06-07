import React, { useState, useContext } from "react";
import StarRatingInput from "./StarRatingInput";
import RatingAverage from "./RatingAverage";
import RatingActions from "./RatingActions";
import { ReviewModal } from "@/components/ui/ReviewModal.jsx";
import { AuthContext } from "@/context/AuthContext.jsx";
import {addFavorite, removeFavorite} from "@/pages/api/favoriteApi.js";

export default function RatingBar({ voteAverage, movieId = null, isFavorite }) {
    const [average, setAverage] = useState(0.0);
    const [liked, setLiked] = useState(isFavorite);
    const [modalOpen, setModalOpen] = useState(false);

    const { isLoggedIn } = useContext(AuthContext);

    const handleRate = async (rating) => {
        if (!isLoggedIn) {
            alert("로그인이 필요합니다!");
            return;
        }

        const res = await fetch("/api/rate", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ voteAverage, rating }),
        });

        const data = await res.json();
        setAverage(data.average);
    };

    const handleLike = async () => {
        if (!isLoggedIn) {
            alert("로그인이 필요합니다!");
            return;
        }

        try {
            if (liked) {
                await removeFavorite(movieId);
            } else {
                await addFavorite(movieId);
            }

            setLiked((prev) => !prev);
        } catch (err) {
            alert(err.message);
        }
    };

    const handleCommentClick = () => {
        if (!isLoggedIn) {
            alert("로그인이 필요합니다!");
            return;
        }
        setModalOpen(true);
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
                        onComment={handleCommentClick}
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
