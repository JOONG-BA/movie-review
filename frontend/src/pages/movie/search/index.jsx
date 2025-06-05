import { useState, useEffect, useRef, useCallback } from "react";
import { useSearchParams } from "react-router-dom";
import MovieList from "@/components/moive/search/MovieList.jsx";
import {searchMovies} from "@/pages/api/movieApi.js";
import InlineLoadingSpinner from "@/components/ui/InlineLodingSpinner.jsx";
import LoadingSpinner from "@/components/ui/LoadingSpinner.jsx";

export default function SearchPage() {
    const [searchParams] = useSearchParams();
    const query = searchParams.get("query");

    const [movies, setMovies] = useState([]);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const [loading, setLoading] = useState(false);
    const [initialLoading, setInitialLoading] = useState(true);
    const loaderRef = useRef(null);

    const fetchMovies = useCallback(async () => {
        if (!query) return;
        setLoading(true);
        try {
            const newMovies = await searchMovies(query, page);
            setMovies((prev) => [...prev, ...newMovies]);
            if (newMovies.length === 0) setHasMore(false);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
            setInitialLoading(false);
        }
    }, [query, page]);

    useEffect(() => {
        setMovies([]);
        setPage(1);
        setHasMore(true);
        setInitialLoading(true);
    }, [query]);

    useEffect(() => {
        fetchMovies();
    }, [page, fetchMovies]);

    useEffect(() => {
        if (!hasMore) return;
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting && !loading) {
                    setPage((prev) => prev + 1);
                }
            },
            { threshold: 1 }
        );
        if (loaderRef.current) observer.observe(loaderRef.current);
        return () => {
            if (loaderRef.current) observer.unobserve(loaderRef.current);
        };
    }, [hasMore, loading]);

    if (initialLoading) return <LoadingSpinner />;

    return (
        <div className="container mt-20">
            <MovieList movies={movies} />
            {loading && <InlineLoadingSpinner />}
            <div ref={loaderRef} style={{ height: "20px" }} />
            {!hasMore && <p className="text-center mt-4"></p>}
        </div>
    );
}
