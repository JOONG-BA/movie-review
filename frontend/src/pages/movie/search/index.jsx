import { useState, useEffect, useRef, useCallback } from "react";
import { useSearchParams } from "react-router-dom";
import MovieList from "@/components/moive/search/MovieList.jsx";
import {searchMovies} from "@/pages/api/movieApi.js";

export default function SearchPage() {
    const [searchParams] = useSearchParams();
    const query = searchParams.get("query");

    const [movies, setMovies] = useState([]);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const loaderRef = useRef(null);

    // fetchMovies에서 page를 받도록 변경
    const fetchMovies = useCallback(async (targetPage) => {
        if (!query) return;
        try {
            const newMovies = await searchMovies(query, targetPage);
            if (targetPage === 1) {
                setMovies(newMovies.content);
            } else {
                setMovies((prev) => [...prev, ...newMovies.content]);
            }
            if (newMovies.content.length === 0) {
                setHasMore(false);
            }
        } catch (error) {
            console.error(error);
        }
    }, [query]);

    useEffect(() => {
        setMovies([]);
        setHasMore(true);
        setPage(1);
    }, [query]);

    useEffect(() => {
        fetchMovies(page);
    }, [page, fetchMovies]);

    useEffect(() => {
        if (!hasMore) return;
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setPage((prev) => prev + 1);
                }
            },
            { threshold: 1 }
        );
        if (loaderRef.current) observer.observe(loaderRef.current);
        return () => {
            if (loaderRef.current) observer.unobserve(loaderRef.current);
        };
    }, [hasMore]);

    return (
        <div className="container mt-20">
            <MovieList movies={movies} />
            <div ref={loaderRef} style={{ height: "20px" }} />
            {!hasMore && <p className="text-center mt-4">더 이상 영화가 없습니다.</p>}
        </div>
    );
}
