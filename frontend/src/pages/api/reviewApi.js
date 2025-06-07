import {authFetch} from "@/context/AuthContext.jsx";

export async function createReview(reviewData) {
    const res = await authFetch("/api/reviews", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(reviewData),
    });

    if (!res.ok) {
        const error = await res.json();
        throw new Error(error.message || "리뷰 등록에 실패했습니다.");
    }

    return await res.json(); // { message, reviewId }
}

export async function deleteReview(reviewId) {
    const res = await fetch(`/api/reviews/${reviewId}`, {
        method: "DELETE",
    });

    if (!res.ok) {
        const error = await res.text();
        throw new Error(error || "리뷰 삭제에 실패했습니다.");
    }

    return true;
}

export async function getReviewsByMovie(movieId) {
    const res = await fetch(`/api/reviews/movies/${movieId}/reviews`);

    if (!res.ok) {
        const error = await res.text();
        throw new Error(error || "리뷰 조회에 실패했습니다.");
    }

    return await res.json(); // List<ReviewResponse>
}
