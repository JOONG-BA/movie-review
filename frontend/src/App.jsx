import React, { useEffect, useState } from 'react';
import axios from 'axios';

function App() {
    const [movies, setMovies] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/api/movies/boxoffice?date=20250510') // .../api/boxoffice 에서 .../api/movies/boxoffice로 수정
            .then(response => {
                setMovies(response.data.boxOfficeResult.dailyBoxOfficeList);
            })
            .catch(error => {
                console.error('API 요청 실패:', error);
            });
    }, []);

    return (
        <div>
            <h1>박스오피스 영화 목록</h1>
            <ul>
                {movies.map(movie => (
                    <li key={movie.movieCd}>
                        {movie.rank}. {movie.movieNm} ({movie.openDt})
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default App;
