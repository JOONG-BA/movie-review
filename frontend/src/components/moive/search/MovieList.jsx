import {IoMdStar} from "react-icons/io";
import {Link} from "react-router-dom";

export default function MovieList({movies}){
    console.log(movies);
    return(
        <div className="grid grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-3">
            {
                movies.map((movie) => (
                    <div>
                        <Link to={`/movie/detail/${movie.id}`} key={movie.id} className="pr-1 sm:pr-2 cursor-pointer">
                            <div className="bg-gray-400 rounded-sm border border-gray-200 overflow-hidden">
                                <img src={movie.posterUrl} alt={movie.title}/>
                            </div>
                            <div className="grid text-left gap-y-1">
                                <p className="mt-2 text-base sm:text-lg font-semibold">{movie.title}</p>
                                <div className="text-sm hidden sm:block font-medium">
                                    {movie.releaseDate}
                                    <p className="inline mx-1">
                                        {movie.genres.map((g, idx) => (
                                            <span key={idx} className="mr-1">· {g.name}</span>
                                        ))}
                                    </p>
                                </div>
                                <p className="flex items-center text-xs sm:text-sm text-gray-500">
                                    평균 <IoMdStar className="mx-1" /> {movie.voteAverage}
                                </p>
                            </div>
                        </Link>
                    </div>
                ))
            }
        </div>
    )
}