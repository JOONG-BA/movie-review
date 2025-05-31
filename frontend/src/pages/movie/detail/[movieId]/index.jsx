import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import {MovieDetailBanner} from "@/components/moive/detail/MovieDetailBanner.jsx";
import {MovieDetailInfo} from "@/components/moive/detail/MovieDetailInfo.jsx";
import {dummyMovies} from "@/pages/index.jsx";
import MovieCredits from "@/components/moive/detail/MovieCredits.jsx";
import MovieComments from "@/components/moive/detail/MovieComments.jsx";
import MovieGallery from "@/components/moive/detail/MovieGallery.jsx";

export default function MovieDetailPage() {
    const { movieId } = useParams();
    const [movie, setMovie] = useState(null);

    /*
    useEffect(() => {
        fetch(`/api/movie/${movieId}`)  // 실제 API URL로 대체
            .then((res) => res.json())
            .then((data) => setMovie(data))
            .catch((err) => {
                console.error("영화 데이터를 불러오지 못했습니다:", err);
            });
    }, [movieId]);

    if (!movie) return <div>로딩 중...</div>;
    */


    return (
        <>
            <MovieDetailBanner movie={dummyMovies[0]} />
            <MovieDetailInfo movie={dummyMovies[0]} />
            <MovieCredits casts={dummyMovies[0].casts} directors={dummyMovies[0].directors}  />
            <MovieComments />
            <MovieGallery images={dummyMovies[0].gallery} />
        </>
    );
}
