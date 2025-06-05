import {useSearchParams} from "react-router-dom";
import MovieList from "@/components/moive/search/MovieList.jsx";
import {dummyMovies} from "@/assets/DummyData.js";


export default function SearchPage( ){
    const [searchParams] = useSearchParams();
    const query = searchParams.get("query");

    return(
        <div className="container mt-20">
            <MovieList movies={dummyMovies} />
        </div>
    )
}