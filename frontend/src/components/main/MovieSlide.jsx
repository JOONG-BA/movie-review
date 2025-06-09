import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import {IoMdStar} from "react-icons/io";
import {Link} from "react-router-dom";
import {NextArrow} from "@/components/slider/PrevArrow.jsx";
import {PrevArrow} from "@/components/slider/NextArrow.jsx";
import {useEffect, useRef, useState} from "react";
import {getPopularByGenreFromApi, getPopularFromApi} from "@/pages/api/movieApi.js";
import LoadingSpinner from "@/components/ui/LoadingSpinner.jsx";

export const MovieSlide = ({ title, genre }) => {
    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(true);

    const effectRan = useRef(false);
    useEffect(() => {
        if (effectRan.current) return;
        effectRan.current = true;

        setLoading(true);

        const fetchMovies = genre === null
            ? getPopularFromApi()
            : getPopularByGenreFromApi(genre);

        fetchMovies
            .then(res => {
                setMovies(res.content);
            })
            .catch(console.error)
            .finally(() => {
                setLoading(false);
            });
    }, [genre]);

    if (loading) return <LoadingSpinner />;

    const settings = {
        dots: false,
        speed: 1000,
        slidesToShow: 5,
        slidesToScroll: 5,
        initialSlide: 0,
        arrows: true,
        infinite: false,
        nextArrow: <NextArrow />,
        prevArrow: <PrevArrow />,
        responsive: [
            {
                breakpoint: 1536,
                settings: {
                    slidesToShow: 5,
                    slidesToScroll: 5,
                },
            },
            {
                breakpoint: 1280,
                settings: {
                    slidesToShow: 4,
                    slidesToScroll: 4,
                },
            },
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 3,
                },
            },
            {
                breakpoint: 768,
                settings: {
                    slidesToShow: 3.2,
                    slidesToScroll: 3.2,
                },
            },
        ],
    };

    return (
        <section className="my-8 container text-left overflow-hidden">
            <h2 className="text-xl sm:text-2xl font-semibold mb-4 ">{title}</h2>
            <div className="slider-container">
                <Slider key={movies.length} className="relative " {...settings}>
                    {movies.map((movie) => (
                        <Link to={`/movie/detail/${movie.id}`} key={movie.id} className="pr-1 sm:pr-2 cursor-pointer">
                            <div className="bg-gray-400 rounded-sm border border-gray-200 overflow-hidden">
                                <img src={"https://image.tmdb.org/t/p/w500/" + movie.posterUrl} alt={movie.title}/>
                            </div>
                            <div className="grid gap-y-1">
                                <p className="mt-2 text-base sm:text-lg font-semibold">{movie.title}</p>
                                <div className="text-sm hidden sm:block font-medium">
                                    {movie.releaseDate}
                                    <p className="inline mx-1">
                                        {movie.genres?.map((g, idx) => (
                                            <span key={idx} className="mr-1">· {g.name}</span>
                                        ))}
                                    </p>
                                </div>
                                <p className="flex items-center text-xs sm:text-sm text-gray-500">
                                    평균 <IoMdStar className="mx-1" /> {movie.voteAverage}
                                </p>
                            </div>
                        </Link>
                    ))}
                </Slider>
            </div>
        </section>
    );
};
