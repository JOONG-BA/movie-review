import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import {MovieDetailBanner} from "@/components/moive/detail/MovieDetailBanner.jsx";
import {MovieDetailInfo} from "@/components/moive/detail/MovieDetailInfo.jsx";
import {dummyMovies} from "@/pages/index.jsx";
import MovieCredits from "@/components/moive/detail/MovieCredits.jsx";
import MovieComments from "@/components/moive/detail/MovieComments.jsx";
import MovieGallery from "@/components/moive/detail/MovieGallery.jsx";
import MovieVideos from "@/components/moive/detail/MovieVideos.jsx";
import {getMovieDetail} from "@/pages/api/movieApi.js";

export default function MovieDetailPage() {
    const { movieId } = useParams();
    const [movie, setMovie] = useState(null);


    useEffect(() => {
        getMovieDetail(movieId)
            .then(res => { setMovie(res); console.log(res); })
            .catch(console.error);
    }, []);

    if (!movie) return <div>로딩 중...</div>;

    return (
        <div className="mb-10">
            <MovieDetailBanner movie={movie} />
            <MovieDetailInfo movie={movie} />
            <MovieCredits casts={movie.cast} directors={Array(movie.director)}  />
            <MovieComments />
            <MovieGallery images={movie.galleryImages} />
            <MovieVideos videos={movie.trailers} />
        </div>
    );
}
