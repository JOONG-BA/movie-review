import { useParams } from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import {MovieDetailBanner} from "@/components/moive/detail/MovieDetailBanner.jsx";
import {MovieDetailInfo} from "@/components/moive/detail/MovieDetailInfo.jsx";
import MovieCredits from "@/components/moive/detail/MovieCredits.jsx";
import MovieComments from "@/components/moive/detail/MovieComments.jsx";
import MovieGallery from "@/components/moive/detail/MovieGallery.jsx";
import MovieVideos from "@/components/moive/detail/MovieVideos.jsx";
import {getMovieDetail} from "@/pages/api/movieApi.js";
import LoadingSpinner from "@/components/ui/LoadingSpinner.jsx";
import {ReviewModal} from "@/components/ui/ReviewModal.jsx";
import {getReviewsByMovie} from "@/pages/api/reviewApi.js";
import AllCommentsModal from "@/components/moive/detail/AllCommentsModal.jsx";
import {AuthContext} from "@/context/AuthContext.jsx";
export default function MovieDetailPage() {
    const { movieId } = useParams();
    const [movie, setMovie] = useState(null);
    const [comments, setComments] = useState([]);
    const [modalOpen, setModalOpen] = useState(false);      // 리뷰 작성 모달
    const [showAllModal, setShowAllModal] = useState(false); // 전체 코멘트 모달
    const [loading, setLoading] = useState(true);
    const [userScore, setUserScore] = useState(null); // 내 점수

    const { user } = useContext(AuthContext);

    const fetchComments = async () => {
        try {
            const data = await getReviewsByMovie(movieId);
            setComments(data);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        if (!movieId || !user) return;
        const fetchData = async () => {
            setLoading(true);
            try {
                const [movieDetail, commentData] = await Promise.all([
                    getMovieDetail(movieId),
                    getReviewsByMovie(movieId),
                ]);
                setMovie(movieDetail);
                setComments(commentData);

                const myReview = commentData.find(r => r.authorUserId === user.userId);
                if (myReview?.score != null) {
                    setUserScore(myReview.score);
                }

            } catch (error) {
                console.error(error);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [movieId, user]);

    if (loading) return  <LoadingSpinner />;

    if (!movie) return null;

    return (
        <>
        <div className="mb-10">
            <MovieDetailBanner movie={movie} />
            <MovieDetailInfo movie={movie} setModalOpen={setModalOpen} userScore={userScore} />
            <MovieCredits casts={movie.cast} directors={Array(movie.director)}  />
            <MovieComments
                comments={comments}
                movieId={movieId}
                setModalOpen={setModalOpen}
                setShowAllModal={setShowAllModal}
            />
            <MovieGallery images={movie.galleryImages} />
            <MovieVideos videos={movie.trailers} />
        </div>

        <ReviewModal
            open={modalOpen}
            setOpen={setModalOpen}
            movieId={movieId}
            onSubmitSuccess={fetchComments}
        />

        <AllCommentsModal
            open={showAllModal}
            setOpen={setShowAllModal}
            comments={comments}
            movieTitle={movie.title}
        />
        </>
    );
}
