import React, { useEffect, useState } from 'react';
import axios from 'axios';

function App() {
    const [movies, setMovies] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/api/movies') // 백엔드에서 TMDb 영화 리스트 가져옴
            .then(response => {
                setMovies(response.data);
            })
            .catch(error => {
                console.error('영화 데이터 요청 실패:', error);
            });
    }, []);

    return (
        <div>
            <h1>🎬 인기 영화 목록 (TMDb)</h1>
            <ul>
                {movies.map(movie => (
                    <li key={movie.id}>
                        <strong>{movie.title}</strong> ({movie.releaseDate})<br />
                        평점: {movie.voteAverage} / 줄거리: {movie.overview?.slice(0, 100)}...
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default App;
