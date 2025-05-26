import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import {IoMdStar} from "react-icons/io";
import {Link} from "react-router-dom";
import {
    MdArrowBackIos,
    MdArrowForwardIos,
    MdOutlineArrowForwardIos,
    MdOutlineKeyboardArrowLeft,
    MdOutlineKeyboardArrowRight
} from "react-icons/md";

const NextArrow = (props) => {
    const { className, style, onClick } = props;
    return (
        <div
            className={`
                absolute z-10 !right-0 translate-x-1/3 top-1/2 -translate-y-12
                cursor-pointer text-xl text-black p-1 rounded-full bg-white border-gray-200 border
                text-gray-500 hover:text-gray-900 hover:bg-gray-50
                ${className?.includes("slick-disabled") ? "hidden" : ""}
            `}
            onClick={onClick}
        >
            <MdOutlineKeyboardArrowRight size={22} />
        </div>
    );
};

const PrevArrow = (props) => {
    const { className, style, onClick } = props;
    return (
        <div
            className={`
                absolute z-10 !left-0 -translate-x-1/3 top-1/2 -translate-y-12
                cursor-pointer text-xl text-black p-1 rounded-full bg-white border-gray-200 border
                text-gray-500 hover:text-gray-900 hover:bg-gray-50
                ${className?.includes("slick-disabled") ? "hidden" : ""}
            `}
            onClick={onClick}
        >
            <MdOutlineKeyboardArrowLeft size={22} />
        </div>
    );
};


let settings = {
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
            }
        },
        {
            breakpoint: 1280,
            settings: {
                slidesToShow: 4,
                slidesToScroll: 4,
            }
        },
        {
            breakpoint: 1024,
            settings: {
                slidesToShow: 3,
                slidesToScroll: 3,
                initialSlide: 3
            }
        },
        {
            breakpoint: 768,
            settings: {
                slidesToShow: 2,
                slidesToScroll: 2
            }
        }
    ]
};

export const MovieSlide = ({ title, movies }) => {
    return (

        <section className="my-8 text-left px-5">
            <h2 className="text-2xl font-semibold mb-4 px-2">{title}</h2>
            <div className="slider-container">
                <Slider className="relative " {...settings}>
                    {movies.map((movie) => (
                        <Link to={""} key={movie.id} className="px-2 cursor-pointer">
                            <div className="bg-gray-400 rounded-sm border border-gray-200 overflow-hidden">
                                <img src={movie.posterUrl} alt={movie.title}/>
                            </div>
                            <div className="grid gap-y-1">
                                <p className="mt-2 text-md font-semibold">{movie.title}</p>
                                <div className="text-sm font-medium">
                                    {movie.releaseYear}
                                    <p className="inline mx-1">
                                        {movie.genre.map((g, idx) => (
                                            <span key={idx} className="mr-1">· {g}</span>
                                        ))}
                                    </p>
                                </div>
                                <p className="flex items-center text-sm text-gray-500">
                                    평균 <IoMdStar className="mx-1" /> {movie.rating}
                                </p>
                            </div>
                        </Link>
                    ))}
                </Slider>
            </div>
        </section>
    );
};
